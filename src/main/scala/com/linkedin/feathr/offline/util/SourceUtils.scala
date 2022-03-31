package com.linkedin.feathr.offline.util

import com.databricks.spark.avro.SchemaConverterUtils
import com.databricks.spark.avro.SchemaConverters.convertStructToAvro
import com.fasterxml.jackson.databind.ObjectMapper
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.linkedin.feathr.common.exception._
import com.linkedin.feathr.common.{AnchorExtractor, DateParam}
import com.linkedin.feathr.offline.client.InputData
import com.linkedin.feathr.offline.generation.SparkIOUtils
import com.linkedin.feathr.offline.source.SourceFormatType
import com.linkedin.feathr.offline.source.SourceFormatType.SourceFormatType
import com.linkedin.feathr.offline.source.dataloader.hdfs.FileFormat
import com.linkedin.feathr.offline.source.dataloader.jdbc.JdbcUtils
import com.linkedin.feathr.offline.source.pathutil.{PathChecker, TimeBasedHdfsPathAnalyzer, TimeBasedHdfsPathGenerator}
import com.linkedin.feathr.offline.util.AclCheckUtils.getLatestPath
import com.linkedin.feathr.offline.util.datetime.OfflineDateTimeUtils
import org.apache.avro.generic.{GenericDatumReader, GenericRecord}
import org.apache.avro.io.DecoderFactory
import org.apache.avro.specific.{SpecificDatumReader, SpecificRecord, SpecificRecordBase}
import org.apache.avro.{Schema, SchemaBuilder}
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.sql.avro.SchemaConverters
import org.apache.spark.sql.types.StructType
import org.joda.time.{Interval, DateTimeZone => JodaTimeZone}
import java.io.{ByteArrayInputStream, DataInputStream}
import java.text.SimpleDateFormat
import java.util
import java.util.{Date, TimeZone}
import scala.collection.JavaConversions.iterableAsScalaIterable
import scala.io.Source
import scala.reflect.ClassTag
import scala.util.Try

/**
 * Load "raw" not-yet-featurized data from data sets
 */
private[offline] object SourceUtils {
  private final val conf: Configuration = new Configuration()

  private val log = Logger.getLogger(getClass)
  // this path is defined by the feathr plugin
  val FEATURE_MP_DEF_CONFIG_BASE_PATH = "feathr-feature-configs/config/offline"
  val FEATURE_MP_DEF_CONFIG_SUFFIX = ".conf"
  val firstRecordName = "topLevelRecord"

  /**
   * get AVRO datum type of a dataset we should use to load,
   * it is determined by the expect datatype from a set of anchor transformers
   * @param transformers  transformers that uses the dataset
   * @return datatype to use, either avro generic record or specific record
   */
  def getExpectDatumType(transformers: Seq[AnyRef] = Nil): Class[_] = {
    // AnchorExtractorSpark does not support getInputType, and this rdd will not be used for AnchorExtractorSpark
    val expectedRecordTypes = transformers.collect {
      case transformer: AnchorExtractor[_] => transformer.getInputType
    }
    // if the transformer expect a specific record, we need to load the dataset as rdd[specificRecord], so return specific record here,
    // otherwise, return GenericRecord, since we will just load it as rdd[GenericRecord]
    val expectDatumType = if (expectedRecordTypes.exists(classOf[SpecificRecord].isAssignableFrom)) {
      // Determine which SpecificRecord subclass we need to use
      val expectedSpecificTypes = expectedRecordTypes.filter(classOf[SpecificRecord].isAssignableFrom).distinct
      assert(expectedSpecificTypes.nonEmpty)
      require(
        expectedSpecificTypes.size == 1,
        s"Can't determine which SpecificRecord subclass to use; " +
          s"transformers $transformers seem to require more than one record type: $expectedSpecificTypes")
      expectedSpecificTypes.head
    } else if (expectedRecordTypes.exists(classOf[GenericRecord].isAssignableFrom)) {
      classOf[GenericRecord]
    } else {
      classOf[AnyRef]
    }
    expectDatumType
  }

  def getPathList(
      sourceFormatType: SourceFormatType,
      sourcePath: String,
      ss: SparkSession,
      dateParam: Option[DateParam],
      targetDate: Option[String] = None,
      failOnMissing: Boolean = true): Seq[String] = {
    sourceFormatType match {
      case SourceFormatType.FIXED_PATH => Seq(HdfsUtils.getLatestPath(sourcePath, ss.sparkContext.hadoopConfiguration))
      case SourceFormatType.TIME_PATH =>
        val pathChecker = PathChecker(ss)
        val pathGenerator = new TimeBasedHdfsPathGenerator(pathChecker)
        val pathAnalyzer = new TimeBasedHdfsPathAnalyzer(pathChecker)
        val pathInfo = pathAnalyzer.analyze(sourcePath)
        pathGenerator.generate(pathInfo, OfflineDateTimeUtils.createTimeIntervalFromDateParam(dateParam, None, targetDate), !failOnMissing)
      case SourceFormatType.LIST_PATH => sourcePath.split(";")
      case _ =>
        throw new FeathrConfigException(
          ErrorLabel.FEATHR_USER_ERROR,
          "Trying to get source path list. " +
            "sourceFormatType should be either FIXED_PATH or DAILY_PATH. Please provide the correct sourceFormatType.")
    }
  }

  /**
   * Returns the avro schema [[Schema]] of a given dataframe
   * @param dataframe input dataframe
   * @return  The avro schema type
   */
  def getSchemaOfDF(dataframe: DataFrame): Schema = {
    val builder = SchemaBuilder.record(firstRecordName).namespace("")
    convertStructToAvro(dataframe.schema, builder, firstRecordName)
  }

  /** estimate RDD size within x seconds(by default, 30s)
   * count could be REALLY slow,
   * WARNING: could possibly return INT64_MAX, if RDD is too big and timeout is too small
   * @param rdd
   * @param timeout timeout in milliseconds
   * @tparam L
   * @return estimate size of rdd
   */
  def estimateRDDRow[L](rdd: RDD[L], timeout: Int = 30000): Long = {
    var estimateSize = 0d
    val estimateSizeInterval = rdd.countApprox(timeout, 0.8)
    val (lowCnt, highCnt) = (estimateSizeInterval.initialValue.low, estimateSizeInterval.initialValue.high)
    estimateSize = (lowCnt + highCnt) / 2.0
    estimateSize.longValue()
  }

  /**
   * write dataframe to a give hdfs path, this function is job-safe, e.g. other job will not be able to see
   * the output path until the write is completed, this is done via writing to a temporary folder, then finally
   * rename to the expected folder
   *
   * @param df         dataframe to write
   * @param dataPath   path to write the dataframe
   * @param parameters spark parameters
   */
  def safeWriteDF(df: DataFrame, dataPath: String, parameters: Map[String, String]): Unit = {
    val tempBasePath = dataPath.stripSuffix("/") + "_temp_"
    HdfsUtils.deletePath(dataPath, true)
    SparkIOUtils.writeDataFrame(df, tempBasePath, parameters)
    if (HdfsUtils.exists(tempBasePath) && !HdfsUtils.renamePath(tempBasePath, dataPath)) {
      throw new FeathrDataOutputException(
        ErrorLabel.FEATHR_ERROR,
        s"Trying to rename temp path to target path in safeWrite." +
          s"Rename ${tempBasePath} to ${dataPath} failed" +
          s"This is likely a system error. Please retry.")
    }
  }

  /**
   * Get Default value from avro record
   * @param field the avro field for which the default value is to be extracted
   * return the JsonNode containing the default value or otherwise null
   */
  def getDefaultValueFromAvroRecord(field: Schema.Field) = {
    // This utility method throws an error if the field does not have a default value, hence we need to check if the field has a default first.
    field.defaultVal()
  }

  /**
   * A Common Function to load Test DFs
   * Will be moved to Test Utils later
   * @param ss    Spark Session
   * @param path  File Path
   */
  def getLocalDF(ss: SparkSession, path: String): DataFrame = {
    val format = FileFormat.getType(path)
    val localPath = getLocalPath(path)
    format match {
      case FileFormat.AVRO_JSON => loadJsonFileAsAvroToDF(ss, localPath).get
      case FileFormat.JDBC => JdbcUtils.loadDataFrame(ss, path)
      case _ => {
        getLocalMockDataPath(ss, path) match {
          case Some(mockData) =>
            loadSeparateJsonFileAsAvroToDF(ss, mockData).getOrElse(throw new FeathrException(ErrorLabel.FEATHR_ERROR, s"Cannot load mock data path ${mockData}"))
          case None => loadAsDataFrame(ss, localPath)
        }
      }
    }
  }

  /**
   * Check if the local mock data exists. If so, return the resolved path to the mock data
   *
   * @param ss         Spark Session
   * @param sourcePath Source path of the dataset, could be a HDFS path
   * @return The resolved path to the mock data.
   *         If it's not in local mode or the mock data doesn't exist, return None.
   */
  def getLocalMockDataPath(ss: SparkSession, sourcePath: String): Option[String] = {
    if (!ss.sparkContext.isLocal) return None

    val mockSourcePath = LocalFeatureJoinUtils.getMockPath(sourcePath)
    val path = new Path(mockSourcePath)
    val hadoopConf = ss.sparkContext.hadoopConfiguration
    val fs = path.getFileSystem(hadoopConf)
    val mockSourcePathWithLatest = getLatestPath(fs, mockSourcePath)
    Some(mockSourcePathWithLatest).filter(HdfsUtils.exists(_))
  }

  // translate a certain string to the time, according to the timezone tz
  def createTimeFromString(dateString: String, formatString: String = "yyyyMMdd", tz: String = "America/Los_Angeles"): Date = {
    val format = new SimpleDateFormat(formatString)
    format.setTimeZone(TimeZone.getTimeZone(tz))
    // we support source path as both /path/to/datasets/daily and /path/to/datasets/daily/, and /path//to/data,
    // so need to strip potential '/' and replace '//' with '/'
    format.parse(dateString.stripMargin('/').replaceAll("//", "/"))
  }

  // create TimeInterval from a start time and end time with format
  def createTimeInterval(
                          startDateOpt: Option[String],
                          endDateOpt: Option[String],
                          formatString: String = "yyyyMMdd",
                          tz: String = "America/Los_Angeles"): Interval = {
    val timeZone = TimeZone.getTimeZone(tz)
    (startDateOpt, endDateOpt) match {
      case (Some(startDate), Some(endDate)) =>
        val startTime = createTimeFromString(startDate, formatString, tz).getTime
        val endTime = createTimeFromString(endDate, formatString, tz).getTime
        new Interval(startTime, endTime, JodaTimeZone.forTimeZone(timeZone))
      case (_, _) =>
        throw new FeathrConfigException(
          ErrorLabel.FEATHR_USER_ERROR,
          s"Trying to create TimeInterval from a start time and end time with specified format. Date is not defined. " +
            s"Please provide date.")
    }
  }

  /**
   * load paths as union datasets
   * @param ss
   * @param inputPath
   * @return
   */
  def loadAsUnionDataFrame(ss: SparkSession, inputPath: Seq[String]): DataFrame = {
    val sparkConf = ss.sparkContext.getConf
    val inputSplitSize = sparkConf.get("spark.feathr.input.split.size", "")
    val dataIOParameters = Map(SparkIOUtils.SPLIT_SIZE -> inputSplitSize)
    val hadoopConf = ss.sparkContext.hadoopConfiguration
    log.info(s"Loading ${inputPath} as union DataFrame, using parameters ${dataIOParameters}")
    SparkIOUtils.createUnionDataFrame(inputPath, dataIOParameters)
  }

  /**
   * load single input path as dataframe
   * @param ss
   * @param inputPath
   * @return
   */
  def loadAsDataFrame(ss: SparkSession, inputPath: String): DataFrame = {
    val sparkConf = ss.sparkContext.getConf
    val inputSplitSize = sparkConf.get("spark.feathr.input.split.size", "")
    val dataIOParameters = Map(SparkIOUtils.SPLIT_SIZE -> inputSplitSize)
    log.info(s"Loading ${inputPath} as DataFrame, using parameters ${dataIOParameters}")
    SparkIOUtils.createDataFrame(inputPath, dataIOParameters)
  }

  /**
   * Load observation data as DataFrame
   * @param ss
   * @param conf
   * @param inputData HDFS path for observation
   * @return
   */
  def loadObservationAsDF(ss: SparkSession, conf: Configuration, inputData: InputData, failOnMissing: Boolean = true): DataFrame = {
    // TODO: Split isLocal case into Test Packages
    val format = FileFormat.getType(inputData.inputPath)
    log.info(s"loading ${inputData.inputPath} input Path as Format: ${format}")
    format match {
      case FileFormat.PATHLIST => {
        val pathList = getPathList(inputData.sourceType, inputData.inputPath, ss, inputData.dateParam, None, failOnMissing)
        if (ss.sparkContext.isLocal) { // for test
          try {
            loadAsUnionDataFrame(ss, pathList)
          } catch {
            case _: Throwable => loadSeparateJsonFileAsAvroToDF(ss, inputData.inputPath).get
          }
        } else {
          loadAsUnionDataFrame(ss, pathList)
        }
      }
      case FileFormat.JDBC => {
        JdbcUtils.loadDataFrame(ss, inputData.inputPath)
      }
      case FileFormat.CSV => {
        ss.read.format("csv").option("header", "true").load(inputData.inputPath)
      }
      case _ => {
        if (ss.sparkContext.isLocal){
          getLocalDF(ss, inputData.inputPath)
        } else {
          loadAsDataFrame(ss, inputData.inputPath)
        }
      }
    }
  }

  def getLocalPath(path: String): String = {
    getClass.getClassLoader.getResource(path).getPath()
  }

  /**
   * parse the input dataArray json as RDD
   * @param ss spark seesion
   * @param dataArrayAsJson input data array as json string
   * @param schemaAsString avro schema of the input data array
   * @return the converted rdd and avro schema
   */
  def parseJsonAsAvroRDD[T](ss: SparkSession, dataArrayAsJson: String, schemaAsString: String)(implicit tag: ClassTag[T]): (RDD[_], Schema) = {
    val sc = ss.sparkContext
    val jackson = new ObjectMapper(new HoconFactory)
    val schema = Schema.parse(schemaAsString)
    val jsonDataArray = jackson.readTree("{ data:" + dataArrayAsJson + " }").get("data")
    val records = jsonDataArray.map(jsonNode => {
      val input = new ByteArrayInputStream(jsonNode.toString.getBytes)
      val din = new DataInputStream(input)
      val decoder = DecoderFactory.get().jsonDecoder(schema, din)
      if (!classOf[SpecificRecordBase].isAssignableFrom(scala.reflect.classTag[T].runtimeClass.asInstanceOf[Class[T]])) {
        val reader = new GenericDatumReader[GenericRecord](schema)
        reader.read(null, decoder)
      } else {
        val reader = new SpecificDatumReader[T](scala.reflect.classTag[T].runtimeClass.asInstanceOf[Class[T]])
        reader.read(null.asInstanceOf[T], decoder)
      }
    })
    (sc.parallelize(records.toSeq), schema)
  }

  /**
   * load rdd from a path which hosts files in separate json format, e.g.
   * /path/
   *     | mockData.json
   *     | schema.avsc
   * @param ss spark session
   * @param path path to load
   */
  def loadSeparateJsonFileAsAvroToRDD[T](ss: SparkSession, path: String)(implicit tag: ClassTag[T]): Option[(RDD[_], Schema)] = {
    try {
      val dataPath = path + "/mockData.json"
      val dataAsString = readLocalConfFileAsString(dataPath)
      val schemaPath = path + "/schema.avsc"
      val schemaAsString = readLocalConfFileAsString(schemaPath)
      Some(parseJsonAsAvroRDD(ss, dataAsString, schemaAsString))
    } catch {
      case e: Exception => None
    }
  }

  /**
   * Load .avro.json file as datafile
   * @param ss
   * @param path
   * @return DataFrame of input .avro.json path
   */
  def loadSeparateJsonFileAsAvroToDF(ss: SparkSession, path: String): Option[DataFrame] = {
    loadSeparateJsonFileAsAvroToRDD(ss, path).map { res =>
      val schema = res._2
      val sqlType = SchemaConverters.toSqlType(schema).dataType.asInstanceOf[StructType]
      val converter = SchemaConverterUtils.converterSql(schema, sqlType)
      val rowRdd = res._1
        .asInstanceOf[RDD[GenericRecord]]
        .flatMap(record => {
          Try(converter(record).asInstanceOf[Row]).toOption
        })
      ss.createDataFrame(rowRdd, sqlType)
    }
  }

  /**
   * Load .avro.json file as RDD
   * @param ss
   * @param path
   * @return return result RDD and schema, it path does not exist, return None
   */
  def loadJsonFileAsAvroToRDD[T](ss: SparkSession, path: String)(implicit tag: ClassTag[T]): Option[(RDD[_], Schema)] = {
    val sc = ss.sparkContext
    require(sc.isLocal)
    require(path.endsWith(".avro.json"))
    val absolutePath = getClass.getClassLoader.getResource(path)
    if (absolutePath != null) {
      val contents = Source.fromFile(absolutePath.toURI).mkString
      val jackson = new ObjectMapper(new HoconFactory)
      val tree = jackson.readTree(contents)
      val schema = Schema.parse(tree.get("schema").toString)
      val jsonDataArray = tree.get("data")

      val records = jsonDataArray.map(jsonNode => {
        val input = new ByteArrayInputStream(jsonNode.toString.getBytes)
        val din = new DataInputStream(input)
        val decoder = DecoderFactory.get().jsonDecoder(schema, din)
        if (!classOf[SpecificRecordBase].isAssignableFrom(scala.reflect.classTag[T].runtimeClass.asInstanceOf[Class[T]])) {
          val reader = new GenericDatumReader[GenericRecord](schema)
          reader.read(null, decoder)
        } else {
          val reader = new SpecificDatumReader[T](scala.reflect.classTag[T].runtimeClass.asInstanceOf[Class[T]])
          reader.read(null.asInstanceOf[T], decoder)
        }
      })
      Some(sc.parallelize(records.toSeq), schema)
    } else None
  }

  /**
   * Load .avro.json file as datafile
   * @param ss
   * @param path
   * @return DataFrame of input .avro.json path
   */
  def loadJsonFileAsAvroToDF(ss: SparkSession, path: String): Option[DataFrame] = {
    loadJsonFileAsAvroToRDD(ss, path) match {
      case Some((rdd, schema)) => convertRddToDataFrame(ss, rdd.asInstanceOf[RDD[GenericRecord]], schema)
      case None => None
    }
  }

  /**
   * convert a RDD of generic record to dataframe
   * @param ss spark session
   * @param rdd rdd of generic record
   * @param schema schema of the RDD
   * @return converted dataframe
   */
  def convertRddToDataFrame(ss: SparkSession, rdd: RDD[GenericRecord], schema: Schema): Option[DataFrame] = {
    val sqlType = SchemaConverters.toSqlType(schema).dataType.asInstanceOf[StructType]
    val converter = SchemaConverterUtils.converterSql(schema, sqlType)
    // Here we need to collect the rows and apply the converter instead of applying the converter to the RDD,
    // because the converter may not be serializable and unable to distribute to the executors.
    // We will trigger a action and collect the data in the driver. It' OK to do so because this function is only
    // used to load local testing files.
    val rows = rdd.collect()
      .flatMap(record => {
        Try(converter(record).asInstanceOf[Row]).toOption
      })
    Some(ss.createDataFrame(util.Arrays.asList(rows: _*), sqlType))
  }

  /*
   * Check if a string is a file path
   * Rules: for HDFS path, it should contain "/"; for unit tests, we now support .csv and .json
   */
  def isFilePath(sourceIdentifier: String): Boolean = {
    sourceIdentifier.contains("/") || sourceIdentifier.contains(".csv") || sourceIdentifier.contains(".json") || sourceIdentifier.contains(".orc")
  }

  /**
   * read local conf file(s) as string
   * @param paths path(s) of local conf files, concatenated by ','
   * @return the conf files as a string
   */
  private[feathr] def readLocalConfFileAsString(paths: String): String = {
    paths
      .split(",")
      .map { path =>
        val bufferedSource = Source.fromFile(path)
        val content = bufferedSource.mkString
        bufferedSource.close
        content
      }
      .mkString("\n")
  }
}

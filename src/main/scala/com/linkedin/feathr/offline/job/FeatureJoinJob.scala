package com.linkedin.feathr.offline.job

import com.linkedin.feathr.common
import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrDataOutputException, FeathrInputDataException}
import com.linkedin.feathr.common.{Header, JoiningFeatureParams}
import com.linkedin.feathr.offline._
import com.linkedin.feathr.offline.client._
import com.linkedin.feathr.offline.config.FeatureJoinConfig
import com.linkedin.feathr.offline.config.datasource.{DataSourceConfigUtils, DataSourceConfigs}
import com.linkedin.feathr.offline.generation.SparkIOUtils
import com.linkedin.feathr.offline.source.SourceFormatType
import com.linkedin.feathr.offline.util.SourceUtils.getPathList
import com.linkedin.feathr.offline.util._
import com.linkedin.feathr.offline.transformation.AnchorToDataSourceMapper
import org.apache.avro.generic.GenericRecord
import org.apache.commons.cli.{Option => CmdOption}
import org.apache.hadoop.conf.Configuration
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.reflect.ClassTag
import scala.util.{Failure, Success}
import collection.JavaConverters._

/**
 * Join features to some observations for training/testing
 *
 * Output Format:
 * For some input observations having schema S,
 * the output will have the following schema <pre>
 * {
 * S,
 * featureGroupA: [ { name: string, term: string, value: double } ],
 * featureGroupB: [ { name: string, term: string, value: double } ],
 * ...
 * }</pre>
 *
 * Note: Most of the actual join logic is found in FeathrClient.joinFeatures.
 * This class is mostly concerned with input/output serialization and formatting, configuration, and wrapping the join
 * logic with a job we can invoke via Azkaban.
 */
object FeatureJoinJob {

  val logger: Logger = Logger.getLogger(getClass)
  val SKIP_OUTPUT = "skip_output"

  // We found that if we have too many parallelism, then during the join shuffling, memoryOverhead could be too high,
  // cap it to 10000 to make sure memoryOverhead is less than 2g (Feathr default value)
  val SPARK_JOIN_MAX_PARALLELISM = "10000"
  val SPARK_JOIN_MIN_PARALLELISM = "10"
  // If spark.default.parallelism is not set, this value will be used as the default and used
  // to cap numPartitions later; otherwise, the spark.default.paralleism value set by user will be used
  val SPARK_JOIN_PARALLELISM_DEFAULT = "5000"
  // Internal parameter (We don't expect user to change it) as an empirical factor 'threshold' to control whether limit the partition or not
  val SPARK_JOIN_LIMIT_PARTITION_FACTOR = 2

  val log: Logger = Logger.getLogger(getClass)

  def run(ss: SparkSession, hadoopConf: Configuration, jobContext: FeathrJoinJobContext): Unit = {
    val joinConfig = FeatureJoinConfig.parseJoinConfig(hdfsFileReader(ss, jobContext.joinConfig))
    print("join config is, ",joinConfig)
    // check read authorization for observation data, and write authorization for output path
    checkAuthorization(ss, hadoopConf, jobContext)

    feathrJoinRun(ss, hadoopConf, joinConfig, jobContext.jobJoinContext, None)
  }

  // Log the feature names for bookkeeping. Global config may be merged with local config(s).
  def stringifyFeatureNames(nameSet: Set[String]): String = nameSet.toSeq.sorted.toArray.mkString("\n\t")

  def hdfsFileReader(ss: SparkSession, path: String): String = {
    print("ss.sparkContext.textFile(path),", path)
    ss.sparkContext.textFile(path).collect.mkString("\n")
  }

  private def checkAuthorization(ss: SparkSession, hadoopConf: Configuration, jobContext: FeathrJoinJobContext): Unit = {
    AclCheckUtils.checkWriteAuthorization(hadoopConf, jobContext.jobJoinContext.outputPath) match {
      case Failure(e) =>
        throw new FeathrDataOutputException(ErrorLabel.FEATHR_USER_ERROR, s"No write permission for output path ${jobContext.jobJoinContext.outputPath}.", e)
      case Success(_) => log.debug("Checked write authorization on output path: " + jobContext.jobJoinContext.outputPath)
    }
    jobContext.jobJoinContext.inputData.map(inputData => {
      val failOnMissing = FeathrUtils.getFeathrJobParam(ss, FeathrUtils.FAIL_ON_MISSING_PARTITION).toBoolean
      val pathList = getPathList(inputData.sourceType, inputData.inputPath, ss, inputData.dateParam, None, failOnMissing)
      AclCheckUtils.checkReadAuthorization(hadoopConf, pathList) match {
        case Failure(e) => throw new FeathrInputDataException(ErrorLabel.FEATHR_USER_ERROR, s"No read permission on observation data $pathList.", e)
        case Success(_) => log.debug("Checked read authorization on observation data of the following paths:\n" + pathList.mkString("\n"))
      }
    })
  }

  /**
   * This function will get the feathr client using the spark session and jobContext, and call FeathrClient#joinObsAndFeatures
   * method.
   * @param ss  spark session
   * @param observations  observations DF
   * @param featureGroupings   feature groups to join
   * @param joinConfig  join config
   * @param jobContext  job context
   * @param localTestConfigOpt  Local test config
   * @return  Dataframe and header associated with it.
   */
  private[offline] def getFeathrClientAndJoinFeatures(
      ss: SparkSession,
      observations: DataFrame,
      featureGroupings: Map[String, Seq[JoiningFeatureParams]],
      joinConfig: FeatureJoinConfig,
      jobContext: JoinJobContext,
      localTestConfigOpt: Option[LocalTestConfig] = None): (DataFrame, Header) = {

    val feathrClient = getFeathrClient(ss, jobContext, localTestConfigOpt)
    feathrClient.doJoinObsAndFeatures(joinConfig, jobContext, observations)
  }

  private[offline] def getFeathrClient(
    ss: SparkSession,
    jobContext: JoinJobContext,
    localTestConfigOpt: Option[LocalTestConfig] = None): FeathrClient = {

    localTestConfigOpt match {
      case None =>
        FeathrClient.builder(ss)
          .addFeatureDefPath(jobContext.feathrFeatureConfig)
          .addLocalOverrideDefPath(jobContext.feathrLocalConfig)
          .build()
      case Some(localTestConfig) =>
        FeathrClient.builder(ss)
          .addFeatureDef(localTestConfig.featureConfig)
          .addLocalOverrideDef(localTestConfig.localConfig)
          .build()
    }
  }

  /**
   * This function will collect the data, build the schema and do the join work for hdfs records.
   *
   * @param ss                 SparkSession
   * @param hadoopConf         Hadoop Configuration
   * @param joinConfig         Feathr join config
   * @param jobContext         Other parameters, including output path, passthroughFeatures, inputData and etc.
   * @param localTestConfig    local featureDef config
   * @return the output DataFrame
   */
  private[feathr] def feathrJoinRun(
      ss: SparkSession,
      hadoopConf: Configuration,
      joinConfig: FeatureJoinConfig,
      jobContext: JoinJobContext,
      localTestConfig: Option[LocalTestConfig] = None): (Option[RDD[GenericRecord]], Option[DataFrame]) = {
    val sparkConf = ss.sparkContext.getConf

    val featureGroupings = joinConfig.featureGroupings

    /*
     * load FeathrClient and perform the Feature Join
     */
    val failOnMissing = FeathrUtils.getFeathrJobParam(ss, FeathrUtils.FAIL_ON_MISSING_PARTITION).toBoolean
    val observationsDF = SourceUtils.loadObservationAsDF(ss, hadoopConf, jobContext.inputData.get, failOnMissing)

    val (joinedDF, _) = getFeathrClientAndJoinFeatures(ss, observationsDF, featureGroupings, joinConfig, jobContext, localTestConfig)

    val parameters = Map(SparkIOUtils.OUTPUT_PARALLELISM -> jobContext.numParts.toString, SparkIOUtils.OVERWRITE_MODE -> "ALL")


    SparkIOUtils.writeDataFrame(joinedDF, jobContext.outputPath, parameters)
    (None, Some(joinedDF))
  }

  /**
   * Parse command line arguments into the join job context.
   *
   * @param args command line arguments
   * @return join job context
   */
  def parseInputArgument(args: Array[String]): FeathrJoinJobContext = {
    val params = Map(
      // option long name, short name, description, arg name (null means not argument), default value (null means required)
      "feathr-config" -> OptionParam("f", "Path of the feathr local config file", "FCONF", ""),
      "feature-config" -> OptionParam("ef", "Names of the feathr feature config files", "EFCONF", ""),
      "local-override-all" -> OptionParam("loa", "Local config overrides all other configs", "LOCAL_OVERRIDE", "true"),
      "join-config" -> OptionParam("j", "Path of the join config file", "JCONF", ""),
      "input" -> OptionParam("i", "Path of the input data set", "INPUT", ""),
      "output" -> OptionParam("o", "Path of the output", "OUTPUT", ""),
      "num-parts" -> OptionParam("n", "Number of output part files", "NPARTS", "-1"),
      "pass-through-field" -> OptionParam("p", "Pass-through feature field name", "PFIELD", ""),
      "pass-through-features" -> OptionParam("t", "Pass-through feature list, comma-separated", "PLIST", ""),
      "source-type" -> OptionParam("st", "Source type of the observation data", "SRCTYPE", "FIXED_PATH"),
      "start-date" -> OptionParam("sd", "Start date of the observation data if it's time based", "SDATE", ""),
      "end-date" -> OptionParam("ed", "End date of the observation data if it's time based", "EDATE", ""),
      "num-days" -> OptionParam("nd", "Number of days before the offset date if it's time based", "NDAYS", ""),
      "date-offset" -> OptionParam("do", "Offset of observation data if it's time based", "DOFFSET", ""),
      "join-parallelism" -> OptionParam("p", "Multiplier to increase the number of partitions of feature datasets during joins", "PARALLEL", "8"),
      "row-bloomfilter-threshold" -> OptionParam("rbt", "Performance tuning, if observation record # is less than the threshold, " +
        "a bloomfilter will be applied", "ROWFILTERTHRESHOLD", "-1"),
      "job-version" -> OptionParam("jv", "Job version, integer, job version 2 uses DataFrame and SQL based anchor, default is 2", "JOBVERSION", "2"),
      "as-tensors" -> OptionParam("at", "If set to true, get features as tensors else as term-vectors", "AS_TENSORS", "false"),
      "s3-config" -> OptionParam("sc", "Authentication config for S3", "S3_CONFIG", ""),
      "adls-config" -> OptionParam("adlc", "Authentication config for ADLS (abfs)", "ADLS_CONFIG", ""),
      "blob-config" -> OptionParam("bc", "Authentication config for Azure Blob Storage (wasb)", "BLOB_CONFIG", ""),
      "sql-config" -> OptionParam("sqlc", "Authentication config for Azure SQL Database (jdbc)", "SQL_CONFIG", ""),
      "snowflake-config" -> OptionParam("sfc", "Authentication config for Snowflake Database (jdbc)", "SNOWFLAKE_CONFIG", "")
    )

    val extraOptions = List(new CmdOption("LOCALMODE", "local-mode", false, "Run in local mode"))

    val cmdParser = new CmdLineParser(args, params, extraOptions)

    val joinConfig = cmdParser.extractRequiredValue("join-config")

    val inputData = {
      val input = cmdParser.extractRequiredValue("input")
      val sourceType = SourceFormatType.withName(cmdParser.extractRequiredValue("source-type"))
      val startDate = cmdParser.extractOptionalValue("start-date")
      val endDate = cmdParser.extractOptionalValue("end-date")
      val numDays = cmdParser.extractOptionalValue("num-days")
      val dateOffset = cmdParser.extractOptionalValue("date-offset")

      InputData(input, sourceType, startDate, endDate, dateOffset, numDays)
    }

    val passThroughFeatures = {
      cmdParser.extractRequiredValue("pass-through-features") match {
        case "" => Set.empty[String]
        case str => str.split(",") map (_.trim) toSet
      }
    }

    val joinJobContext = {
      val feathrLocalConfig = cmdParser.extractOptionalValue("feathr-config")
      val feathrFeatureConfig = cmdParser.extractOptionalValue("feature-config")
      val localOverrideAll = cmdParser.extractRequiredValue("local-override-all")
      val outputPath = cmdParser.extractRequiredValue("output")
      val numParts = cmdParser.extractRequiredValue("num-parts").toInt

      JoinJobContext(
        feathrLocalConfig,
        feathrFeatureConfig,
        Some(inputData),
        outputPath,
        numParts
        )
    }

    val dataSourceConfigs = DataSourceConfigUtils.getConfigs(cmdParser)
    FeathrJoinJobContext(joinConfig, joinJobContext, dataSourceConfigs)
  }

  type KeyTag = Seq[String]
  type FeatureName = String

  /**
   */
  def parseJoinConfig(joinConfString: String): FeatureJoinConfig = FeatureJoinConfig.parseJoinConfig(joinConfString)

  /**
   * Load the DataFrames for sources that needs preprocessing by Pyspark.
   * @param args Same arguments for the main job.
   * @param featureNamesInAnchorSet A set of feature names of an anchor sorted and joined by comma. For example,
   *                                anchor1 -> f1, f2, anchor2 -> f3, f4. Then the set is ("f1,f2", "f3,f4")
   * @return A Java map whose key is Feature names of an anchor sorted and joined by comma and value is the DataFrame
   *         for this anchor source. For example, anchor1 -> f1, f2, anchor2 -> f3, f4. Then the result is
   *         Map("f1,f2" -> df1, "f3,f4" -> df2).
   */
  def loadSourceDataframe(args: Array[String], featureNamesInAnchorSet: java.util.Set[String]): java.util.Map[String, DataFrame] = {
    logger.info("FeatureJoinJob args are: " + args)
    logger.info("Feature join job: loadDataframe")
    logger.info(featureNamesInAnchorSet)
    val feathrJoinPreparationInfo = prepareSparkSession(args)
    val sparkSession = feathrJoinPreparationInfo.sparkSession
    val hadoopConf = feathrJoinPreparationInfo.hadoopConf
    val jobContext = feathrJoinPreparationInfo.jobContext

    // check read authorization for observation data, and write authorization for output path
    checkAuthorization(sparkSession, hadoopConf, jobContext)

    // Doesn't support loading local test client for this yet
    val feathrClient = getFeathrClient(sparkSession, jobContext.jobJoinContext)
    val allAnchoredFeatures = feathrClient.allAnchoredFeatures

    // Using AnchorToDataSourceMapper to load DataFrame for preprocessing
    val failOnMissing = FeathrUtils.getFeathrJobParam(sparkSession, FeathrUtils.FAIL_ON_MISSING_PARTITION).toBoolean
    val anchorToDataSourceMapper = new AnchorToDataSourceMapper()
    val anchorsWithSource = anchorToDataSourceMapper.getBasicAnchorDFMapForJoin(
      sparkSession,
      allAnchoredFeatures.values.toSeq,
      failOnMissing)

    // Only load DataFrames for anchors that have preprocessing UDF
    // So we filter out anchors that doesn't have preprocessing UDFs
    // We use feature names sorted and merged as the key to find the anchor
    // For example, f1, f2 belongs to anchor. Then Map("f1,f2"-> anchor)
    val dataFrameMapForPreprocessing = anchorsWithSource
      .filter(x => featureNamesInAnchorSet.contains(x._1.featureAnchor.features.toSeq.sorted.mkString(",")))
      .map(x => (x._1.featureAnchor.features.toSeq.sorted.mkString(","), x._2.get()))

    // Pyspark only understand Java map so we need to convert Scala map back to Java map.
    dataFrameMapForPreprocessing.asJava
  }

  def mainWithPreprocessedDataFrame(args: Array[String], preprocessedDfMap: java.util.Map[String, DataFrame]) {
    // Set the preprocessed DataFrame here for future usage.
    PreprocessedDataFrameManager.preprocessedDfMap = preprocessedDfMap.asScala.toMap

    main(args)
  }

  def main(args: Array[String]) {
    logger.info("FeatureJoinJob args are: " + args)
    val feathrJoinPreparationInfo = prepareSparkSession(args)

    run(feathrJoinPreparationInfo.sparkSession, feathrJoinPreparationInfo.hadoopConf, feathrJoinPreparationInfo.jobContext)
  }

  def prepareSparkSession(args: Array[String]): FeathrJoinPreparationInfo = {
    val jobContext = parseInputArgument(args)

    val sparkConf = new SparkConf().registerKryoClasses(Array(classOf[GenericRecord]))
    // sparkConf.set("spark.kryo.registrator", "org.apache.spark.serializer.AvroGenericArrayKryoRegistrator")

    val sparkSessionBuilder = SparkSession
      .builder()
      .config(sparkConf)
      .appName(getClass.getName)
      .enableHiveSupport()

    val sparkSession = sparkSessionBuilder.getOrCreate()
    val conf = sparkSession.sparkContext.hadoopConfiguration

    DataSourceConfigUtils.setupHadoopConf(sparkSession, jobContext.dataSourceConfigs)

    FeathrUdfRegistry.registerUdf(sparkSession)
    HdfsUtils.deletePath(jobContext.jobJoinContext.outputPath, recursive = true, conf)

    val enableDebugLog = FeathrUtils.getFeathrJobParam(sparkConf, FeathrUtils.ENABLE_DEBUG_OUTPUT).toBoolean
    if (enableDebugLog) {
      Logger.getRootLogger.setLevel(Level.DEBUG)
    }

    FeathrJoinPreparationInfo(sparkSession, conf, jobContext)
  }
}

case class FeathrJoinPreparationInfo(sparkSession: SparkSession, hadoopConf: Configuration, jobContext: FeathrJoinJobContext)

case class FeathrJoinJobContext(joinConfig: String, jobJoinContext: JoinJobContext, dataSourceConfigs: DataSourceConfigs) {}

/**
 * This case class describes feature record after join process
 */
case class FeathrJointFeatureRecord[L: ClassTag](observation: L, joinedFeatures: Map[StringTaggedFeatureName, common.FeatureValue])

package com.linkedin.feathr.offline.source.dataloader

import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrInputDataException}
import com.linkedin.feathr.offline.config.location.DataLocation
import com.linkedin.feathr.offline.generation.SparkIOUtils
import com.linkedin.feathr.offline.job.DataSourceUtils.getSchemaFromAvroDataFile
import com.linkedin.feathr.offline.source.dataloader.DataLoaderHandler
import com.linkedin.feathr.offline.util.DelimiterUtils.checkDelimiterOption
import org.apache.avro.Schema
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * load data from HDFS path .
 * @param ss the spark session
 * @param path input data path
 */
private[offline] class BatchDataLoader(ss: SparkSession, location: DataLocation, dataLoaderHandlers: List[DataLoaderHandler]) extends DataLoader {

  /**
   * get the schema of the source. It's only used in the deprecated DataSource.getDataSetAndSchema
   * @return an Avro Schema
   */
  override def loadSchema(): Schema = {
    val conf = ss.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(conf)
    val status = fs.listStatus(new Path(location.getPath))

    // paths of all the avro files in the directory
    val avroFiles = status.filter(_.getPath.getName.endsWith(".avro"))

    // return null if directory doesn't contain any avro file.
    if (avroFiles.length == 0) {
      throw new FeathrInputDataException(ErrorLabel.FEATHR_USER_ERROR, s"Load the Avro schema for Avro data set in HDFS but no avro files found in ${location.getPath}.")
    }

    // get the first avro file in the directory
    val dataFileName = avroFiles(0).getPath.getName
    val dataFilePath = new Path(location.getPath, dataFileName).toString

    // Get the schema of the avro GenericRecord
    getSchemaFromAvroDataFile(dataFilePath, new JobConf(conf))
  }

  /**
   * load the source data as dataframe.
   * @return an dataframe
   */
  override def loadDataFrame(): DataFrame = {
    loadDataFrame(Map(), new JobConf(ss.sparkContext.hadoopConfiguration))
  }

  /**
   * load the source data as dataframe.
   * @param dataIOParameters extra parameters
   * @param jobConf Hadoop JobConf to be passed
   * @return an dataframe
   */
  def loadDataFrame(dataIOParameters: Map[String, String], jobConf: JobConf): DataFrame = {
    val sparkConf = ss.sparkContext.getConf
    val inputSplitSize = sparkConf.get("spark.feathr.input.split.size", "")
    val dataIOParametersWithSplitSize = Map(SparkIOUtils.SPLIT_SIZE -> inputSplitSize) ++ dataIOParameters
    val dataPath = location.getPath

    log.info(s"Loading ${location} as DataFrame, using parameters ${dataIOParametersWithSplitSize}")

    // Get csvDelimiterOption set with spark.feathr.inputFormat.csvOptions.sep and check if it is set properly (Only for CSV and TSV)
    val csvDelimiterOption = checkDelimiterOption(ss.sqlContext.getConf("spark.feathr.inputFormat.csvOptions.sep", ","))

    try {
      import scala.util.control.Breaks._

      var dfOpt: Option[DataFrame] = None
      breakable {
        for(dataLoaderHandler <- dataLoaderHandlers) {
          println(s"Applying dataLoaderHandler ${dataLoaderHandler}")
          if (dataLoaderHandler.validatePath(dataPath)) {
            dfOpt = Some(dataLoaderHandler.createDataFrame(dataPath, dataIOParametersWithSplitSize, jobConf))
            break
          }
        }
      }
      val df = dfOpt match {
        case Some(df) => df
        case _ => location.loadDf(ss, dataIOParametersWithSplitSize)
      }
      df
    } catch {
      case feathrException: FeathrInputDataException =>
        println(feathrException.toString)
        throw feathrException // Throwing exception to avoid dataLoaderHandler hook exception from being swallowed.
      case e: Throwable => //TODO: Analyze all thrown exceptions, instead of swalling them all, and reading as a csv
        println(e.toString)
        ss.read.format("csv").option("header", "true").option("delimiter", csvDelimiterOption).load(dataPath)
    }
  }
}
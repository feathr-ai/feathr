package com.linkedin.feathr.offline.source.dataloader

import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrInputDataException}
import com.linkedin.feathr.offline.config.location.DataLocation
import com.linkedin.feathr.offline.generation.SparkIOUtils
import com.linkedin.feathr.offline.job.DataSourceUtils.getSchemaFromAvroDataFile
import org.apache.avro.Schema
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * load data from HDFS path .
 * @param ss the spark session
 * @param path input data path
 */
private[offline] class BatchDataLoader(val ss: SparkSession,
                                       val location: DataLocation,
                                       val dataLoaderHandlers: List[DataLoaderHandler]) extends DataLoader {

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
     loadDataFrameWithRetry(Map(), new JobConf(ss.sparkContext.hadoopConfiguration), MAX_DATA_LOAD_RETRY)
  }
  /**
   * load the source data as dataframe.
   * @param dataIOParameters extra parameters
   * @param jobConf Hadoop JobConf to be passed
   * @param retry number of times to retry when data loading fails
   * @return an dataframe
   */
  def loadDataFrameWithRetry(dataIOParameters: Map[String, String], jobConf: JobConf, retry: Int): DataFrame = {
    val sparkConf = ss.sparkContext.getConf
    val inputSplitSize = sparkConf.get("spark.feathr.input.split.size", "")
    val dataIOParametersWithSplitSize = Map(SparkIOUtils.SPLIT_SIZE -> inputSplitSize) ++ dataIOParameters
    val dataPath = location.getPath

    log.info(s"Loading ${location} as DataFrame, using parameters ${dataIOParametersWithSplitSize}")

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
      case _: Throwable =>
        // If data loading from source failed, retry it automatically, as it might due to data source still being written into.
        log.info(s"Loading ${location} failed, retrying for ${retry}-th time..")
        if (retry > 0) {
          Thread.sleep(DATA_LOAD_WAIT_IN_MS)
          loadDataFrameWithRetry(dataIOParameters, jobConf, retry - 1)
        } else {
          // Throwing exception to avoid dataLoaderHandler hook exception from being swallowed.
          throw new FeathrInputDataException(ErrorLabel.FEATHR_USER_ERROR, s"Failed to load ${dataPath} after ${MAX_DATA_LOAD_RETRY} retries.")
        }
    }
  }
  // Retry 2 times if data source loading fails
  val MAX_DATA_LOAD_RETRY = 2
  // Wait for 10 minutes and retry if data source loading fails
  val DATA_LOAD_WAIT_IN_MS = 10*60*1000
}
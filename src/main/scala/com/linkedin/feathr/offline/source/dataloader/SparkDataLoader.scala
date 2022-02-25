package com.linkedin.feathr.offline.source.dataloader

import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrInputDataException}
import com.linkedin.feathr.offline.generation.SparkIOUUtil
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
private[offline] class SparkDataLoader(ss: SparkSession, path: String) extends DataLoader {

  /**
   * get the schema of the source. It's only used in the deprecated DataSource.getDataSetAndSchema
   * @return an Avro Schema
   */
  override def loadSchema(): Schema = {
    val conf = ss.sparkContext.hadoopConfiguration
    val fs = FileSystem.get(conf)
    val status = fs.listStatus(new Path(path))

    // paths of all the avro files in the directory
    val avroFiles = status.filter(_.getPath.getName.endsWith(".avro"))

    // return null if directory doesn't contain any avro file.
    if (avroFiles.length == 0) {
      throw new FeathrInputDataException(ErrorLabel.FEATHR_USER_ERROR, s"Load the Avro schema for Avro data set in HDFS but no avro files found in $path.")
    }

    // get the first avro file in the directory
    val dataFileName = avroFiles(0).getPath.getName
    val dataFilePath = new Path(path, dataFileName).toString

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
    val dataIOParametersWithSplitSize = Map(SparkIOUUtil.SPLIT_SIZE -> inputSplitSize) ++ dataIOParameters
    log.info(s"Loading ${path} as DataFrame, using parameters ${dataIOParametersWithSplitSize}")
    try {
      SparkIOUUtil.createDataFrame(path, dataIOParametersWithSplitSize)
    } catch {
      case _: Throwable =>
        ss.read.format("csv").option("header", "true").load(path)
    }
  }
}

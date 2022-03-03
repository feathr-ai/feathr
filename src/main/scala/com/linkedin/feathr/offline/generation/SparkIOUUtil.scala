package com.linkedin.feathr.offline.generation
import com.linkedin.feathr.common.exception.FeathrException
import org.apache.avro.generic.GenericRecord
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

object SparkIOUUtil {


  def createUnionDataFrame(existingHdfsPaths: Seq[String], dataIOParameters: Map[String, String] = Map()): DataFrame = {
    // existingHdfsPaths may be folder or file with suffix
    // Currently only support parquet file but not folder with parquet files
    val format = if (existingHdfsPaths.head.endsWith(".parquet")) PARQUET else dataIOParameters.getOrElse(DATA_FORMAT, AVRO).toUpperCase
    val df = format match {
      case AVRO =>
        spark.read.format(AVRO_DATASOURCE).load(existingHdfsPaths: _*)
      case ORC =>
        spark.read.format(ORC_DATASOURCE).load(existingHdfsPaths: _*)
      case PARQUET =>
        spark.read.parquet(existingHdfsPaths: _*)
      case _ => throw new FeathrException(s"Unsupported data format $format. Only AVRO and ORC are supported.")
    }
    df
  }

  def createDataFrame(path: String, dataIOParams: Map[String, String] = Map()): DataFrame = {
    createUnionDataFrame(Seq(path), dataIOParams)
  }

  def writeDataFrame(outputDF: DataFrame, path: String, parameters: Map[String, String] = Map()): DataFrame = {
    outputDF.write.mode(SaveMode.Overwrite).format("avro").save(path)
    outputDF
  }

  def createGenericRDD(inputPath: String, dataIOParameters: Map[String, String], jobConf: JobConf): RDD[GenericRecord] = ???

  private lazy val spark = SparkSession.builder.getOrCreate


  val OUTPUT_SCHEMA = "output.schema"
  val DATA_FORMAT = "data.format"
  val OUTPUT_PARALLELISM = "output.parallelism"
  val SPLIT_SIZE = "split.size"
  val OVERWRITE_MODE = "override.mode"
  val FILTER_EXP = "filter.exp"

  // String constants used in reader/writer APIs
  private val AVRO = "AVRO"
  private val ORC = "ORC"
  private val PARQUET = "PARQUET"
  private val JDBC = "JDBC"
  private val ALL_OVERWRITE = "ALL"
  private val PARTITION_OVERWRITE = "PARTITION"
  private val NO_OVERWRITE = "NONE"
  private val APPEND_NO_OVERWRITE = "APPEND"
  private val AVRO_DATASOURCE = "com.databricks.spark.avro"
  private val AVRO_SCHEMA = "avroSchema"

  // Use Spark native orc reader instead of hive-orc since Spark 2.3
  private val ORC_DATASOURCE = "org.apache.spark.sql.execution.datasources.orc"
}

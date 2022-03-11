package com.linkedin.feathr.offline.source.dataloader.hdfs

import com.linkedin.feathr.common.exception.FeathrException
import com.linkedin.feathr.offline.source.dataloader._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame


object FileFormat {
  private lazy val ss = SparkSession.builder.getOrCreate

  val CSV = "CSV"
  val AVRO_JSON = "AVRO_JSON"
  val AVRO = "AVRO"
  val ORC = "ORC"
  val PARQUET = "PARQUET"
  val PATHLIST = "PATHLIST"
  val JDBC = "JDBC"

  private val AVRO_DATASOURCE = "com.databricks.spark.avro"
  // Use Spark native orc reader instead of hive-orc since Spark 2.3
  private val ORC_DATASOURCE = "org.apache.spark.sql.execution.datasources.orc"

  val DATA_FORMAT = "data.format"

  /**
   * To define if the file is JDBC, Single File or Path list (default)
   * @param path
   * @return
   */
  def getType(path: String): String = {
    val p = path.toLowerCase()
    p match {
      case p if p.endsWith(".csv") => CSV
      case p if p.endsWith(".parquet") => PARQUET
      case p if p.endsWith(".orc") => ORC
      case p if p.endsWith(".avro.json") => AVRO_JSON
      case p if p.startsWith("jdbc:") => JDBC
      case _ => PATHLIST}
  }

  // TODO: Complete a general loadDataFrame and replace current adhoc load data frame code
  def loadDataFrame(ss: SparkSession, path: String, format: String = CSV): DataFrame = {
    format match {
      case AVRO => new AvroJsonDataLoader(ss, path).loadDataFrame()
      case CSV => ss.read.format("csv").option("header", "true").load(path)
      case PARQUET => new ParquetDataLoader(ss, path).loadDataFrame()
      case _ => ???
    }
  }

  // TODO: How can we merge below 2 functions into the general logic? They are refactored from SparkIOUtils
  // existingHdfsPaths may be folder or file with suffix
  // Currently only support parquet file but not folder with parquet files
  def getTypeForUnionDF(existingHdfsPaths: Seq[String], dataIOParameters: Map[String, String] = Map()): String = {
    if (existingHdfsPaths.head.endsWith(".parquet")) PARQUET else dataIOParameters.getOrElse(DATA_FORMAT, AVRO).toUpperCase
  }

  def loadHdfsDataFrame(format: String, existingHdfsPaths: Seq[String]): DataFrame = {
    val df = format match {
      case CSV =>
        ss.read.format("csv").option("header", "true").load(existingHdfsPaths: _*)
      case AVRO =>
        ss.read.format(AVRO_DATASOURCE).load(existingHdfsPaths: _*)
      case ORC =>
        ss.read.format(ORC_DATASOURCE).load(existingHdfsPaths: _*)
      case PARQUET =>
        ss.read.parquet(existingHdfsPaths: _*)
      case _ => throw new FeathrException(s"Unsupported data format $format. Only AVRO and ORC are supported.")
    }
    df
  }
}

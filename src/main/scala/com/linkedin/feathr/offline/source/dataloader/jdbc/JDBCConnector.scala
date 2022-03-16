package com.linkedin.feathr.offline.source.dataloader.jdbc

import org.apache.spark.sql._

abstract class JdbcConnector(ss: SparkSession){
  lazy val _ss: SparkSession = ss
  val format = "jdbc"

  /**
   * jdbc Options needs to contain necessary information except jdbc url, include:
   * driver option, authentication options, dbTable option, etc.
   * extend a new class when new JDBC data connector is added.
   * @param jdbcOptions
   * @return
   */
  def getDFReader(jdbcOptions: Map[String, String]): DataFrameReader = {
    _ss.read.format(format).options(jdbcOptions)
  }

  def getDFWriter(df:DataFrame, jdbcOptions: Map[String, String]): DataFrameWriter[Row] = {
    df.write.format(format).options(jdbcOptions)
  }

  def loadDataFrame(url: String, jdbcOptions: Map[String, String] = Map[String, String]()): DataFrame = {
    val sparkReader = getDFReader(jdbcOptions)
    sparkReader.option("url", url).load()
  }
}




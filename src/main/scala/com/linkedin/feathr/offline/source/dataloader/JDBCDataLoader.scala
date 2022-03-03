package com.linkedin.feathr.offline.source.dataloader

import org.apache.avro.Schema
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

/**
 * Load JDBC file for testing.
 * @param ss the spark session
 * @param url input resource path
 */
private[offline] class JDBCDataLoader(ss: SparkSession, url: String) extends DataLoader {
  override def loadSchema(): Schema = {
    ???
  }

  /**
   * load the source data as DataFrame with username and password.
   */
  override def loadDataFrame(): DataFrame = {
    ss.read.format("jdbc")
      .option("url", url)
      .option("dbtable", ss.conf.get("jdbc.table"))
      .option("user", ss.conf.get("jdbc.user"))
      .option("password", ss.conf.get("jdbc.password"))
      .load()
  }

  // TODO: Enable Access token Authentication
  def loadDataFrame(accessToken: String): DataFrame = {
    ss.read
      .format("com.microsoft.sqlserver.jdbc.spark")
      .option("url", url)
      .option("dbtable", ss.conf.get("jdbc.table"))
      .option("accessToken", accessToken)
      .option("encrypt", "true")
      .option("hostNameInCertificate", "*.database.windows.net")
      .load()
  }
}
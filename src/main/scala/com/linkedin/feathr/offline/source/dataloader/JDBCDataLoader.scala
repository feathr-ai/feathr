package com.linkedin.feathr.offline.source.dataloader

import com.linkedin.feathr.offline.source.dataloader.jdbc.JDBCUtils
import org.apache.avro.Schema
import org.apache.spark.sql.{DataFrame, SparkSession}

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
   * load the source data as DataFrame
   */
  override def loadDataFrame(): DataFrame = {
    JDBCUtils.loadDataFrame(ss, url)
  }
}
package com.linkedin.feathr.offline.source.dataloader

import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrException}
import com.linkedin.feathr.offline.source.dataloader.jdbc.JdbcUtils
import org.apache.avro.Schema
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Load JDBC file for testing.
 * @param ss the spark session
 * @param url input resource path
 */
private[offline] class JdbcDataLoader(ss: SparkSession, url: String) extends DataLoader {
  override def loadSchema(): Schema = {
    ???
  }

  /**
   * load the source data as DataFrame
   */
  override def loadDataFrame(): DataFrame = {
    JdbcUtils.loadDataFrame(ss, url)
  }
}
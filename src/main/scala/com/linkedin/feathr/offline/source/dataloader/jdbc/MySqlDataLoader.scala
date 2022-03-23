package com.linkedin.feathr.offline.source.dataloader.jdbc

import com.linkedin.feathr.offline.source.dataloader.jdbc.JdbcUtils.DRIVER_CONF
import org.apache.spark.sql.{DataFrameReader, SparkSession}

/**
 * This is used for Mysql data source JDBC connector
 * the default driver is set and can be overwrite by custom value in jdbcOptions
 * @param ss
 */
class MySqlDataLoader(ss: SparkSession) extends JdbcConnector(ss) {
  val MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver"
  override def getDFReader(jdbcOptions: Map[String, String], url: String): DataFrameReader = {
    if (jdbcOptions.contains(DRIVER_CONF)) {
      _ss.read.format(format).options(jdbcOptions)
    } else{
      _ss.read.format(format).options(jdbcOptions).option("driver", MYSQL_JDBC_DRIVER)
    }
  }
}
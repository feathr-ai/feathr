package com.linkedin.feathr.offline.source.dataloader.jdbc

import com.linkedin.feathr.offline.source.dataloader.jdbc.JDBCUtils.DRIVER_CONF
import org.apache.spark.sql.{DataFrameReader, SparkSession}

class MySqlDataLoader(ss: SparkSession) extends JDBCConnector(ss) {
  val MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver"
  override def getDFReader(jdbcOptions: Map[String, String]): DataFrameReader = {
    if (jdbcOptions.contains(DRIVER_CONF)) {
      _ss.read.format(format).options(jdbcOptions)
    } else{
      _ss.read.format(format).options(jdbcOptions).option("driver", MYSQL_JDBC_DRIVER)
    }
  }
}
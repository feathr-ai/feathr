package com.linkedin.feathr.offline.source.dataloader.jdbc

import org.apache.spark.sql.{DataFrame, SparkSession}

class SqlServerDataLoader(ss: SparkSession) extends JDBCConnector(ss){
  override val format = "com.microsoft.sqlserver.jdbc.spark"
}

package com.linkedin.feathr.offline.source.dataloader.jdbc

import org.apache.spark.sql.{DataFrame, SparkSession}

sealed trait SqlType

/**
 * Each Sql Type represents a SQL data source
 * When enabling new JDBC data source:
 * 1. A new SqlType will be added.
 * 2. Update getType tp identify this JDBC source
 * 3. The JDBC driver needs to be checkin in SBT dependencies
 * 4. A specific DataLoader needs to be added to specify the driver or add custom logics
 */
object SqlType {
  case object SqlServer extends SqlType
  case object MySql extends SqlType
  case object Postgres extends SqlType
  case object DefaultJDBC extends SqlType

  def getType (url: String): SqlType = url match {
    case url if url.startsWith("jdbc:sqlserver") => SqlServer
    case url if url.startsWith("jdbc:mysql") => MySql
    case url if url.startsWith("jdbc:postgresql:") => Postgres
    case _ => DefaultJDBC
  }

  def loadDataFrame(ss: SparkSession, url: String, options: Map[String, String]): DataFrame = {
    val sqlType = getType(url)
    val dataLoader = sqlType match {
      case SqlServer => new SqlServerDataLoader(ss)
      case MySql => new MySqlDataLoader(ss)
      case _ => new SqlServerDataLoader(ss)  //default jdbc data loader place holder
    }
    dataLoader.loadDataFrame(url, options)
  }
}

package com.linkedin.feathr.offline.source.dataloader.jdbc

import org.apache.spark.sql.{DataFrame, SparkSession}

sealed trait SqlDbType

/**
 * Each Sql Type represents a SQL data source
 * When enabling new JDBC data source:
 * 1. A new SqlDbType will be added.
 * 2. Update getType tp identify this JDBC source
 * 3. The JDBC driver needs to be checkin in SBT dependencies
 * 4. A specific DataLoader needs to be added to specify the driver or add custom logics
 */
object SqlDbType {
  case object SqlServer extends SqlDbType
  case object MySql extends SqlDbType
  case object Postgres extends SqlDbType
  case object DefaultJDBC extends SqlDbType

  def getType (url: String): SqlDbType = url match {
    case url if url.startsWith("jdbc:sqlserver") => SqlServer
    case url if url.startsWith("jdbc:mysql") => MySql
    case url if url.startsWith("jdbc:postgresql:") => Postgres
    case _ => DefaultJDBC
  }

  def loadDataFrame(ss: SparkSession, url: String, options: Map[String, String]): DataFrame = {
    val sqlDbType = getType(url)
    val dataLoader = sqlDbType match {
      case SqlServer => new SqlServerDataLoader(ss)
      case MySql => new MySqlDataLoader(ss)
      case _ => new SqlServerDataLoader(ss)  //default jdbc data loader place holder
    }
    dataLoader.loadDataFrame(url, options)
  }
}

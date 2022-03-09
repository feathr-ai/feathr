package com.linkedin.feathr.offline.source.dataloader.jdbc

import org.apache.spark.sql.{DataFrame, SparkSession}

sealed trait SQLType

object SQLType {
  case object SqlServer extends SQLType
  case object MySql extends SQLType
  case object Postgres extends SQLType
  case object DefaultJDBC extends SQLType

  def getType (url: String): SQLType = url match {
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

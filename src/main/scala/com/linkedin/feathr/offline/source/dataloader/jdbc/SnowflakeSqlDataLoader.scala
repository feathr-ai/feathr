package com.linkedin.feathr.offline.source.dataloader.jdbc

import org.apache.commons.httpclient.URI
import org.apache.http.client.utils.URLEncodedUtils
import org.apache.spark.sql.{DataFrame, DataFrameReader, SparkSession}

import scala.collection.JavaConverters.asScalaBufferConverter
import java.nio.charset.Charset

/**
 * This is used for Snowflake data source JDBC connector
 *
 */
class SnowflakeSqlDataLoader(ss: SparkSession) extends JdbcConnector(ss) {
  val SNOWFLAKE_SOURCE_NAME = "net.snowflake.spark.snowflake"

  override def getDFReader(jdbcOptions: Map[String, String], url: String): DataFrameReader = {
    val dfReader = _ss.read
      .format(SNOWFLAKE_SOURCE_NAME)
      .options(jdbcOptions)

    val uri = new URI(url)
    val charset = Charset.forName("UTF-8")
    val params = URLEncodedUtils.parse(uri.getQuery, charset).asScala
    params.foreach(x => {
      dfReader.option(x.getName, x.getValue)
    })
    dfReader
  }

  override def extractJdbcOptions(ss: SparkSession, url: String): Map[String, String] = {
      val jdbcOptions1 = getJdbcParams(ss)
      val jdbcOptions2 = getJdbcAuth(ss)
    jdbcOptions1 ++ jdbcOptions2
  }

  def getJdbcParams(ss: SparkSession): Map[String, String] = {
    Map[String, String](
      "sfURL" -> ss.conf.get("sfURL"),
      "sfUser" -> ss.conf.get("sfUser"),
      "sfRole" -> ss.conf.get("sfRole"),
    )
  }

  def getJdbcAuth(ss: SparkSession): Map[String, String] = {
    // If user set password, then we use password to auth
    ss.conf.getOption("sfPassword") match {
      case Some(_) =>
        Map[String, String](
          "sfUser" -> ss.conf.get("sfUser"),
          "sfRole" -> ss.conf.get("sfRole"),
          "sfPassword" -> ss.conf.get("sfPassword"),
        )
      case _ => {
        // TODO Add token support
        Map[String, String]()
      }
    }
  }

  override def loadDataFrame(url: String, jdbcOptions: Map[String, String] = Map[String, String]()): DataFrame = {
    val sparkReader = getDFReader(jdbcOptions, url)
    sparkReader
      .option("url", url)
      .load()
  }
}
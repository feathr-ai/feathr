package com.linkedin.feathr.offline.config.location

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.offline.source.dataloader.jdbc.JdbcUtils
import com.linkedin.feathr.offline.source.dataloader.jdbc.JdbcUtils.DBTABLE_CONF
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.eclipse.jetty.util.StringUtil

@CaseClassDeserialize()
case class Jdbc(url: String, @JsonAlias(Array("query")) dbtable: String, user: String = "", password: String = "", token: String = "", useToken: Boolean = false, anonymous: Boolean = false) extends InputLocation {
  override def loadDf(ss: SparkSession, dataIOParameters: Map[String, String] = Map()): DataFrame = {
    println(s"Jdbc.loadDf, location is ${this}")
    var reader = ss.read.format("jdbc")
      .option("url", url)
    if (StringUtil.isBlank(dbtable)) {
      // Fallback to default table name
      reader = reader.option("dbtable", ss.conf.get(DBTABLE_CONF))
    } else {
      val q = dbtable.trim
      if("\\s".r.findFirstIn(q).nonEmpty) {
        // This is a SQL instead of a table name
        reader = reader.option("query", q)
      } else {
        reader = reader.option("dbtable", q)
      }
    }
    if (useToken) {
      reader.option("accessToken", LocationUtils.envSubstitute(token))
        .option("hostNameInCertificate", "*.database.windows.net")
        .option("encrypt", true)
        .load
    } else {
      if (StringUtil.isBlank(user) && StringUtil.isBlank(password)) {
        if (anonymous) {
          reader.load()
        } else {
          // Fallback to global JDBC credential
          println("Fallback to default credential")
          ss.conf.set(DBTABLE_CONF, dbtable)
          JdbcUtils.loadDataFrame(ss, url)
        }
      } else {
        reader.option("user", LocationUtils.envSubstitute(user))
          .option("password", LocationUtils.envSubstitute(password))
      }.load
    }
  }

  override def getPath: String = url

  override def getPathList: List[String] = List(url)

  override def isFileBasedLocation(): Boolean = false

  // These members don't contain actual secrets
  override def toString: String = s"Jdbc(url=$url, dbtable=$dbtable, useToken=$useToken, anonymous=$anonymous, user=$user, password=$password, token=$token)"
}

object Jdbc {
  /**
   * Create JDBC InputLocation with required info and user/password auth
   * @param url
   * @param dbtable
   * @param user
   * @param password
   * @return Newly created InputLocation instance
   */
  def apply(url: String, dbtable: String, user: String, password: String): Jdbc = Jdbc(url, dbtable, user = user, password = password, useToken = false)

  /**
   * Create JDBC InputLocation with required info and OAuth token auth
   * @param url
   * @param dbtable
   * @param token
   * @return Newly created InputLocation instance
   */
  def apply(url: String, dbtable: String, token: String): Jdbc = Jdbc(url, dbtable, token = token, useToken = true)

  /**
   * Create JDBC InputLocation with required info and OAuth token auth
   * In this case, the auth info is taken from default setting passed from CLI/API, details can be found in `Jdbc#loadDf`
   * @see com.linkedin.feathr.offline.source.dataloader.jdbc.JDBCUtils#loadDataFrame
   * @param url
   * @param dbtable
   * @return Newly created InputLocation instance
   */
  def apply(url: String, dbtable: String): Jdbc = Jdbc(url, dbtable, useToken = false)
}

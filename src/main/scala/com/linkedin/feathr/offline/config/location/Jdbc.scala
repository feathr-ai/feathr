package com.linkedin.feathr.offline.config.location

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.offline.source.dataloader.jdbc.JdbcUtils
import com.linkedin.feathr.offline.source.dataloader.jdbc.JdbcUtils.DBTABLE_CONF
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.eclipse.jetty.util.StringUtil

@CaseClassDeserialize()
case class Jdbc(url: String, dbtable: String, user: String = "", password: String = "", token: String = "", useToken: Boolean = false) extends InputLocation {
  override def loadDf(ss: SparkSession): DataFrame = {
    var reader = ss.read.format("jdbc")
      .option("url", url)
    if (StringUtil.isBlank(dbtable)) {
      // Fallback to default table name
      reader = reader.option("dbtable", ss.conf.get(DBTABLE_CONF))
    } else {
      reader = reader.option("dbtable", dbtable)
    }
    if (useToken) {
      reader.option("accessToken", LocationUtils.envSubstitute(token)).load
    } else {
      if (StringUtil.isBlank(user) && StringUtil.isBlank(password)) {
        // Fallback to global JDBC credential
        JdbcUtils.loadDataFrame(ss, url)
      } else {
        reader.option("user", LocationUtils.envSubstitute(user))
          .option("password", LocationUtils.envSubstitute(password))
      }.load
    }
  }

  override def getPath: String = url

  override def getPathList: Array[String] = Array(url)
}

object Jdbc {
  def apply(url: String, dbtable: String, user: String, password: String): Jdbc = Jdbc(url, dbtable, user = user, password = password, useToken = false)

  def apply(url: String, dbtable: String, token: String): Jdbc = Jdbc(url, dbtable, token = token, useToken = true)

  def apply(url: String, dbtable: String): Jdbc = Jdbc(url, dbtable, useToken = false)
}

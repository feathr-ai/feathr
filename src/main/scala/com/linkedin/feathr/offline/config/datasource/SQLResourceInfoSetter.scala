package com.linkedin.feathr.offline.config.datasource

import org.apache.spark.SparkConf

import java.util.Properties

private[feathr] class SQLResourceInfoSetter extends ResourceInfoSetter() {
  val JDBC_TABLE = "JDBC_TABLE"
  val JDBC_USER = "JDBC_USER"
  val JDBC_PASSWORD = "JDBC_PASSWORD"

  override val params = List(JDBC_TABLE, JDBC_USER, JDBC_PASSWORD)

  def setupSparkConf(sparkConf: SparkConf, context: Option[DataSourceConfig], resource: Option[Resource]): Unit = {
    val table = getAuthStr(JDBC_TABLE, context, resource)
    val user = getAuthStr(JDBC_USER, context, resource)
    val password = getAuthStr(JDBC_PASSWORD, context, resource)

    sparkConf.set("jdbc.table", table)
    sparkConf.set("jdbc.user", user)
    sparkConf.set("jdbc.password", password)
  }


  def getAuthFromConfig(str: String, resource: Resource): String = {
    str match {
      case JDBC_TABLE => resource.azureResource.jdbcTable
      case JDBC_USER => resource.azureResource.jdbcUser
      case JDBC_PASSWORD => resource.azureResource.jdbcPassword
      case _ => EMPTY_STRING
    }
  }
}

private[feathr] object SQLResourceInfoSetter{
  val sqlSetter = new SQLResourceInfoSetter()

  def setup(sparkConf: SparkConf, config: DataSourceConfig, resource: Resource): Unit ={
    sqlSetter.setupSparkConf(sparkConf, Some(config), Some(resource))
  }
}
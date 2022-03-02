package com.linkedin.feathr.offline.config.datasource

import java.util.Properties

private[feathr] class SQLResourceInfoSetter extends ResourceInfoSetter() {
  val JDBC_USER = "JDBC_USER"
  val JDBC_PASSWORD = "JDBC_PASSWORD"

  override val params = List(JDBC_USER, JDBC_PASSWORD)

  def setup(jdbcProperties: Properties, context: DataSourceConfig, resource : Resource): Unit = {
    setupProperties(jdbcProperties, Some(context), Some(resource))
  }

  def setupProperties(jdbcProperties: Properties, context: Option[DataSourceConfig] = None, resource: Option[Resource] = None): Unit = {
    jdbcProperties.put("user", getAuthStr(JDBC_USER, context, resource))
    jdbcProperties.put("password", getAuthStr(JDBC_PASSWORD, context, resource))
  }

  def getAuthFromConfig(str: String, resource: Resource): String = {
    str match {
      case JDBC_USER => resource.azureResource.jdbcUser
      case JDBC_PASSWORD => resource.azureResource.jdbcPassword
      case _ => EMPTY_STRING
    }
  }
}
package com.linkedin.feathr.offline.config.datasource

import java.util.Properties

private[feathr] class SQLConfigExtractor extends ConfigExtractor() {
  val JDBC_USER = "JDBC_USER"
  val JDBC_PASSWORD = "JDBC_PASSWORD"

  override val params = List(JDBC_USER, JDBC_PASSWORD)

   def setup(jdbcProperties: Properties, context: AuthContext, resource : Resource){
    setupProperties(jdbcProperties, Some(context), Some(resource))
  }

  def setupProperties(jdbcProperties: Properties, context: Option[AuthContext] = None, resource: Option[Resource] = None): Unit = {
    // val jdbcUrl = s"jdbc:sqlserver://${JDBC_HOSTNAME}:${JDBC_PORT};database=${JDBC_DATABASE};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=60;"
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

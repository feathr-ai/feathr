package com.linkedin.feathr.offline.config.datasource

import org.apache.spark.sql.SparkSession

private[feathr] class ADLSConfigExtractor extends ConfigExtractor() {
  val ADLS_ACCOUNT = "ADLS_ACCOUNT"
  val ADLS_KEY = "ADLS_KEY"

  override val params = List(ADLS_ACCOUNT, ADLS_KEY)

  override def setupHadoopConfig(ss: SparkSession, context: Option[AuthContext] = None, resource: Option[Resource] = None): Unit = {
    val adlsParam = s"fs.azure.account.key.${getAuthStr(ADLS_ACCOUNT, context, resource)}.dfs.core.windows.net"
    val adlsKey = getAuthStr(ADLS_KEY, context, resource)

    ss.sparkContext
      .hadoopConfiguration.set(adlsParam, adlsKey)
  }

  def getAuthFromConfig(str: String, resource: Resource): String = {
    str match {
      case ADLS_ACCOUNT => resource.azureResource.adlsAccount
      case ADLS_KEY => resource.azureResource.adlsKey
      case _ => EMPTY_STRING
    }
  }
}

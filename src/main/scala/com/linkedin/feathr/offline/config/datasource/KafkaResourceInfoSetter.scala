package com.linkedin.feathr.offline.config.datasource

import com.linkedin.feathr.offline.config.datasource.KafkaResourceInfoSetter.{ENDPOINT, SHARED_ACCESS_KEY, SHARED_ACCESS_KEY_NAME, USERNAME}
import org.apache.spark.sql.SparkSession

/**
 * Class to setup Kafka authentication
 */
private[feathr]  class KafkaResourceInfoSetter extends ResourceInfoSetter() {

  override val params = List(USERNAME, ENDPOINT, SHARED_ACCESS_KEY_NAME, SHARED_ACCESS_KEY)

  def setupSparkStreamingConf(ss: SparkSession, context: Option[DataSourceConfig], resource: Option[Resource]): Unit = {
    val endpoint = getAuthStr(ENDPOINT, context, resource)
    val accessKeyName = getAuthStr(SHARED_ACCESS_KEY_NAME, context, resource)
    val accessKey = getAuthStr(SHARED_ACCESS_KEY, context, resource)
    val username = getAuthStr(USERNAME, context, resource)

    ss.conf.set(ENDPOINT, endpoint)
    ss.conf.set(SHARED_ACCESS_KEY_NAME, accessKeyName)
    ss.conf.set(SHARED_ACCESS_KEY, accessKey)
    ss.conf.set(USERNAME, username)
  }

  def getAuthFromConfig(str: String, resource: Resource): String = ???
}

private[feathr] object KafkaResourceInfoSetter{
  val kafkaSetter = new KafkaResourceInfoSetter()
  val ENDPOINT = "ENDPOINT"
  val USERNAME = "USERNAME"
  val SHARED_ACCESS_KEY_NAME = "SHARED_ACCESS_KEY_NAME"
  val SHARED_ACCESS_KEY = "SHARED_ACCESS_KEY"
  def setup(ss: SparkSession, config: DataSourceConfig, resource: Resource): Unit ={
    if (config.configStr.isDefined){
      kafkaSetter.setupSparkStreamingConf(ss, Some(config), Some(resource))
    }
  }
}



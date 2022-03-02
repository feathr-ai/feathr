package com.linkedin.feathr.offline.config.datasource

import com.typesafe.config.{Config, ConfigFactory}

/**
 * A base class to parse config context from Config String
 *
 * @param configStr
 */
class DataSourceConfig(val configStr: Option[String] = None) {
  val config: Option[Config] = configStr.map(configStr => ConfigFactory.parseString(configStr))
}
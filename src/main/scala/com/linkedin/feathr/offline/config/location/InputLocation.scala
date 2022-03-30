package com.linkedin.feathr.offline.config.location

import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.{ObjectNode, TextNode}
import com.fasterxml.jackson.databind.{DeserializationContext, DeserializationFeature, JsonDeserializer, ObjectMapper}
import com.fasterxml.jackson.module.caseclass.mapper.CaseClassObjectMapper
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.linkedin.feathr.common.FeathrJacksonScalaModule
import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrConfigException}
import com.linkedin.feathr.offline.config.DataSourceLoader
import com.linkedin.feathr.offline.source.DataSource
import org.apache.spark.sql.{DataFrame, SparkSession}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = classOf[SimplePath])
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[SimplePath], name = "path"),
    new JsonSubTypes.Type(value = classOf[Jdbc], name = "jdbc"),
  ))
trait InputLocation {
  def getPath: String

  def getPathList: Array[String]

  def loadDf(ss: SparkSession): DataFrame

  override def toString: String = getPath
}

object LocationUtils {
  def propOrEnvOrElse(key: String, alt: String): String = {
    scala.util.Properties.propOrElse(key, scala.util.Properties.envOrElse(key, alt))
  }

  def envSubstitute(s: String): String = {
    """(\$\{[A-Za-z0-9_-]+})""".r.replaceAllIn(s, m => propOrEnvOrElse(m.toString().substring(2).dropRight(1), ""))
  }

  def getMapper(): ObjectMapper = {
    (new ObjectMapper(new HoconFactory) with CaseClassObjectMapper)
      .registerModule(FeathrJacksonScalaModule) // DefaultScalaModule causes a fail on holdem
      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new SimpleModule().addDeserializer(classOf[DataSource], new DataSourceLoader))
  }
}

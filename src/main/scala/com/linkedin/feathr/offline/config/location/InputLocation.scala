package com.linkedin.feathr.offline.config.location

import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.caseclass.mapper.CaseClassObjectMapper
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.linkedin.feathr.common.FeathrJacksonScalaModule
import com.linkedin.feathr.offline.config.DataSourceLoader
import com.linkedin.feathr.offline.source.DataSource
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * An InputLocation is a data source definition, it can either be HDFS files or a JDBC database connection
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = classOf[SimplePath])
@JsonSubTypes(
  Array(
    new JsonSubTypes.Type(value = classOf[KafkaEndpoint], name = "kafka"),
    new JsonSubTypes.Type(value = classOf[SimplePath], name = "path"),
    new JsonSubTypes.Type(value = classOf[PathList], name = "pathlist"),
    new JsonSubTypes.Type(value = classOf[Jdbc], name = "jdbc"),
  ))
trait InputLocation {
  /**
   * Backward Compatibility
   * Many existing codes expect a simple path
   * @return the `path` or `url` of the data source
   */
  def getPath: String

  /**
   * Backward Compatibility
   * @return the `path` or `url` of the data source, wrapped in an List
   */
  def getPathList: List[String]

  /**
   * Load DataFrame from Spark session
   * @param ss SparkSession
   * @return
   */
  def loadDf(ss: SparkSession, dataIOParameters: Map[String, String] = Map()): DataFrame

  override def toString: String = getPath
}

object LocationUtils {
  private def propOrEnvOrElse(key: String, alt: String): String = {
    scala.util.Properties.propOrElse(key, scala.util.Properties.envOrElse(key, alt))
  }

  /**
   * String template substitution, replace "...${VAR}.." with corresponding System property or environment variable
   * Non-existent pattern is replaced by empty string.
   * @param s String template to be processed
   * @return Processed result
   */
  def envSubstitute(s: String): String = {
    """(\$\{[A-Za-z0-9_-]+})""".r.replaceAllIn(s, m => propOrEnvOrElse(m.toString().substring(2).dropRight(1), ""))
  }

  /**
   * Get an ObjectMapper to deserialize DataSource
   * @return the ObjectMapper
   */
  def getMapper(): ObjectMapper = {
    (new ObjectMapper(new HoconFactory) with CaseClassObjectMapper)
      .registerModule(FeathrJacksonScalaModule) // DefaultScalaModule causes a fail on holdem
      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new SimpleModule().addDeserializer(classOf[DataSource], new DataSourceLoader))
  }
}

package com.linkedin.feathr.offline.config.datasource

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.linkedin.feathr.offline.util.CmdLineParser
import org.apache.log4j.Logger
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

import scala.io.Source

object DataSourceConfigUtils {
  val logger: Logger = Logger.getLogger(getClass)

  private val yamlMapper = new ObjectMapper(new YAMLFactory())
  val featureStoreConfig: FeathrStoreConfig = loadYamlConfig("feathr_project/data/feathr_user_workspace/feathr_config.yaml")

  private def loadYamlConfig(yamlPath: String) = {
    try {
      val contents = Source.fromFile(yamlPath).mkString
      yamlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      val config = yamlMapper.readValue(contents, classOf[FeathrStoreConfig])
      config
    } catch {
      case _: Throwable =>
        FeathrStoreConfig(null)
    }
  }

  private[feathr] def getConfigs(cmdParser: CmdLineParser): DataSourceConfigs ={
    new DataSourceConfigs(
      redisConfigStr = cmdParser.extractOptionalValue("redis-config"),
      s3ConfigStr = cmdParser.extractOptionalValue("s3-config"),
      adlsConfigStr = cmdParser.extractOptionalValue("adls-config"),
      blobConfigStr = cmdParser.extractOptionalValue("blob-config"),
      sqlConfigStr = cmdParser.extractOptionalValue("sql-config")
    )
  }

  private[feathr] def setupHadoopConf(ss:SparkSession , configs: DataSourceConfigs): Unit ={
    val resource = featureStoreConfig.resource
    val adlsConfigExtractor = new ADLSConfigExtractor()
    adlsConfigExtractor.setup(ss, configs.adlsConfig, resource)
    val blobConfigExtractor = new BlobConfigExtractor()
    blobConfigExtractor.setup(ss, configs.blobConfig, resource)
    val s3ConfigExtractor = new S3ConfigExtractor()
    s3ConfigExtractor.setup(ss, configs.s3Config, resource)
    logger.info("Data Source Hadoop Configs Extracted Successfully.")
  }

  private[feathr] def setupSparkConf(sparkConf: SparkConf, configs: DataSourceConfigs): Unit ={
    val resource = featureStoreConfig.resource
    val redisConfigExtractor = new RedisConfigExtractor()
    redisConfigExtractor.setup(sparkConf, configs.redisConfig, resource)
    logger.info("Redis SparkConf Configs Extracted Successfully.")
  }
}





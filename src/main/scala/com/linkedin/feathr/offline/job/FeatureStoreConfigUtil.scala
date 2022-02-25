package com.linkedin.feathr.offline.job
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.typesafe.config.Config
import org.apache.spark.sql.SparkSession

import scala.io.Source

case class AzureResource(@JsonProperty("REDIS_PASSWORD") redisPassword: String,
                         @JsonProperty("REDIS_HOST") redisHost: String,
                         @JsonProperty("REDIS_PORT") redisPort: String,
                         @JsonProperty("REDIS_SSL_ENABLED") redisSslEnabled: String,
                         @JsonProperty("ADLS_ACCOUNT") adlsAccount: String,
                         @JsonProperty("ADLS_KEY") adlsKey: String,
                         @JsonProperty("BLOB_ACCOUNT") blobAccount: String,
                         @JsonProperty("BLOB_KEY") blobKey: String)
case class AwsResource(@JsonProperty("S3_ENDPOINT") s3Endpoint: String,
                       @JsonProperty("S3_ACCESS_KEY") s3AccessKey: String,
                       @JsonProperty("S3_SECRET_KEY") s3SecretKey: String)
case class Resource(@JsonProperty("azure")azureResource: AzureResource,
                    @JsonProperty("aws")awsResource: AwsResource)
case class FeathrStoreConfig(@JsonProperty("resource")resource: Resource)

object FeatureStoreConfigUtil {
  val REDIS_PASSWORD = "REDIS_PASSWORD"
  val REDIS_HOST = "REDIS_HOST"
  val REDIS_PORT = "REDIS_PORT"
  val REDIS_SSL_ENABLED = "REDIS_SSL_ENABLED"

  val S3_ENDPOINT = "S3_ENDPOINT"
  val S3_ACCESS_KEY = "S3_ACCESS_KEY"
  val S3_SECRET_KEY = "S3_SECRET_KEY"

  val ADLS_ACCOUNT = "ADLS_ACCOUNT"
  val ADLS_KEY = "ADLS_KEY"

  val BLOB_ACCOUNT = "BLOB_ACCOUNT"
  val BLOB_KEY = "BLOB_KEY"

  val EMPTY_STRING = ""
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

  def getADLSAuthStr(str: String, featureGenContext: Option[FeatureGenJobContext]): String = {
    sys.env.get(str).getOrElse(
      if (featureGenContext.isDefined) {
        getADLSAuthFromContext(str, featureGenContext.get)
      } else if(featureStoreConfig.resource != null) {
        getADLSAuthFromConfig(str)
      } else EMPTY_STRING
    )
  }

  private def getADLSAuthFromContext(str: String, featureGenContext: FeatureGenJobContext): String = {
    featureGenContext.adlsConfig.map(config => {
      str match {
        case ADLS_ACCOUNT => config.getString(ADLS_ACCOUNT)
        case ADLS_KEY => config.getString(ADLS_KEY)
        case _ => EMPTY_STRING
      }
    }).getOrElse(EMPTY_STRING)
  }

  private def getADLSAuthFromConfig(str: String): String = {
    str match {
      case ADLS_ACCOUNT => featureStoreConfig.resource.azureResource.adlsAccount
      case ADLS_KEY => featureStoreConfig.resource.azureResource.adlsKey
      case _ => EMPTY_STRING
    }
  }

  def getBlobAuthStr(str: String, featureGenContext: Option[FeatureGenJobContext]): String = {
    sys.env.get(str).getOrElse(
      if (featureGenContext.isDefined) {
        getBlobAuthFromContext(str, featureGenContext.get)
      } else if(featureStoreConfig.resource != null) {
        getBlobAuthFromConfig(str)
      } else EMPTY_STRING
    )
  }

  private def getBlobAuthFromContext(str: String, featureGenContext: FeatureGenJobContext): String = {
    featureGenContext.blobConfig.map(config => {
      str match {
        case BLOB_ACCOUNT => config.getString(BLOB_ACCOUNT)
        case BLOB_KEY => config.getString(BLOB_KEY)
        case _ => EMPTY_STRING
      }
    }).getOrElse(EMPTY_STRING)
  }

  private def getBlobAuthFromConfig(str: String): String = {
    str match {
      case BLOB_ACCOUNT => featureStoreConfig.resource.azureResource.blobAccount
      case BLOB_KEY => featureStoreConfig.resource.azureResource.blobKey
      case _ => EMPTY_STRING
    }
  }

  def getS3AuthStr(str: String, s3ConfigOption: Option[Config]): String = {
    sys.env.get(str).getOrElse(
      if (s3ConfigOption.isDefined) {
        getS3AuthFromContext(str, s3ConfigOption.get)
      } else if(featureStoreConfig.resource != null) {
        getS3AuthFromConfig(str)
      } else EMPTY_STRING
    )
  }


  def getS3AuthFromContext(str: String, s3Config: Config): String = {
    str match {
      case S3_ENDPOINT => s3Config.getString(S3_ENDPOINT)
      case S3_ACCESS_KEY => s3Config.getString(S3_ACCESS_KEY)
      case S3_SECRET_KEY => s3Config.getString(S3_SECRET_KEY)
      case _ => EMPTY_STRING
    }
  }

  private[feathr] def setupS3Params(ss: SparkSession, s3ConfigOption: Option[Config] = None) = {
    val s3Endpoint = FeatureStoreConfigUtil.getS3AuthStr(S3_ENDPOINT, s3ConfigOption)
    val s3AccessKey = FeatureStoreConfigUtil.getS3AuthStr(S3_ACCESS_KEY, s3ConfigOption)
    val s3SecretKey = FeatureStoreConfigUtil.getS3AuthStr(S3_SECRET_KEY, s3ConfigOption)

    ss.sparkContext
      .hadoopConfiguration.set("fs.s3a.endpoint", s3Endpoint)
    ss.sparkContext
      .hadoopConfiguration.set("fs.s3a.access.key", s3AccessKey)
    ss.sparkContext
      .hadoopConfiguration.set("fs.s3a.secret.key", s3SecretKey)
  }

  private def getS3AuthFromConfig(str: String): String = {
    str match {
      case S3_ENDPOINT => featureStoreConfig.resource.awsResource.s3Endpoint
      case S3_ACCESS_KEY => featureStoreConfig.resource.awsResource.s3AccessKey
      case S3_SECRET_KEY => featureStoreConfig.resource.awsResource.s3SecretKey
      case _ => EMPTY_STRING
    }
  }

  // If the environment variable does not exist, fall back to get value from generation context
  def getRedisAuthStr(str: String, featureGenContext: Option[FeatureGenJobContext]): String = {
    sys.env.get(str).getOrElse(
      if (featureGenContext.isDefined) {
        getRedisAuthFromContext(str, featureGenContext.get)
      } else if(featureStoreConfig.resource != null) {
        getRedisAuthFromConfig(str)
      } else EMPTY_STRING
    )
  }

  private def getRedisAuthFromConfig(str: String): String = {
    str match {
      case REDIS_HOST => featureStoreConfig.resource.azureResource.redisHost
      case REDIS_PASSWORD => featureStoreConfig.resource.azureResource.redisPassword
      case REDIS_PORT => featureStoreConfig.resource.azureResource.redisPort
      case REDIS_SSL_ENABLED => featureStoreConfig.resource.azureResource.redisSslEnabled
      case _ => EMPTY_STRING
    }
  }

  private def getRedisAuthFromContext(str: String, featureGenContext: FeatureGenJobContext): String = {
    featureGenContext.redisConfig.map(config => {
      str match {
        case REDIS_HOST => config.getString(REDIS_HOST)
        case REDIS_PASSWORD => config.getString(REDIS_PASSWORD)
        case REDIS_PORT => config.getString(REDIS_PORT)
        case REDIS_SSL_ENABLED =>  config.getString(REDIS_SSL_ENABLED)
        case _ => EMPTY_STRING
      }
    }).getOrElse(EMPTY_STRING)
  }
}

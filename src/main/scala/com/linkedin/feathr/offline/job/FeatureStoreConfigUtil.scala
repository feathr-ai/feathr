package com.linkedin.feathr.offline.job
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import scala.io.Source

case class AzureResource(@JsonProperty("REDIS_PASSWORD") redisPassword: String,
                         @JsonProperty("REDIS_HOST") redisHost: String,
                         @JsonProperty("REDIS_PORT") redisPort: String,
                         @JsonProperty("REDIS_SSL_ENABLED") redisSslEnabled: String)
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
      case _ =>
        FeathrStoreConfig(null)
    }
  }

  def getS3AuthStr(str: String, featureGenContext: Option[FeatureGenJobContext]): String = {
    sys.env.get(str).getOrElse(
      if (featureGenContext.isDefined) {
        getS3AuthFromContext(str, featureGenContext.get)
      } else if(featureStoreConfig.resource != null) {
        getS3AuthFromConfig(str)
      } else EMPTY_STRING
    )
  }


  private def getS3AuthFromContext(str: String, featureGenContext: FeatureGenJobContext): String = {
    featureGenContext.s3Config.map(config => {
      str match {
        case S3_ENDPOINT => config.getString(S3_ENDPOINT)
        case S3_ACCESS_KEY => config.getString(S3_ACCESS_KEY)
        case S3_SECRET_KEY => config.getString(S3_SECRET_KEY)
        case _ => EMPTY_STRING
      }
    }).getOrElse(EMPTY_STRING)
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

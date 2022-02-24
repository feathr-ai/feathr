package com.linkedin.feathr.offline.job

import com.fasterxml.jackson.annotation.JsonProperty
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger
import org.apache.spark.sql.SparkSession

import java.util.Properties

case class AzureResource(@JsonProperty("REDIS_PASSWORD") redisPassword: String,
                         @JsonProperty("REDIS_HOST") redisHost: String,
                         @JsonProperty("REDIS_PORT") redisPort: String,
                         @JsonProperty("REDIS_SSL_ENABLED") redisSslEnabled: String,
                         @JsonProperty("ADLS_ACCOUNT") adlsAccount: String,
                         @JsonProperty("ADLS_KEY") adlsKey: String,
                         @JsonProperty("BLOB_ACCOUNT") blobAccount: String,
                         @JsonProperty("BLOB_KEY") blobKey: String,
                         @JsonProperty("BLOB_KEY") jdbcUser: String,
                         @JsonProperty("BLOB_KEY") jdbcPassword: String)
case class AwsResource(@JsonProperty("S3_ENDPOINT") s3Endpoint: String,
                       @JsonProperty("S3_ACCESS_KEY") s3AccessKey: String,
                       @JsonProperty("S3_SECRET_KEY") s3SecretKey: String)
case class Resource(@JsonProperty("azure")azureResource: AzureResource,
                    @JsonProperty("aws")awsResource: AwsResource)

/**
 * A base class to parse config context from Config String
 * @param ConfigStr
 */
class AuthContext(val ConfigStr: Option[String] = None) {
  val config: Option[Config] = ConfigStr.map(configStr => ConfigFactory.parseString(configStr))
}

/**
 * Base class to implement different data source configuration extraction.
 * It Contains:
 * 1. setup() combination of setupSparkConfig and setupProperties
 * 2. setupSparkConfig() updates spark session configuration with authentication parameters.
 * 3. setupProperties() merge spark read options into properties.
 * 4. getAuthStr() get Authrntication string from job context or local configs
 * 5. getAuthFromContext() get Authentication from job context
 * 6. getAuthFromConfig() get Authentication from local configs
 */
private[feathr] abstract class DataSourceConfigExtractor() {
  val EMPTY_STRING = ""
  val params = List()

  def setup(ss: SparkSession, context: Option[AuthContext] = None, resource: Option[Resource] = None): Properties ={
    setupSparkConfig(ss, context, resource)
    setupProperties(context, resource)
  }

  def setupSparkConfig(ss: SparkSession, context: Option[AuthContext] = None, resource: Option[Resource] = None): Unit = {}

  def setupProperties(context: Option[AuthContext] = None, resource: Option[Resource] = None): Properties = {new Properties()}

  def getAuthStr(str: String, context: Option[AuthContext] = None, resource: Option[Resource] = None): String = {
    sys.env.getOrElse(str, if (context.isDefined) {
      getAuthFromContext(str, context.get)
    } else if (resource.isDefined) {
      getAuthFromConfig(str, resource.get)
    } else EMPTY_STRING)
  }

  def getAuthFromContext(str: String, context: AuthContext): String = {
    context.config.map(config => {if (params.contains(str)) config.getString(str) else EMPTY_STRING}).getOrElse(EMPTY_STRING)
  }

  def getAuthFromConfig(str: String, resource: Resource): String
}

private[feathr] class ADLSConfigExtractor extends DataSourceConfigExtractor (){
  val ADLS_ACCOUNT = "ADLS_ACCOUNT"
  val ADLS_KEY = "ADLS_KEY"

  override val params = List(ADLS_ACCOUNT, ADLS_KEY)

  override def setupSparkConfig(ss: SparkSession, context: Option[AuthContext] = None, resource: Option[Resource] = None): Unit = {
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

private[feathr] class SQLConfigExtractor extends DataSourceConfigExtractor (){
  val JDBC_USER = "JDBC_USER"
  val JDBC_PASSWORD = "JDBC_PASSWORD"

  override val params = List(JDBC_USER, JDBC_PASSWORD)

  override def setupProperties(context: Option[AuthContext] = None, resource: Option[Resource] = None): Properties = {
    val jdbcProperties = new Properties()
    // val jdbcUrl = s"jdbc:sqlserver://${JDBC_HOSTNAME}:${JDBC_PORT};database=${JDBC_DATABASE};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=60;"
    jdbcProperties.put("user",getAuthStr(JDBC_USER, context))
    jdbcProperties.put("password",getAuthStr(JDBC_PASSWORD, context))
    jdbcProperties
  }

  def getAuthFromConfig(str: String, resource: Resource): String = {
    str match {
      case JDBC_USER => resource.azureResource.jdbcUser
      case JDBC_PASSWORD => resource.azureResource.jdbcPassword
      case _ => EMPTY_STRING
    }
  }
}

private[feathr] class S3ConfigExtractor extends DataSourceConfigExtractor (){
  val S3_ENDPOINT = "S3_ENDPOINT"
  val S3_ACCESS_KEY = "S3_ACCESS_KEY"
  val S3_SECRET_KEY = "S3_SECRET_KEY"

  override val params = List(S3_ENDPOINT, S3_ACCESS_KEY, S3_SECRET_KEY)

  override def setupSparkConfig(ss: SparkSession, context: Option[AuthContext], resource: Option[Resource]): Unit = {
    ss.sparkContext
      .hadoopConfiguration.set("fs.s3a.endpoint", getAuthStr(S3_ENDPOINT, context))
    ss.sparkContext
      .hadoopConfiguration.set("fs.s3a.access.key", getAuthStr(S3_ACCESS_KEY, context))
    ss.sparkContext
      .hadoopConfiguration.set("fs.s3a.secret.key", getAuthStr(S3_SECRET_KEY, context))
  }

  def getAuthFromConfig(str: String, resource: Resource): String = {
    str match {
      case S3_ENDPOINT => resource.awsResource.s3Endpoint
      case S3_ACCESS_KEY => resource.awsResource.s3AccessKey
      case S3_SECRET_KEY => resource.awsResource.s3SecretKey
      case _ => EMPTY_STRING
    }
  }
}

private[feathr] object DataSource extends Enumeration {
  type DataSource = Value
  val ADLS, Blob, S3, SQL, Redis = Value
}

object DataSourceSetter {
  val logger: Logger = Logger.getLogger(getClass)

  def setup(ss: SparkSession, dataSource: String, context: Option[AuthContext] = None, resource: Option[Resource]): Properties = {
    dataSource match {
      case DataSource.ADLS => val adlsConfigExtractor = new ADLSConfigExtractor()
        adlsConfigExtractor.setup(ss, context, resource)
      case DataSource.SQL => val sqlConfigExtractor = new SQLConfigExtractor()
        sqlConfigExtractor.setup(ss, context, resource)
      case DataSource.S3 => val s3ConfigExtractor = new S3ConfigExtractor()
        s3ConfigExtractor.setup(ss, context, resource)
      case _ => new Properties()
    }
  }
}
package com.linkedin.feathr.offline.source.dataloader.stream

import com.linkedin.feathr.offline.config.datasource.KafkaResourceInfoSetter
import com.linkedin.feathr.offline.config.location.KafkaEndpoint
import org.apache.avro.Schema
import org.apache.spark.sql.streaming.DataStreamReader
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.UUID;

/**
 * This is used to load Kafka data source into a DataFrame
 *
 */
class KafkaDataLoader(ss: SparkSession, input: KafkaEndpoint) extends StreamDataLoader(ss) {
  val format = "kafka"
  val sharedAccessKeyName = KafkaResourceInfoSetter.SHARED_ACCESS_KEY_NAME
  val kafkaEndpoint = KafkaResourceInfoSetter.ENDPOINT
  val kafkaUsername = KafkaResourceInfoSetter.USERNAME
  val sharedAccessKey = KafkaResourceInfoSetter.SHARED_ACCESS_KEY

  private def getKafkaAuth(ss: SparkSession): String = {
    // If user set password, then we use password to auth
    ss.conf.getOption(kafkaEndpoint) match {
      case Some(_) =>
          val accessKeyName = ss.conf.get(sharedAccessKeyName)
          val endpoint = ss.conf.get(kafkaEndpoint)
          val username = ss.conf.get(kafkaUsername)
          val accessKey = ss.conf.get(sharedAccessKey)
          val EH_SASL = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""+username+"\"" +
            " password=\"Endpoint="+endpoint+";SharedAccessKeyName="+accessKeyName+";SharedAccessKey="+accessKey+"\";"
          EH_SASL
      case _ => {
        throw new RuntimeException(s"Invalid Kafka authentication! ${kafkaEndpoint} is not set in Spark conf.")
      }
    }
  }

  override def getDFReader(kafkaOptions: Map[String, String]): DataStreamReader = {
    val EH_SASL = getKafkaAuth(ss)
    _ss.readStream
        .format(format)
        .options(kafkaOptions)
        .option("kafka.group.id", UUID.randomUUID().toString)
        .option("kafka.sasl.jaas.config", EH_SASL)
        .option("kafka.sasl.mechanism", "PLAIN")
        .option("kafka.security.protocol", "SASL_SSL")
        .option("kafka.request.timeout.ms", "60000")
        .option("kafka.session.timeout.ms", "60000")
        .option("failOnDataLoss", "false")
  }

  /**
   * load the source data as dataframe.
   *
   * @return an dataframe
   */
  override def loadDataFrame(): DataFrame = {
    val TOPIC = input.topics.mkString(",")
    val BOOTSTRAP_SERVERS = input.brokers.mkString(",")
    getDFReader(Map())
      .option("subscribe", TOPIC)
      .option("kafka.bootstrap.servers", BOOTSTRAP_SERVERS)
      .load()
  }

  /**
   * get the schema of the source. It's only used in the deprecated DataSource.getDataSetAndSchema
   *
   * @return an Avro Schema
   */
  override def loadSchema(): Schema = ???
}
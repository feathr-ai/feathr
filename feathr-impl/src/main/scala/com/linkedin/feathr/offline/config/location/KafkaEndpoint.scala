package com.linkedin.feathr.offline.config.location

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.codehaus.jackson.annotate.JsonProperty

/**
 * Kafka source config.
 * Example:
 *  kafkaStreamingSource: {
      type: KAFKA
      config: {
        brokers: ["feathrazureci.servicebus.windows.net:9093"]
        topics: [feathrcieventhub]
        schema: {
          type = "avro"
          avroJson:"......"
        }
      }
    }
 *
 *
 */
@CaseClassDeserialize()
case class KafkaSchema(@JsonProperty("type") `type`: String,
                       @JsonProperty("avroJson") avroJson: String)

@CaseClassDeserialize()
case class KafkaEndpoint(@JsonProperty("brokers") brokers: List[String],
                         @JsonProperty("topics") topics: List[String],
                         @JsonProperty("schema") schema: KafkaSchema) extends InputLocation {
  override def loadDf(ss: SparkSession, dataIOParameters: Map[String, String] = Map()): DataFrame = ???

  override def getPath: String = "kafka://" + brokers.mkString(",")+":"+topics.mkString(",")

  override def getPathList: List[String] = ???
}



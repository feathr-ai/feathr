package com.linkedin.feathr.offline.config

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.linkedin.feathr.common.FeathrJacksonScalaModule
import com.linkedin.feathr.offline.source.{DataSource, SourceFormatType}
import org.scalatest.FunSuite


class TestDataSourceLoader extends FunSuite {
  /// Base line test to ensure backward compatibility
  test("DataSourceLoader.deserialize BaseLine") {
    val configDoc =
      """
        |{
        |    location: { path: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv" }
        |    timeWindowParameters: {
        |      timestampColumn: "lpep_dropoff_datetime"
        |      timestampColumnFormat: "yyyy-MM-dd HH:mm:ss"
        |    }
        |}
        |""".stripMargin
    val jackson = new ObjectMapper(new HoconFactory)
      .registerModule(FeathrJacksonScalaModule) // DefaultScalaModule causes a fail on holdem
      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new SimpleModule().addDeserializer(classOf[DataSource], new DataSourceLoader))
    val ds = jackson.readValue(configDoc, classOf[DataSource])
    assert(ds.path=="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv")
    assert(ds.sourceType == SourceFormatType.FIXED_PATH)
  }
}

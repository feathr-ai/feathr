package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.caseclass.mapper.CaseClassObjectMapper
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.linkedin.feathr.common.FeathrJacksonScalaModule
import com.linkedin.feathr.offline.config.DataSourceLoader
import com.linkedin.feathr.offline.config.datasourceprovider.location.{FixedPath, InputLocation}
import com.linkedin.feathr.offline.source.DataSource
import org.scalatest.FunSuite

class TestDesLocation extends FunSuite{
  test("Deserialize Location") {
    val jackson = (new ObjectMapper(new HoconFactory) with CaseClassObjectMapper)
      .registerModule(FeathrJacksonScalaModule) // DefaultScalaModule causes a fail on holdem
      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new SimpleModule().addDeserializer(classOf[DataSource], new DataSourceLoader))

    {
      val configDoc = """{ path: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv" }"""
      val ds = jackson.readValue(configDoc, classOf[InputLocation])
      ds match {
        case FixedPath(path) => {
          assert(path == "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv")
        }
        case _ => assert(false)
      }

    }
    
  }
}

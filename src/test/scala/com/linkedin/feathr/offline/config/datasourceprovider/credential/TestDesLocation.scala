package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.caseclass.mapper.CaseClassObjectMapper
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.linkedin.feathr.common.FeathrJacksonScalaModule
import com.linkedin.feathr.offline.config.DataSourceLoader
import com.linkedin.feathr.offline.config.datasourceprovider.location.LocationUtils.envSubstitute
import com.linkedin.feathr.offline.config.datasourceprovider.location.{InputLocation, InputLocationDeserializer, Jdbc, SimplePath}
import com.linkedin.feathr.offline.source.DataSource
import org.scalatest.FunSuite

class TestDesLocation extends FunSuite{
  test("envSubstitute") {
    scala.util.Properties.setProp("PROP1", "foo")
    val s="xyz${PROP1}abc"
    assert(envSubstitute(s)=="xyzfooabc")
  }
  test("Deserialize Location") {
    val jackson = (new ObjectMapper(new HoconFactory) with CaseClassObjectMapper)
      .registerModule(FeathrJacksonScalaModule) // DefaultScalaModule causes a fail on holdem
      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new SimpleModule().addDeserializer(classOf[DataSource], new DataSourceLoader))
      .registerModule(new SimpleModule().addDeserializer(classOf[InputLocation], new InputLocationDeserializer))

    {
      val configDoc = """{ path: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv" }"""
      val ds = jackson.readValue(configDoc, classOf[InputLocation])
      ds match {
        case SimplePath(path) => {
          assert(path == "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv")
        }
        case _ => assert(false)
      }

    }

    {
      val configDoc =
        """
          |{
          | type: "jdbc"
          | url: "jdbc:sqlserver://bet-test.database.windows.net:1433;database=bet-test"
          | user: "bet"
          | password: "foo"
          |}""".stripMargin
      val ds = jackson.readValue(configDoc, classOf[InputLocation])
      ds match {
        case Jdbc(url, dbtable, user, password, token, useToken) => {
          assert(url == "jdbc:sqlserver://bet-test.database.windows.net:1433;database=bet-test")
          assert(user=="bet")
          assert(password=="foo")
        }
        case _ => assert(false)
      }
    }

    {
      val configDoc =
        """
          |{
          |  location: { url: "jdbc:sqlserver://bet-test.database.windows.net:1433;database=bet-test"
          |    type: "jdbc"
          |    user: "bet"
          |    password: "foo"
          |  }
          |  timeWindowParameters: {
          |    timestampColumn: "lpep_dropoff_datetime"
          |    timestampColumnFormat: "yyyy-MM-dd HH:mm:ss"
          |  }
          |}
          |""".stripMargin
      val ds = jackson.readValue(configDoc, classOf[DataSource])
      ds.location match {
        case Jdbc(url, dbtable, user, password, token, useToken) => {
          assert(url == "jdbc:sqlserver://bet-test.database.windows.net:1433;database=bet-test")
          assert(user=="bet")
          assert(password=="foo")
        }
        case _ => assert(false)
      }
    }
  }
}

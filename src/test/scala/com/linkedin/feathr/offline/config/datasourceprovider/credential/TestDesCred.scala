package com.linkedin.feathr.offline.config.datasourceprovider.credential

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.caseclass.mapper.CaseClassObjectMapper
import com.jasonclawson.jackson.dataformat.hocon.HoconFactory
import com.linkedin.feathr.common.FeathrJacksonScalaModule
import com.linkedin.feathr.offline.config.DataSourceLoader
import com.linkedin.feathr.offline.source.{DataSource, SourceFormatType}
import org.scalatest.FunSuite

class TestDesCred extends FunSuite {
  test("Deserialize Credential") {
    val jackson = (new ObjectMapper(new HoconFactory) with CaseClassObjectMapper)
      .registerModule(FeathrJacksonScalaModule) // DefaultScalaModule causes a fail on holdem
      .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .registerModule(new SimpleModule().addDeserializer(classOf[DataSource], new DataSourceLoader))

    {
      val configDoc = "{}"
      val ds = jackson.readValue(configDoc, classOf[Credential])
      ds match {
        case AnonymousCredential() => {}
        case _ => assert(false)
      }
    }

    {
      val configDoc =
        """
          |{
          | type: "aad"
          | appId: "some id"
          | secret: "some secret"
          |}
          |""".stripMargin
      val ds = jackson.readValue(configDoc, classOf[Credential])
      ds match {
        case AadAppCredential(appId, secret) => {
          assert(appId == "some id")
          assert(secret == "some secret")
        }
        case _ => assert(false)
      }
    }

    {
      val configDoc =
        """
          |{
          | type: "key"
          | key: "some key"
          |}
          |""".stripMargin
      val ds = jackson.readValue(configDoc, classOf[Credential])
      ds match {
        case AccessKeyCredential(accessKey) => {
          assert(accessKey == "some key")
        }
        case _ => assert(false)
      }
    }

  }
}

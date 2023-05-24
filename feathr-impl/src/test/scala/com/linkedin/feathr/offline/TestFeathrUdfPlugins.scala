package com.linkedin.feathr.offline

import com.linkedin.feathr.common.FeatureValue
import com.linkedin.feathr.common.util.CoercionUtils
import com.linkedin.feathr.offline.anchored.keyExtractor.AlienSourceKeyExtractorAdaptor
import com.linkedin.feathr.offline.client.plugins.FeathrUdfPluginContext
import com.linkedin.feathr.offline.derived.AlienDerivationFunctionAdaptor
import com.linkedin.feathr.offline.mvel.FeathrFeatureValueAsAlien
import com.linkedin.feathr.offline.mvel.plugins.FeathrExpressionExecutionContext
import com.linkedin.feathr.offline.plugins.{AlienFeatureValue, AlienFeatureValueTypeAdaptor}
import com.linkedin.feathr.offline.util.FeathrTestUtils
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{FloatType, StringType, StructField, StructType}
import org.testng.Assert.{assertEquals, assertTrue}
import org.testng.annotations.{BeforeClass, Test}
import scala.collection.JavaConverters._

class TestFeathrUdfPlugins extends FeathrIntegTest {

  val MULTILINE_QUOTE = "\"\"\""

  private val mvelContext = new FeathrExpressionExecutionContext()


  @BeforeClass
  override def setFeathrConfig(): Unit = {
    mvelContext.setupExecutorMvelContext(classOf[AlienFeatureValue], new AlienFeatureValueTypeAdaptor(), ss.sparkContext)
    FeathrUdfPluginContext.registerUdfAdaptor(new AlienDerivationFunctionAdaptor(), ss.sparkContext)
    FeathrUdfPluginContext.registerUdfAdaptor(new AlienSourceKeyExtractorAdaptor(), ss.sparkContext)
  }

  @Test
  def testFeatureValueWrapper(): Unit = {
    val featureValue = new FeatureValue(2.0f)
    val featureFeatureValueAsAlien = new FeathrFeatureValueAsAlien(featureValue)
    assertTrue(mvelContext.canConvert(FeatureValue.getClass, featureFeatureValueAsAlien.getClass))
    assertEquals(mvelContext.convert(featureFeatureValueAsAlien, FeatureValue.getClass), featureValue)
    assertEquals(CoercionUtils.coerceToVector(featureValue),  Map("" -> 2.0f).asJava)
  }

  @Test (enabled = true)
  def testMvelUdfPluginSupport: Unit = {
    val df = runLocalFeatureJoinForTest(
      joinConfigAsString = """
                             | features: {
                             |   key: a_id
                             |   featureList: ["f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "fA"]
                             | }
      """.stripMargin,
      featureDefAsString = s"""
                              |anchors: {
                              |  anchor1: {
                              |    source: "anchor1-source.csv"
                              |    key: "mId"
                              |    features: {
                              |      // create an alien-type feature value, and expect Feathr to consume it via plugin
                              |      f1: $MULTILINE_QUOTE
                              |          import com.linkedin.feathr.offline.plugins.AlienFeatureValueMvelUDFs;
                              |          AlienFeatureValueMvelUDFs.sqrt_float(gamma)
                              |          $MULTILINE_QUOTE
                              |
                              |      // create an alien-type feature value, and pass it to a UDF that expects Feathr feature value
                              |      f2: $MULTILINE_QUOTE
                              |          import com.linkedin.feathr.offline.plugins.AlienFeatureValueMvelUDFs;
                              |          import com.linkedin.feathr.offline.plugins.FeathrFeatureValueMvelUDFs;
                              |          FeathrFeatureValueMvelUDFs.inverse_ffv(AlienFeatureValueMvelUDFs.sqrt_float(gamma))
                              |          $MULTILINE_QUOTE
                              |
                              |      // create a Feathr feature value, and pass it to a UDF that expects the alien feature value
                              |      f3: $MULTILINE_QUOTE
                              |          import com.linkedin.feathr.offline.plugins.AlienFeatureValueMvelUDFs;
                              |          import com.linkedin.feathr.offline.plugins.FeathrFeatureValueMvelUDFs;
                              |          AlienFeatureValueMvelUDFs.sqrt_afv(FeathrFeatureValueMvelUDFs.inverse_float(gamma))
                              |          $MULTILINE_QUOTE
                              |
                              |      f4: {
                              |        type: CATEGORICAL
                              |        def: $MULTILINE_QUOTE
                              |          import com.linkedin.feathr.offline.plugins.AlienFeatureValueMvelUDFs;
                              |          AlienFeatureValueMvelUDFs.uppercase_string(alpha);
                              |          $MULTILINE_QUOTE
                              |      }
                              |    }
                              |  }
                              |  anchor2: {
                              |    source: "anchor1-source.csv"
                              |      keyExtractor: "com.linkedin.feathr.offline.anchored.keyExtractor.AlienSampleKeyExtractor"
                              |      features: {
                              |       fA: {
                              |         def: cast_float(beta)
                              |         type: NUMERIC
                              |         default: 0
                              |       }
                              |      }
                              |  }
                              |}
                              |
                              |derivations: {
                              |  // use an UDF that expects/returns alien-valued feature value
                              |  f5: {
                              |    type: NUMERIC
                              |    definition: $MULTILINE_QUOTE
                              |      import com.linkedin.feathr.offline.plugins.AlienFeatureValueMvelUDFs;
                              |      AlienFeatureValueMvelUDFs.sqrt_float(f3)
                              |      $MULTILINE_QUOTE
                              |  }
                              |  f6: {
                              |     type: NUMERIC
                              |     definition: $MULTILINE_QUOTE
                              |       import com.linkedin.feathr.offline.plugins.AlienFeatureValueMvelUDFs;
                              |       AlienFeatureValueMvelUDFs.sqrt_float(f2)
                              |       $MULTILINE_QUOTE
                              |  }
                              |  f7: {
                              |     type: CATEGORICAL
                              |     definition: $MULTILINE_QUOTE
                              |       import com.linkedin.feathr.offline.plugins.AlienFeatureValueMvelUDFs;
                              |       AlienFeatureValueMvelUDFs.lowercase_string_afv(f4);
                              |       $MULTILINE_QUOTE
                              |  }
                              |  f8: {
                              |    key: ["mId"]
                              |    inputs: [{ key: "mId", feature: "f6" }]
                              |    class: "com.linkedin.feathr.offline.derived.SampleAlienFeatureDerivationFunction"
                              |    type: NUMERIC
                              |  }
                              |}
        """.stripMargin,
      observationDataPath = "anchorAndDerivations/testMVELLoopExpFeature-observations.csv",
      mvelContext = Some(mvelContext))


    val selectedColumns = Seq("a_id", "fA")
    val filteredDf = df.data.select(selectedColumns.head, selectedColumns.tail: _*)

    val expectedDf = ss.createDataFrame(
      ss.sparkContext.parallelize(
        Seq(
          Row(
            "1",
            10.0f),
          Row(
            "2",
            10.0f),
          Row(
            "3",
            10.0f))),
      StructType(
        List(
          StructField("a_id", StringType, true),
          StructField("fA", FloatType, true))))
    def cmpFunc(row: Row): String = row.get(0).toString
    FeathrTestUtils.assertDataFrameApproximatelyEquals(filteredDf, expectedDf, cmpFunc)
  }


}

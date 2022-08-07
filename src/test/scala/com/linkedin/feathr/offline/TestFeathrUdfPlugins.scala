package com.linkedin.feathr.offline

import com.linkedin.feathr.offline.mvel.plugins.FeathrMvelPluginContext
import com.linkedin.feathr.offline.plugins.{AlienFeatureValue, AlienFeatureValueTypeAdaptor}
import org.testng.annotations.Test

class TestFeathrUdfPlugins extends FeathrIntegTest {

  val MULTILINE_QUOTE = "\"\"\""

  @Test
  def testMvelUdfPluginSupport: Unit = {
    FeathrMvelPluginContext.builder().addFeatureTypeAdaptor(classOf[AlienFeatureValue], new AlienFeatureValueTypeAdaptor()).build().installTypeAdaptorsIntoMvelRuntime()

    val df = runLocalFeatureJoinForTest(
      joinConfigAsString = """
                             | features: {
                             |   key: a_id
                             |   featureList: ["f1", "f2", "f3", "f4", "f5", "f6", "f7"]
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
                             |}
        """.stripMargin,
      observationDataPath = "anchorAndDerivations/testMVELLoopExpFeature-observations.csv")

    df.data.show()

    // TODO UPDATE THE EXPECTED DF BELOW
//    val selectedColumns = Seq("a_id", "featureWithNull")
//    val filteredDf = df.data.select(selectedColumns.head, selectedColumns.tail: _*)
//
//    val expectedDf = ss.createDataFrame(
//      ss.sparkContext.parallelize(
//        Seq(
//          Row(
//            // a_id
//            "1",
//            // featureWithNull
//            1.0f),
//          Row(
//            // a_id
//            "2",
//            // featureWithNull
//            0.0f),
//          Row(
//            // a_id
//            "3",
//            // featureWithNull
//            3.0f))),
//      StructType(
//        List(
//          StructField("a_id", StringType, true),
//          StructField("featureWithNull", FloatType, true))))
//    def cmpFunc(row: Row): String = row.get(0).toString
//    FeathrTestUtils.assertDataFrameApproximatelyEquals(filteredDf, expectedDf, cmpFunc)
  }
}

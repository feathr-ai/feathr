package com.linkedin.feathr.offline.generation

import com.linkedin.feathr.common.types.protobuf.FeatureValueOuterClass
import com.linkedin.feathr.common.{FeatureInfo, FeatureTypes, Header, TaggedFeatureName}
import com.linkedin.feathr.offline.generation.outputProcessor.PushToRedisOutputProcessor
import com.linkedin.feathr.offline.{AssertFeatureUtils, TestFeathr}
import org.apache.spark.sql.Row
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.types.{ArrayType, BooleanType, FloatType, IntegerType, StringType, StructField, StructType}
import org.scalatest.mockito.MockitoSugar
import org.testng.annotations.Test

import java.util.Base64

class TestPushToRedisOutputProcessor extends TestFeathr with MockitoSugar {

  /**
   * Test evaluateStage returns the input DataFrame if the derived feature already exists.
   */
  @Test
  def testEncodeDataFrame(): Unit = {
    val pushToRedisOutputProcessor = new PushToRedisOutputProcessor(null)


    val expSchema = StructType(
      List(
        StructField("key0", IntegerType, nullable = false),
        StructField("__feathr_feature_f", FloatType, nullable = false),
        StructField("__feathr_feature_f2", IntegerType, nullable = false),
        StructField("__feathr_feature_g", StringType, nullable = true),
        StructField("__feathr_feature_h", BooleanType, nullable = false),
        StructField("__feathr_feature_j", ArrayType(IntegerType, containsNull = false), nullable = true),
        StructField(
          "__feathr_feature_sparse1",
          StructType(List(
            StructField("indices0", ArrayType(IntegerType, containsNull = false), nullable = false),
            StructField("values", ArrayType(FloatType, containsNull = false), nullable = false))),
          nullable = true),
      ))

    val rawDf = ss.createDataFrame(
      ss.sparkContext.parallelize(
        Seq(
          Row(1, 1.6f, 2, "g1", true, List(1, 2, 3), Row(List(1), List(1.0f)), Row(List("a"), List(1.0f))),
          Row(2, -1.6f, -2, "g2", false, List(3, 4, 5), Row(List(2), List(-1.0f)), Row(List("a"), List(-1.0f))))),
      expSchema)

    rawDf.show()
    val featureInfoMap = Map(
      new TaggedFeatureName("", "__feathr_feature_f") -> new FeatureInfo("__feathr_feature_f", FeatureTypes.NUMERIC),
      new TaggedFeatureName("", "__feathr_feature_f2") -> new FeatureInfo("__feathr_feature_f2", FeatureTypes.NUMERIC),
      new TaggedFeatureName("", "__feathr_feature_g") -> new FeatureInfo("__feathr_feature_g", FeatureTypes.CATEGORICAL),
      new TaggedFeatureName("", "__feathr_feature_h") -> new FeatureInfo("__feathr_feature_h", FeatureTypes.BOOLEAN),
      new TaggedFeatureName("", "__feathr_feature_j") -> new FeatureInfo("__feathr_feature_j", FeatureTypes.TENSOR),
      new TaggedFeatureName("", "__feathr_feature_sparse1") -> new FeatureInfo("__feathr_feature_sparse1", FeatureTypes.TENSOR),
    )
    val header = new Header(featureInfoMap)

    val encoded = pushToRedisOutputProcessor.encodeDataFrame(header, rawDf)
    encoded.show()



    val encoder = RowEncoder(expSchema)
    val encodedDfSchema = encoded.schema
    val decodedDf = encoded.map(row => {
      Row.fromSeq(encodedDfSchema.indices.map { i =>
      {
        if (encodedDfSchema.fields(i).name != "key0") {
          val qqq = row.get(i).asInstanceOf[String]
          val decoded = Base64.getDecoder.decode(qqq)
          val featureValue=  FeatureValueOuterClass.FeatureValue.parseFrom(decoded)
          if (featureValue.hasFloatValue) {
            featureValue.getFloatValue
          } else if (featureValue.hasBooleanValue) {
              featureValue.getBooleanValue
          } else if (featureValue.hasStringValue) {
            featureValue.getStringValue
          } else if (featureValue.hasIntValue) {
            featureValue.getIntValue
          } else if (featureValue.hasFloatValue) {
            featureValue.getFloatValue
          } else if (featureValue.hasDoubleValue) {
            featureValue.getDoubleValue
          } else if (featureValue.hasIntArray) {
            featureValue.getIntArray.getIntsList.toArray
          } else if (featureValue.hasSparseFloatArray) {
            Row(
              featureValue.getSparseFloatArray.getIntegersList.toArray,
              featureValue.getSparseFloatArray.getFloatsList.toArray,
            )
          } else {
            throw new RuntimeException("can't be decoded.")
          }
        } else {
          row.get(i)
        }
      }})
    })(encoder)


    decodedDf.show()

    AssertFeatureUtils.assertDataFrameEquals(decodedDf, rawDf)
  }
}

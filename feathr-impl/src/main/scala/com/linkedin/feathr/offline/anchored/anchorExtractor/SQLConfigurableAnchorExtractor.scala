package com.linkedin.feathr.offline.anchored.anchorExtractor

import com.fasterxml.jackson.annotation.JsonProperty
import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrConfigException}
import com.linkedin.feathr.offline.config.SQLFeatureDefinition
import com.linkedin.feathr.offline.transformation.FeatureColumnFormat.{FeatureColumnFormat, RAW}
import com.linkedin.feathr.sparkcommon.SimpleAnchorExtractorSpark
import org.apache.log4j.Logger
import org.apache.spark.sql.functions.expr
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Column, DataFrame}

/**
 * A SQL-based configurable FeatureAnchor that extract features based on its SQL expression definitions
 * Unlike {@link SimpleConfigurableAnchorExtractor }, this anchor extractor extends AnchorExtractorSpark,
 * which provides a dataFrame based batch transformation
 * */
private[offline] case class SQLKeys(@JsonProperty("sqlExpr") val sqlExpr: Seq[String])

private[offline] class SQLConfigurableAnchorExtractor(
   @JsonProperty("key") val key: SQLKeys,
   @JsonProperty("features") val features: Map[String, SQLFeatureDefinition]) extends SimpleAnchorExtractorSpark {
  @transient private lazy val log = Logger.getLogger(getClass)
  private val featureToDefs = features
  private val columnNameToFeatureDefs = featureToDefs.map(f => (f._1, f._2.featureExpr))

  def getProvidedFeatureNames: Seq[String] = features.keys.toIndexedSeq

  /**
   * Apply the user defined SQL transformation to the dataframe and produce the (feature name, feature column) pairs,
   * one pair for each provided feature.
   * @param inputDF input dataframe
   * @return
   */
  override def transformAsColumns(inputDF: DataFrame): Seq[(String, Column)] = {
    // apply sql transformation for the features
    val featureNames = featureToDefs.map(_._1).toSet
    columnNameToFeatureDefs
      .map(featureNameAndDef => {
        // if there's another feature that is conflicting,
        // e.g, the two features will result in exception, because Bar definition 'Foo' is ambiguous, could be feature Foo
        // or a field named 'Foo' in the dataframe
        // features {
        //  Foo.sqlExpr: Foo
        //  Bar.sqlExpr: Foo
        // }
        if (featureNames.contains(featureNameAndDef._2) && (featureNameAndDef._1 != featureNameAndDef._2)) {
          throw new FeathrConfigException(
            ErrorLabel.FEATHR_ERROR,
            s"Feature ${featureNameAndDef._1} should not be defined as ${featureNameAndDef._2}, " +
              s"as there's another feature named ${featureNameAndDef._2} already, thus ambiguous.")
        }
        (featureNameAndDef._1, expr(featureNameAndDef._2))
      })
      .toSeq
  }

  /**
   * This is used in experimental-tensor-mode.
   * We cannot use the getFeatures() API in SimpleAnchorExtractorSpark, because the parameter 'expectedColSchemas'
   * in that API is Map[String, StructType], while in FDS format, the type is Map[String, DataType].

   * @param dataFrameWithKeyColumns source feature data
   * @param expectedColSchemas expected schemas for each requested features
   * @return A map from (feature name, feature column) pair to its FeatureColumnFormat.
   */
  def getTensorFeatures(dataFrameWithKeyColumns: DataFrame, expectedColSchemas: Map[String, DataType]): Map[(String, Column), FeatureColumnFormat] = {
    import org.apache.spark.sql.functions._
    columnNameToFeatureDefs.collect {
      case (featureName, featureDef) if (expectedColSchemas.keySet.contains(featureName)) =>
        val (rewrittenDef, featureColumnFormat) = (featureDef, RAW)
        ((featureName, expr(rewrittenDef)), featureColumnFormat)
    }
  }
}

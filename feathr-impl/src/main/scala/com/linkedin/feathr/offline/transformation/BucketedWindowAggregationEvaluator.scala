package com.linkedin.feathr.offline.transformation

import com.linkedin.feathr.common.FeatureTypeConfig
import com.linkedin.feathr.offline.anchored.anchorExtractor.TimeWindowConfigurableAnchorExtractor
import com.linkedin.feathr.offline.anchored.feature.FeatureAnchorWithSource
import com.linkedin.feathr.offline.job.TransformedResult
import com.linkedin.feathr.offline.swa.SlidingWindowFeatureUtils
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Evaluator that transforms features using MultiLevelAggregationTransform,
 */

private[offline] object BucketedWindowAggregationEvaluator {

  /**
   * Transform and add feature column to input dataframe using TimeWindowConfigurableAnchorExtractor
   * @param transformer SimpleAnchorExtractorSpark implementation
   * @param inputDf input dataframe
   * @param requestedFeatureNameAndPrefix feature names and prefix pairs.
   * @param featureAnchorWithSource feature anchor with source that has the transformer
   * @return (dataframe with features, feature column format), feature column format can only be FeatureColumnFormatRAW for now
   */
  def transform(
      transformer: TimeWindowConfigurableAnchorExtractor,
      df: DataFrame,
      requestedFeatureNameAndPrefix: Seq[(String, String)],
      featureAnchorWithSource: FeatureAnchorWithSource,
      keyColumnExprAndAlias: Seq[(String, String)]): TransformedResult = {
    val ss = SparkSession.builder().getOrCreate()
    val evaluator = new MultiLevelAggregationTransform(ss)
    val resultDf = transformer.features.foldLeft(df)(
      (inputDf, featureNameDefPair) => {
        val (featureName, featureDef) = featureNameDefPair
        val timeWindowParams = SlidingWindowFeatureUtils.getTimeWindowParam(featureAnchorWithSource.source)
        evaluator.applyAggregate(inputDf, featureDef.`def`,
          featureName,
          featureDef.window_str,
          keyColumnExprAndAlias,
          timeWindowParams.timestampColumn,
          timeWindowParams.timestampColumnFormat,
          featureDef.aggregationType.toString)
      }
    )
    TransformedResult(
      requestedFeatureNameAndPrefix,
      resultDf,
      requestedFeatureNameAndPrefix.map(c => (c._1, FeatureColumnFormat.RAW)).toMap,
      Map.empty[String, FeatureTypeConfig])
  }
}

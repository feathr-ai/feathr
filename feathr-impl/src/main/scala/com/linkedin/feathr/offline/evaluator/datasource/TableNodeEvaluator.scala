package com.linkedin.feathr.offline.evaluator.datasource

import com.linkedin.feathr.common.AnchorExtractor
import com.linkedin.feathr.compute.KeyExpressionType
import com.linkedin.feathr.offline.client.{FeathrClient2, NodeContext}
import com.linkedin.feathr.offline.config.ConfigLoaderUtils
import com.linkedin.feathr.offline.source.accessor.DataSourceAccessor
import com.linkedin.feathr.offline.source.{DataSource, SourceFormatType}
import com.linkedin.feathr.sparkcommon.SourceKeyExtractor
import com.linkedin.frame.core.config.producer.common.KeyListExtractor
import org.apache.spark.sql.SparkSession

import scala.collection.JavaConverters._

private[offline] object TableNodeEvaluator {
  def processTableNode(ss: SparkSession, dataSourceNode: com.linkedin.feathr.compute.DataSource): NodeContext = {
    assert(dataSourceNode.hasConcreteKey)
    assert(dataSourceNode.getConcreteKey.getKey.asScala.nonEmpty)
    val path = dataSourceNode.getExternalSourceRef // We are using ExternalSourceRef for way too many things at this point.

    // Augment time information also here. Table node should not have time info?
    val dataSource = DataSource(path, SourceFormatType.FIXED_PATH)
    val dataSourceAccessor = DataSourceAccessor(ss, dataSource, None, None, failOnMissingPartition = false)
    val sourceDF = dataSourceAccessor.get()
    val (df, keyExtractor) = if (dataSourceNode.getKeyExpressionType == KeyExpressionType.UDF) {
      if (Class.forName(dataSourceNode.getKeyExpression()).newInstance.isInstanceOf[SourceKeyExtractor]) {
        val keyExtractorClass = Class.forName(dataSourceNode.getKeyExpression()).newInstance.asInstanceOf[SourceKeyExtractor]
        val updatedDf = keyExtractorClass.appendKeyColumns(sourceDF)
        (updatedDf, keyExtractorClass.getKeyColumnNames())
      } else if (Class.forName(dataSourceNode.getKeyExpression()).newInstance.isInstanceOf[AnchorExtractor[_]]) {
        // key will be evaluated at the time of anchor evaluation.
        (sourceDF, Seq())
      } else {
        throw new RuntimeException("Undefined")
      }
    } else {
      val featureKeys = ConfigLoaderUtils.javaListToSeqWithDeepCopy(KeyListExtractor.getInstance().extractFromHocon(dataSourceNode.getKeyExpression()))
      (sourceDF, featureKeys)
    }

    NodeContext(df, keyExtractor, dataSource = Some(dataSource))
  }
}

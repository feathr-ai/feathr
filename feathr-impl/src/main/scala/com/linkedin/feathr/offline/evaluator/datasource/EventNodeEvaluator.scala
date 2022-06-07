package com.linkedin.feathr.offline.evaluator.datasource

import com.linkedin.feathr.compute.KeyExpressionType
import com.linkedin.feathr.core.config.producer.common.KeyListExtractor
import com.linkedin.feathr.offline.client.NodeContext
import com.linkedin.feathr.offline.config.ConfigLoaderUtils
import com.linkedin.feathr.offline.source.accessor.{DataPathHandler, DataSourceAccessor}
import com.linkedin.feathr.offline.source.{DataSource, SourceFormatType, TimeWindowParams}
import com.linkedin.feathr.offline.swa.SlidingWindowFeatureUtils.{EPOCH, TIMESTAMP_PARTITION_COLUMN, constructTimeStampExpr}
import com.linkedin.feathr.offline.util.datetime.DateTimeInterval
import com.linkedin.feathr.sparkcommon.SourceKeyExtractor
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

import scala.collection.JavaConverters._

private[offline] object EventNodeEvaluator {
  def processEventNode(ss: SparkSession, dataSourceNode: com.linkedin.feathr.compute.DataSource, timeRange: Option[DateTimeInterval],
    dataPathHandlers: List[DataPathHandler]): NodeContext = {
    assert(dataSourceNode.hasConcreteKey)
    assert(dataSourceNode.getConcreteKey.getKey.asScala.nonEmpty)
    val path = dataSourceNode.getExternalSourceRef // We are using ExternalSourceRef for way too many things at this point.

    // Augment time information also here. Table node should not have time info?
    val source = DataSource(path, SourceFormatType.TIME_SERIES_PATH, if (dataSourceNode.hasTimestampColumnInfo) {
      Some(TimeWindowParams(dataSourceNode.getTimestampColumnInfo().getExpression(),
        dataSourceNode.getTimestampColumnInfo().getFormat))
    } else None, if (dataSourceNode.hasFilePartitionFormat) {
      Some(dataSourceNode.getFilePartitionFormat)
    } else None)

    val timeWindowParam = if (dataSourceNode.hasTimestampColumnInfo) {
      TimeWindowParams(dataSourceNode.getTimestampColumnInfo().getExpression, dataSourceNode.getTimestampColumnInfo().getFormat)
    } else {
      TimeWindowParams(TIMESTAMP_PARTITION_COLUMN, "epoch")
    }
    val timeStampExpr = constructTimeStampExpr(timeWindowParam.timestampColumn, timeWindowParam.timestampColumnFormat)
    val needTimestampColumn = if (dataSourceNode.hasTimestampColumnInfo) false else true
    val dataSourceAccessor = DataSourceAccessor(ss, source, timeRange, None,
      failOnMissingPartition = false, addTimestampColumn = needTimestampColumn, dataPathHandlers = dataPathHandlers)
    val sourceDF = dataSourceAccessor.get()
    val (df, keyExtractor) = if (dataSourceNode.getKeyExpressionType == KeyExpressionType.UDF) {
      val keyExtractorClass = Class.forName(dataSourceNode.getKeyExpression()).newInstance.asInstanceOf[SourceKeyExtractor]
      (keyExtractorClass.appendKeyColumns(sourceDF), keyExtractorClass.getKeyColumnNames() :+ timeStampExpr)
    } else {
      val featureKeys = ConfigLoaderUtils.javaListToSeqWithDeepCopy(KeyListExtractor.getInstance().
        extractFromHocon(dataSourceNode.getKeyExpression)).map(k => s"CAST (${k} AS string)")
      (sourceDF, (featureKeys :+ timeStampExpr))
    }

    // Only for datasource node, we will append the timestampExpr with the key field. TODO - find a better way of doing this.
    NodeContext(df, keyExtractor)
  }
}
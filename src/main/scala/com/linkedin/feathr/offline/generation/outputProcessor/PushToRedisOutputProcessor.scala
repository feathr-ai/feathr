package com.linkedin.feathr.offline.generation.outputProcessor

import com.linkedin.feathr.common.Header
import com.linkedin.feathr.common.configObj.generation.OutputProcessorConfig
import com.linkedin.feathr.offline.generation.FeatureGenUtils
import com.linkedin.feathr.offline.generation.outputProcessor.PushToRedisOutputProcessor.TABLE_PARAM_CONFIG_NAME
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * feature generation output processor used to push data to Redis store
 * @param config config object of output processor, built from the feature generation config
 */

private[offline] class PushToRedisOutputProcessor(config: OutputProcessorConfig, endTimeOpt: Option[String] = None) extends WriteToHDFSOutputProcessor(config, endTimeOpt) {
  /**
   * process single dataframe, e.g, convert feature data schema
   *
   * @param ss         spark session
   * @param df         feature dataframe
   * @param header     meta info of the input dataframe
   * @param parentPath path to save feature data
   * @return processed dataframe and header
   */
  override def processSingle(ss: SparkSession, df: DataFrame, header: Header, parentPath: String): (DataFrame, Header) = {
    val keyColumns = FeatureGenUtils.getKeyColumnsFromHeader(header)

    val tableName = config.getParams.getString(TABLE_PARAM_CONFIG_NAME)
    val allFeatureCols = header.featureInfoMap.map(x => (x._2.columnName)).toSet
    RedisOutputUtils.writeToRedis(ss, df, tableName, keyColumns, allFeatureCols, SaveMode.Overwrite)
    (df, header)
  }
}

object PushToRedisOutputProcessor {
  // Parameter name in Redis output processor config for table name
  val TABLE_PARAM_CONFIG_NAME = "table_name"
}
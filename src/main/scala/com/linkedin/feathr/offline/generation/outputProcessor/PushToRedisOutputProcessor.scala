package com.linkedin.feathr.offline.generation.outputProcessor

import com.linkedin.feathr.common.Header
import com.linkedin.feathr.common.configObj.generation.OutputProcessorConfig
import com.linkedin.feathr.offline.generation.FeatureGenUtils
import org.apache.spark.sql.functions.{concat_ws, expr, when}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

/**
 * feature generation output processor used to push data to Redis store
 * @param config config object of output processor, built from the feature generation config
 */

private[offline] class PushToRedisOutputProcessor(config: OutputProcessorConfig, endTimeOpt: Option[String] = None) extends WriteToHDFSOutputProcessor(config, endTimeOpt) {

  /**
    * process single dataframe, e.g, convert feature data schema
    * @param ss spark session
    * @param df feature dataframe
    * @param header meta info of the input dataframe
    * @param parentPath path to save feature data
    * @return processed dataframe and header
    */
  override def processSingle(ss: SparkSession, df: DataFrame, header: Header, parentPath: String): (DataFrame, Header) = {
    val keyColumns = FeatureGenUtils.getKeyColumnsFromHeader(header)
    val nullElementGuardString = "_null_"
    val newColExpr = concat_ws("#", keyColumns.map(c => {
      val casted = expr(s"CAST (${c} as string)")
      // If any key in the keys is null, replace with special value and remove the row later
      when(casted.isNull, nullElementGuardString).otherwise(casted)
    }): _*)
    val tableParam = "table_name"
    val tableName = config.getParams.getString(tableParam)
    val outputKeyColumnName = "feature_key"
    val decoratedDf = df.withColumn(outputKeyColumnName, newColExpr)
      .drop(keyColumns: _*)
    decoratedDf.write
      .format("org.apache.spark.sql.redis")
      .option("table", tableName)
      .option("key.column", outputKeyColumnName)
      .mode(SaveMode.Overwrite)
      .save()
    (df, header)
  }
}

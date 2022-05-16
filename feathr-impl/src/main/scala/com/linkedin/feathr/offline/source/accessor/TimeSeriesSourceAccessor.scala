package com.linkedin.feathr.offline.source.accessor

import com.linkedin.feathr.common.DateTimeResolution.DateTimeResolution
import com.linkedin.feathr.offline.generation.SparkIOUtils
import com.linkedin.feathr.offline.source.DataSource
import com.linkedin.feathr.offline.source.dataloader.BatchDataLoader
import com.linkedin.feathr.offline.swa.SlidingWindowFeatureUtils
import com.linkedin.feathr.offline.util.datetime.{DateTimeInterval, OfflineDateTimeUtils}
import org.apache.hadoop.mapred.JobConf
import org.apache.log4j.Logger
import org.apache.spark.sql.functions.expr
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 *
 * @param ss The Spark session.
 * @param source source info of the whole dataset
 * @param sourceTimeInterval timespan of dataset
 * @param dateTimeResolution the resolution of the timespan. Either daily or hourly.
 * @param failOnMissingPartition   if true, we will validate all the date partitions exist in the source path.
 * @param addTimestampColumn whether to create a timestamp column from the time partition of the source.
 */
private[offline] class TimeSeriesSourceAccessor(
    ss: SparkSession,
    source: DataSource,
    sourceTimeInterval: DateTimeInterval,
    dateTimeResolution: DateTimeResolution,
    failOnMissingPartition: Boolean,
    addTimestampColumn: Boolean)
    extends TimeBasedDataSourceAccessor(source, sourceTimeInterval, failOnMissingPartition) {
  private val log = Logger.getLogger(getClass)
  override def get(timeIntervalOpt: Option[DateTimeInterval]): DataFrame = {
    val interval = timeIntervalOpt.map(sourceTimeInterval.intersection).getOrElse(sourceTimeInterval)
    val dateRange = OfflineDateTimeUtils.dateRange(interval, dateTimeResolution)
    val jobConf = new JobConf(ss.sparkContext.hadoopConfiguration)
    val df = new BatchDataLoader(ss, source.location).loadDataFrame(Map(SparkIOUtils.FILTER_EXP -> dateRange), jobConf)
    if (addTimestampColumn) {
      log.info(s"added timestamp column to source ${source.path}.")
      df.withColumn(
        SlidingWindowFeatureUtils.TIMESTAMP_PARTITION_COLUMN,
        expr(SlidingWindowFeatureUtils.constructTimeStampExpr("datepartition", "yyyy-MM-dd-HH")))
    } else {
      df
    }
  }
}

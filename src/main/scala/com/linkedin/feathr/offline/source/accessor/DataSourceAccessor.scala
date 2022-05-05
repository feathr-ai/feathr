package com.linkedin.feathr.offline.source.accessor

import com.linkedin.feathr.offline.source.dataloader.DataLoaderFactory
import com.linkedin.feathr.offline.source.pathutil.{PathChecker, TimeBasedHdfsPathAnalyzer}
import com.linkedin.feathr.offline.source.{DataSource, SourceFormatType}
import com.linkedin.feathr.offline.util.PartitionLimiter
import com.linkedin.feathr.offline.util.datetime.DateTimeInterval
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.io.File

/**
 * the base class to access and load the data source
 * The DataSourceAccessor can load a fixed path, a list of path as well as a path containing time series data.
 * @param source the data source.
 */
private[offline] abstract class DataSourceAccessor(val source: DataSource) {

  /**
   * get source data as a dataframe
   *
   * @return the dataframe
   */
  def get(): DataFrame
}

private[offline] object DataSourceAccessor {

  /**
   * create time series/composite source that contains multiple day/hour data
   *
   * @param ss              spark session
   * @param source          source definition
   * @param dateIntervalOpt timespan of dataset
   * @param expectDatumType expect datum type in RDD form
   * @param failOnMissingPartition   whether to fail the file loading if some of the date partitions are missing.
   * @param addTimestampColumn whether to create a timestamp column from the time partition of the source.
   * @return a TimeSeriesSource
   */
  def apply(
      ss: SparkSession,
      source: DataSource,
      dateIntervalOpt: Option[DateTimeInterval],
      expectDatumType: Option[Class[_]],
      failOnMissingPartition: Boolean,
      addTimestampColumn: Boolean = false,
      isStreaming: Boolean = false): DataSourceAccessor = {
    val sourceType = source.sourceType
    val dataLoaderFactory = DataLoaderFactory(ss, isStreaming)
    if (isStreaming) {
      new StreamDataSourceAccessor(ss, dataLoaderFactory, source)
    } else if (dateIntervalOpt.isEmpty || sourceType == SourceFormatType.FIXED_PATH || sourceType == SourceFormatType.LIST_PATH) {
      // if no input interval, or the path is fixed or list, load whole dataset
      new NonTimeBasedDataSourceAccessor(ss, dataLoaderFactory, source, expectDatumType)
    } else {
      val timeInterval = dateIntervalOpt.get
      createFromHdfsPath(ss, source, timeInterval, expectDatumType, failOnMissingPartition, addTimestampColumn)
    }
  }

  /**
   * create DataSourceAccessor from HDFS path
   *
   * @param ss              spark session
   * @param source          source definition
   * @param timeInterval timespan of dataset
   * @param expectDatumType expect datum type in RDD form
   * @param failOnMissingPartition whether to fail the file loading if some of the date partitions are missing.
   * @param addTimestampColumn whether to create a timestamp column from the time partition of the source.
   * @return a TimeSeriesSource
   */
  private def createFromHdfsPath(
      ss: SparkSession,
      source: DataSource,
      timeInterval: DateTimeInterval,
      expectDatumType: Option[Class[_]],
      failOnMissingPartition: Boolean,
      addTimestampColumn: Boolean): DataSourceAccessor = {
    val pathChecker = PathChecker(ss)
    val fileLoaderFactory = DataLoaderFactory(ss)
    val partitionLimiter = new PartitionLimiter(ss)
    val pathAnalyzer = new TimeBasedHdfsPathAnalyzer(pathChecker)
    val fileName = new File(source.path).getName
    if (source.timePartitionPattern.isDefined) {
      // case 1: the timePartitionPattern exists
      val pathInfo = pathAnalyzer.analyze(source.path, source.timePartitionPattern.get)
      PathPartitionedTimeSeriesSourceAccessor(
        pathChecker,
        fileLoaderFactory,
        partitionLimiter,
        pathInfo,
        source,
        timeInterval,
        failOnMissingPartition,
        addTimestampColumn)
    } else {
      // legacy configurations without timePartitionPattern
      if (fileName.endsWith("daily") || fileName.endsWith("hourly") || source.sourceType == SourceFormatType.TIME_PATH) {
        // case 2: if it's daily/hourly data, load the partitions
        val pathInfo = pathAnalyzer.analyze(source.path)
        PathPartitionedTimeSeriesSourceAccessor(
          pathChecker,
          fileLoaderFactory,
          partitionLimiter,
          pathInfo,
          source,
          timeInterval,
          failOnMissingPartition,
          addTimestampColumn)
      } else {
        // case 3: load as whole dataset
        new NonTimeBasedDataSourceAccessor(ss, fileLoaderFactory, source, expectDatumType)
      }
    }
  }
}

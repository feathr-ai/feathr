package com.linkedin.feathr.offline.transformation

import java.time.Duration
import com.linkedin.feathr.common.{DateParam, DateTimeResolution}
import com.linkedin.feathr.offline.source.SourceFormatType._
import com.linkedin.feathr.offline.anchored.feature.FeatureAnchorWithSource
import com.linkedin.feathr.offline.config.location.{PathList, SimplePath}
import com.linkedin.feathr.offline.generation.IncrementalAggContext
import com.linkedin.feathr.offline.source.DataSource
import com.linkedin.feathr.offline.source.accessor.DataSourceAccessor
import com.linkedin.feathr.offline.source.accessor.DataPathHandler
import com.linkedin.feathr.offline.source.dataloader.DataLoaderHandler
import com.linkedin.feathr.offline.source.pathutil.{PathChecker, TimeBasedHdfsPathAnalyzer}
import com.linkedin.feathr.offline.swa.SlidingWindowFeatureUtils
import com.linkedin.feathr.offline.util.SourceUtils
import com.linkedin.feathr.offline.util.datetime.{DateTimeInterval, OfflineDateTimeUtils}
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * The primary responsibility of this class is to Map the anchored features to its DataFrame.
 */
private[offline] class AnchorToDataSourceMapper(dataPathHandlers: List[DataPathHandler]) {

  /**
   * Get basic anchored feature to datasource mapping for feature join use case.
   * The DataAccessor is instantiated with no DateInterval specification.
   * Note: To support time-based join, this API will evolve to take in date range of observation as input.
   *
   * @param ss                      spark session
   * @param requiredFeatureAnchors  all required anchored features, non-sliding window aggregation feature.
   * @param failOnMissingPartition  fail loading the data if some of the date partitions are missing.
   * @return a map from a FeatureAnchorWithSource to the dataset the anchor is defined on
   */
  def getBasicAnchorDFMapForJoin(
      ss: SparkSession,
      requiredFeatureAnchors: Seq[FeatureAnchorWithSource],
      failOnMissingPartition: Boolean): Map[FeatureAnchorWithSource, DataSourceAccessor] = {
    // get a Map from each source to a list of all anchors based on this source
    val sourceToAnchor = requiredFeatureAnchors
      .map(anchor => (anchor.source, anchor))
      .groupBy(_._1) // group by source
      .map({
        case (source, grouped) => (source, grouped.map(_._2))
      })
    sourceToAnchor.flatMap({
      case (source, anchorsWithDate) =>
        val dateParams = anchorsWithDate.map(_.dateParam)
        val expectDatumType = SourceUtils.getExpectDatumType(anchorsWithDate.map(_.featureAnchor.extractor))
        // find a minimum interval to cover all the intervals, as they share the same source, we should create only
        // one DataSourceAccessor for them
        val dateInterval = dateParams.foldLeft(None: Option[DateTimeInterval]) {
          (timeIntervalOpt: Option[DateTimeInterval], otherDateOpt) =>
            if (otherDateOpt.getOrElse(None).equals(DateParam())) {
              // DateParam(None, None, None, None) is not a valid DateParam parameter
              timeIntervalOpt
            } else {
              val otherTimeIntervalOpt = otherDateOpt.map(date => OfflineDateTimeUtils.createTimeIntervalFromDateParam(Some(date), None))
              (timeIntervalOpt, otherTimeIntervalOpt) match {
                case (Some(interval), Some(otherInterval)) => Some(interval.minCoverage(otherInterval))
                case (Some(interval), _) => Some(interval)
                case (_, otherInterval) => otherInterval
              }
            }
        }
        val timeSeriesSource = DataSourceAccessor(ss = ss,
                                                  source = source, 
                                                  dateIntervalOpt = dateInterval, 
                                                  expectDatumType = Some(expectDatumType), 
                                                  failOnMissingPartition = failOnMissingPartition,
                                                  dataPathHandlers = dataPathHandlers)
        
        anchorsWithDate.map(anchor => (anchor, timeSeriesSource))
    })
  }

  /**
   * This API is used to get DataSource for SWA features. It uses the time range of observation data,
   * gets the needed fact dataset for a window aggregation feature anchor as a DataFrame.
   *
   * @param ss Spark Session
   * @param factDataSource Source path of fact dataset, could be a HDFS path
   * @param obsTimeRange the time range of observation data
   * @param window the length of window time
   * @param timeDelays an array with delay durations
   * @param failOnMissingPartition  fail loading the data if some of the date partitions are missing.
   * @return loaded fact dataset, as a DataFrame.
   */
  def getWindowAggAnchorDFMapForJoin(
      ss: SparkSession,
      factDataSource: DataSource,
      obsTimeRange: DateTimeInterval,
      window: Duration,
      timeDelays: Array[Duration],
      failOnMissingPartition: Boolean): DataFrame = {

    val dataLoaderHandlers: List[DataLoaderHandler] = dataPathHandlers.map(_.dataLoaderHandler)

    // Only file-based source has real "path", others are just single dataset
    val adjustedObsTimeRange = if (factDataSource.location.isFileBasedLocation()) {
      val pathChecker = PathChecker(ss)
      val pathAnalyzer = new TimeBasedHdfsPathAnalyzer(pathChecker, dataLoaderHandlers)
      val pathInfo = pathAnalyzer.analyze(factDataSource.path)
      if (pathInfo.dateTimeResolution == DateTimeResolution.DAILY)
      {
        obsTimeRange.adjustWithDateTimeResolution(DateTimeResolution.DAILY)
      } else obsTimeRange
    } else {
      obsTimeRange
    }

    val timeInterval = OfflineDateTimeUtils.getFactDataTimeRange(adjustedObsTimeRange, window, timeDelays)
    val needCreateTimestampColumn = SlidingWindowFeatureUtils.needCreateTimestampColumnFromPartition(factDataSource)
    val timeSeriesSource =
      DataSourceAccessor(
        ss = ss,
        source = factDataSource,
        dateIntervalOpt = Some(timeInterval),
        expectDatumType = None,
        failOnMissingPartition = failOnMissingPartition,
        addTimestampColumn = needCreateTimestampColumn,
        dataPathHandlers = dataPathHandlers)
    timeSeriesSource.get()
  }

  /**
   * Get anchored feature to DataSource mapping for feature generation use case.
   * This API infers the time interval for filtering feature data from the source based on the operational config
   * specifications. Incremental Aggregation context is required to calculate the dateInterval based on last available
   * aggregation snapshot.
   * @param ss                      Spark Session.
   * @param requiredFeatureAnchors  All required anchored features (SWA and non-SWA features).
   * @param incrementalAggContext   Incremental aggregation related metadata.
   * @param failOnMissingPartition  fail loading the data if some of the date partitions are missing.
   * @return a map from a FeatureAnchorWithSource to the dataset the anchor is defined on.
   */
  def getAnchorDFMapForGen(
      ss: SparkSession,
      requiredFeatureAnchors: Seq[FeatureAnchorWithSource],
      incrementalAggContext: Option[IncrementalAggContext],
      failOnMissingPartition: Boolean,
      isStreaming: Boolean = false): Map[FeatureAnchorWithSource, DataSourceAccessor] = {
    // get a Map from each source to a list of all anchors based on this source
    val sourceToAnchor = requiredFeatureAnchors
      .map(anchor => (anchor.source, anchor))
      .groupBy(_._1) // group by source
      .map({
        case (source, grouped) => (source, grouped.map(_._2))
      })

    // For every source, build the source accessor.
    sourceToAnchor.flatMap({
      case (source, anchors) =>
        val expectDatumType = SourceUtils.getExpectDatumType(anchors.map(_.featureAnchor.extractor))
        val dateIntervals = anchors.collect {
          case anchor if anchor.dateParam.isDefined => OfflineDateTimeUtils.createIntervalFromFeatureGenDateParam(anchor.dateParam.get)
        }
        val dateIntervalOpt = {
          if (dateIntervals.nonEmpty) {
            Some(getSmallestInterval(dateIntervals, source.sourceType, incrementalAggContext.get.daysSinceLastAgg))
          } else {
            None
          }
        }
        val needCreateTimestampColumn = source.timePartitionPattern.nonEmpty && source.timeWindowParams.isEmpty
        val timeSeriesSource = DataSourceAccessor(
          ss = ss,
          source = source,
          dateIntervalOpt = dateIntervalOpt,
          expectDatumType = Some(expectDatumType),
          failOnMissingPartition = failOnMissingPartition,
          addTimestampColumn = needCreateTimestampColumn,
          isStreaming = isStreaming,
          dataPathHandlers = dataPathHandlers)
          
        anchors.map(anchor => (anchor, timeSeriesSource))
    })
  }

  /**
   * Get the smallest date time interval for the sequence time intervals specified in dateparams field.
   * @param dateParams the anchor's date params fields.
   * @param sourceType Source type of the anchored feature.
   * @param daysSinceLastAgg number of days since the last aggregation snapshot was run.
   * @return a datasource with a new path based on the observation timestamp.
   */
  private[transformation] def getSmallestInterval(
      dateParams: Seq[DateTimeInterval],
      sourceType: SourceFormatType,
      daysSinceLastAgg: Option[Long]): DateTimeInterval = {
    // find the smallest interval that cover all the given dataParam ranges
    val interval = dateParams.reduce(_ span _)
    if (sourceType == TIME_SERIES_PATH && daysSinceLastAgg.isDefined) {
      val extraDays = daysSinceLastAgg.get + 1
      val startWithGap = interval.getStart.minusDays(extraDays)
      new DateTimeInterval(startWithGap, interval.getEnd)
    } else {
      interval
    }
  }
}

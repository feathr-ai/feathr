package com.linkedin.feathr.offline.transformation

import com.linkedin.feathr.offline.swa.SlidingWindowFeatureUtils.constructTimeStampExpr
import com.linkedin.feathr.offline.util.AnchorUtils.removeNonAlphaNumChars
import com.linkedin.feathr.swj.aggregate.AggregationType
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.TimeZone

class MultiLevelAggregationTransform(ss: SparkSession,
                                     debug: Boolean = false) {
  val sqlContext = ss.sqlContext
  import sqlContext.implicits._
  // Define the interval size in seconds
  // Set to legacy so that week of the year can be recognized,
  // see https://spark.apache.org/docs/latest/sql-ref-datetime-pattern.html
  ss.conf.set("spark.sql.legacy.timeParserPolicy","LEGACY")

  val minutes = (x:Int) => x * 60
  val dataTimezoneName = "Etc/UTC"

  val dataTimezoneOffset = "+00:00"
  val timeZone = TimeZone.getDefault
  val currentSystemTimezone = timeZone.getID
  val basicBucketColumnNameReadable = "roundedToBasicIntervalInMinutes"
  val UtcTimestampColumnName = "utc_ts_long"
  val oneHourBucketTimestampColumn = "roundedToHr"
  val utc_min = "utc_min"
  val roundedMin = "roundedMin"
  val roundedBasicBucketTimestampColumn = "unixBasicBucketInMin"
  val utc_ts_string = "utc_ts_string"
  val aggregatedItemsSoFarInCurrentBasicBucket = "aggregatedItemsSoFarInCurrentBasicBucket"
  val aggregatedItemsEveryFullBasicBucket = "aggregatedItemsEveryFullBasicBucket"
  val featureValueAtBasicLevelFullBucket = "featureValueAtBasicLevelFullBucket"
  val oneHourCountDistinctFeatureName = "oneHourCountDistinctFeatureName"
  val useHyperLogLog = false

  object RollUpLevel extends Enumeration {
    case class RollUpLevelVal(name: String,
                              timeStampFormat: String,
                              durationInSecond: Int,
                              lowerLevel: Option[RollUpLevelVal] = None) extends super.Val {
    }
    val FiveMin = RollUpLevelVal("5m", "yyyy-MM-dd-HH-mm", 60*5)
    val OneHour = RollUpLevelVal("1h", "yyyy-MM-dd-HH", 60*60, Some(FiveMin))
    val OneDay = RollUpLevelVal("1d", "yyyy-MM-dd", 60*60*24, Some(OneHour))
    val OneWeek = RollUpLevelVal("1w", "yyyy-w", 60*60*24*7, Some(OneDay))
    val OneMonth = RollUpLevelVal("1M", "yyyy-MM", 60*60*24*30, Some(OneDay))
    val OneYear = RollUpLevelVal("1y", "yyyy", 60*60*24*30*12, Some(OneMonth))
  }
  import RollUpLevel._

  /**
   * Compute the count distinct
   * @param df source dataframe
   * @param keyColumnExprAndAlias key column expression and alias mapping
   * @param featureDefFieldField The field/expression to aggregate
   * @param basicLevel The basic rollup level of this aggregation
   * @param outputFeatureColumnName the output feature column name
   * @return The source dataframe with the feature column appended and the intermediate columns added
   */
  def applyAggregationAtBasicLevel(df: DataFrame,
                                   keyColumnExprAndAlias: Seq[(String, String)],
                                   featureDefFieldField: String,
                                   basicLevel: RollUpLevelVal,
                                   outputFeatureColumnName: String,
                                   timeColumnName: String,
                                   timeStampFormat: String,
                                   aggregateFunction: AggregationType.Value): (DataFrame, Seq[String]) = {
    val basicIntervalInMinutes = basicLevel.durationInSecond / 60
    // TODO Try orderBy(col("start").cast("timestamp").cast("long")) for performance
    val withTimestampsDf = df
      // utc_ts is a long type
      .withColumn(UtcTimestampColumnName, expr(constructTimeStampExpr(timeColumnName, timeStampFormat, Some(dataTimezoneName))))
      // convert to a string representation such as "2017-07-14 02:40:00"
      .withColumn(utc_ts_string, to_utc_timestamp(to_timestamp(col(UtcTimestampColumnName)), currentSystemTimezone))
      .withColumn(oneHourBucketTimestampColumn, date_format(col(utc_ts_string), "yyyy-MM-dd-HH"))
      .withColumn(utc_min, (date_format(from_utc_timestamp(to_timestamp(col(UtcTimestampColumnName)), currentSystemTimezone), "mm")))
      .withColumn(roundedMin, (col(utc_min)/basicIntervalInMinutes).cast("integer")*basicIntervalInMinutes)
      .withColumn(basicBucketColumnNameReadable,
        concat(col(oneHourBucketTimestampColumn),
          // minutesPadding
          when(col(roundedMin) >=10, lit("-")).otherwise(lit("-0")),
          col(roundedMin))
      )
      .withColumn(roundedBasicBucketTimestampColumn,  expr(constructTimeStampExpr(basicBucketColumnNameReadable, basicLevel.timeStampFormat, Some(dataTimezoneName))))

    val newKeyColumnExprs = keyColumnExprAndAlias.filter(p => !p._1.equals(p._2))
    val withKeyColumnsDf = newKeyColumnExprs.foldLeft(withTimestampsDf) { (acc, alias) =>
      acc.withColumn(alias._2, expr(alias._1))
    }
    val keyColumns = keyColumnExprAndAlias.map(_._2).toSeq
    val sortByTimeColumn = UtcTimestampColumnName

    val windowSpec = Window.partitionBy(roundedBasicBucketTimestampColumn, keyColumns: _*)
      .orderBy(col(sortByTimeColumn))
      .rangeBetween(
        Window.currentRow - minutes(basicIntervalInMinutes),
        Window.currentRow - 1
      )

    val distinctCount = if (useHyperLogLog) {
      withKeyColumnsDf.withColumn(outputFeatureColumnName,
        approx_count_distinct(expr(featureDefFieldField)).over(windowSpec))
    } else {
      withKeyColumnsDf.withColumn(aggregatedItemsSoFarInCurrentBasicBucket,
        collect_set(expr(featureDefFieldField)).over(windowSpec))
        .withColumn(outputFeatureColumnName, size(col(aggregatedItemsSoFarInCurrentBasicBucket)))
    }
    val fullBasicUnitWindowSpec = Window.partitionBy(keyColumns.head, keyColumns.tail: _*)
      .orderBy(col(roundedBasicBucketTimestampColumn))
      .rangeBetween(
        Window.currentRow,
        Window.currentRow
      )
    val withFeatureDf = distinctCount.withColumn(aggregatedItemsEveryFullBasicBucket,
      collect_set(expr(featureDefFieldField)).over(fullBasicUnitWindowSpec)
    ).withColumn(featureValueAtBasicLevelFullBucket, size(col(aggregatedItemsEveryFullBasicBucket)))
    val intermediateCols = Seq(UtcTimestampColumnName, utc_ts_string, oneHourBucketTimestampColumn, utc_min
      , roundedMin, basicBucketColumnNameReadable, roundedBasicBucketTimestampColumn, aggregatedItemsSoFarInCurrentBasicBucket,
      aggregatedItemsEveryFullBasicBucket, featureValueAtBasicLevelFullBucket, outputFeatureColumnName) ++ newKeyColumnExprs.map(_._2)
    (withFeatureDf, intermediateCols)
  }

  def multiLevelRollUpAggregate(rollUpLevels: Seq[RollUpLevelVal],
                       df: DataFrame,
                       keyColumns: Seq[String],
                       featureDefAggField: String,
                       basicLevelAggregationFeatureColumn: String,
                       aggregationFunction: AggregationType.Value): DataFrame = {
    val baseFeatureName = "agg_" + featureDefAggField
    val basicRollupOutputData = if (debug) {
      RollUpOutputData(df,
        roundedBasicBucketTimestampColumn,
        aggregatedItemsEveryFullBasicBucket,
        aggregatedItemsSoFarInCurrentBasicBucket)
    } else {
      RollUpOutputData(df,
        roundedBasicBucketTimestampColumn,
        featureValueAtBasicLevelFullBucket,
        basicLevelAggregationFeatureColumn)
    }
    val result = rollUpLevels.foldLeft(basicRollupOutputData)((result, highLevel) =>
      rollUpAggregateRecursive(result.df,
        keyColumns,
        UtcTimestampColumnName,
        utc_ts_string,
        highLevel.lowerLevel.get,
        highLevel,
        result.roundedHighLevelBucketTimestampColumn,
        result.aggedDataForEveryFullBucketAtHighLevel,
        result.aggedDataAtCurrentBucketAtHighLevelSoFar,
        result.aggedDataForEveryFullBucketAtHighLevel,
        result.aggedDataAtCurrentBucketAtHighLevelSoFar,
        baseFeatureName + "_" + highLevel.name,
        aggregationFunction
      )
    )
    result.df
  }

  /**
   * Apply
   * @param df
   * @param featureDefAggField
   * @param outputFeatureColumnName
   * @param window
   * @param keyColumnExprAndAlias
   * @param timeColumnName
   * @param timeStampFormat
   * @return
   */
  def applyAggregate(df: DataFrame,
                     featureDefAggField: String,
                     outputFeatureColumnName: String,
                     window: String,
                     keyColumnExprAndAlias: Seq[(String, String)],
                     timeColumnName: String,
                     timeStampFormat: String,
                     aggregateFunc: String): DataFrame = {
    val aggregateFunction = AggregationType.withName(aggregateFunc)
    val supportedLevels = Seq(RollUpLevel.OneYear, RollUpLevel.OneMonth,
      RollUpLevel.OneWeek, RollUpLevel.OneDay,
      RollUpLevel.OneHour, RollUpLevel.FiveMin)

    val foundLevels = supportedLevels.filter(_.name.equals(window))
    if (foundLevels.isEmpty) {
      throw new RuntimeException(s"Unsupported window ${window}")
    }
    if (aggregateFunction != AggregationType.BUCKETED_COUNT_DISTINCT) {
      throw new RuntimeException(s"Unsupported aggregation function ${aggregateFunc}")
    }

    val highLevel = foundLevels.head
    val basicLevel = if (highLevel.lowerLevel.isDefined) {
      highLevel.lowerLevel.get
    } else {
      highLevel
    }

    val basicAggFeatureName = "feathr_agg_" + removeNonAlphaNumChars(featureDefAggField) + "_" + basicLevel.name
    val (withBasicLevelAggDf, intermediateBasicCols) = applyAggregationAtBasicLevel(df,
      keyColumnExprAndAlias,
      featureDefAggField,
      basicLevel,
      basicAggFeatureName,
      timeColumnName,
      timeStampFormat,
      aggregateFunction)

    val keyColumns = keyColumnExprAndAlias.map(_._2)
    val (resultDf, intermediateHighLevelCols) = if (highLevel.lowerLevel.isDefined) {
        val result = rollUpAggregateRecursive(withBasicLevelAggDf,
          keyColumns,
          UtcTimestampColumnName,
          utc_ts_string,
          highLevel.lowerLevel.get,
          highLevel,
          roundedBasicBucketTimestampColumn,
          featureValueAtBasicLevelFullBucket,
          basicAggFeatureName,
          featureValueAtBasicLevelFullBucket,
          basicAggFeatureName,
          outputFeatureColumnName,
          aggregateFunction)
      (result.df, result.intermediateOutputCols)
    } else {
      (withBasicLevelAggDf.withColumnRenamed(basicAggFeatureName, outputFeatureColumnName), Seq())
    }
    // val columns = keyColumns ++ Seq(timeColumnName, outputFeatureColumnName)
    val intermediateCols = intermediateBasicCols ++ intermediateHighLevelCols
    resultDf.drop(intermediateCols :_*)
  }

  /**
   * Holds all info needed to roll up aggregations based on the low level result
   * @param df dataframe with low level aggregation result
   * @param roundedHighLevelBucketTimestampColumn rounded timestamp for the high level bucket
   * @param highLevelBucketIntervalInMinutes number of minutes for each high level bucket
   * @param aggedDataForEveryFullBucketAtHighLevel  aggregated result for each full bucket at high level
   * @param aggedDataAtCurrentBucketAtHighLevelSoFar aggregated result for the current bucket at high level
   */
  case class RollUpOutputData(df: DataFrame,
                              roundedHighLevelBucketTimestampColumn: String,
                              //highLevelBucketIntervalInMinutes: Int,
                              aggedDataForEveryFullBucketAtHighLevel: String,
                              aggedDataAtCurrentBucketAtHighLevelSoFar: String,
                              intermediateOutputCols: Seq[String] = Seq.empty[String])

  /**
   * Roll up the aggregated data from basic/low level to higher level, e.g. from 5 minutes to 1 hour, 1 hour to 1 day, etc.
   * This is computed by leveraging a certain amount of buckets of low level data and the current bucket data.
   * e.g. rolling up 5 minutes (low level) aggregated data to one hour (high level) data require 11 previous low level aggregated
   * data and the current low level aggregated data.
   * rolling up 1 hour (low level) aggregated data to one day (high level) data require 23 previous low level aggregated
   * data and the current low level aggregated data.
   * Examples of 5-minutes bucket are:
   * [00:00AM, 00:05AM), [00:05AM, 00:10AM), ..., [00:55AM, 01:00AM), [01:00AM, 01:05AM)...
   * If we are rollup up from the above 5 minutes bucket data to 1 hour data, and we want to get the last hour aggregated
   * data at the observation of 01:03AM, then the previous 11 buckets used are [00:05AM, 00:10AM), ..., [00:55AM, 01:00AM),
   * and the current bucket is [01:00AM, 01:03AM).
   * Note that the current bucket is partial/running, as it ends at the current observation timestamp of 01:03AM,
   * instead of the actual end timestamp of the window, which is 01:05AM. Also note that all the previous bucket
   * are full data, i.e. each of them is 5 minutes in duration.
   * The algorithm is:
   * The aggregated high-level value = aggregateFunc(the running aggregated data within current bucket,
   *                                                aggregateFunc(last X amount of full bucket data)).
   * e.g. in sum example, high-level value = sum(current running sum in the bucket, sum(last X amount of full bucket data)
   * @param df the dataframe with the low-level aggregated data
   * @param keyColumns key columns of the dataframe, used to partition into the window/bucket
   * @param exactTimestampColumnName The exact(non-rounded) timestamp in UTC, used to sort
   * @param lowLevelName level name, e.g. 5min, 1hour
   * @param highLevelName, e.g. 1 hour, 1 day
   *
   * @param roundedLowLevelBucketTimestampColumn the column that contains the rounded timestamp column for the basic/low-level,
   *                                          the timestamp should be rounded to align at the low level bucket, e.g.
   *                                          at 5 minutes, or one hour, or one day. It should never have data point such as 12:11AM,
   *                                          as it is not aligned to any low level bucket.
   *
   * @param itemsForEveryFullBucketAtLowLevel This is for debugging. TODO remove it
   *                                          This is the aggregated data at the low level for each full bucket.
   *                                       e.g. each full 5 minute buckets. Full bucket means that all the raw data rows whose
   *                                        timestamp falls into this timestamp
   *                                       bucket is aggregated into this column already. This mean all the rows will have the same
   *                                       value regardless of their order of appearance in the data.
   *                                      This is similar to the effect of aggregation after a groupBy operation,
   *                                      unlike a running aggregate style data.
   * @param itemsAtCurrentBucketAtLowLevelSoFar This is for debugging. TODO remove it
   *                                            This is the result column of a running aggregation for the current bucket.
   *                                            It ends(exclusive) at the timestamp of the current row.
   *
   * @param aggregateDataAtHighLevel This is the final output column, which is roll up from the input low-level data.
   *                                 It is based on aggregatedDataFromAllPreviousLowLevelBuckets and itemsAtCurrentBucketAtLowLevelSoFar
   */
  def rollUpAggregateRecursive(df: DataFrame,
                               keyColumns: Seq[String],
                               exactTimestampColumnName: String,
                               utc_ts_string: String,
                               // rollupLevels: Map[String, Int],
                               lowLevelName: RollUpLevelVal,
                               highLevelName: RollUpLevelVal,

                               roundedLowLevelBucketTimestampColumn: String,
                               itemsForEveryFullBucketAtLowLevel: String,
                               itemsAtCurrentBucketAtLowLevelSoFar: String,
                               featureValueForEveryFullBucketAtLowLevel: String,
                               featureValueAtCurrentBucketAtLowLevelSoFar: String,
                               // output columns
                               aggregateDataAtHighLevel: String,
                               aggregationFunction: AggregationType.Value
                              ) : RollUpOutputData = {
    /*The number of previous buckets to use, in order to roll up to next level.
                               e.g. rolling up 5 minutes aggregated data to one hour data would require 60/5-1=11 previous buckets,
                               rolling up 1 hour aggregated data to one day would require 23 previous buckets
     */
    val numPreviousBuckets = highLevelName.durationInSecond / lowLevelName.durationInSecond - 1
    // Number of minutes for each basic level bucket, e.g. 5 for 5-minutes level, 60 for the 1 hour level
    val lowLevelBucketIntervalInMinutes: Int = lowLevelName.durationInSecond / 60
    val nameSuffix = "_" + lowLevelName + "_to_" + highLevelName
    val itemsInEveryFullBucketAtLowLevelOneHot = "itemsInEveryFullBucketAtLowLevelOneHot" + nameSuffix
    val previousRoundedBasicBucketInMin = "previousRoundedBasicBucketInMin" + nameSuffix
    val aggregatedDataFromAllPreviousLowLevelBuckets = "AllPreviousBucketItems" + nameSuffix
    val windowSpecToUpdateFull5MinutesCounts = Window.partitionBy(keyColumns.head, keyColumns.tail: _*)
      .orderBy(col(exactTimestampColumnName))
    val withPreviousAggedDataDf = df
      .withColumn(previousRoundedBasicBucketInMin, lag(col(roundedLowLevelBucketTimestampColumn), 1).over(windowSpecToUpdateFull5MinutesCounts))
      /* itemsInEveryFullBucketAtLowLevelOneHot
     One hot means that the aggregated data for the full bucket is modified in a way so that
     only the first row of each bucket has the actual aggregated value, the rest rows should be null/empty.
     This is because we will later on need to sum/aggregate the all these values to compute the high-level data.
     If the values are not in one-hot format, the data in the same low-level bucket will be aggregated multiple
     times toward the high-level data, which is incorrect.
     TODO Is it possible to use 'first' aggregation so we don't need this one-hot format?
    */
      .withColumn(
        itemsInEveryFullBucketAtLowLevelOneHot,
        if (debug) {
          when(
            // isTheFirstRecordInRounded5Minutes
            isnull(col(previousRoundedBasicBucketInMin)) || col(roundedLowLevelBucketTimestampColumn) =!= col(previousRoundedBasicBucketInMin),
            col(itemsForEveryFullBucketAtLowLevel)
          ).otherwise(expr("array()"))
        } else {
          when(
            // isTheFirstRecordInRounded5Minutes
            isnull(col(previousRoundedBasicBucketInMin)) || col(roundedLowLevelBucketTimestampColumn) =!= col(previousRoundedBasicBucketInMin),
            coalesce(col(featureValueForEveryFullBucketAtLowLevel), lit(0))
          ).otherwise(lit(0))
        }
      )
    val windowSpec1Hour = Window.partitionBy(keyColumns.head, keyColumns.tail: _*)
      // TODO should sort by utcTimestampColumnName?
      .orderBy(col(roundedLowLevelBucketTimestampColumn))
      .rangeBetween(
        // TODO check off by one
        Window.currentRow - minutes(lowLevelBucketIntervalInMinutes) * numPreviousBuckets,
        -1
      )
    /*
    aggregatedDataFromAllPreviousLowLevelBuckets contains aggregated data from all
    previous buckets used toward computing the current value at high level.
    It is based on itemsInEveryFullBucketAtLowLevelOneHot, e.g.
    apply sum on itemsInEveryFullBucketAtLowLevelOneHot.
    */
    val withAllPreviousLowLevelBucketsDf = withPreviousAggedDataDf.withColumn(aggregatedDataFromAllPreviousLowLevelBuckets,
      if (debug) {
        collect_list(itemsInEveryFullBucketAtLowLevelOneHot).over(windowSpec1Hour)
      } else {
        coalesce(sum(itemsInEveryFullBucketAtLowLevelOneHot).over(windowSpec1Hour), lit(0))
      }
    )
    // TODO fix the datatype and remove try
    val withOutputFeatureDf =
      if (debug) {
        try (
          withAllPreviousLowLevelBucketsDf
            .withColumn(aggregateDataAtHighLevel,
              array(array(col(itemsAtCurrentBucketAtLowLevelSoFar)), col(aggregatedDataFromAllPreviousLowLevelBuckets)))
          ) catch {
          case e =>
            try (
              withAllPreviousLowLevelBucketsDf
                .withColumn(aggregateDataAtHighLevel,
                  array(array(array(array(col(itemsAtCurrentBucketAtLowLevelSoFar)))), (col(aggregatedDataFromAllPreviousLowLevelBuckets))))
              ) catch {
              case e =>
                withAllPreviousLowLevelBucketsDf
                  .withColumn(aggregateDataAtHighLevel,
                    array(array(array(array(array(col(itemsAtCurrentBucketAtLowLevelSoFar))))), (col(aggregatedDataFromAllPreviousLowLevelBuckets))))
            }
        }
      } else {
        withAllPreviousLowLevelBucketsDf
          .withColumn(aggregateDataAtHighLevel,
            col(featureValueAtCurrentBucketAtLowLevelSoFar) + col(aggregatedDataFromAllPreviousLowLevelBuckets))
      }

    // Create these output columns for next level roll up
    val itemsForEveryFullBucketAtHighLevel = "itemsForEveryFullBucketAtHighLevel_" + highLevelName
    val itemsAtCurrentBucketAtHighSoFar = "itemsAtCurrentBucketAtHighSoFar_" + highLevelName
    val roundedHighLevelBucketTimestampColumn: String = "roundedHighLevelBucketTimestampColumn_" + highLevelName

    val timeColumnName = "roundedTo" + highLevelName
    val timeStampFormat = highLevelName.timeStampFormat
    val withRoundedHighLevelBucketTimestampColumn = withOutputFeatureDf
      .withColumn(timeColumnName, date_format(col(utc_ts_string), timeStampFormat))
      .withColumn(roundedHighLevelBucketTimestampColumn,
        expr(constructTimeStampExpr(timeColumnName, timeStampFormat, Some(dataTimezoneName)))
      )
    val windowSpecHighLevel = Window.partitionBy(roundedHighLevelBucketTimestampColumn, keyColumns: _*)
      .orderBy(col(roundedHighLevelBucketTimestampColumn))
      .rangeBetween(
        Window.currentRow,
        Window.currentRow
      )

    val withHighLevelOutputColumnDf = withRoundedHighLevelBucketTimestampColumn.withColumn(itemsForEveryFullBucketAtHighLevel,
      if(debug) {
        collect_set(itemsInEveryFullBucketAtLowLevelOneHot).over(windowSpecHighLevel)
      } else {
        sum(itemsInEveryFullBucketAtLowLevelOneHot).over(windowSpecHighLevel)
      }
    )

    val highLevelBucketIntervalInMinutes: Int = highLevelName.durationInSecond / 60
    val windowSpecAtHighLevelSoFar = Window.partitionBy(roundedHighLevelBucketTimestampColumn, keyColumns: _*)
      .orderBy(col(roundedLowLevelBucketTimestampColumn))
      .rangeBetween(
        Window.unboundedPreceding,
        Window.currentRow - 1
      )
    // e.g. if 5m -> 1h, this means all the full 5m within the current hour, it might be 0 - 11.
    val aggregatedDataFromAllPreviousLowLevelFullBuckets = "aggregatedDataFromAllPreviousLowLevelFullBuckets" + nameSuffix
    val withFullBucketLowLevelDf = withHighLevelOutputColumnDf.withColumn(aggregatedDataFromAllPreviousLowLevelFullBuckets,
      if (debug) {
        collect_set(itemsInEveryFullBucketAtLowLevelOneHot).over(windowSpecAtHighLevelSoFar)
      } else {
        coalesce(sum(itemsInEveryFullBucketAtLowLevelOneHot).over(windowSpecAtHighLevelSoFar), lit(0))
      }
    )
    val withCurrentHighLevelBucketDf = if (debug) {
      withFullBucketLowLevelDf.withColumn(itemsAtCurrentBucketAtHighSoFar,
        concat(col(aggregatedDataFromAllPreviousLowLevelFullBuckets).cast("string"),
          col(itemsAtCurrentBucketAtLowLevelSoFar).cast("string"))
      )
    } else {
      withFullBucketLowLevelDf.withColumn(itemsAtCurrentBucketAtHighSoFar,
        col(aggregatedDataFromAllPreviousLowLevelFullBuckets) +
          col(featureValueAtCurrentBucketAtLowLevelSoFar)
      )
    }

    val intermediateOutputCols = Seq(previousRoundedBasicBucketInMin, itemsInEveryFullBucketAtLowLevelOneHot,
      aggregatedDataFromAllPreviousLowLevelBuckets, timeColumnName, roundedHighLevelBucketTimestampColumn,
      itemsForEveryFullBucketAtHighLevel, aggregatedDataFromAllPreviousLowLevelFullBuckets, itemsAtCurrentBucketAtHighSoFar)
    RollUpOutputData(withCurrentHighLevelBucketDf,
      roundedHighLevelBucketTimestampColumn,
      itemsForEveryFullBucketAtHighLevel,
      itemsAtCurrentBucketAtHighSoFar,
      intermediateOutputCols)
  }
}

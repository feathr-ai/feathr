package com.linkedin.feathr.offline.source.pathutil

import com.linkedin.feathr.common.DateTimeResolution
import com.linkedin.feathr.common.DateTimeResolution.DateTimeResolution
import com.linkedin.feathr.offline.util.datetime.OfflineDateTimeUtils

/**
 * Analyze how a given path is partitioned: daily, hourly or date partitioned.
 * @param pathChecker the path checker is used to check whether a file path exists.
 */
private[offline] class TimeBasedHdfsPathAnalyzer(pathChecker: PathChecker) {

  /**
   * check whether the given path is daily or hourly partitioned.
   *
   * If the path has daily/hourly suffix, then it's daily/hourly partitioned,
   * Otherwise, check whether daily/hourly subfolder exists.
   * If the daily/hourly subfolder doesn't exist, assume it's date partitioned.
   *
   * @param filePath the input file path
   * @return a PathInfo object to show how the data source is partitioned.
   */
  def analyze(filePath: String): PathInfo = {
    val dailyFolder = "daily/"
    val hourlyFolder = "hourly/"
    val dailyPattern = "yyyy/MM/dd"
    val hourlyPattern = "yyyy/MM/dd/HH"
    val fileFolder = if (filePath.endsWith("/")) filePath else filePath + "/"
    if (fileFolder.endsWith(dailyFolder)) {
      PathInfo(fileFolder, DateTimeResolution.DAILY, dailyPattern)
    } else if (fileFolder.endsWith(hourlyFolder)) {
      PathInfo(fileFolder, DateTimeResolution.HOURLY, hourlyPattern)
    } else if (pathChecker.exists(fileFolder + dailyFolder)) {
      PathInfo(fileFolder + dailyFolder, DateTimeResolution.DAILY, dailyPattern)
    } else if (pathChecker.exists(fileFolder + hourlyFolder)) {
      PathInfo(fileFolder + hourlyFolder, DateTimeResolution.HOURLY, hourlyPattern)
    } else {
      // Daily data can be Orc/Hive data following in HomeDir/datepartition=yyyy-MM-dd-00
      PathInfo(fileFolder + "datepartition=", DateTimeResolution.DAILY, "yyyy-MM-dd-00")
    }
  }

  /**
   * get analyzed PathInfo from the given path and the timePathPattern
   * @param filePath the input file path
   * @param timePartitionPattern the time pattern format string, such as yyyy-MM-dd
   * @return a PathInfo object to show how the data source is partitioned
   */
  def analyze(filePath: String, timePartitionPattern: String): PathInfo = {
    val basePath = if (filePath.endsWith("/") || filePath.endsWith("=")) filePath else filePath + "/"
    val dateTimeResolution = OfflineDateTimeUtils.getDateTimeResolutionFromPattern(timePartitionPattern)
    PathInfo(basePath, dateTimeResolution, timePartitionPattern)
  }
}

/**
 * Information about how the path is partitioned
 * @param basePath the base path without date
 * @param dateTimeResolution whether it's partitioned by hour or day.
 * @param datePathPattern the format pattern of the date path, such as yyyy/MM/dd
 */
private[offline] case class PathInfo(basePath: String, dateTimeResolution: DateTimeResolution, datePathPattern: String)

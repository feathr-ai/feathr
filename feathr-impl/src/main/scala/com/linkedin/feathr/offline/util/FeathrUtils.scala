package com.linkedin.feathr.offline.util

import java.util.Properties

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

private[offline] object FeathrUtils {

  val ENVIRONMENT = "offline"
  val ENABLE_DEBUG_OUTPUT = "debug.enabled"
  val DEBUG_OUTPUT_PATH = "debug.output.path"
  val DEBUG_OUTPUT_PART_NUM = "debug.output.num.parts"
  val FEATHR_PARAMS_PREFIX = "spark.feathr."
  /*
   * The execution config controls feathr-offline behavior when loading date partitioned feature data.
   * If set to true, feature join / feature generation workflows fail if a date partition is missing in feature data source.
   * Else, the missing partition is ignored.
   * Default value: false.
   */
  val FAIL_ON_MISSING_PARTITION = "fail.on.missing.partition"
  /*
   * When a base feature in Sequential Join has ArrayType keys, the DataFrame join condition is constructed as array_contains.
   * In Spark 2.3, this results in BroadcastNestedLoopJoin which adds memory pressure on the driver. (See: APA-21936).
   * This is a user hidden / dev only config when enabled, explodes ArrayType join keys to avoid BroadcastNestedLoopJoin.
   */
  val SEQ_JOIN_ARRAY_EXPLODE_ENABLED = "seq.join.array.explode.enabled"
  val ENABLE_SALTED_JOIN = "enable.salted.join"
  val SALTED_JOIN_FREQ_ITEM_THRESHOLD = "salted.join.freq.item.threshold"
  val SALTED_JOIN_FREQ_ITEM_ESTIMATOR = "salted.join.freq.item.estimator"
  val SALTED_JOIN_PERSIST = "salted.join.persist"
  val SALTED_JOIN_REPLICATION_FACTOR_HIGH = "salted.join.replication.factor.high"
  val ENABLE_SLICK_JOIN = "enable.slickJoin"
  val ENABLE_METRICS = "enable.metrics"
  val ENABLE_CHECKPOINT = "enable.checkpoint"
  val ROW_BLOOMFILTER_MAX_THRESHOLD = "row.bloomfilter.maxThreshold"
  val SPARK_JOIN_MAX_PARALLELISM = "max.parallelism"
  val CHECKPOINT_OUTPUT_PATH = "checkpoint.dir"
  val SPARK_JOIN_MIN_PARALLELISM = "min.parallelism"

  val defaultParams: Map[String, String] = Map(
    ENABLE_DEBUG_OUTPUT -> "false",
    DEBUG_OUTPUT_PATH -> "/tmp/debug/feathr/output",
    CHECKPOINT_OUTPUT_PATH -> "/tmp/feathr/checkpoints",
    ENABLE_CHECKPOINT -> "false",
    DEBUG_OUTPUT_PART_NUM -> "200",
    FAIL_ON_MISSING_PARTITION -> "false",
    SEQ_JOIN_ARRAY_EXPLODE_ENABLED -> "true",
    ENABLE_SALTED_JOIN -> "false",
    // If one key appears more than 0.02% in the dataset, we will salt this join key and split them into multiple partitions
    // This is an empirical value
    SALTED_JOIN_FREQ_ITEM_THRESHOLD -> "0.0002",
    SALTED_JOIN_REPLICATION_FACTOR_HIGH -> "10",
    SALTED_JOIN_FREQ_ITEM_ESTIMATOR -> "spark",

    ENABLE_SLICK_JOIN -> "false",
    SALTED_JOIN_PERSIST -> "true",
    ROW_BLOOMFILTER_MAX_THRESHOLD -> "-1",
    // We found that if we have too many parallelism, then during the join shuffling, memoryOverhead could be too high,

    ENABLE_METRICS -> "false",
    // cap it to 10000 to make sure memoryOverhead is less than 2g (Feathr default value)
    SPARK_JOIN_MAX_PARALLELISM -> "10000",
    SPARK_JOIN_MIN_PARALLELISM -> "10")

  /**
   * Get Feathr Offline version string from .properties file that gets created at build time
   * @return Feathr Offline version string
   */
  def feathrVersion: String = {
    val versionProperties = new Properties()
    val resourceStream = getClass.getClassLoader.getResourceAsStream("metric.properties")
    versionProperties.load(resourceStream)
    versionProperties.getProperty("version")
  }

  /**
   * Get Feathr job parameter values
   * @param ss spark session
   * @param paramName parameter name
   */
  def getFeathrJobParam(ss: SparkSession, paramName: String): String = {
    val sparkConf = ss.sparkContext.getConf
    getFeathrJobParam(sparkConf, paramName)
  }

  /**
   * Get Feathr job parameter values
   * @param sparkConf spark conf
   * @param paramName parameter name
   */
  def getFeathrJobParam(sparkConf: SparkConf, paramName: String): String = {
    sparkConf.get(s"${FEATHR_PARAMS_PREFIX}${paramName}", defaultParams(paramName))
  }

  /**
   * Check and enable debug logging if it is configured by the end user
   * @param sparkConf spark conf
   */
  def enableDebugLogging(sparkConf: SparkConf): Unit = {
    val enableDebugLog = isDebugOutputEnabled(sparkConf)
    if (enableDebugLog) {
      Logger.getRootLogger.setLevel(Level.DEBUG)
    }
  }

  /**
   * Helper method to parse a hdfs file path
   *
   * @param path File path string
   * @return contents of the file as a string
   */
  def readFileFromHDFS(ss: SparkSession, path: String): String =
    ss.sparkContext.textFile(path).collect.mkString("\n")

  /**
   * Check if debug output is configured by the end user
   * @param sparkConf spark conf
   */
  private def isDebugOutputEnabled(sparkConf: SparkConf): Boolean = {
    FeathrUtils.getFeathrJobParam(sparkConf, FeathrUtils.ENABLE_DEBUG_OUTPUT).toBoolean
  }

}

package com.linkedin.feathr.offline.config.location

import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import net.minidev.json.annotate.JsonIgnore
import org.apache.spark.sql.{DataFrame, SparkSession}

@CaseClassDeserialize()
case class GenericLocation(format: String, @JsonIgnore options: collection.mutable.Map[String, String] = collection.mutable.Map[String, String]()) extends DataLocation {
  /**
   * Backward Compatibility
   * Many existing codes expect a simple path
   *
   * @return the `path` or `url` of the data source
   *
   *         WARN: This method is deprecated, you must use match/case on DataLocation,
   *         and get `path` from `SimplePath` only
   */
  override def getPath: String = s"GenericLocation(${format})"

  /**
   * Backward Compatibility
   *
   * @return the `path` or `url` of the data source, wrapped in an List
   *
   *         WARN: This method is deprecated, you must use match/case on DataLocation,
   *         and get `paths` from `PathList` only
   */
  override def getPathList: List[String] = List(getPath)

  /**
   * Load DataFrame from Spark session
   *
   * @param ss SparkSession
   * @return
   */
  override def loadDf(ss: SparkSession, dataIOParameters: Map[String, String]): DataFrame = {
    ss.read.format(format)
      .options(getOptions)
      .load()
  }

  /**
   * Write DataFrame to the location
   *
   * @param ss SparkSession
   * @param df DataFrame to write
   */
  override def writeDf(ss: SparkSession, df: DataFrame): Unit = {
    df.write.format(format)
      .options(getOptions)
      .save()
  }

  /**
   * Tell if this location is file based
   *
   * @return boolean
   */
  override def isFileBasedLocation(): Boolean = false

  def getOptions(): Map[String, String] = {
    options.map(e => e._1 -> LocationUtils.envSubstitute(e._2)).toMap
  }

  @JsonAnySetter
  def setOption(key: String, value: Any) = {
    options += (key -> value.toString)
  }
}

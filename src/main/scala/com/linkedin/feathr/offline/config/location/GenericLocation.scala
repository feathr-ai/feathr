package com.linkedin.feathr.offline.config.location

import com.fasterxml.jackson.annotation.JsonAnySetter
import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.common.exception.FeathrException
import net.minidev.json.annotate.JsonIgnore
import org.apache.spark.sql.{DataFrame, SparkSession}

@CaseClassDeserialize()
case class GenericLocation(format: String,
                           mode: Option[String] = None,
                           @JsonIgnore options: collection.mutable.Map[String, String] = collection.mutable.Map[String, String](),
                           @JsonIgnore conf: collection.mutable.Map[String, String] = collection.mutable.Map[String, String]()
                          ) extends DataLocation {
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
    conf.foreach(e => {
      ss.conf.set(e._1, e._2)
    })
    ss.read.format(format)
      .options(options)
      .load()
  }

  /**
   * Write DataFrame to the location
   *
   * @param ss SparkSession
   * @param df DataFrame to write
   */
  override def writeDf(ss: SparkSession, df: DataFrame): Unit = {
    conf.foreach(e => {
      ss.conf.set(e._1, e._2)
    })
    val keyDf = if (!df.columns.contains("id")) {
      if(df.columns.contains("key0")) {
        df.withColumnRenamed("key0", "id")
      } else {
        throw new FeathrException("DataFrame doesn't have id column")
      }
    } else {
      df
    }
    val w = mode match {
      case Some(m) => {
        keyDf.write.format(format)
          .options(options)
          .mode(m)
      }
      case None => {
        keyDf.write.format(format)
          .options(options)
      }
    }
    w.save()
  }

  /**
   * Tell if this location is file based
   *
   * @return boolean
   */
  override def isFileBasedLocation(): Boolean = false

  @JsonAnySetter
  def setOption(key: String, value: Any) = {
    if (key.startsWith("__conf__")) {
      conf += (key.stripPrefix("__conf__").replace("__", ".") -> LocationUtils.envSubstitute(value.toString))
    } else {
      options += (key.replace("__", ".") -> LocationUtils.envSubstitute(value.toString))
    }
  }
}

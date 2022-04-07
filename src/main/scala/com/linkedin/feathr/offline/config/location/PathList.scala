package com.linkedin.feathr.offline.config.location

import com.linkedin.feathr.offline.generation.SparkIOUtils
import org.apache.spark.sql.{DataFrame, SparkSession}

case class PathList(paths: List[String]) extends InputLocation {
  override def getPath: String = paths.mkString(";")

  override def getPathList: List[String] = paths

  override def loadDf(ss: SparkSession, dataIOParameters: Map[String, String] = Map()): DataFrame = {
    SparkIOUtils.createUnionDataFrame(getPathList, dataIOParameters)
  }
}

object PathList {
  def apply(path: String): PathList = PathList(path.split(";").toList)
  def unapply(pathList: PathList): Option[List[String]] = Some(pathList.paths)
}

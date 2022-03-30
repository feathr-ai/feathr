package com.linkedin.feathr.offline.config.location

import org.apache.spark.sql.{DataFrame, SparkSession}

case class PathList(paths: Array[String]) extends InputLocation {
  override def getPath: String = paths.mkString(";")

  override def getPathList: Array[String] = paths

  override def loadDf(ss: SparkSession): DataFrame = ???
}

object PathList {
  def apply(path: String): PathList = PathList(path.split(";"))
  def unapply(o: PathList): Option[Array[String]] = Some(o.paths)
}

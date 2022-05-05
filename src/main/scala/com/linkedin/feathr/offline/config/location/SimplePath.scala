package com.linkedin.feathr.offline.config.location

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.offline.generation.SparkIOUtils
import com.linkedin.feathr.offline.source.dataloader.BatchDataLoader
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.codehaus.jackson.annotate.JsonProperty

@CaseClassDeserialize()
case class SimplePath(@JsonProperty("path") path: String) extends InputLocation {
  override def loadDf(ss: SparkSession, dataIOParameters: Map[String, String] = Map()): DataFrame = {
    SparkIOUtils.createUnionDataFrame(getPathList, dataIOParameters)
  }

  override def getPath: String = path

  override def getPathList: List[String] = List(path)
}

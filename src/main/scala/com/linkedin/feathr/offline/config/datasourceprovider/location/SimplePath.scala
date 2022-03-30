package com.linkedin.feathr.offline.config.datasourceprovider.location

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.offline.source.dataloader.SparkDataLoader
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.codehaus.jackson.annotate.JsonProperty

@CaseClassDeserialize()
case class SimplePath(@JsonProperty("path") path: String = "") extends InputLocation {
  override def loadDf(ss: SparkSession): DataFrame = {
    val loader = new SparkDataLoader(ss, this)
    loader.loadDataFrame()
  }

  override def getPath: String = path

  override def getPathList: Array[String] = Array(path)
}

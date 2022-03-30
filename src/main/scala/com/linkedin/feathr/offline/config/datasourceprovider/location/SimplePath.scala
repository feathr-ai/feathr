package com.linkedin.feathr.offline.config.datasourceprovider.location

import com.fasterxml.jackson.module.caseclass.annotation.CaseClassDeserialize
import com.linkedin.feathr.offline.source.dataloader.SparkDataLoader
import org.apache.spark.sql.{DataFrame, SparkSession}

@CaseClassDeserialize()
case class Path(path: String = "") extends InputLocation {
  override def loadDf(ss: SparkSession): DataFrame = {
    val loader = new SparkDataLoader(ss, path)
    loader.loadDataFrame()
  }
}

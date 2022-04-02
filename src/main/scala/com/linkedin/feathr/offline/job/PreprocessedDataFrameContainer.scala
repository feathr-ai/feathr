package com.linkedin.feathr.offline.job

import org.apache.spark.sql.DataFrame

object PreprocessedDataFrameContainer {
  var preprocessedDfMap: Map[String, DataFrame] = Map()
  var globalFuncMap: Map[String, String] = Map()
}

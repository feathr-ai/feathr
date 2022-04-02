package com.linkedin.feathr.offline.job

import org.apache.spark.sql.DataFrame

/**
 * Object to keep the preprocessed DataFrames by Pyspark.
 */
object PreprocessedDataFrameContainer {
  var preprocessedDfMap: Map[String, DataFrame] = Map()
}

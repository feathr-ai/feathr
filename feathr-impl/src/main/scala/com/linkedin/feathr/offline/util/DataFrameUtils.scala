package com.linkedin.feathr.offline.util

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col

private[offline] object DataFrameUtils {
  def filterNulls(dataframe: DataFrame, keyColumnNames: Seq[String]): DataFrame = {
    val filterCondition = keyColumnNames.map(col(_).isNull).reduce(_ && _)
    dataframe.filter(!filterCondition)
  }

  def filterNonNulls(dataframe: DataFrame, keyColumnNames: Seq[String]): DataFrame = {
    val filterCondition = keyColumnNames.map(col(_).isNull).reduce(_ && _)
    dataframe.filter(filterCondition)
  }

}

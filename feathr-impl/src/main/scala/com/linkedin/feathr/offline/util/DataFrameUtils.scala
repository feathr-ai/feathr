package com.linkedin.feathr.offline.util

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, expr}

/**
 * Utility methods to perform specific operations on Dataframes.
 */
private[offline] object DataFrameUtils {

  /**
   * Filters the rows where keys are nulls
   * @param dataframe
   * @param keyColumnNames
   * @return
   */
  def filterNulls(dataframe: DataFrame, keyColumnNames: Seq[String]): DataFrame = {
    val filterCondition = keyColumnNames.map(expr(_).isNull).reduce(_ && _)
    dataframe.filter(!filterCondition)
  }

  /**
   * Filters the rows where keys are not nulls
   *
   * @param dataframe
   * @param keyColumnNames
   * @return
   */
  def filterNonNulls(dataframe: DataFrame, keyColumnNames: Seq[String]): DataFrame = {
    val filterCondition = keyColumnNames.map(expr(_).isNull).reduce(_ && _)
    dataframe.filter(filterCondition)
  }

}

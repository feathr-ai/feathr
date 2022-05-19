package com.linkedin.feathr.common

import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema

/**
 * An extractor trait that provides APIs to transform a Spark GenericRowWithSchema into feature values
 */
trait SparkRowExtractor {

  /**
   * Get key from input row
   * @param datum input row
   * @return list of feature keys
   */
  def getKeyFromRow(datum: GenericRowWithSchema): Seq[String]

  /**
   * Get the feature value from the row
   * @param datum input row
   * @return A map of feature name to feature value
   */
  def getFeaturesFromRow(datum: GenericRowWithSchema): Map[String, FeatureValue]
}
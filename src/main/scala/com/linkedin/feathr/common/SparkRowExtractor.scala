package com.linkedin.feathr.common

import org.apache.avro.generic.IndexedRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

/**
 * An extractor trait that provides APIs to transform a Spark GenericRowWithSchema into feature values
 */
trait SparkRowExtractor {

  /**
   * Get key from input row
   * @param datum input row
   * @return list of feature keys
   */
  def getKeyFromRow(datum: Any): Seq[String]

  /**
   * Get the feature value from the row
   * @param datum input row
   * @return A map of feature name to feature value
   */
  def getFeaturesFromRow(datum: Any): Map[String, FeatureValue]

  /**
   * Whether the Row extractor needs a one-time batch preprocessing before calling
   * getKeyFromRow() and getFeaturesFromRow().
   * This is especially useful when users need to convert the source DataFrame
   * into specific datatype, e.g. Avro GenericRecord or SpecificRecord.
   */
  def hasBatchPreProcessing() = false

  /**
   * One time batch preprocess the input data source into a RDD[_] for feature extraction later
   * @param df input data source
   * @return batch preprocessed dataframe, as RDD[IndexedRecord]
   */
  def batchPreProcess(df: DataFrame) : RDD[IndexedRecord] = throw new NotImplementedError("Batch preprocess is not implemented")
}
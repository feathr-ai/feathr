package com.linkedin.feathr.offline.transformation

import com.linkedin.feathr.common.FeatureTypes
import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrException}
import org.apache.spark.util.AccumulatorV2

/**
 * Evaluator that transforms features using MVEL based on DataFrame
 * It evaluates the extractor class against the input data to get the feature value
 * This is used in sequential join's MVEL based expand feature evaluation, and anchored passthrough feature evaluation.
 * It is essentially a batch version of getFeatures() for dataframe
 */
private[offline] object DataFrameBasedMVELEvaluator {

  // offline/anchored/anchorExtractor/SimpleConfigurableAnchorExtractor.scala here
}

// Used to 'accumulate' the feature type for a feature while transforming the feature for each row in the dataset
private[offline] class FeatureTypeAccumulator(var featureType: FeatureTypes) extends AccumulatorV2[FeatureTypes, FeatureTypes] {

  def reset(): Unit = {
    featureType = FeatureTypes.UNSPECIFIED
  }

  def add(input: FeatureTypes): Unit = {
    featureType = updateFeatureType(featureType, input)
  }

  def value: FeatureTypes = {
    featureType
  }

  def isZero: Boolean = {
    featureType == FeatureTypes.UNSPECIFIED
  }

  def copy(): FeatureTypeAccumulator = {
    new FeatureTypeAccumulator(featureType)
  }

  def merge(other: AccumulatorV2[FeatureTypes, FeatureTypes]): Unit = {
    featureType = updateFeatureType(featureType, other.value)
  }

  /**
   * Update existing feature type (inferred by previous rows) with the new feature type inferred from a new row
   * Rules:
   * type + type => type
   * Unspecified + type => type
   * CATEGORICAL + CATEGORICAL_SET => CATEGORICAL_SET
   * DENSE_VECTOR + VECTOR => DENSE_VECTOR
   *
   * @param existingType existing feature type
   * @param currentType new feature type
   * @return new feature type
   */
  private def updateFeatureType(existingType: FeatureTypes, currentType: FeatureTypes): FeatureTypes = {
    if (existingType != currentType) {
      (existingType, currentType) match {
        case (eType, FeatureTypes.UNSPECIFIED) => eType
        case (FeatureTypes.UNSPECIFIED, tType) => tType
        case (eType, tType) if (Set(eType, tType).subsetOf(Set(FeatureTypes.CATEGORICAL_SET, FeatureTypes.CATEGORICAL))) =>
          FeatureTypes.CATEGORICAL_SET
        case (eType, tType) if (Set(eType, tType).subsetOf(Set(FeatureTypes.DENSE_VECTOR))) =>
          FeatureTypes.DENSE_VECTOR
        case (eType, tType) =>
          throw new FeathrException(ErrorLabel.FEATHR_USER_ERROR, s"A feature should have only one feature type, but found ${eType} and ${tType}")
      }
    } else currentType
  }
}

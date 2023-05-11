package com.linkedin.feathr.offline.mvel.plugins

/**
 * Trait that wraps a Frame or Feathr FeatureValue
 * @tparam T FeatureValue type to be wrapped
 */
trait FeatureValueWrapper[T] {
  // Get the wrapped feature value
  def getFeatureValue(): T
}
package com.linkedin.feathr.offline.config

import com.linkedin.feathr.common.{FeatureValue, PegasusDefaultFeatureValueResolver}
import com.linkedin.feathr.compute.FeatureVersion
import com.linkedin.feathr.core.utils.MlFeatureVersionUrnCreator

private[offline] class PegasusRecordDefaultValueConverter private (
  pegasusDefaultFeatureValueResolver: PegasusDefaultFeatureValueResolver,
  mlFeatureVersionUrnCreator: MlFeatureVersionUrnCreator) {

  private val _pegasusDefaultFeatureValueResolver = pegasusDefaultFeatureValueResolver
  private val _mlFeatureVersionUrnCreator = mlFeatureVersionUrnCreator

  /**
   * Convert Frame-Core FeatureTypeConfig to Offline [[FeatureTypeConfig]]
   */
  def convert(features: Map[String, FeatureVersion]): Map[String, FeatureValue] = {
    features
      .transform((k, v) => _pegasusDefaultFeatureValueResolver.resolveDefaultValue(_mlFeatureVersionUrnCreator.create(k), v))
      .filter(_._2.isPresent)
      .mapValues(_.get)
      // get rid of not serializable exception:
      // https://stackoverflow.com/questions/32900862/map-can-not-be-serializable-in-scala/32945184
      .map(identity)
  }
}

private[offline] object PegasusRecordDefaultValueConverter {
  def apply(): PegasusRecordDefaultValueConverter = {
    new PegasusRecordDefaultValueConverter(PegasusDefaultFeatureValueResolver.getInstance, MlFeatureVersionUrnCreator.getInstance)
  }

  def apply(
    pegasusDefaultFeatureValueResolver: PegasusDefaultFeatureValueResolver,
    mlFeatureVersionUrnCreator: MlFeatureVersionUrnCreator): PegasusRecordDefaultValueConverter = {
    new PegasusRecordDefaultValueConverter(pegasusDefaultFeatureValueResolver, mlFeatureVersionUrnCreator)
  }
}

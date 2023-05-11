package com.linkedin.feathr.offline.mvel

import com.linkedin.feathr.common.FeatureValue
import com.linkedin.feathr.offline.mvel.plugins.FeatureValueWrapper
import com.linkedin.feathr.offline.plugins.AlienFeatureValue

class FeathrFeatureValueAsAlien(feathrFeatureValue: FeatureValue) extends AlienFeatureValue with FeatureValueWrapper[FeatureValue] {
  override def getFeatureValue(): FeatureValue = feathrFeatureValue
}

package com.feathr.online;

import java.util.Map;
import java.util.Set;


/**
 * Transforms your raw source data fetched from database etc into features.
 * For example, the raw data contains user age in string form. You can convert it into integer format in this class.
 *
 * This function will be called by Feathr-online engine and applied to raw source data fetched from database etc.
 */
public interface FeatureTransformationInterface {
  Map<String, Object> batchTransform(Set<String> featureNames, Object sourceData);

  Object transform(String featureName, Object sourceData);
}

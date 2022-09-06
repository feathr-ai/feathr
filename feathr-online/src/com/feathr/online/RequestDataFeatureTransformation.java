package com.feathr.online;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * A sample feature transformation implementation for request data source.
 *
 */
public class RequestDataFeatureTransformation implements FeatureTransformationInterface {
  /**
   * Use this when you can produce multiple features from one source data
   */
  @Override
  public Map<String, Object> batchTransform(Set<String> featureNames, Object sourceData) {
    Map<String, Object> features = new HashMap<>();
    for (String featureName : featureNames) {
      Object feature = transform(featureName, sourceData);
      features.put(featureName, feature);
    }
    return features;
  }


  @Override
  public Object transform(String featureName, Object sourceData) {
    // data cleaning, null or empty data handling etc

    MyApplicationRequestData requestData = (MyApplicationRequestData) sourceData;
    requestData.getRawDataA();
    requestData.getRawDataA();
    if (featureName.equals("f3")) {
      return requestData.getRawDataA();
    }
    if (featureName.equals("f4")) {
      return requestData.getRawDataB();
    }
    return null;
  }
}

package com.feathr.online;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * A sample feature transformation implementation.
 * Our database stores the job salary median in string form with value and currency sign. For example, 1000 and $,
 * and 5000 and euro. We need to convert them into same value for our AI model. For example, we all convert them into
 * US dollar amount.
 *
 */
public class JobSalaryFeatureTransformation implements FeatureTransformationInterface {
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

    SampleSourceDataModel sampleSourceData = (SampleSourceDataModel) sourceData;
    double result = sampleSourceData.getValue();
    if (sampleSourceData.getCurrency().equals("euro")) {
      result = sampleSourceData.getValue() / 0.98; // euro to USD conversion rate is 0.98
    }
    return result;
  }
}

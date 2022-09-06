package com.feathr.online;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class FeatureExecutionPlan {
  public String sourceName;
  public String featureName;
  public String featureTransformationClass;
  public Set<String> featureNames;

  public FeatureExecutionPlan(String featureName, String sourceName, String featureTransformationClass) {
    this.featureName = featureName;
    this.sourceName = sourceName;
    this.featureTransformationClass = featureTransformationClass;
    this.featureNames = new HashSet<>(Arrays.asList(featureName));
  }
}

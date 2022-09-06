package com.feathr.online;

import java.util.HashSet;
import java.util.Set;


/**
 * Build {@link FeatureExecutionPlan}. This is a mock implementation.
 *
 * It will support two modes:
 * 1. read from feature registry to create a feature execution plan.
 * 2. read from local feature definition artifact to create a feature execution plan.
 *
 * In real implementation, this will be executed once and results will be cached for better performance.
 */
public class FeatureExecutionPlanBuilder {

  public Set<FeatureExecutionPlan> buildPlans(Set<String> featureNames) {
    Set<FeatureExecutionPlan> plans = new HashSet<>();

    // TODO: for now, just hard-code feature names with f1 and f2. Later, we will actually use featureNames.
    FeatureExecutionPlan featureExecutionPlan1 = new FeatureExecutionPlan("f1", "redis", "");
    FeatureExecutionPlan featureExecutionPlan2 = new FeatureExecutionPlan("f2", "aerospike", "com.feathr.online.JobSalaryFeatureTransformation");
    FeatureExecutionPlan featureExecutionPlan3 = new FeatureExecutionPlan("f3", "requestSource", "com.feathr.online.RequestDataFeatureTransformation");
    FeatureExecutionPlan featureExecutionPlan4 = new FeatureExecutionPlan("f4", "requestSource", "com.feathr.online.RequestDataFeatureTransformation");
    plans.add(featureExecutionPlan1);
    plans.add(featureExecutionPlan2);
    plans.add(featureExecutionPlan3);
    plans.add(featureExecutionPlan4);
    return plans;
  }
}

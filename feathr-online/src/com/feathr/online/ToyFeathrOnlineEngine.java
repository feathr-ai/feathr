package com.feathr.online;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;


/**
 * A toy Feathr-online engine to demonstrate the user experience and some basic capabilities.
 */
public class ToyFeathrOnlineEngine {
  FeatureExecutionPlanBuilder featureExecutionPlanBuilder;
  MockSourceFeather mockSourceFeather;
  public ToyFeathrOnlineEngine() {
    this.featureExecutionPlanBuilder = new FeatureExecutionPlanBuilder();
    this.mockSourceFeather = new MockSourceFeather();
  }

  /**
   * Batch get features for those Entities.
   * @param entityKeys {@link EntityKey}s to fetch features for
   * @param featureNames features we want to fetch
   * @return A {@link CompletableFuture} that contains the feature data.
   */
  public CompletableFuture<Map<EntityKey, Map<String, Object>>> batchGetFeatures(Set<EntityKey> entityKeys, Set<String> featureNames) {
    System.out.println("FeathrOnlineClient: batch get features for entities.");

    Map<String, Object> featureResult = new HashMap<>();
    // TODO: for simplicity, we just handle one EntityKey
    // from feature to execution plan
    Set<FeatureExecutionPlan> featureExecutionPlans = featureExecutionPlanBuilder.buildPlans(featureNames);
    for (FeatureExecutionPlan plan : featureExecutionPlans) {
      Object sourceData = mockSourceFeather.getSourceData(plan.sourceName);
      if (plan.featureTransformationClass.isEmpty()) {
        featureResult.put(plan.featureName, sourceData);
        continue;
      }
      try {
        FeatureTransformationInterface aClass =
            (FeatureTransformationInterface) Class.forName(plan.featureTransformationClass).newInstance();
        Map<String, Object> features = aClass.batchTransform(plan.featureNames, sourceData);

        featureResult.putAll(features);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }

    // For demo, simply return same features all entity keys
    Map<EntityKey, Map<String, Object>> result = new HashMap<>();
    for (EntityKey entityKey : entityKeys) {
      result.put(entityKey, featureResult);
    }
    CompletableFuture<Map<EntityKey, Map<String, Object>>> mapCompletableFuture = new CompletableFuture<>();
    mapCompletableFuture.complete(result);
    return mapCompletableFuture;
  }

  /**
   * Get features for the {@link EntityKey}.
   */
  public CompletableFuture<Map<String, Object>>  getFeatures(EntityKey entityKey, Set<String> featureNames) {
    throw new RuntimeException("This is not implemented yet.");
  }
}

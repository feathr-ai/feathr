package com.feathr.online;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;



public class FeathrOnlineClient {
  private ToyFeathrOnlineEngine toyFeathrOnlineEngine;
  public FeathrOnlineClient() {
    toyFeathrOnlineEngine = new ToyFeathrOnlineEngine();
  }

  /**
   * Batch get features for those Entities.
   * @param entityKeys {@link EntityKey}s to fetch features for
   * @param featureNames features we want to fetch
   * @return A {@link CompletableFuture} that contains the feature data.
   */
  public CompletableFuture<Map<EntityKey, Map<String, Object>>> batchGetFeatures(Set<EntityKey> entityKeys, Set<String> featureNames) {
    System.out.println("FeathrOnlineClient: batch get features for entities.");

    CompletableFuture<Map<EntityKey, Map<String, Object>>> mapCompletableFuture =
        toyFeathrOnlineEngine.batchGetFeatures(entityKeys, featureNames);
    // To be implemented
    return mapCompletableFuture;
  }

  /**
   * Get features for the {@link EntityKey}.
   */
  public CompletableFuture<Map<String, Object>>  getFeatures(EntityKey entityKey, Set<String> featureNames) {
    System.out.println("FeathrOnlineClient: get features for entities.");

    // To be implemented
    return null;
  }
}

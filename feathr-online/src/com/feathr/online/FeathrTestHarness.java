package com.feathr.online;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * Demo class to showcase how Feathr online works with mocked components.
 *
 * Scenario:
 * your model need features f1, f2 for model inference.Feature f1 wants feature for memberId=123, f2 wants feature for
 * companyName=companyNameX.
 * For f1, the data is in Redis in featurized form so we don't need extra transformation.
 * For f2, the data is in Aerospike but we need some additional transformations({@link JobSalaryFeatureTransformation})
 * to get the features we want.
 *
 * For feature consumers:
 * Users who use {@link FeathrOnlineClient} can get features by feature names via {@link #batchGetFeatures}.
 * See example below.
 *
 * For feature producers:
 * Features are defined by Feathr Python client in the same way as offline.
 * Let's define a feature with transformation:
 *    1. define a source and how to fetch the source data for an EntityKey
 *       redisSource = RedisSource(name="nycTaxiBatchSource",
 *                         tableName="myRedisFeatureTable",
 *                         sourceOverride=com.feathr.MyFeathrFeatureSourceOverrider) // optional, see {@link MyFeatureSourceOverrider}
 *    2. define transformation if necessary
 *       featureAnchor = FeatureAnchor(name="aggregationFeatures",
 *                            source=redisSource,
 *                            transformation=com.feathr.JobSalaryFeatureTransformation, // {@link JobSalaryFeatureTransformation}
 *                            features=["f1", "f2"])
 *    3. Implement com.feathr.MyFeathrFeatureSourceOverrider and com.feathr.FeatureTransformation. Include them in the jar
 *    so FeathrOnlineClient can access them during runtime.
 *
 * Let's define a feature on top of request data
 *    1. define a source and how to fetch the source data for an EntityKey
 *       onlineRequestData = OnlineRequestDataSource(name="onlineRequestData")
 *    2. define transformation if necessary
 *       featureAnchor = FeatureAnchor(name="requestData",
 *                            source=onlineRequestData,
 *                            transformation=com.feathr.RequestDataFeatureTransformation, // {@link RequestDataFeatureTransformation}
 *                            features=["f3", "f4"])
 *    3. Implement com.feathr.RequestDataFeatureTransformation. Include them in the jar
 *    so FeathrOnlineClient can access them during runtime.
 *
 * Based on above information, FeathrOnlineClient can decode how to get different features with the {@link EntityKey}
 * information. {@link FeathrOnlineClient} also does all the optimizations internally with those information including:
 *   1. merging features of same source into one remote call
 *      for example, you have f1 and f2 both points to one redis table. Then we will merge them into one call to Redis.
 *   2. remove duplicate EntityKeys
 *      for example, the user provide mutiple same memberIds. We will remove duplicate and only make unique calls.
 *
 *
 * FeathrOnlineClient also provides performance and scalability:
 *  1. we handle thread pools and you can configure it based on your need
 *  2. we have a lot experiences building high-performance applications and libraries
 *
 * FeathrOnlineClient also provides resiliency:
 *  1. failure of a certain feature or source won't fail other features
 *  2. timeouts and retries
 *
 * FeathrOnlineClient also provides observability etc.
 */
public class FeathrTestHarness {

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    System.out.println("Hello world");

    // this is our application request data. Like geo location, browser information. These information usually is only
    // available at inference time.
    // request data can be different for different requests
    MyApplicationRequestData applicationRequestData = new MyApplicationRequestData();

    // Step1: create a Feathr-online client
    FeathrOnlineClient feathrOnlineClient = new FeathrOnlineClient();

    // Step2: specify features we want to fetch
    Set<String> featureNames = new HashSet<>();
    featureNames.add("f1");
    featureNames.add("f2");
    // f3 and f4 are features defined on top of request data
    featureNames.add("f3");
    featureNames.add("f4");

    // Step3: specify the entities you want to fetch
    // also attach the applicationRequestData with the EntityKey so we know which applicationRequestData to use for
    // different EntityKey
    Set<EntityKey> entityKeys = new HashSet<>();
    EntityKey memberEntityKey = new EntityKey("memberId", 123, applicationRequestData);
    EntityKey companyEntity = new EntityKey("companyName", "companyNameX", applicationRequestData);
    entityKeys.add(memberEntityKey);
    entityKeys.add(companyEntity);

    // Step4: get the features
    CompletableFuture<Map<EntityKey, Map<String, Object>>> mapCompletableFuture =
        feathrOnlineClient.batchGetFeatures(entityKeys, featureNames);

    // the result is: f1=software_engineer, f2=510.2040816326531, f3=1, f4=true
    System.out.println("Your feature data is: ");
    System.out.println(mapCompletableFuture.get().values().iterator().next());
  }
}

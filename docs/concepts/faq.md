---
layout: default
title: Feathr FAQ
parent: Feathr Concepts
---

# Feathr FAQ

This page addresses the most frequently asked questions we receive from end users.

## When is a feature store needed, and when is it not?

A feature store is typically needed when you have entities/keys (e.g. you are trying to model a user's behavior, an account behavior, for a specific item, etc.). However, it's likely not necessary for tasks like regular image recognition.

## What is a key, and what types of features require one?

In Feathr, each feature is typically associated with a specific key. Keys are also referred to as `Entities` in many other feature stores, signifying that a feature is connected to a specific entity. For instance, if you are creating a recommendation system, you may have `f_item_sales_1_week` for item sales, and `f_user_location` for historical user purchasing data. In this case, `f_item_sales_1_week` should be linked to `item_id`, and `f_user_location` should be associated with `user_id`. This is because each feature represents something specific to a particular `Entity` (item or user, in this case).

When querying features, you'll need to specify the key if you're seeking item-related features, such as `f_item_sales_1_week`. The exception to this rule is for features defined in `INPUT_CONTEXT`, which generally do not require keys as they're directly computed from observation data and aren't typically reused.

## What is the role of Feature Anchors in Purview? Are we actually generating feature values to display as a view?

Feature `Anchors` can be likened to "views" in standard SQL terms. They don't store the feature value; instead, they are a collection of features with individual `Features` being columns in the view. Hence, when grouping features together, it's important to note that they are essentially feature groupings.

## What are `key_column` and `full_name`? Where are they used?

`key_column` is a map to the source table, while `full_name` is used for reference.

## In transformation, can we include more than one column name?

Yes, although it may lead to errors if the source table splits.

## What is a DerivedFeature?

DerivedFeatures are features calculated from other features. They could be computed from anchored features or other derived features.


## What does the `client.get_offline_features()` function do?

This function joins observation data with a feature list.

## Why isn't FeatureAnchor input to the call?

FeatureAnchor is implicitly used. The method uses anchors and features created using `build_features`.

## Are multiple queries fired in parallel to retrieve data?

Yes, multiple queries may be initiated simultaneously.

## What does `client.register_features()` do?

This function registers features that were part of built features in `client.build()` or features from configuration files.

## Are any actions or scheduled jobs required to get updated feature data?

Only for online data.

## What is the purpose of the online backfill API? Is it for the latest feature only?

Yes, the online backfill API is specifically for the latest feature.

## Can Feathr be used to retrieve a feature set that has multiple columns?

Yes, this is referred to as a "Feathr Anchor".

## Is it possible to modify a feature retrieved from the Registry (Purview) and update it in the registry using Feathr Client?

This would involve using the `get_features_from_registry` function of FeathrClient for a specific project name, viewing the feature code, and modifying the feature.

## Is there a way to handle identity pass through for Feathr?

This becomes important when using features from the registry in the consumption flow, as the user must have access to all source data files before the feature can be utilized. This can be challenging, particularly in our data lake and DDS setup.

## Can multiple UDF functions be passed in preprocessing?

Currently, it seems that only one function can be passed, but this is subject to change based on specific requirements.

## What does the error message "java.lang.RuntimeException: The 0th field 'key0' of input row cannot be null" mean?

This error message signifies that some rows in the input data lack a key. Users should add a filter to the source to exclude these rows as they cannot be utilized.

## Do keys need to be unique in the input data?

Keys do not have to be unique for input data, but they cannot be null. The output dataset is a key-value map with the key specified, and the value is the combination of all requested features for each key. This process can be viewed as grouping or bucketing.

## If I'm going to materialize the feature data into offline storage, how can I read that through the Feathr API?

The offline store is simply a standard table. Currently, the offline store consists of parquet/avro files on hdfs. Users can read the table in offline storage without the Feathr API.

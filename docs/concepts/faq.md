---
layout: default
title: Feathr FAQ
nav_order: 7
---

# Feathr FAQ

This page covered the most asked questions that we've heard from end users.

## when do you need a feature store and when do you don't?

when you have entities/keys you usually need it (like for deep learning etc.)

when you don't (say just doing regular image recognition task) you probably don't need feature store.

## What is a key and which kind of features need this?

For Feathr keys, think that every feature needs it by default, i.e. think each Feathr Feature is associated with a certain key.

Key is also called `Entity` in many other feature store, so think a Feature is "associated" with a certain entity. For example, if you are building a recommendation system, you probably have `f_item_sales_1_week` for item sales, and `f_user_location` for user historical buying, so `f_item_sales_1_week` should be "keyed" to `item_id`, and `f_user_location` should be "keyed" to `user_id`. The reason is - each feature is only representing something for a particular `Entity` (item or user, in the above case), so the features must be associated with them. Otherwise, it doesn't make sense to use `f_user_location` on items, or use `f_item_sales_1_week` on users.

That's why when querying features, if you want to get a bunch of item related features, say `f_item_sales_1_week` and others, you will need to specify the key as well.

The only exception here would be Features defined in `INPUT_CONTEXT` don't need keys, the reason being that they will usually not be "reused", and are directly computed on observation data, so doesn't make sense to have keys.

## What is the essence of Feature Anchors in Purview? Are we really generating feature values to show it as a view ?

Think `Anchors` like just "views" in regular SQL terms, where it doesn't store the feature value, but it's merely a group of features; individual `Feature` is just a column in that view. That's why when you "group" features together, you need to

Think they are just feature grouping

## What are key_column and full_name? where used?

key_column: maps to source table. Ignore full_name (used for reference)

Feature

name: f_day_of_week

key:

feature_type: INT32

transformation: "dayofweek(lpep_dropoff_datetime)"


## transform - can we have more than one column name in transform?

yes (can be error prone - if source table splits)

## why no key in the example?

INPUT_CONTEXT needs no key

DerivedFeature

Features that are computed from other features. They could be computed from anchored features, or other derived features

name

feature_type

input_features= array/list of features

transform

FeatureAnchor (feature view)

It is a collection of features. Contains:

name

source

feature list

FeatureQuery

feature list

key

question
## why is key needed since Features have it?

advanced usecases in linkedin

client.get_offline_features()

Joins observation data with feature list

observation settings: source, time stamp column in source

feature query

output path

Questions:

Is FeatureAnchor not input to the call?

it is implicitly. This method uses anchors and features built using build_features.

What if Feature A is in a different source and Feature B is in different source? get_offline_features accepts only one source?

You can have different anchors associated with different sources.

the feature list in sample has sample features and agg features. Are multiple queries fired in parallel to get data?

Yes multiple queries may be fired.

client.register_features()

Registers the features (a) which were part of built features in client.build() OR (b) features from configuration files.
question:

how do i load registered features?

Docs will be published soon

Questions

any action/scheduled job needed to get updated feature data?

Only for online data

online backfill api: only latest feature?

yes

API

## online store

backfill_time = BackfillTime(start=datetime(

    2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))

redisSink = RedisSink(table_name="nycTaxiDemoFeature")

settings = MaterializationSettings("nycTaxiTable",

                                   backfill_time=backfill_time,

                                   sinks=[redisSink],

                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])

client.materialize_features(settings)

## Offline transformation:

Does it support popular transformations (e.g. MinMaxScaler for numerical data or one-hot encoding (mapping categories to integers)?
Does is support customized transformations (e.g. quantile clipping)?

Online transformation:
Does is support to store necessary information to transform new streaming data (e.g. min and max values of offline data (training set), which is used for model training to normalize new data or integers used before to map categorical data (men → 0 and women → 1)?

1. Assuming that feature set has multiple columns, is it possible to retrieve feature set with Feathr?
   Yes (that’s called “Feathr Anchor” (link to the motivation)

2. We use location in both batch source and observation settings, how differently are these locations used?
   It’s also explained here, but basically in observation setting you only need two columns: an ID column, and a timestamp column. Other fields are all optional.
   The existing NYC driver sample is a bit confusing since we are using a same file for two purpose. I’ll update them shortly to make sure it’s less confusing.

Does feathr support "local spark" runtime? other than databricks and synapse. For users to build and test features locally, without much changes to Way of working.

Is it possible to update the feature got from Registry(Purview)?Consider I have got the features from purview with get_features_from_registry function of FeathrClient for a particular project name. Now I would like to see the code of the feature, modify the feature and update the feature in registry with Feathr Client.

type_system_initialization: true in feathr_conf(Purview)
What is the use of this parameter?

While using features from registry in consumption flow, it is required that the user has access to all the source datafiles before the feature can be used. This will be tricky especially in our datalake and DDS setup. Any way to handle this

how he can pass a list in preprocessing to execute multiple UDF functions currently it looks like it only supports passing in a single function

We have a huge volume of sign-in events from the login token service which would make up our observation data.

Each event or observation will have keys like UserId and TenantId which we can use to define features on. (for example: time since last login for each user). We will use feathr to precompute and store these values in the offline store, then when we request a feature feathr will join those user or tenant based features back with the event/observation data with point in time correctness. However, it is my understanding that we would never want to store features on eventId as a key, since the volume is so large and eventIds are not reused. Instead we should instead build features on INPUT_CONTEXT. Feathr will serve these features "on-demand".

Does it make sense to use INPUT_CONTEXT for features like the following? Is there a better way?

- mapping of categorical values to machine understandable format like enum values or one hot encoding

- simple feature extraction like "does column x in observation data contain string y?"

- aggregation of column x across all observations - ex: avg request time

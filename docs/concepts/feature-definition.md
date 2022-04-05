---
layout: default
title: Feathr Feature Definition
parent: Feathr Concepts
---

# Feature Definition

## Prerequisite
* [Feathr Expression Language](../how-to-guides/expression-language.md)

## Introduction
In Feathr, a feature is viewed as a function, mapping from entity id or key, and timestamp to a feature value. 
1) The entity key (a.k.a. entity id) identifies the subject of feature, e.g. a user id, 123.
2) The feature name is the aspect of the entity that the feature is indicating, e.g. the age of the user.
3) The feature value is the actual value of that aspect at a particular time, e.g. the value is 30 at year 2022.

The feature definition has three sections, including sources, anchors and derivations.

## Step1: Define Sources Section
A feature source is needed for anchored features that describes the raw data in which the feature values are computed from.
See an examples below:

```python
batch_source = HdfsSource(name="nycTaxiBatchSource",
                          path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                          event_timestamp_column="lpep_dropoff_datetime",
                          timestamp_format="yyyy-MM-dd HH:mm:ss")
```

See the python documentation to get the details on each input column.

## Step2: Define Anchors and Features
A feature is called an anchored feature when the feature is directly 
extracted from the source data, rather than computed on top of other features. The latter case is called derived feature.

Anchors are required in Feathr. Here is an sample:

```python
f_trip_distance = Feature(name="f_trip_distance",
                          feature_type=FLOAT, transform="trip_distance")
f_trip_time_duration = Feature(name="f_trip_time_duration",
                               feature_type=INT32,
                               transform="time_duration(lpep_pickup_datetime, lpep_dropoff_datetime, 'minutes')")
features = [
    f_trip_distance,
    f_trip_time_duration,
    Feature(name="f_is_long_trip_distance",
            feature_type=BOOLEAN,
            transform="cast_float(trip_distance)>30"),
    Feature(name="f_day_of_week",
            feature_type=INT32,
            transform="dayofweek(lpep_dropoff_datetime)"),
]
request_anchor = FeatureAnchor(name="request_features",
                               source=INPUT_CONTEXT,
                               features=features)
```


For the features field above, there are two different types, simple anchored features and window aggregation features.

### Simple anchored features

1) For simple anchored features, see the example below:

```python
f_trip_time_duration = Feature(name="f_trip_time_duration",
                               feature_type=INT32,
                               transform="time_duration(lpep_pickup_datetime, lpep_dropoff_datetime, 'minutes')")
```

Note that for `transform` section, you can put a simple expression to transform your features. For more information, please refer to [Feathr Expression Language](../how-to-guides/expression-language.md).

### Window aggregation features

2) For window aggregation features, see the supported fields below:

```python

location_id = TypedKey(key_column="DOLocationID",
                       key_column_type=ValueType.INT32,
                       description="location id in NYC",
                       full_name="nyc_taxi.location_id")

Feature(name="f_location_avg_fare",
        key=location_id,
        feature_type=FLOAT,
        transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                          agg_func="AVG",
                                          window="90d")),
Feature(name="f_location_max_fare",
        key=location_id,
        feature_type=FLOAT,
        transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                          agg_func="MAX",
                                          window="90d"))
```


Note that the `agg_func` should be any of these:

| Aggregation Type | Input Type | Description |
| --- | --- | --- |
|SUM, COUNT, MAX, MIN, AVG	|Numeric|Applies the the numerical operation on the numeric inputs. |
|MAX_POOLING, MIN_POOLING, AVG_POOLING	| Numeric Vector | Applies the max/min/avg operation on a per entry bassis for a given a collection of numbers.|
|LATEST| Any |Returns the latest not-null values from within the defined time window |



After you have defined features and sources, bring them together to build an anchor:

```python
agg_anchor = FeatureAnchor(name="aggregationFeatures",
                           source=batch_source,
                           features=agg_features)
request_anchor = FeatureAnchor(name="request_features",
                               source=INPUT_CONTEXT,
                               features=features)
```

Note that if the data source is from the observation data, the `source` section should be `INPUT_CONTEXT` to indicate the source of those defined anchors.

## Step3: Derived Features Section
Derived features are the features that are computed from other features. They could be computed from anchored features, or other derived features.


```python
f_trip_distance = Feature(name="f_trip_distance",
                          feature_type=FLOAT, transform="trip_distance")
f_trip_time_duration = Feature(name="f_trip_time_duration",
                               feature_type=INT32,
                               transform="time_duration(lpep_pickup_datetime, lpep_dropoff_datetime, 'minutes')")
f_trip_time_distance = DerivedFeature(name="f_trip_time_distance",
                                      feature_type=FLOAT,
                                      input_features=[
                                          f_trip_distance, f_trip_time_duration],
                                      transform="f_trip_distance * f_trip_time_duration")
f_trip_time_rounded = DerivedFeature(name="f_trip_time_rounded",
                                     feature_type=INT32,
                                     input_features=[f_trip_time_duration],
                                     transform="f_trip_time_duration % 10")
```

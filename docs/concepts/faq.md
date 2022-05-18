---
layout: default
title: Feathr FAQ
nav_order: 7
---

This page covered the most asked questions that we've heard from end users.


# What is a key and which kind of features need this

# Scenarios of using a Key

# What is the essence of Feature Anchors in Purview? Are we really generating feature values to show it as a view ?




Feathr Notes 

Source 

name="nycTaxiBatchSource", 
path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data.csv", 
event_timestamp_column="lpep_dropoff_datetime", 
preprocessing=feathr_udf_day_calc, 
timestamp_format="yyyy-MM-dd HH:mm:ss" 

Key 

key_column="DOLocationID" 
key_column_type=ValueType.INT32, 
description="location id in NYC", 
full_name="nyc_taxi.location_id" 

Question: what are key_column and full_name? where used? 

key_column: maps to source table. Ignore full_name (used for reference) 

Feature 

name: f_day_of_week 

key: 

feature_type: INT32 

transformation: "dayofweek(lpep_dropoff_datetime)" 

questions 

can we have more than one column name in transform? 

yes (can be error prone - if source table splits) 

why no key in the example? 

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
why is key needed since Features have it? 

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

location_id = TypedKey(key_column="DOLocationID", 

                       key_column_type=ValueType.INT32, 

                       description="location id in NYC", 

                       full_name="nyc_taxi.location_id") 

agg_features = [Feature(name="f_location_avg_fare", 

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

                                                          window="90d")), 

                Feature(name="f_location_total_fare_cents", 

                        key=location_id, 

                        feature_type=FLOAT, 

                        transform=WindowAggTransformation(agg_expr="fare_amount_cents", 

                                                          agg_func="SUM", 

                                                          window="90d")), 

                ] 

agg_anchor = FeatureAnchor(name="aggregationFeatures", 

                           source=batch_source, 

                           features=agg_features) 

 

f_trip_time_distance = DerivedFeature(name="f_trip_time_distance", 

                                      feature_type=FLOAT, 

                                      input_features=[ 

                                          f_trip_distance, f_trip_time_duration], 

                                      transform="f_trip_distance * f_trip_time_duration") 

 

f_trip_time_rounded = DerivedFeature(name="f_trip_time_rounded", 

                                     feature_type=INT32, 

                                     input_features=[f_trip_time_duration], 

                                     transform="f_trip_time_duration % 10") 

 

 

client.build_features(anchor_list=[agg_anchor, request_anchor], derived_feature_list=[ 

                      f_trip_time_distance, f_trip_time_rounded]) 

 

feature_query = FeatureQuery( 

    feature_list=["f_location_avg_fare", "f_trip_time_rounded", "f_is_long_trip_distance", "f_location_total_fare_cents"], key=location_id) 

 

settings = ObservationSettings(    observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04_with_index.csv", 

    event_timestamp_column="lpep_dropoff_datetime", 

    timestamp_format="yyyy-MM-dd HH:mm:ss") 

 

client.get_offline_features(observation_settings=settings, 

                            feature_query=feature_query, 

                            output_path=output_path) 

 

# online store 

backfill_time = BackfillTime(start=datetime( 

    2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1)) 

 

redisSink = RedisSink(table_name="nycTaxiDemoFeature") 

 

settings = MaterializationSettings("nycTaxiTable", 

                                   backfill_time=backfill_time, 

                                   sinks=[redisSink], 

                                   feature_names=["f_location_avg_fare", "f_location_max_fare"]) 

 

client.materialize_features(settings) 

 
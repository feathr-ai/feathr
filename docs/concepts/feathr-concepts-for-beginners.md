# Feathr Concepts Guide for Beginners

In this guide, we will cover the high level concepts for Feathr. Don't treat this as a user manual, but treat this as blogpost to cover the highlevel thinkings for Feathr beginners.

## What are `Observation` data, and why do we need `key(s)`, `Anchor`, concept in Feathr?

In order to fully utilize Feathr's power, we need to understand the object models that Feathr is expecting. 

In Feathr, always think that there is some `Observation` dataset which is the central dataset that you will be using. This dataset usually have labels in it, but it's also fine that an `observation` data doesn't have a label.

For example, the `observation` dataset can be user click streams, credit card transactions, etc., while the data science team is trying to predict whether the user clicks on something, or whether the transcation is a fraud trasction, etc.

Usually you will need addtional data (or feature) to augment this `observation` dataset. For example, you want to augment the user click stream data by adding some historical features, such as the total amount that the user spent in the last one week. This additional dataset is usually in a different storage, say in your historical database, or data lake.

In this case, how would we "link" the `observation` dataset, and the "additional dataset"? This is called "Feature Join" in Feature Store, but basically think this process as joining two tables.

Since this is a Join process, we need to specify which `key(s)` that the join would happen. Those `keys` are usually some IDs, but can be others as well. In the above example, if we want to augment the user click stream data with user purchase history, we will use the user ID as key, so that the `click_stream` table and the `historical_buying` table can be joined together. Other cases might be we want to get the historical blood pressure history for a patient, so patient ID will be used as key in that case.

Since the additional features are from different sources, we want to define an `Anchor` to process it further. Think `Anchor` as a `Feature View`, where it sits on top of the raw data source and have some possible preprocessing capabilities (where you can process the raw data from its source with customized PySpark functions). Basically `Anchor` is a collection of features and their corresponding sources. Think `Feature` just as a column in your dataset but it contains some useful information that you want to use in your machine learning scenario.

`Source` concept:

That's why you will see something like:

```python

batch_source = HdfsSource(name="nycTaxiBatchSourcePurview",
                          path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/feathr_delta_table",
                          event_timestamp_column="lpep_dropoff_datetime",
                          timestamp_format="yyyy-MM-dd HH:mm:ss")

f_trip_distance = Feature(name="f_trip_distance",
                          feature_type=FLOAT, transform="trip_distance", 
                          )
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
                               source=batch_source,
                               features=features)
```

- In the above code, we have a datasource that is an HDFS style source, indicating where we will get data from. 
- We also define a few features on top of those source data, for example we can simply do renaming (`f_trip_distance` is a rename of `trip_distance`), pre-processing, etc. 
- we then "assemble" the features and sources into an `FeatureAnchor`, so that it can be built and registered later. Again, think `FeatureAnchor` as a "View"


## Motivation on `Derived Feature`

That sounds all good, but what if we want to compute something that is across two "Feature View"/"Anchor"s? Let's say in the above example, we want to calculate a feature that based on two input features, and those features are not in the same anchor (so you cannot use "preprocessing" in Anchor to pre calculate it). Here's why there is a concept in Feathr called "derived features", which allows you to calculate features based on other features, with transformation. For example, 

```python
f_trip_time_distance = DerivedFeature(name="f_trip_time_distance",
                                      feature_type=FLOAT,
                                      input_features=[
                                          f_trip_distance, f_trip_time_duration],
                                      transform="f_trip_distance * f_trip_time_duration")
```

## Why does Feathr need `Feature Join` and `Feature Query`?

After setting the above concept on Anchors, Sources, and Features, we want to explain the motivation on `feature join` and `feature query`. But before that, we also want to introduce the workflows that we usually see in organizations - the "feature producer" and "feature consumer" patttern, like below:

![Feature Producer and Consumer](../images/feature_store_producer_consumer.png)

As you can see, there are usually "feature producers" where they will define features and put them in the feature registry. Those feature producers will use the `Anchors` and `Feature Definitions` that we talked above to produce (or define) the features.

However, there are also a group of other people who will be the feature consumers. They don't care about how the features are defined, they "just" know that there are some features availble for use, for example a "feature anchor" describing user activities, which they can reuse to predict whether it is a fraud activity or not. In this case, `Feature Join` and `Feature Query` are in particular useful for those feature consumers.

After you have defined all the features, you probably don't want to use all of them in this particular program. In this case, instead of putting every features in this `FeatureQuery` part, you can just put a selected list of features. Note that they have to be of the same key, because essentially it is a table join and you need to specify the keys and make sure they are the same key(s).

Since this is a process with "join", you need to specify the input data and the features you want to join on (the feature query list), and the result will be the observation data plus the additional features.

```python
feature_query = FeatureQuery(
    feature_list=["f_location_avg_fare", "f_trip_time_rounded", "f_is_long_trip_distance"], key=location_id)
settings = ObservationSettings(
    observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
    event_timestamp_column="lpep_dropoff_datetime",
    timestamp_format="yyyy-MM-dd HH:mm:ss")
client.get_offline_features(observation_settings=settings,
                            feature_query=feature_query,
                            output_path=output_path)
```


## What is "materialization" in Feathr?
## What if I want to get features from "`Observation`" data?

There are a lot of use cases where users want to calculate features based on input features as well. Users can simply do renaming, or want to get some 


- What if we want to directly get features from the input data? INPUT_CONTEXT
- self join.



Additional datathat have additional features, which you want to augment the centralized dataset. For example, user purchase history, total spending in the past month.

## Feature Joins to get all the historical dataset/get offline dataset

we need to specify a key in feature join, and for each feature query, the key needs to be the same, because each feature join is like augmenting the "`observation`" dataset by additional feature from other tables once more.


## Point in time joins and aggregations

Assuming users are already familar with the "regular" joins, for example inner join or outer join, and in many of the use cases, we care about time, that is why in Feathr we provide a capability called Point in time Join (and with other time based aggregations).

For more details on how to utilize Feathr to perform point-in-time joins, refer to the [Point in Time Join Guide](../concepts/point-in-time-join.md)
- Talk why do we need a timestamp column and point in time join.

## Feature Registry



## others

Talk about feathr object model

- projects, anchors, relationships, feature tables, etc.


```mermaid
stateDiagram-v2
    FeathrProject --> Anchor_2
    FeathrProject -->  Anchor_1
    FeathrProject --> DerivedFeature1
    FeathrProject --> DerivedFeature2
    Anchor_2 --> Source_1
    Anchor_2 --> AnchorFeature_1
    Anchor_2 --> AnchorFeature_2
    Anchor_1 --> Source_2
    Anchor_1 --> AnchorFeature_3
    Anchor_1 --> AnchorFeature_4
    AnchorFeature_4 --> DerivedFeature1
    AnchorFeature_3 --> DerivedFeature1    
```
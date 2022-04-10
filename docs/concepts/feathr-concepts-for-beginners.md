# Feathr Concepts for Beginners

In this guide, we will cover the high level concepts for Feathr. Don't treat this as a user manual, instead treat this as blogpost to cover the highlevel motivations on why Feathr introduces those concepts.

## What are `Observation` data, and why does Feathr need `key(s)`, `Anchor`, `Source`?

An illustration of the concepts that we are going to talk about is like this:
![Feature Join Process](../images/observation_data.png)

In order to fully utilize Feathr's power, we need to understand the object models that Feathr is expecting.

In Feathr, always think that there is some `Observation` dataset which is the central dataset that you will be using. This dataset usually have labels in it, but it's also fine that an `observation` data doesn't have a label. In the latter case, an observation data will usually have two columns: a timestamp column and a column containing IDs.

For example, the `observation` dataset can be user click streams, credit card transactions, etc., while the data science team is trying to predict whether the user clicks on something, or whether the transcation is a fraud trasction, etc.

Usually you will need addtional features to augment this `observation` dataset. For example, you want to augment the user click stream data by adding some historical features, such as the total amount that the user spent in the last one week. This additional dataset is usually in a different storage, say in your historical database, or data lake.

In this case, how would we "link" the `observation` dataset, and the "additional dataset"? This is called "Feature Join" in Feathr, but basically think this process as joining two tables.

Since this is a Join process, we need to specify which `key(s)` that the join would happen. Those `keys` are usually some IDs, but can be others as well. In the above example, if we want to augment the user click stream data with user purchase history, we will use the user ID as `key`, so that the `click_stream` table and the `historical_buying` table can be joined together. Other cases might be we want to get the historical blood pressure history for a patient, so `patient ID` will be used as key in that case.

Since those additional features are from different sources, we want to define an `Anchor` to process it further. Think `Anchor` as a `Feature View`, where it is a collection of features and their corresponding sources. Think `Feature` just as a column in your dataset but it contains some useful information that you want to use in your machine learning scenario.

`Source` in Feathr just represents the source data that you will need to use to extract features from. It also comes with handy customizations that you can run almost arbitrary PySpark/SparkSQL code.

That's why you will see something like below, where you define a Feathr Source (in this case it's an HDFS like source) and corresponding features, and then compose an `Anchor` that combines the `Features` and `Source`.

```python
batch_source = HdfsSource(name="nycTaxiBatchSourcePurview",
                          path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/feathr_delta_table",
                          event_timestamp_column="lpep_dropoff_datetime",
                          timestamp_format="yyyy-MM-dd HH:mm:ss")
features = [
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

## Motivation on `Derived Feature`

That sounds all good, but what if we want to compute something that is across two "Feature View"/"Anchor"s? Let's say in the above example, we want to calculate a feature that based on two input features, and those features are in different anchors and you cannot use "preprocessing" . Here's why there is a concept in Feathr called "derived features", which allows you to calculate features based on other features, with transformation. In practice, people can build features on top of other features and have a "feature chain".

One example for Derived Feautre is as below:

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

However, there are also a group of other people who will be the feature consumers. They don't care about how the features are defined, they "just know" that there are some features availble for use, for example a feature describing user activities, which they can reuse to predict whether it is a fraud activity or not. In this case, `Feature Join` and `Feature Query` are in particular useful for those feature consumers.

After the feature producer have defined all the features, the feature consumer probably don't want to use all of them. In this case, feature consumers can select which set of features they want to put in `FeatureQuery`, so that they can get the features and join them on the input observation data.

Since this is a process with "join", you need to specify the observation data source and the features you want to get, and the result will be the observation data plus the additional features.

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

You are very likely to train a machine learning model with the features that you just queried. After you have trained a machine learning model, say a fraud detection model, you are likely to put the machine learning model into an online envrionment and do online inference. 

In that case, you will need to retrieve the features (for example the user historical spending) in real time, since the fraud detection model is very time sensitive. Usually some key-value store is used for that scenario (for example Redis), and Feathr will help you to materialize features in the online environment for faster inference. That is why you will see something like below, where you specify Redis as the online storage you want to use, and retrieve features from online envrionment from there:


```python
redisSink = RedisSink(table_name="nycTaxiDemoFeature")
# Materialize two features into a redis table.
settings = MaterializationSettings("nycTaxiMaterializationJob",
sinks=[redisSink],
feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
client.get_online_features(feature_table = "agg_features",
                           key = "265",
                           feature_names = ['f_location_avg_fare', 'f_location_max_fare'])
```

## Point in time joins and aggregations

Assuming users are already familar with the "regular" joins, for example inner join or outer join, and in many of the use cases, we care about time, that is why in Feathr we provide a capability called Point in time Join (and with other time based aggregations).

For more details on how to utilize Feathr to perform point-in-time joins, refer to the [Point in Time Join Guide](../concepts/point-in-time-join.md)

- Talk why do we need a timestamp column and point in time join.

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

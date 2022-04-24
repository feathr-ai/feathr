---
layout: default
title: Getting Historical Features in Feathr
parent: Feathr Concepts
---

# Feature Join
## Intuitions of Frame Join
Observation dataset has 2 records as below, and we want to use it as the 'spine' dataset, joining two 
features onto it:

1) Feature 'page_view_count' from dataset 'page_view_data'

2) Feature 'like_count' from dataset 'like_count_data' 

The Feathr feature join in this case, will use the field 'id' as join key of the observation data, and also consider the timestamp of each row during the join, making sure the joined feature values are collected **before** the observation_time of each row.

| id | observe_time | Label 
| --- | --- | --- 
| 1 | 2022-01-01 | Yes 
| 1 | 2022-01-02 | Yes 
| 2 | 2022-01-02 | No 


Dataset 'page_view_data' contains “page_view_count” of each user at a given time:

| UserId | log_time | page_view_count |
| --- | --- | --- | 
|1 | 2022-01-01 | 101 |
|1 | 2022-01-02 | 102 |
|1 | 2022-01-03 | 103 |
|2 | 2022-01-02 | 200 |
|3 | 2022-01-02 | 300 |


Dataset 'like_count_data' contains "like_count" of each user at a given time:

| UserId | updated_time | 'like_count' |
| --- | --- | --- | 
|1 | 2022-01-01 | 11 |
|1 | 2022-01-02 | 12 |
|1 | 2022-01-03 | 13 |
|2 | 2022-01-02 | 20 |
|3 | 2022-01-02 | 30 |

The expected joined output, a.k.a. training dataset would be assuming feature:

| id | observe_time | Label | f_page_view_count | f_like_count|
| --- | --- | --- | --- | --- |
|1 | 2022-01-01 | Yes | 101 | 11 |
|1 | 2022-01-02 | Yes | 102 | 12 |
|2 | 2022-01-02 | No | 200 | 20

Note: In the above example, feature f_page_view_count and f_like_count are defined as simply a reference of field
'page_view_count' and 'like_count' respectively. Timestamp in these 3 datasets are considered automatically.

## Feature join config

An example is like below:

```python
feature_query = FeatureQuery(
    feature_list=["f_location_avg_fare"], key=location_id)
settings = ObservationSettings(
    observation_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
    event_timestamp_column="lpep_dropoff_datetime",
    timestamp_format="yyyy-MM-dd HH:mm:ss")
client.get_offline_features(observation_settings=settings,
                            feature_query=feature_query,
                            output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro")

```


After you have defined the features (as described in the [Feature Definition](feature-definition.md)) part, you can define how you want to join them.

### Observation data

The path of a dataset as the 'spine' for the to-be-created training dataset. We call this input 'spine' dataset the 'observation' dataset. Typically, each row of the observation data contains:

1. Column(s) representing entity id(s), which will be used as the join key to look up(join) feature value.

2. A column representing the event time of the row. By default, Feathr will make sure the feature values joined have a timestamp earlier than it, ensuring no data leakage in the resulting training dataset.

3. Other columns will be simply pass through onto the output training dataset.
The key fields from the observation data, which are used to joined with the feature data.
List of feature names to be joined with the observation data. They must be pre-defined in the Python APIs.

The time information of the observation data used to compare with the feature's timestamp during the join.

## FeatureQuery
After you have defined all the features, you probably don't want to use all of them in this particular program. In this case, instead of putting every features in this `FeatureQuery` part, you can just put a selected list of features. Note that they have to be of the same key.
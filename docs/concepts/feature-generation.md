---
layout: default
title: Feathr Feature Generation
parent: Feathr Concepts
---

# Feature Generation
Feature generation is the process to create features from raw source data into a certain persisted storage.

User could utilize feature generation to pre-compute and materialize pre-defined features to online and/or offline storage. This is desirable when the feature transformation is computation intensive or when the features can be reused(usually in offline setting). Feature generation is also useful in generating embedding features. Embedding distill information from large data and it is usually more compact.

## Generating Features to Online Store
When we need to serve the models online, we also need to serve the features online. We provide APIs to generate features to online storage for future consumption. For example:
```python
client = FeathrClient()
redisSink = RedisSink(table_name="nycTaxiDemoFeature")
# Materialize two features into a redis table.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[redisSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
```
([MaterializationSettings API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.materialization_settings.MaterializationSettings),
[RedisSink API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.sink.RedisSink))

In the above example, we define a Redis table called `nycTaxiDemoFeature` and materialize two features called `f_location_avg_fare` and `f_location_max_fare` to Redis.

It is also possible to backfill the features for a previous time range, like below. If the `BackfillTime` part is not specified, it's by default to `now()` (i.e. if not specified, it's equivilant to `BackfillTime(start=now, end=now, step=timedelta(days=1))`).

```python
client = FeathrClient()
# Simulate running the materialization job every day for a time range between 2/1/22 and 2/20/22
backfill_time = BackfillTime(start=datetime(2022, 2, 1), end=datetime(2022, 2, 20), step=timedelta(days=1))
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[redisSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"],
                                   backfill_time=backfill_time)
client.materialize_features(settings)
```
([BackfillTime API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.materialization_settings.BackfillTime),
[client.materialize_features() API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.client.FeathrClient.materialize_features))

## Consuming the online features

```python
client.wait_job_to_finish(timeout_sec=600)

res = client.get_online_features('nycTaxiDemoFeature', '265', [
                                     'f_location_avg_fare', 'f_location_max_fare'])
```
([client.get_online_features API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.client.FeathrClient.get_online_features))

After we finish running the materialization job, we can get the online features by querying the feature name, with the 
corresponding keys. In the example above, we query the online features called `f_location_avg_fare` and 
`f_location_max_fare`, and query with a key `265` (which is the location ID).

## Generating Features to Offline Store

This is a useful when the feature transformation is computation intensive and features can be re-used. For example, you 
have a feature that needs more than 24 hours to compute and the feature can be reused by more than one model training 
pipeline. In this case, you should consider generate features to offline. Here is an API example:
```python
client = FeathrClient()
offlineSink = OfflineSink(output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/")
# Materialize two features into a Offline store.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[offlineSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
```
This will generate features on latest date(assuming it's `2022/05/21`) and output data to the following path: 
`abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2022/05/21`


You can also specify a BackfillTime so the features will be generated for those dates. For example:
```Python
backfill_time = BackfillTime(start=datetime(
    2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
offline_sink = OfflineSink(output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/")
settings = MaterializationSettings("nycTaxiTable",
                                   sinks=[offline_sink],
                                   feature_names=[
                                       "f_location_avg_fare", "f_location_max_fare"],
                                   backfill_time=backfill_time)
```
This will generate features only for 2020/05/20 for me and it will be in folder: 
`abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/20`

([MaterializationSettings API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.materialization_settings.MaterializationSettings),
[OfflineSink API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.sink.OfflineSink))

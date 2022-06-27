---
layout: default
title: Feature Generation and Materialization
parent: Feathr Concepts
---

# Feature Generation and Materialization

Feature generation (also known as feature materialization) is the process to create features from raw source data into a certain persisted storage in either offline store (for further reuse), or online store (for online inference).

User can utilize feature generation to pre-compute and materialize pre-defined features to online and/or offline storage. This is desirable when the feature transformation is computation intensive or when the features can be reused (usually in offline setting). Feature generation is also useful in generating embedding features, where those embeddings distill information from large data and is usually more compact.

## Generating Features to Online Store

When the models are served in an online environment, we also need to serve the corresponding features in the same online environment as well. Feathr provides APIs to generate features to online storage for future consumption. For example:

```python
client = FeathrClient()
redisSink = RedisSink(table_name="nycTaxiDemoFeature")
# Materialize two features into a redis table.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[redisSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
```

More reference on the APIs:

- [MaterializationSettings API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.MaterializationSettings)
- [RedisSink API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.RedisSink)

In the above example, we define a Redis table called `nycTaxiDemoFeature` and materialize two features called `f_location_avg_fare` and `f_location_max_fare` to Redis.

## Feature Backfill

It is also possible to backfill the features for a particular time range, like below. If the `BackfillTime` part is not specified, it's by default to `now()` (i.e. if not specified, it's equivalent to `BackfillTime(start=now, end=now, step=timedelta(days=1))`).

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

Note that if you don't have features available in `now`, you'd better specify a `BackfillTime` range where you have features.

Also, Feathr will submit a materialization job for each of the step for performance reasons. I.e. if you have `BackfillTime(start=datetime(2022, 2, 1), end=datetime(2022, 2, 20), step=timedelta(days=1))`, Feathr will submit 20 jobs to run in parallel for maximum performance.

More reference on the APIs:

- [BackfillTime API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.BackfillTime)
- [client.materialize_features() API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.FeathrClient.materialize_features)

## Consuming features in online environment

After the materialization job is finished, we can get the online features by querying the `feature table`, corresponding `entity key` and a list of `feature names`. In the example below, we query the online features called `f_location_avg_fare` and `f_location_max_fare`, and query with a key `265` (which is the location ID).

```python
res = client.get_online_features('nycTaxiDemoFeature', '265', ['f_location_avg_fare', 'f_location_max_fare'])
```

More reference on the APIs:

- [client.get_online_features API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.FeathrClient.get_online_features)

## Materializing Features to Offline Store

This is useful when the feature transformation is compute intensive and features can be re-used. For example, you have a feature that needs more than 24 hours to compute and the feature can be reused by more than one model training pipeline. In this case, you should consider generating features to offline.

The API call is very similar to materializing features to online store, and here is an API example:

```python
client = FeathrClient()
offlineSink = HdfsSink(output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/")
# Materialize two features into a Offline store.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[offlineSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
```

This will generate features on latest date(assuming it's `2022/05/21`) and output data to the following path:
`abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2022/05/21`

You can also specify a `BackfillTime` so the features will be generated only for those dates. For example:

```Python
backfill_time = BackfillTime(start=datetime(
    2020, 5, 10), end=datetime(2020, 5, 20), step=timedelta(days=1))
offline_sink = HdfsSink(output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/")
settings = MaterializationSettings("nycTaxiTable",
                                   sinks=[offline_sink],
                                   feature_names=[
                                       "f_location_avg_fare", "f_location_max_fare"],
                                   backfill_time=backfill_time)
```

This will generate features from `2020/05/10` to `2020/05/20` and the output will have 11 folders, from
`abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/10` to `abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/20`. Note that currently Feathr only supports materializing data in daily step (i.e. even if you specify an hourly step, the generated features in offline store will still be presented in a daily hierarchy).

You can also specify the format of the materialized features in the offline store by using `execution_configurations` like below. Please refer to the [documentation](../how-to-guides/feathr-job-configuration.md) here for those configuration details.

```python

from feathr import HdfsSink
offlineSink = HdfsSink(output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_data/")
# Materialize two features into a Offline store.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[offlineSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings, execution_configurations={ "spark.feathr.outputFormat": "parquet"})

```

For reading those materialized features, Feathr has a convenient helper function called `get_result_df` to help you view the data. For example, you can use the sample code below to read from the materialized result in offline store:

```python

path = "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/20/"
res = get_result_df(client=client, format="parquet", res_url=path)
```

More reference on the APIs:

- [MaterializationSettings API](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.MaterializationSettings)
- [HdfsSink API](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.HdfsSource)

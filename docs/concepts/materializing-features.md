---
layout: default
title: Feature Materialization (also known as feature generation)
parent: Feathr Concepts
---

# Feature Materialization (also known as feature generation)

Feature materialization (also known as feature generation) is the process to create features for a certain entity from raw source data into a certain persisted storage in either offline store (for further reuse), or online store (for online inference).

User can utilize feature generation to pre-compute and materialize pre-defined features to online and/or offline storage. This is desirable when the feature transformation is computation intensive or when the features can be reused (usually in offline setting). Feature generation is also useful in generating embedding features, where those embeddings distill information from large data and is usually more compact. Also, please note that you can only materialize features for a specific entity/key in the same `materialize_features` call.

## Materializing Features to Online Store

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

## Incremental Aggregation
Use incremental aggregation will significantly expedite the WindowAggTransformation feature calculation. 
For example, aggregation sum of a feature F within a 180-day window at day T can be expressed as: F(T) = F(T - 1)+DirectAgg(T-1)-DirectAgg(T - 181). 
Once a SNAPSHOT of the first day is generated, the calculation for the following days can leverage it.

A storeName is required if incremental aggregated is enabled. There could be multiple output Datasets, and each of them need to be stored in a separate folder. The storeName is used as the folder name to create under the base "path".

Incremental aggregation is enabled by default when using HdfsSink.

More reference on the APIs:
- [HdfsSink API doc](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.HdfsSink)

## Feature Backfill

It is also possible to backfill the features till a particular time, like below. If the `BackfillTime` part is not specified, it's by default to `now()` (i.e. if not specified, it's equivalent to `BackfillTime(start=now, end=now, step=timedelta(days=1))`).

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

Feathr will submit a materialization job for each of the step for performance reasons. I.e. if you have `BackfillTime(start=datetime(2022, 2, 1), end=datetime(2022, 2, 20), step=timedelta(days=1))`, Feathr will submit 20 jobs to run in parallel for maximum performance.

Please note that the parameter forms a closed interval, which means that both start and end date will be included in materialized job,

Please also note that the `start` and `end` parameter means the cutoff start and end time. For example, we might have a dataset like below:

| TrackingID | UserId | Spending | Date       |
| ---------- | ------ | -------- | ---------- |
| 1          | 1      | 10       | 2022/05/01 |
| 2          | 2      | 15       | 2022/05/02 |
| 3          | 3      | 19       | 2022/05/03 |
| 4          | 1      | 18       | 2022/05/04 |
| 5          | 3      | 7        | 2022/05/05 |

If we call the API like this:
`BackfillTime(start=datetime(2022, 5, 2), end=datetime(2022, 5, 4), step=timedelta(days=1))`

Feathr will trigger 3 jobs:

- job 1 will backfill all data till 2022/05/02 (so feature using data in 2022/05/01 will also be materialized)
- job 2 will backfill all data till 2022/05/03 (so feature using data in 2022/05/01 and 2022/05/02 will also be materialized)
- job 3 will backfill all data till 2022/05/04 (so feature using data in 2022/05/01, 2022/05/02, and 2022/05/03 will also be materialized)

This is in particular useful for aggregated features. For example, if there is a feature defined as `user_purchase_in_last_2_days`, this will grantee that all the materialized features come with the right result.

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
offlineSink = HdfsSink(output_path="abfss://{adls_fs_name}@{adls_account}.dfs.core.windows.net/materialize_offline_test_data/")
# Materialize two features into a Offline store.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[offlineSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
```

This will generate features on latest date(assuming it's `2022/05/21`) and output data to the following path:
`abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2022/05/21`

You can also specify a `BackfillTime` which will specify a cutoff time for feature materialization. For example:

```python
backfill_time = BackfillTime(start=datetime(
    2020, 5, 10), end=datetime(2020, 5, 20), step=timedelta(days=1))
offline_sink = HdfsSink(output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/materialize_offline_test_data/")
settings = MaterializationSettings("nycTaxiTable",
                                   sinks=[offline_sink],
                                   feature_names=[
                                       "f_location_avg_fare", "f_location_max_fare"],
                                   backfill_time=backfill_time)
```

This will materialize features with cutoff time from `2020/05/10` to `2020/05/20` correspondingly, and the output will have 11 folders, from
`abfss://{adls_fs_name}@{adls_account}.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/10` to `abfss://{adls_fs_name}@{adls_account}.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/20`. Note that currently Feathr only supports materializing data in daily step (i.e. even if you specify an hourly step, the generated features in offline store will still be presented in a daily hierarchy). For more details on how `BackfillTime` works, refer to the [BackfillTime section](#feature-backfill) above.

You can also specify the format of the materialized features in the offline store by using `execution_configurations` like below. Please refer to the [documentation](../how-to-guides/feathr-job-configuration.md) here for those configuration details.

```python

from feathr import HdfsSink
offlineSink = HdfsSink(output_path="abfss://{adls_fs_name}@{adls_account}.dfs.core.windows.net/materialize_offline_data/")
# Materialize two features into a Offline store.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[offlineSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings, execution_configurations={ "spark.feathr.outputFormat": "parquet"})

```

For reading those materialized features, Feathr has a convenient helper function called `get_result_df` to help you view the data. For example, you can use the sample code below to read from the materialized result in offline store:

```python
from feathr import get_result_df
path = "abfss://{adls_fs_name}@{adls_account}.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/20/"
res = get_result_df(client=client, format="parquet", res_url=path)
```

More reference on the APIs:

- [MaterializationSettings API](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.MaterializationSettings)
- [HdfsSink API](https://feathr.readthedocs.io/en/latest/feathr.html#feathr.HdfsSource)

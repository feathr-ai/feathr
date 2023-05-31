---
layout: default
title: Feature Materialization (also known as feature generation)
parent: Feathr Concepts
---

# Feature Materialization (also known as feature generation)

Feature materialization (also known as feature generation) is the process of creating features for a certain entity from raw source data into a certain persisted storage in either offline store (for further reuse), or online store (for online inference).

Users can utilize feature generation to pre-compute and materialize pre-defined features to online and/or offline storage. This is desirable when the feature transformation is computation intensive or when the features can be reused (usually in offline setting). Feature generation is also useful in generating embedding features, where those embeddings distill information from large data and is usually more compact. Also, please note that you can only materialize features for a specific entity/key in the same `materialize_features` call.

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
Using incremental aggregation will significantly expedite the WindowAggTransformation feature calculation. 
For example, the aggregation sum of a feature F within a 180-day window at day T can be expressed as: F(T) = F(T - 1)+DirectAgg(T-1)-DirectAgg(T - 181). 
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

Feathr will submit a materialization job for each step for performance reasons. I.e. if you have `BackfillTime(start=datetime(2022, 2, 1), end=datetime(2022, 2, 20), step=timedelta(days=1))`, Feathr will submit 20 jobs to run in parallel for maximum performance.

Please note that the parameter forms a closed interval, which means that both the start and end date will be included in the materialized job.

Also note that the `start` and `end` parameters signify the cutoff start and end time. For example, we might have a dataset like below:

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

- job 1 will backfill all data till 2022/05/02 (so features using data in 2022/05/01 will also be materialized)
- job 2 will backfill all data till 2022/05/03 (so features using data in 2022/05/01 and 2022/05/02 will also be materialized)
- job 3 will backfill all data till 2022/05/04 (so features using data in 2022/05/01, 2022/05/02, and 2022/05/03 will also be materialized)

This is particularly useful for aggregated features. For example, if there is a feature defined as `user_purchase_in_last_2_days`, this will grantee that all the materialized features come with the right result.

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

This is useful when the feature transformation is computing intensive and features can be re-used. For example, you have a feature that needs more than 24 hours to compute and the feature can be reused by more than one model training pipeline. In this case, you should consider generating features to an offline store.

The API call is very similar to materializing features to an online store, and here is an API example:

```python
client = FeathrClient()
offlineSink = HdfsSink(output_path="abfss://{adls_fs_name}@{adls_account}.dfs.core.windows.net/materialize_offline_test_data/")
# Materialize two features into a Offline store.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[offlineSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
```

This will generate features on the latest date (assuming it's `2022/05/21`) and output data to the following path:
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

This will materialize features with a cutoff time from `2020/05/10` to `2020/05/20` correspondingly, and the output will have 11 folders, from
`abfss://{adls_fs_name}@{adls_account}.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/10` to `abfss://{adls_fs_name}@{adls_account}.dfs.core.windows.net/materialize_offline_test_data/df0/daily/2020/05/20`. Note that currently Feathr only supports materializing data in daily steps (i.e. even if you specify an hourly step, the generated features in the offline store will still be presented in a daily hierarchy). For more details on how `BackfillTime` works, refer to the [BackfillTime section](#feature-backfill) above.

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

## Expected behavior on Feature Materialization

When end users materialize features to a sink, what is the expected behavior?

It seems to be a straightforward question, but it actually is not. When end users want to materialize a feature, Feathr expects the following: for a certain entity key (say a user_id), there will be multiple features (say user_total_gift_card_balance, and user_purchase_in_last_week). So two checks will be performed:

1. Those features should have the same entity key (say a user_id). You cannot materialize features for two entity keys in the same materialization job (although you can do it in different jobs), such as materializing `uer_total_purchase` and `product_sold_in_last_week` in the same Feathr materialization job.
2. Those features should all be "aggregated" features. I.e. they should be features which have type `WindowAggTransformation`, such as `product_sold_in_last_week` or `user_latest_total_gift_card_balance`.

The first constraint is pretty straightforward to explain - since when Feathr materializes certain features, they are used to describe certain aspects of a given entity, such as the users. Describing `product_sold_in_last_week` would not make sense for users.

The second constraint is a bit more interesting. For example, you have defined `user_total_gift_card_balance` and it has different values for the same user across different times. Let's say the corresponding values are 40, 30, 20, 20 for the last 4 days, like below.
Original data:

| UserId | user_total_gift_card_balance | Date       |
| ------ | ---------------------------- | ---------- |
| 1      | 40                           | 2022/01/01 |
| 1      | 30                           | 2022/01/02 |
| 1      | 20                           | 2022/01/03 |
| 1      | 20                           | 2022/01/04 |
| 2      | 40                           | 2022/01/01 |
| 2      | 30                           | 2022/01/02 |
| 2      | 20                           | 2022/01/03 |
| 2      | 20                           | 2022/01/04 |
| 3      | 40                           | 2022/01/01 |
| 3      | 30                           | 2022/01/02 |
| 3      | 20                           | 2022/01/03 |
| 3      | 20                           | 2022/01/04 |

However, the materialized features have no dates associated with them. I.e. the materialized result should be something like this:

| UserId | user_total_gift_card_balance |
| ------ | ---------------------------- |
| 1      | ?                            |
| 2      | ?                            |
| 3      | ?                            |

When you ask Feathr to "materialize" `user_total_gift_card_balance` for you, there's only one value that can be materialized, since the materialized feature does not have a date associated with them. So the problem is - for a given `user_id`, only one `user_total_gift_card_balance` can be its feature. Which value do you choose out of the 4 values? A random value? The latest value?

It might be natural to think that "we should materialize the latest feature", and that behavior, by definition, is an "aggregation" operation, since we have 4 values for a given `user_id` but we are only materializing and using one of them. In that case, Feathr asks you to explicitly say that you want to materialize the latest feature (i.e. by using [Point-in-time Join](./point-in-time-join.md))

```python
feature = Feature(name="user_total_gift_card_balance",
            key=UserId,
            feature_type=FLOAT,
            transform=WindowAggTransformation(agg_expr="gift_card_balance",
                                              agg_func="LATEST",
                                              window="7d"))
```
# Feature Generation

## Generating Features to Online Store

User could utilize feature generation to pre-compute and materialize pre-defined features to online and/or offline storage. This is a common practice when the feature transformation is computation intensive.

Examples:

```python
client = FeathrClient()
redisSink = RedisSink(table_name="nycTaxiDemoFeature")
# Materialize two features into a redis table.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[redisSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
```

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

## Consuming the online features

```python
client.wait_job_to_finish(timeout_sec=600)

res = client.get_online_features('nycTaxiDemoFeature', '265', [
                                     'f_location_avg_fare', 'f_location_max_fare'])
```

After we finish running the materialization job, we can get the online features by querying the feature name, with the corresponding keys. In the exmaple above, we query the online features called `f_location_avg_fare` and `f_location_max_fare`, and query with a key `265` (which is the location ID).

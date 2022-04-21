---
layout: default
title: Feathr Feature Generation
parent: Feathr Concepts
---

# Feature Generation

## Generating Features to Online Store

User could utilize feature generation to pre-compute and materialize pre-defined features to online and/or offline storage. This is a common practice when the feature transformation is computation intensive. For example:
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

## Streaming feature ingestion

1. Define Kafka streaming input source

```python
# Define input data schema
schema = AvroJsonSchema(schemaStr="""
{
    "type": "record",
    "name": "DriverTrips",
    "fields": [
        {"name": "driver_id", "type": "long"},
        {"name": "trips_today", "type": "int"},
        {
        "name": "datetime",
        "type": {"type": "long", "logicalType": "timestamp-micros"}
        }
    ]
}
""")
stream_source = KafKaSource(name="kafkaStreamingSource",
                            kafkaConfig=KafkaConfig(brokers=["feathrazureci.servicebus.windows.net:9093"],
                                                    topics=["feathrcieventhub"],
                                                    schema=schema)
                            )
```

2. Define feature definition with the Kafka source

```python
driver_id = TypedKey(key_column="driver_id",
                     key_column_type=ValueType.INT64,
                     description="driver id",
                     full_name="nyc driver id")

kafkaAnchor = FeatureAnchor(name="kafkaAnchor",
                            source=stream_source,
                            features=[Feature(name="f_modified_streaming_count",
                                              feature_type=INT32,
                                              transform="trips_today + 1",
                                              key=driver_id),
                                      Feature(name="f_modified_streaming_count2",
                                              feature_type=INT32,
                                              transform="trips_today + 2",
                                              key=driver_id)]
                            )

```
Note that only Feathr ExpressionTransformation is allowed in streaming anchor at the moment.
Other transformations support are in the roadmap.

3. Start streaming job

```python
redisSink = RedisSink(table_name="kafkaSampleDemoFeature", streaming=True, streamingTimeoutMs=10000)
settings = MaterializationSettings(name="kafkaSampleDemo",
                                   sinks=[redisSink],
                                   feature_names=['f_modified_streaming_count']
                                   )
client.materialize_features(settings) # Will streaming for 10 seconds since streamingTimeoutMs is 10000
```
4. Fetch streaming feature values

```python

    res = client.get_online_features('kafkaSampleDemoFeature', '1',
                                     ['f_modified_streaming_count'])
    # Get features for multiple feature keys
    res = client.multi_get_online_features('kafkaSampleDemoFeature',
                                    ['1', '2'],
                                    ['f_modified_streaming_count'])

```
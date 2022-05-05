---
layout: default
title: Streaming Source Ingestion
parent: Feathr How-to Guides
---
# Streaming feature ingestion

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
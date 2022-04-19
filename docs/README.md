---
layout: default
title: Home
nav_order: 1
description: "Feathr – An Enterprise-Grade, High Performance Feature Store"
permalink: /
---

# Feathr – An Enterprise-Grade, High Performance Feature Store

## What is Feathr?

Feathr lets you:

- **define features** based on raw data sources, including time-series data, using simple APIs.
- **get those features by their names** during model training and model inferencing.
- **share features** across your team and company.

Feathr automatically computes your feature values and joins them to your training data, using point-in-time-correct semantics to avoid data leakage, and supports materializing and deploying your features for use online in production.

## Quick Start

- Follow the [quick start Jupyter Notebook](https://github.com/linkedin/feathr/blob/main/feathr_project/feathrcli/data/feathr_user_workspace/nyc_driver_demo.ipynb) to try it out. There is also a companion [quick start guide](./quickstart.md) containing a bit more explanation on the notebook.
- For more details, read our [documentation](https://linkedin.github.io/feathr/).

## Defining Features with Transformation

```python
features = [
    Feature(name="f_trip_distance",                         # Ingest feature data as-is
            feature_type=FLOAT),
    Feature(name="f_is_long_trip_distance",
            feature_type=BOOLEAN,
            transform="cast_float(trip_distance)>30"),      # SQL-like syntax to transform raw data into feature
    Feature(name="f_day_of_week",
            feature_type=INT32,
            transform="dayofweek(lpep_dropoff_datetime)")   # Provides built-in transformation
]

anchor = FeatureAnchor(name="request_features",             # Features anchored on same source
                       source=batch_source,
                       features=features)
```

## Accessing Features

```python
from feathr import FeathrClient

# Requested features to be joined
# Define the key for your feature
location_id = TypedKey(key_column="DOLocationID",
                       key_column_type=ValueType.INT32,
                       description="location id in NYC",
                       full_name="nyc_taxi.location_id")
feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=[location_id])

# Observation dataset settings
settings = ObservationSettings(
  observation_path="abfss://green_tripdata_2020-04.csv",    # Path to your observation data
  event_timestamp_column="lpep_dropoff_datetime",           # Event timepstamp field for your data, optional
  timestamp_format="yyyy-MM-dd HH:mm:ss")                   # Event timestamp format， optional

# Prepare training data by joining features to the input (observation) data.
# feature-join.conf and features.conf are detected and used automatically.
feathr_client.get_offline_features(observation_settings=settings,
                                   output_path="abfss://output.avro",
                                   feature_query=feature_query)
```

## Deploy Features to Online (Redis) Store

```python
from feathr import FeathrClient, BackfillTime, MaterializationSettings, RedisSink

client = FeathrClient()
redisSink = RedisSink(table_name="nycTaxiDemoFeature")
# Materialize two features into a redis table.
settings = MaterializationSettings("nycTaxiMaterializationJob",
sinks=[redisSink],
feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)

```

Get features from online store:

```python
from feathr import FeathrClient
client = FeathrClient()
# Get features for a locationId (key)
client.get_online_features(feature_table = "agg_features",
                           key = "265",
                           feature_names = ['f_location_avg_fare', 'f_location_max_fare'])
# Batch get for multiple locationIds (keys)
client.multi_get_online_features(feature_table = "agg_features",
                                 key = ["239", "265"],
                                 feature_names = ['f_location_avg_fare', 'f_location_max_fare'])
```

# More on Defining Features

## Defining Window Aggregation Features

```python
agg_features = [Feature(name="f_location_avg_fare",
                        key=location_id,                          # Query/join key of the feature(group)
                        feature_type=FLOAT,
                        transform=WindowAggTransformation(        # Window Aggregation transformation
                            agg_expr="cast_float(fare_amount)",
                            agg_func="AVG",                       # Apply average aggregation over the window
                            window="90d")),                       # Over a 90-day window
                ]

agg_anchor = FeatureAnchor(name="aggregationFeatures",
                           source=batch_source,
                           features=agg_features)
```

## Defining Named Data Sources

```python
batch_source = HdfsSource(
    name="nycTaxiBatchSource",                              # Source name to enrich your metadata
    path="abfss://green_tripdata_2020-04.csv",              # Path to your data
    event_timestamp_column="lpep_dropoff_datetime",         # Event timestamp for point-in-time correctness
    timestamp_format="yyyy-MM-dd HH:mm:ss")                 # Supports various fromats inculding epoch
```

## Defining Preprocessing on Source
If you need some complex transformation that can be done with provided transformations, you can use source preprocessing
to achieve your goal.

The preprocessing takes in the DataFrame loaded by the source and then return a preprocessed DataFrame by the preprocessing
function. The preprocessing function can't refer to other functions or dependencies that are not provided in the Spark cluster.

```python
def my_preprocessing(df: DataFrame) -> DataFrame:
    df = df.withColumn("new_improvement_surcharge", col("improvement_surcharge") + 1000000)
    df = df.withColumn("new_tip_amount", col("tip_amount") + 1000000)
    df = df.withColumn("new_lpep_pickup_datetime", col("lpep_pickup_datetime"))

    return df

batch_source = HdfsSource(
    name="nycTaxiBatchSource",                              # Source name to enrich your metadata
    path="abfss://green_tripdata_2020-04.csv",              # Path to your data
    preprocessing=my_preprocessing,
    event_timestamp_column="lpep_dropoff_datetime",         # Event timestamp for point-in-time correctness
    timestamp_format="yyyy-MM-dd HH:mm:ss")                 # Supports various fromats inculding epoch
```

## Define features on top of other features - Derived Features

```python
# Compute a new feature(a.k.a. derived feature) on top of an existing feature
derived_feature = DerivedFeature(name="f_trip_time_distance",
                                 feature_type=FLOAT,
                                 key=trip_key,
                                 input_features=[f_trip_distance, f_trip_time_duration],
                                 transform="f_trip_distance * f_trip_time_duration")

# Another example to compute embedding similarity
user_embedding = Feature(name="user_embedding", feature_type=DENSE_VECTOR, key=user_key)
item_embedding = Feature(name="item_embedding", feature_type=DENSE_VECTOR, key=item_key)

user_item_similarity = DerivedFeature(name="user_item_similarity",
                                      feature_type=FLOAT,
                                      key=[user_key, item_key],
                                      input_features=[user_embedding, item_embedding],
                                      transform="cosine_similarity(user_embedding, item_embedding)")
```

## Define Streaming Features

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

## Cloud Architecture

Feathr has native integration with Azure and other cloud services, and here's the high-level architecture to help you get started.
![Architecture](images/architecture.png)

# Next Steps

## Quickstart

- [Quickstart](quickstart.md)

## Concepts

- [Feature Definition](concepts/feature-definition.md)
- [Feature Generation](concepts/feature-generation.md)
- [Feature Join](concepts/feature-join.md)
- [Point-in-time Correctness](concepts/point-in-time-join.md)

## How-to-guides

- [Azure Deployment](how-to-guides/azure-deployment.md)
- [Local Feature Testing](how-to-guides/local-feature-testing.md)
- [Feature Definition Troubleshooting Guide](how-to-guides/troubleshoot-feature-definition.md)
- [Feathr Expression Language](how-to-guides/expression-language.md)
- [Feathr Job Configuration](how-to-guides/feathr_job_configuration.md)

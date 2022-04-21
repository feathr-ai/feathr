# Feathr – An Enterprise-Grade, High Performance Feature Store

## What is Feathr?

Feathr lets you:

- **Define features** based on raw data sources, including time-series data, using simple APIs.
- **Get those features by their names** during model training and model inferencing.
- **Share features** across your team and company.

Feathr automatically computes your feature values and joins them to your training data, using point-in-time-correct semantics to avoid data leakage, and supports materializing and deploying your features for use online in production.

For more details, read our [documentation](https://linkedin.github.io/feathr/). We also have two blog posts talking about [Open Sourcing Feathr](https://engineering.linkedin.com/blog/2022/open-sourcing-feathr---linkedin-s-feature-store-for-productive-m) and [Feathr on Azure](https://azure.microsoft.com/en-us/blog/feathr-linkedin-s-feature-store-is-now-available-on-azure/).

## Running Feathr on Azure with 3 Simple Steps

Feathr has native cloud integration. To use Feathr on Azure, you only need three steps:

1. Get the `Principal ID` of your account by running `az ad signed-in-user show --query objectId -o tsv` in the link below (Select "Bash" if asked), and write down that value (something like `b65ef2e0-42b8-44a7-9b55-abbccddeefff`). Think this ID as something representing you when accessing Azure, and it will be used to grant permissions in the next step in the UI.

[Launch Cloud Shell](https://shell.azure.com/bash)

2. Click the button below to deploy a minimal set of Feathr resources for demo purpose. You will need to fill in the `Principal ID` and `Resource Prefix`. You will need "Owner" permission of the selected subscription.

[![Deploy to Azure](https://aka.ms/deploytoazurebutton)](https://portal.azure.com/#create/Microsoft.Template/uri/https%3A%2F%2Fraw.githubusercontent.com%2Flinkedin%2Ffeathr%2Fmain%2Fdocs%2Fhow-to-guides%2Fazure_resource_provision.json)

3. Run the Feathr Jupyter Notebook by clicking the button below. You only need to change the specified `Resource Prefix`.

[![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/gh/linkedin/feathr/main?labpath=feathr_project%2Ffeathrcli%2Fdata%2Ffeathr_user_workspace%2Fnyc_driver_demo.ipynb)

## Installing Feathr Client Locally

If you are not using the above Jupyter Notebook and want to install Feathr client locally, use this:

```bash
pip install -U feathr
```

Or use the latest code from GitHub:

```bash
pip install git+https://github.com/linkedin/feathr.git#subdirectory=feathr_project
```

## Feathr Highlights

### Defining Features with Transformation

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

### Rich UDF Support

Feathr has highly customizable UDFs with native PySpark and Spark SQL integration to lower learning curve for data scientists:

```python
def add_new_dropoff_and_fare_amount_column(df: DataFrame):
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    df = df.withColumn("fare_amount_cents", df.fare_amount.cast('double') * 100)
    return df

batch_source = HdfsSource(name="nycTaxiBatchSource",
                        path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                        preprocessing=add_new_dropoff_and_fare_amount_column,
                        event_timestamp_column="new_lpep_dropoff_datetime",
                        timestamp_format="yyyy-MM-dd HH:mm:ss")
```

### Accessing Features

```python
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

### Deploy Features to Online (Redis) Store

```python
client = FeathrClient()
redisSink = RedisSink(table_name="nycTaxiDemoFeature")
# Materialize two features into a redis table.
settings = MaterializationSettings("nycTaxiMaterializationJob",
sinks=[redisSink],
feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)
```

And get features from online store:

```python
# Get features for a locationId (key)
client.get_online_features(feature_table = "agg_features",
                           key = "265",
                           feature_names = ['f_location_avg_fare', 'f_location_max_fare'])
# Batch get for multiple locationIds (keys)
client.multi_get_online_features(feature_table = "agg_features",
                                 key = ["239", "265"],
                                 feature_names = ['f_location_avg_fare', 'f_location_max_fare'])
```

### Defining Window Aggregation Features

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

### Defining Named Data Sources

```python
batch_source = HdfsSource(
    name="nycTaxiBatchSource",                              # Source name to enrich your metadata
    path="abfss://green_tripdata_2020-04.csv",              # Path to your data
    event_timestamp_column="lpep_dropoff_datetime",         # Event timestamp for point-in-time correctness
    timestamp_format="yyyy-MM-dd HH:mm:ss")                 # Supports various fromats inculding epoch
```

### Define features on top of other features - Derived Features

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

### Define Streaming Features

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

## Running Feathr Examples

Follow the [quick start Jupyter Notebook](./feathr_project/feathrcli/data/feathr_user_workspace/nyc_driver_demo.ipynb) to try it out. There is also a companion [quick start guide](./docs/quickstart.md) containing a bit more explanation on the notebook.

## Cloud Integrations

| Feathr component             | Cloud Integrations                                                          |
| ---------------------------- | --------------------------------------------------------------------------- |
| Offline store – Object Store | Azure Blob Storage, Azure ADLS Gen2, AWS S3                                 |
| Offline store – SQL          | Azure SQL DB, Azure Synapse Dedicated SQL Pools, Azure SQL in VM, Snowflake |
| Online store                 | Azure Cache for Redis                                                       |
| Feature Registry             | Azure Purview                                                               |
| Compute Engine               | Azure Synapse Spark Pools, Databricks                                       |
| Machine Learning Platform    | Azure Machine Learning, Jupyter Notebook                                    |
| File Format                  | Parquet, ORC, Avro, Delta Lake                                              |

## Roadmap

> `Public Preview` release may introduce API changes.

- [x] Private Preview release
- [x] Public Preview release
- [ ] Future release
  - [x] Support streaming
  - [x] Support common data sources
  - [ ] Support online transformation
  - [ ] Support feature versioning

## Community Guidelines

Build for the community and build by the community. Check out [Community Guidelines](CONTRIBUTING.md).

## Slack Channel

Join our [Slack channel](https://feathrai.slack.com) for questions and discussions (or click the [invitation link](https://join.slack.com/t/feathrai/shared_invite/zt-17lugq6e8-Qu3KJXDA25tZqlFsmM94Dg)).

# Feathr – An Enterprise-Grade, High Performance Feature Store

## What is Feathr?

Feathr lets you:

- **define features** based on raw data sources, including time-series data, using simple APIs.
- **get those features by their names** during model training and model inferencing.
- **share features** across your team and company.

Feathr automatically computes your feature values and joins them to your training data, using point-in-time-correct semantics to avoid data leakage, and supports materializing and deploying your features for use online in production.

## Installation

Install Feathr using pip:

```bash
pip install -U feathr
```

Or if you want to use the latest Feathr code from GitHub:

```bash
pip install git+https://github.com/linkedin/feathr.git#subdirectory=feathr_project
```

## Setup Feathr on Azure Resources

You only need two steps:
1. Get the principal ID of your account by running `az ad signed-in-user show --query objectId -o tsv` in the link below (Select "Bash" if you are asked to choose one), and write down that value (will be something like `b65ef2e0-42b8-44a7-9b55-abbccddeefff`)


[![Launch Cloud Shell](https://shell.azure.com/images/launchcloudshell.png "Launch Cloud Shell")](https://shell.azure.com/bash)

2. Click the button below to deploy a minimal set of Feathr resources. Note that you should have "Owner" access in your subscription to perform some of the actions, and if you don't, please ask your IT admin to use [this quick start guide](./docs/how-to-guides/azure-deployment.md) to provision a service principal for you to use.

[![Deploy to Azure](https://aka.ms/deploytoazurebutton)](https://portal.azure.com/#create/Microsoft.Template/uri/https%3A%2F%2Fraw.githubusercontent.com%2Flinkedin%2Ffeathr%2Fone_click_deployment%2Fdocs%2Fhow-to-guides%2Fazure_resource_provision.json)


## Running Feathr Examples

- Follow the [quick start Jupyter Notebook](./feathr_project/feathrcli/data/feathr_user_workspace/nyc_driver_demo.ipynb) to try it out. There is also a companion [quick start guide](./docs/quickstart.md) containing a bit more explanation on the notebook. 
- We recommend using **Visual Studio Code**, or **Azure Machine Learning Service** to run the above notebook, since those environments will help you login and reterive necessary credentials.

## Documentation

For more details, read our [documentation](https://linkedin.github.io/feathr/).


## Feathr Capabilities
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

### Accessing Features

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

### Deploy Features to Online (Redis) Store

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

And get features from online store:

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

### Beyond Features on Raw Data Sources - Derived Features

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

## Cloud Integrations

| Feathr component             | Cloud Integrations                                                                            |
| ---------------------------- | --------------------------------------------------------------------------------------------- |
| Offline store – Object Store | Azure Blob Storage, Azure ADLS Gen2, AWS S3                                                   |
| Offline store – SQL          | Azure SQL DB, Azure Synapse Dedicated SQL Pools (formerly SQL DW), Azure SQL in VM, Snowflake |
| Online store                 | Azure Cache for Redis                                                                         |
| Feature Registry             | Azure Purview                                                                                 |
| Compute Engine               | Azure Synapse Spark Pools, Databricks                                                         |
| Machine Learning Platform    | Azure Machine Learning, Jupyter Notebook                                                      |
| File Format                  | Parquet, ORC, Avro, Delta Lake                                                                |

## Roadmap

> `Public Preview` release may introduce API changes.

- [x] Private Preview release
- [x] Public Preview release
- [ ] Future release
  - [ ] Support streaming and online transformation
  - [ ] Support feature versioning
  - [ ] Support more data sources

## Community Guidelines

Build for the community and build by the community. Check out [Community Guidelines](CONTRIBUTING.md).

## Slack Channel

Join our [Slack channel](https://feathrai.slack.com) for questions and discussions (or click the [invitation link](https://join.slack.com/t/feathrai/shared_invite/zt-14sxrbacj-7qo2bKL0LVG~4m0Z8gytZQ)).

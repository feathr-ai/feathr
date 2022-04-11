# Feathr – An Enterprise-Grade, High Performance Feature Store

## What is Feathr?

Feathr lets you:

- **define features** based on raw data sources, including time-series data, using simple APIs.
- **get those features by their names** during model training and model inferencing.
- **share features** across your team and company.

Feathr automatically computes your feature values and joins them to your training data, using point-in-time-correct semantics to avoid data leakage, and supports materializing and deploying your features for use online in production.

## Running Feathr with 3 Simple Steps

Feathr has native cloud integration and getting started with Feathr is very straightforward. You only need three steps:

1. Get the principal ID of your account by running `az ad signed-in-user show --query objectId -o tsv` in the link below (Select "Bash" if you are asked), and write down that value (something like `b65ef2e0-42b8-44a7-9b55-abbccddeefff`)

[Launch Cloud Shell](https://shell.azure.com/bash)

2. Click the button below to deploy a minimal set of Feathr resources for demo purpose.

[![Deploy to Azure](https://aka.ms/deploytoazurebutton)](https://portal.azure.com/#create/Microsoft.Template/uri/https%3A%2F%2Fraw.githubusercontent.com%2Flinkedin%2Ffeathr%2Fone_click_deployment%2Fdocs%2Fhow-to-guides%2Fazure_resource_provision.json)

3. Run the Feathr Jupyter Notebook by clicking the button below. You only need to fill in the resource prefix.

[![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/gh/linkedin/feathr/main?labpath=feathr_project%2Ffeathrcli%2Fdata%2Ffeathr_user_workspace%2Fnyc_driver_demo.ipynb)

## Feathr Installation

Install Feathr using pip:

```bash
pip install -U feathr
```

Or if you want to use the latest Feathr code from GitHub:

```bash
pip install git+https://github.com/linkedin/feathr.git#subdirectory=feathr_project
```

## Running Feathr Examples

- Follow the [quick start Jupyter Notebook](./feathr_project/feathrcli/data/feathr_user_workspace/nyc_driver_demo.ipynb) to try it out. There is also a companion [quick start guide](./docs/quickstart.md) containing a bit more explanation on the notebook.
- We recommend using **Visual Studio Code**, or **Azure Machine Learning Service** to run the above notebook, since those environments will help you login and reterive necessary credentials.

## Documentation

For more details, read our [documentation](https://linkedin.github.io/feathr/).

## Feathr Highlights

For more capabilities on Feathr, please refer to [Feathr Capabilities](./docs/concepts/feathr-capabilities.md)

### Rich UDF Support

Highly customizable UDFs with native PySpark and Spark SQL to lower learning curve for data scientists:

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

### Build Features on Top of Other Features

Feathr allows users to build features on top of existing features to encourage feature reuse across organizations.

```python
# Compute a new feature(a.k.a. derived feature) on top of an existing feature
derived_feature = DerivedFeature(name="f_trip_time_distance",
                                 feature_type=FLOAT,
                                 key=trip_key,
                                 input_features=[f_trip_distance, f_trip_time_duration],
                                 transform="f_trip_distance * f_trip_time_duration")
```

### ML Native Type System
Feathr has rich type system including support for embeddings for advanced ML/DL scenarios

```python
# Another example to compute embedding similarity
user_embedding = Feature(name="user_embedding", feature_type=DENSE_VECTOR, key=user_key)
item_embedding = Feature(name="item_embedding", feature_type=DENSE_VECTOR, key=item_key)

user_item_similarity = DerivedFeature(name="user_item_similarity",
                                      feature_type=FLOAT,
                                      key=[user_key, item_key],
                                      input_features=[user_embedding, item_embedding],
                                      transform="cosine_similarity(user_embedding, item_embedding)")
```

### Rich Support for Point-in-time Joins and Aggregations

Feathr has performant built-in operators designed for feature store, including point in time joins, time-aware sliding window aggregation, look up features, all with point-in-time correctness.


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
  - [ ] Support streaming and online transformation
  - [ ] Support feature versioning
  - [ ] Support more data sources

## Community Guidelines

Build for the community and build by the community. Check out [Community Guidelines](CONTRIBUTING.md).

## Slack Channel

Join our [Slack channel](https://feathrai.slack.com) for questions and discussions (or click the [invitation link](https://join.slack.com/t/feathrai/shared_invite/zt-14sxrbacj-7qo2bKL0LVG~4m0Z8gytZQ)).

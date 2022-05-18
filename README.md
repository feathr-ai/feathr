# Feathr – An Enterprise-Grade, High Performance Feature Store

## What is Feathr?

Feathr is the feature store that is used in production in LinkedIn for many years and was open sourced in April 2022. Read our announcement on [Open Sourcing Feathr](https://engineering.linkedin.com/blog/2022/open-sourcing-feathr---linkedin-s-feature-store-for-productive-m) and [Feathr on Azure](https://azure.microsoft.com/en-us/blog/feathr-linkedin-s-feature-store-is-now-available-on-azure/).

Feathr lets you:

- **Define features** based on raw data sources (batch and streaming) using pythonic APIs.
- **Register and get features by names** during model training and model inferencing.
- **Share features** across your team and company.

Feathr automatically computes your feature values and joins them to your training data, using point-in-time-correct semantics to avoid data leakage, and supports materializing and deploying your features for use online in production.

## Feathr Highlights

- **Scalable with built-in optimizations.** For example, based on some internal use case, Feathr can process billions of rows and PB scale data with built-in optimizations such as bloom filters and salted joins.
- **Rich support for point-in-time joins and aggregations:** Feathr has high performant built-in operators designed for Feature Store, including time-based aggregation, sliding window joins, look-up features, all with point-in-time correctness.
- **Highly customizable user-defined functions (UDFs)** with native PySpark and Spark SQL support to lower the learning curve for data scientists.
- **Pythonic APIs** to access everything with low learning curve; Integrated with model building so data scientists can be productive from day one.
- **Rich type system** including support for embeddings for advanced machine learning/deep learning scenarios. One of the common use cases is to build embeddings for customer profiles, and those embeddings can be reused across an organization in all the machine learning applications.
- **Native cloud integration** with simplified and scalable architecture, which is illustrated in the next section.
- **Feature sharing and reuse made easy:** Feathr has built-in feature registry so that features can be easily shared across different teams and boost team productivity.

## Documentation

For more details on Feathr, read our [documentation](https://linkedin.github.io/feathr/). For Python API references, read the [Python API Reference](https://feathr.readthedocs.io/).

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

## Feathr Examples

Please read [Feathr Capabilities](https://linkedin.github.io/feathr/concepts/feathr-capabilities.html) for more examples. Below are a few selected ones:

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

Read the [Streaming Source Ingestion Guide](https://linkedin.github.io/feathr/how-to-guides/streaming-source-ingestion.html) for more details.


### Point in Time Joins

Read [Point-in-time Correctness and Point-in-time Join in Feathr](https://linkedin.github.io/feathr/concepts/point-in-time-join.html) for more details.


## Running Feathr Examples

Follow the [quick start Jupyter Notebook](./feathr_project/feathrcli/data/feathr_user_workspace/nyc_driver_demo.ipynb) to try it out. There is also a companion [quick start guide](https://linkedin.github.io/feathr/quickstart.html) containing a bit more explanation on the notebook.

## Cloud Integrations

| Feathr component             | Cloud Integrations                                                          |
| ---------------------------- | --------------------------------------------------------------------------- |
| Offline store – Object Store | Azure Blob Storage, Azure ADLS Gen2, AWS S3                                 |
| Offline store – SQL          | Azure SQL DB, Azure Synapse Dedicated SQL Pools, Azure SQL in VM, Snowflake |
| Streaming Source             | Kafka, EventHub                                                                 |
| Online store                 | Azure Cache for Redis                                                       |
| Feature Registry             | Azure Purview                                                               |
| Compute Engine               | Azure Synapse Spark Pools, Databricks                                       |
| Machine Learning Platform    | Azure Machine Learning, Jupyter Notebook                                    |
| File Format                  | Parquet, ORC, Avro, Delta Lake                                              |

## Roadmap

- [x] Private Preview release
- [x] Public Preview release
- [ ] Future release
  - [x] Support streaming
  - [x] Support common data sources
  - [ ] Support online transformation
  - [ ] Support feature versioning
  - [ ] Support feature monitoring
  - [ ] Support feature store UI
      - [ ] Lineage
      - [ ] Search
  - [ ] Support feature data deletion and rentenion

## Community Guidelines

Build for the community and build by the community. Check out [Community Guidelines](CONTRIBUTING.md).

## Slack Channel

Join our [Slack channel](https://feathrai.slack.com) for questions and discussions (or click the [invitation link](https://join.slack.com/t/feathrai/shared_invite/zt-17lugq6e8-Qu3KJXDA25tZqlFsmM94Dg)).

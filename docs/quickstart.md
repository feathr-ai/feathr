# Quickstart

This tutorial demonstrates the key capabilities of Feathr, including:
1. Install and set up a feature store on Azure Cloud.
2. Create shareable features with Feathr feature definition configs.
3. Create a training dataset via point-in-time feature join.
4. Materialize feature value to online store.
5. Fetch feature value in real-time from online store for online scoring.

You can run this tutorial in Jupyter notebook locally, by following guide:


## Overview

In this tutorial, we use Feathr Feature Store to create a model that predicts NYC Yellow Taxi fares.
The dataset comes from [here](https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page).

The major problems Feathr solve for the use case are:

1. Create, share and manage useful features from raw source data.
2. Provide Point-in-time feature join to create training dataset to ensure no data leakage.
3. Deploy the same feature data to online store to eliminate training and inference data skew.

## Provision cloud resources

Please follow the steps [here](./how-to-guides/azure-deployment.md) to provision required cloud resources. Due to the complexity of the possible cloud environment, it is almost impossible to create a script that works for all the use cases. Because of this, [azure_resource_provision.sh](./how-to-guides/azure_resource_provision.sh) is a full end to end command line to create all the required resources, and you can tailor the script as needed, while [the companion documentation](./how-to-guides/azure-deployment.md) can be used as a complete guide for using that shell script.


## Step 1: Install Feathr

Install the Feathr CLI using pip:

```bash
pip install feathr
```

## Step 2: Create a feature workspace

```bash
feathr init
cd feathr_user_workspace

```

Output:
```bash
Creating a workspace and some default example config files and mock data ...
Feathr initialization completed.
```

In this created workspace, it has the following structure:
* `feathr_config.yaml` contains the feature store configurations
* `feature_conf` contains the feature definition written in Feathr feature config
* `feature_gen_conf` contains the feature generation/materialization config
* `feature_join_conf` contains the feature join/training dataset creation config
* `mockdata` contains the mockdata for the demo
* `nyc_driver_demo.ipynb` contains the demo notebook
* `notebook_resource`	contains other resource for the demo notebook


feathr_config.yaml:
```yaml
# DO NOT MOVE OR DELETE THIS FILE

# version of API settings
api_version: 1
project_config:
  project_name: "feathr_getting_started"
  # Information that are required to be set via environment variables.
  required_environment_variables:
    # Redis password for your online store
    - "REDIS_PASSWORD"
    # Client IDs and Client Secret for the service principal. Read the getting started docs on how to get those information.
    - "AZURE_CLIENT_ID"
    - "AZURE_TENANT_ID"
    - "AZURE_CLIENT_SECRET"


offline_store:
...

spark_config:
...

online_store:
...

feature_registry:
...

```

Credentials need to be set up in the environment variables so Feathr CLI or FeathrClient can successfully manage the resources. For examle, with the default setup, we need Redis and Azure Synapse. Then REDIS_PASSWORD, AZURE_CLIENT_ID, AZURE_TENANT_ID and AZURE_CLIENT_SECRET are required. So we can set it up via:
```
export REDIS_PASSWORD='...'
export AZURE_CLIENT_ID='...'
export AZURE_TENANT_ID='...'
export AZURE_CLIENT_SECRET='...'
```

## Step 3: Create features in the workspace

In Feathr, a feature is viewed as a function, mapping from entity id or key, and timestamp to a feature value.
1) The entity key (a.k.a. entity id) identifies the subject of feature, e.g. a user id, 123.
2) The feature name is the aspect of the entity that the feature is indicating, e.g. the age of the user.
3) The feature value is the actual value of that aspect at a particular time, e.g. the value is 30 at year 2022.

Note that, in some cases, such as features defined on top of request data, may have no entity key or timestamp.
It is merely a function/transformation executing against request data at runtime.
For example, the day of week of the request, which is calculated by converting the request UNIX timestamp.

Feature definitions:
```python
# anchored features
# Define the key for your feature
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

# aggregation feature
# Define the key for your feature
location_id = TypedKey(key_column="DOLocationID",
                       key_column_type=ValueType.INT32,
                       description="location id in NYC",
                       full_name="nyc_taxi.location_id")

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

# derived features
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

# And the source
batch_source = HdfsSource(
    name="nycTaxiBatchSource",                              # Source name to enrich your metadata
    path="abfss://green_tripdata_2020-04.csv",              # Path to your data
    event_timestamp_column="lpep_dropoff_datetime",         # Event timestamp for point-in-time correctness
    timestamp_format="yyyy-MM-dd HH:mm:ss")                 # Supports various fromats inculding epoch
```


## Step 4: Register feature definitions to the central registry

The `register` command will scan all the feature definition config files under `feature_conf` and register all features 
within these files to central registry on Azure, so that they can be discovered later.

```bash
feathr register
```

Or with Python:
```python
from feathr.client import FeathrClient

client = FeathrClient()
client.register_features()
```

## Step 5: Create training data using point-in-time correct feature join

A training dataset usually contains entity id columns, multiple feature columns, event timestamp
column and label/target column. <br/>

To create a training dataset using Feathr, one needs to provide a feature join configuration file to specify 
what features and how these features should be joined to the observation data. The feature join config file mainly contains: <br/>
1) The path of a dataset as the 'spine' for the to-be-created training dataset. We call this input 'spine' dataset the 'observation'
   dataset. Typically, each row of the observation data contains: <br/>
    a) Column(s) representing entity id(s), which will be used as the join key to look up(join) feature value. <br/>
    b) A column representing the event time of the row. By default, Feathr will make sure the feature values joined have 
        a timestamp earlier than it, ensuring no data leakage in the resulting training dataset. <br/>
    c) Other columns will be simply pass through onto the output training dataset. 
2) The key fields from the observation data, which are used to joined with the feature data. 
3) List of feature names to be joined with the observation data. The features must be defined in the feature 
   definition configs.
4) The time information of the observation data used to compare with the feature's timestamp during the join.

Create training dataset via feature join:
```bash
feathr join
```
Or with Python:
```python
returned_spark_job = client.join_offline_features()
df_res = client.get_job_result()
```

The following feature join config is used:
```python
# Requested features to be joined 
feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=["DOLocationID"])

# Observation dataset settings
settings = ObservationSettings(
    observation_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",   
    event_timestamp_column="lpep_dropoff_datetime",           # Event timepstamp field for your data, optional
    timestamp_format="yyyy-MM-dd HH:mm:ss")                   # Event timestamp format

# Request the data for offline training
feathr_client.get_offline_features(observation_settings=settings,
                                   output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro",
                                   feature_query=feature_query)
```

## Step 6: Materialize feature value into offline/online storage
While Feathr can compute the feature value from the feature definition on-the-fly at request time, it can also pre-compute 
and materialize the feature value to offline and/or online storage. <br/>
The `deploy` command line call will materialize feature value according to the feature generation config under feature_gen_conf.

```bash
feathr deploy
```
Or in Python:
```python
job_res = client.materialize_features()
```
The following feature generation config is used to materialize feature value to Redis:

```
operational: {
  name: generateWithDefaultParams
  endTime: 2021-01-02
  endTimeFormat: "yyyy-MM-dd"
  resolution: DAILY
  output:[
  {
      name: REDIS
      params: {
        table_name: "nycTaxiDemoFeature"
      }
   }
  ]
}
features: [f_location_avg_fare, f_location_max_fare]
```

## Step 7: Fetching feature value for online inference
For features that are already materialized by the previous step, their latest value can be queried via the client's
`online_get_features` or `online_batch_get_features` API.

```python
client.online_get_features("nycTaxiDemoFeature", "265", ['f_location_avg_fare', 'f_location_max_fare'])
client.online_batch_get_features("nycTaxiDemoFeature", ["239", "265"], ['f_location_avg_fare', 'f_location_max_fare'])
```

## Next steps
* Run the demo notebook to understand the workflow of Feathr: `jupyter notebook`.
* Read the [Concepts](concepts/) page to understand the Feathr abstractions.
* Read this guide to understand [how to setup Feathr on Azure cloud](../how-to-guides/azure-deployment.md).

# Quickstart

This tutorial demonstrates the key capabilities of Feathr, including:

1. Install and set up Feathr with Azure
2. Create shareable features with Feathr feature definition configs.
3. Create a training dataset via point-in-time feature join.
4. Materialize feature value to online store.
5. Fetch feature value in real-time from online store for online scoring.

You can run this tutorial in Jupyter notebook locally, by following this guide.

## Overview

In this tutorial, we use Feathr Feature Store to create a model that predicts NYC Yellow Taxi fares.
The dataset comes from [here](https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page).

The major problems Feathr solves are:

1. Create, share and manage useful features from raw source data.
2. Provide Point-in-time feature join to create training dataset to ensure no data leakage.
3. Deploy the same feature data to online store to eliminate training and inference data skew.

## Step 1: Provision cloud resources

First step is to provision required cloud resources if you want to use Feathr. Feathr provides a python based client to interact with cloud resources.

Please follow the steps [here](./how-to-guides/azure-deployment.md) to provision required cloud resources. Due to the complexity of the possible cloud environment, it is almost impossible to create a script that works for all the use cases. Because of this, [azure_resource_provision.sh](./how-to-guides/azure_resource_provision.sh) is a full end to end command line to create all the required resources, and you can tailor the script as needed, while [the companion documentation](./how-to-guides/azure-deployment.md) can be used as a complete guide for using that shell script.

At the end of the script, it should give you some output which you will need later. For example, the Service Principal IDs, Redis endpoint, etc.

Please also note that at the end of this step, you need to **manually** grant your service principal "Data Curator" permission of your Azure Purview account, due to a current limiation with Azure Purview.

## Step 2: Install Feathr

Install the Feathr CLI using pip:

```bash
pip install -U feathr
```

## Step 3: Create a Feathr workspace

```bash
feathr init
cd feathr_user_workspace

```

In this created workspace, it has the following structure:

- `feathr_config.yaml` contains the feature store configurations
- `feature_conf` contains the feature definition written in Feathr feature config
- `feature_gen_conf` contains the feature generation/materialization config
- `feature_join_conf` contains the feature join/training dataset creation config
- `mockdata` contains the mockdata for the demo
- `nyc_driver_demo.ipynb` contains the demo notebook

# Step 4: Update Feathr config

Feathr relies on `feathr_config.yaml` to get all the required configurations. For example, you might want to update the Synapse workspace, the workspace directory, etc., based on your cloud environment:

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
---
spark_config:
---
online_store:
---
feature_registry:
```

# Step 5: Turn on the type system initialization

In the above `feathr_config.yaml` file, set `type_system_initialization` to `true` to initialize the type system in Azure Purview. This is a manual step and only need to be called once (i.e. you can turn it to `false` as long as you are using the same Azure Purview endpoint).

# Step 6: Use the notebook as a starting point

There should be a notebook called `nyc_driver_demo.ipynb` in the Feathr workspace, which you can use as a getting started template.

In the notebook, first setup a few environment variables, such as REDIS_PASSWORD, AZURE_CLIENT_ID, AZURE_TENANT_ID and AZURE_CLIENT_SECRET:

```python
import os
os.environ['REDIS_PASSWORD'] = ''
os.environ['AZURE_CLIENT_ID'] = ''
os.environ['AZURE_TENANT_ID'] = ''
os.environ['AZURE_CLIENT_SECRET'] = ''
```

## Step 7: Create features with the Python APIs

In Feathr, a feature is viewed as a function, mapping from entity id or key, and timestamp to a feature value.

1. The entity key (a.k.a. entity id) identifies the subject of feature, e.g. a user id, 123.
2. The feature name is the aspect of the entity that the feature is indicating, e.g. the age of the user.
3. The feature value is the actual value of that aspect at a particular time, e.g. the value is 30 at year 2022.

Note that, in some cases, such as features defined on top of request data, may have no entity key or timestamp.
It is merely a function/transformation executing against request data at runtime.
For example, the day of week of the request, which is calculated by converting the request UNIX timestamp.

Feature definitions:

```python
features = [
    Feature(name="trip_distance", feature_type=FLOAT),
    Feature(name="f_is_long_trip_distance",
            feature_type=BOOLEAN,
            transform=ExpressionTransformation("trip_distance>30")),
]

anchor = FeatureAnchor(name="nonAggFeatures",
                                batch_source=PASSTHROUGH_SOURCE,
                                features=features)
```

## Step 8: Register feature definitions to the central registry

```python
from feathr.client import FeathrClient

client = FeathrClient()
client.register_features()
```

## Step 9: Create training data using point-in-time correct feature join

A training dataset usually contains entity id columns, multiple feature columns, event timestamp
column and label/target column. <br/>

To create a training dataset using Feathr, one needs to provide a feature join configuration file to specify
what features and how these features should be joined to the observation data. The feature join config file mainly contains: <br/>

1. The path of a dataset as the 'spine' for the to-be-created training dataset. We call this input 'spine' dataset the 'observation'
   dataset. Typically, each row of the observation data contains: <br/>
   a) Column(s) representing entity id(s), which will be used as the join key to look up(join) feature value. <br/>
   b) A column representing the event time of the row. By default, Feathr will make sure the feature values joined have
   a timestamp earlier than it, ensuring no data leakage in the resulting training dataset. <br/>
   c) Other columns will be simply pass through onto the output training dataset.
2. The key fields from the observation data, which are used to joined with the feature data.
3. List of feature names to be joined with the observation data. The features must be defined in the feature
   definition configs.
4. The time information of the observation data used to compare with the feature's timestamp during the join.

Create training dataset via feature join:

```python
returned_spark_job = client.join_offline_features()
df_res = client.get_job_result()
```

The following feature join config is used:

```python
feature_query = [FeatureQuery(feature_list=["f_location_avg_fare"], key=["DOLocationID"])]
        settings = ObservationSettings(
            observation_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
            output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro",
            event_timestamp_column="lpep_dropoff_datetime", timestamp_format="yyyy-MM-dd HH:mm:ss")
client.get_offline_features(feature_query=feature_query, observation_settings=settings)
```

## Step 10: Materialize feature value into offline/online storage

While Feathr can compute the feature value from the feature definition on-the-fly at request time, it can also pre-compute
and materialize the feature value to offline and/or online storage. 

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

## Step 11: Fetching feature value for online inference

For features that are already materialized by the previous step, their latest value can be queried via the client's
`get_online_features` or `multi_get_online_features` API.

```python
client.get_online_features("nycTaxiDemoFeature", "265", ['f_location_avg_fare', 'f_location_max_fare'])
client.multi_get_online_features("nycTaxiDemoFeature", ["239", "265"], ['f_location_avg_fare', 'f_location_max_fare'])
```

## Next steps

- Run the demo notebook to understand the workflow of Feathr: `jupyter notebook`.
- Read the [Concepts](./concepts/) page to understand the Feathr abstractions.
- Read guide to understand [how to setup Feathr on Azure](../how-to-guides/azure-deployment.md).

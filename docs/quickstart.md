---
layout: default
title: Feathr Quick Start Guide
nav_order: 2
---

# Feathr Quickstart Guide

## Overview

In this tutorial, we use Feathr Feature Store to create a model that predicts NYC Yellow Taxi fares.
The dataset comes from [here](https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page).

The major problems Feathr solves are:

1. Create, share and manage useful features from raw source data.
2. Provide Point-in-time feature join to create training dataset to ensure no data leakage.
3. Deploy the same feature data to online store to eliminate training and inference data skew.

## Step 1: Provision cloud resources

First step is to provision required cloud resources if you want to use Feathr. Feathr provides a python based client to interact with cloud resources.

Feathr has native cloud integration. To use Feathr on Azure, you only need three steps:

1. Get the `Principal ID` of your account by running `az ad signed-in-user show --query objectId -o tsv` in the link below (Select "Bash" if asked), and write down that value (something like `b65ef2e0-42b8-44a7-9b55-abbccddeefff`). Think this ID as something representing you when accessing Azure, and it will be used to grant permissions in the next step in the UI.

[Launch Cloud Shell](https://shell.azure.com/bash)

2. Click the button below to deploy a minimal set of Feathr resources for demo purpose. You will need to fill in the `Principal ID` and `Resource Prefix`. You will need "Owner" permission of the selected subscription.

[![Deploy to Azure](https://aka.ms/deploytoazurebutton)](https://portal.azure.com/#create/Microsoft.Template/uri/https%3A%2F%2Fraw.githubusercontent.com%2Flinkedin%2Ffeathr%2Fmain%2Fdocs%2Fhow-to-guides%2Fazure_resource_provision.json)

3. Run the Feathr Jupyter Notebook by clicking the button below. You only need to change the specified `Resource Prefix`.

[![Binder](https://mybinder.org/badge_logo.svg)](https://mybinder.org/v2/gh/linkedin/feathr/main?labpath=feathr_project%2Ffeathrcli%2Fdata%2Ffeathr_user_workspace%2Fnyc_driver_demo.ipynb)

## Step 2: Install Feathr

Install Feathr using pip:

```bash
pip install -U feathr
```

Or if you want to use the latest Feathr code from GitHub:

```bash
pip install git+https://github.com/linkedin/feathr.git#subdirectory=feathr_project
```

## Step 3: Run the sample notebook

We've provided a self-contained [sample notebook](https://github.com/linkedin/feathr/blob/main/feathr_project/feathrcli/data/feathr_user_workspace/nyc_driver_demo.ipynb) to act as the main content of this getting started guide. This documentation should be used more like highlights and further explanations of that demo notebook.

## Step 4: Update Feathr config

In the sample notebook, you will see some settings like below. You should update those settings based on your environment, for exmaple the spark runtime, synapse/databricks endpoint, etc.

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

All the configurations can be overwritten by environment variables with concatenation of `__` for different layers of this config file. For example, `feathr_runtime_location` for databricks config can be overwritten by setting `SPARK_CONFIG__DATABRICKS__FEATHR_RUNTIME_LOCATION` environment variable.

Another example would be overwriting Redis host with this config: `ONLINE_STORE__REDIS__HOST`.
if you want to override this setting in a shell environment:

```bash
export ONLINE_STORE__REDIS__HOST=feathrazure.redis.cache.windows.net
```

Or set this in python:

```python
os.environ['ONLINE_STORE__REDIS__HOST'] = 'feathrazure.redis.cache.windows.net'
```

## Step 5: Setup environment variables.

In the self-contained [sample notebook](https://github.com/linkedin/feathr/blob/main/feathr_project/feathrcli/data/feathr_user_workspace/nyc_driver_demo.ipynb), you also have to setup a few environment variables like below in order to access those cloud resources. You should be able to get those values from the first step.

These values can also be reterived by using cloud key value store, such as [Azure Key Vault](https://azure.microsoft.com/en-us/services/key-vault/):

```python
import os
os.environ['REDIS_PASSWORD'] = ''
os.environ['AZURE_CLIENT_ID'] = ''
os.environ['AZURE_TENANT_ID'] = ''
os.environ['AZURE_CLIENT_SECRET'] = ''
```

## Step 6: Create features with Python APIs

In Feathr, a feature is viewed as a function, mapping from entity id or key, and timestamp to a feature value. There are more explanations in the sample notebook.

## Step 7: Register feature definitions to the central registry

```python
from feathr.client import FeathrClient

client = FeathrClient()
client.register_features()
client.list_registered_features(project_name="feathr_getting_started")
```

## Step 8: Create training data using point-in-time correct feature join

A training dataset usually contains entity id columns, multiple feature columns, event timestamp
column and label/target column.

To create a training dataset using Feathr, one needs to provide a feature join configuration file to specify
what features and how these features should be joined to the observation data. The feature join config file mainly contains:

1. The path of a dataset as the 'spine' for the to-be-created training dataset. We call this input 'spine' dataset the 'observation'
   dataset. Typically, each row of the observation data contains:
   a) Column(s) representing entity id(s), which will be used as the join key to look up(join) feature value.
   b) A column representing the event time of the row. By default, Feathr will make sure the feature values joined have
   a timestamp earlier than it, ensuring no data leakage in the resulting training dataset.
   c) Other columns will be simply pass through onto the output training dataset.
2. The key fields from the observation data, which are used to joined with the feature data.
3. List of feature names to be joined with the observation data. The features must be defined in the feature
   definition configs.
4. The time information of the observation data used to compare with the feature's timestamp during the join.

Create training dataset via feature join:

```python
from feathr import FeathrClient

# Requested features to be joined
feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=[location_id])

# Observation dataset settings
settings = ObservationSettings(
    observation_path="abfss://green_tripdata_2020-04.csv",    # Path to your observation data
    event_timestamp_column="lpep_dropoff_datetime",           # Event timepstamp field for your data, optional
    timestamp_format="yyyy-MM-dd HH:mm:ss")                   # Event timestamp format， optional

# Prepare training data by joining features to the input (observation) data.
# feature-join.conf and features.conf are detected and used automatically.
client.get_offline_features(observation_settings=settings,
                                   output_path="abfss://output.avro",
                                   feature_query=feature_query)
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

## Step 9: Materialize feature value into offline/online storage

While Feathr can compute the feature value from the feature definition on-the-fly at request time, it can also pre-compute
and materialize the feature value to offline and/or online storage.

## Step 10: Fetching feature value for online inference

For features that are already materialized by the previous step, their latest value can be queried via the client's
`get_online_features` or `multi_get_online_features` API.

```python
client.get_online_features("nycTaxiDemoFeature", "265", ['f_location_avg_fare', 'f_location_max_fare'])
client.multi_get_online_features("nycTaxiDemoFeature", ["239", "265"], ['f_location_avg_fare', 'f_location_max_fare'])
```

## Next steps

- Run the [demo notebook](https://github.com/linkedin/feathr/blob/main/feathr_project/feathrcli/data/feathr_user_workspace/nyc_driver_demo.ipynb) to understand the workflow of Feathr.
- Read the [Feathr Documentation Page](https://linkedin.github.io/feathr/) page to understand the Feathr abstractions.
- Read guide to understand [how to setup Feathr on Azure](../how-to-guides/azure-deployment.md).
- Read [Python API Documentation](https://feathr.readthedocs.io/en/latest/)

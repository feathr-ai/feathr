---
layout: default
title: Quick Start Guide with Databricks
---

# Feathr Quick Start Guide with Databricks

For Databricks, you can simply upload [this notebook](./samples/databricks/databricks_quickstart_nyc_taxi_driver.ipynb) to your Databricks cluster and just run it in the Databricks cluster. It has been pre-configured to use the current Databricks cluster to submit jobs.

1. Import Notebooks in your Databricks cluster:

![Import Notebooks](./images/databricks_quickstart1.png)

2. Paste the [link to Databricks getting started notebook](./samples/databricks/databricks_quickstart_nyc_taxi_driver.ipynb):

![Import Notebooks](./images/databricks_quickstart2.png)

3. Run the whole notebook. It will automatically install Feathr in your cluster and run the feature ingestion jobs.

# Authoring Feathr jobs in local environment and submit to remote Databricks cluster

Not everyone wants to use databricks notebook as the main development environment, and the above part is more for quick start purpose. For a more serious development, we usually recommend using Visual Studio Code, where [it has native support for Python and Jupyter Notebooks](https://code.visualstudio.com/docs/datascience/jupyter-notebooks) with many great features such as syntax highlight and IntelliSense.

In [this notebook](./samples/databricks/databricks_quickstart_nyc_taxi_driver.ipynb), there are a few lines of code like this:

```python
# Get current databricks notebook context
ctx = dbutils.notebook.entry_point.getDbutils().notebook().getContext()
host_name = ctx.tags().get("browserHostName").get()
host_token = ctx.apiToken().get()
cluster_id = ctx.tags().get("clusterId").get()
```

This is the only part you need to change to author the Feathr job in local environment and submit to a remote Databricks cluster. When running those code in Databricks, Feathr will automatically read the current cluster's host name and authentication token using the above code, but this is not true if authoring the job locally. In that case, you will need to change the above lines to below:

```python
# Authoring Feathr jobs in local environment and submit to remote Databricks cluster
host_name = 'https://adb-6885802458123232.12.azuredatabricks.net/'
host_token = 'dapi11111111111111111111'
```

And that's it! Feathr will automatically submit the job to the cluster you specified.

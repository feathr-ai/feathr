---
layout: default
title: Manage library for Azure Spark Compute (Azure Databricks and Azure Synapse)
parent: How-to Guides
---

# Manage libraries for Azure Synapse

Sometimes you might run into dependency issues where a particular dependency might be missing on your spark compute, most likely you will face this issue while executing sample notebooks in that environment.

If you want to intall maven, PyPi or private packages on your Synapse cluster, you can follow the [official documentation](https://learn.microsoft.com/en-us/azure/synapse-analytics/spark/apache-spark-azure-portal-add-libraries) of how to do it for workspace, pool and session and the difference between each one of them.


## Manage libraries for Azure Databricks

Similarly to install an external library from PyPi, Maven or private packages on databricks you can follow the official [databricks documentation](https://learn.microsoft.com/en-us/azure/databricks/libraries/cluster-libraries)

Note: There is currently a known issue with using azure.cosmos.spark package on databricks. Somehow this dependency is not resolving correctly on databricks when packaged through gradle and results in ClassNotFound error. To mitigate this issue, please install the azure.cosmos.spark package directly from [maven central](https://mvnrepository.com/artifact/com.azure.cosmos.spark/azure-cosmos-spark_3-1_2-12) following the above steps.
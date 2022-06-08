---
layout: default
title: Configuration and environment varialbles in Feathr
parent: Feathr How-to Guides
---

# Configuration and environment varialbles in Feathr


This file contains the configurations that are used by Feathr
All the configurations can be overwritten by environment variables with concatenation of `__` for different layers of this config file.
For example, `feathr_runtime_location` for databricks can be overwritten by setting this environment variable:
SPARK_CONFIG__DATABRICKS__FEATHR_RUNTIME_LOCATION
Another example would be overwriting Redis host with this config: `ONLINE_STORE__REDIS__HOST`
For example if you want to override this setting in a shell environment:
export ONLINE_STORE__REDIS__HOST=feathrazure.redis.cache.windows.net

# A list of environment variables that Feathr uses

|Environment Variable                 | Description                                                          |
| ------------------------------- | --------------------------------------------------------------------------- |
| REDIS_PASSWORD    | Azure Blob Storage, Azure ADLS Gen2, AWS S3                                 |
| AZURE_CLIENT_ID            | Azure SQL DB, Azure Synapse Dedicated SQL Pools, Azure SQL in VM, Snowflake |
| AZURE_TENANT_ID                | Kafka, EventHub                                                             |
| AZURE_CLIENT_SECRET                   | Azure Cache for Redis                                                       |
| ADLS_ACCOUNT | Azure Purview                                                               |
| WASB_ACCOUNT                  | Azure Synapse Spark Pools, Databricks                                       |
| WASB_KEY      | Azure Machine Learning, Jupyter Notebook, Databricks Notebook               |
| S3_ACCESS_KEY                     | Parquet, ORC, Avro, JSON, Delta Lake                                        |
| S3_SECRET_KEY                     | Azure Key Vault                                                             |
| JDBC_TABLE                     | Azure Key Vault                                                             |

| JDBC_USER                     | Azure Key Vault                                                             |
| JDBC_PASSWORD                     | Azure Key Vault                                                             |
| KAFKA_SASL_JAAS_CONFIG                     | Azure Key Vault                                                             |
|OFFLINE_STORE__ADLS__ADLS_ENABLED|  |
|OFFLINE_STORE__WASB__WASB_ENABLED|  |
|OFFLINE_STORE__S3__S3_ENABLED|  |
|OFFLINE_STORE__S3__S3_ENDPOINT|  |
|OFFLINE_STORE__SNOWFLAKE__URL|  |
|OFFLINE_STORE__SNOWFLAKE__USER|  |
|OFFLINE_STORE__SNOWFLAKE__ROLE|  |
|OFFLINE_STORE__JDBC__JDBC_ENABLED|  |
|OFFLINE_STORE__JDBC__JDBC_DATABASE|  |
|OFFLINE_STORE__JDBC__JDBC_TABLE|  |
|SPARK_CONFIG__AZURE_SYNAPSE__DEV_URL|  |
|SPARK_CONFIG__AZURE_SYNAPSE__POOL_NAME|  |
|SPARK_CONFIG__AZURE_SYNAPSE__WORKSPACE_DIR|  |
|SPARK_CONFIG__AZURE_SYNAPSE__EXECUTOR_SIZE|  |
|SPARK_CONFIG__AZURE_SYNAPSE__EXECUTOR_NUM|  |
|SPARK_CONFIG__AZURE_SYNAPSE__FEATHR_RUNTIME_LOCATION|  |
|SPARK_CONFIG__DATABRICKS__WORKSPACE_INSTANCE_URL|  |
|SPARK_CONFIG__DATABRICKS__CONFIG_TEMPLATE|  |
|SPARK_CONFIG__DATABRICKS__WORK_DIR|  |
|SPARK_CONFIG__DATABRICKS__FEATHR_RUNTIME_LOCATION|  |
|ONLINE_STORE__REDIS__HOST|  |
|ONLINE_STORE__REDIS__PORT|  |
|ONLINE_STORE__REDIS__SSL_ENABLED|  |
|FEATURE_REGISTRY__PURVIEW__PURVIEW_NAME|  |
|FEATURE_REGISTRY__PURVIEW__DELIMITER|  |
|FEATURE_REGISTRY__PURVIEW__TYPE_SYSTEM_INITIALIZATION|  |
# Default behaviors

Feathr will get the required configuration in the following order:

1. If the key is set in the envrionment variable, Feathr will use the value of that environment variable
2. If it's not set in the environment, then a default is retrieved from from the feathr_config.yaml file with the same config key.
3. If it's not available in the feathr_config.yaml file, Feathr will try to reterive the value from key vault. Note that usually a key vault is case sensitive.
---
layout: default
title: Passing Through Credentials in Feathr
parent: How-to Guides
---

# Passing Through Credentials in Feathr

Sometimes, instead of using key-based credential to access the underlying storage (such as Azure Data Lake Storage), it makes more sense to use a user/service principal to access it, usually for security reasons.

Feathr has native support for this use case. For example, if you are currently using Databricks and want to access Azure Data Lake Storage using a certain user/principal credential, here are the steps:

1. Setup an Azure Data Lake Storage account and the corresponding Service Principals. More instructions can be found in this [Tutorial: Azure Data Lake Storage Gen2, Azure Databricks & Spark](https://learn.microsoft.com/en-us/azure/storage/blobs/data-lake-storage-use-databricks-spark).



2. After the first step, you should have an Azure Data Lake Storage account and a Service Principal. The second step is to pass those credentials to Feathr's spark settings, like below: 
```python
execution_configs = {"fs.azure.account.auth.type": "OAuth",
       "fs.azure.account.oauth.provider.type": "org.apache.hadoop.fs.azurebfs.oauth2.ClientCredsTokenProvider",
       "fs.azure.account.oauth2.client.id": "<appId>",
       "fs.azure.account.oauth2.client.secret": "<clientSecret>",
       "fs.azure.account.oauth2.client.endpoint": "https://login.microsoftonline.com/<tenant>/oauth2/token",
       "fs.azure.createRemoteFileSystemDuringInitialization": "true"}

# if running `get_offline_features` job
client.get_offline_features(observation_settings=settings,
                            feature_query=feature_query,
                            execution_configurations=execution_configs,
                            output_path=output_path)
# if running feature materialization job
client.materialize_features(settings, allow_materialize_non_agg_feature=True, execution_configurations=execution_configs)
```

In this code block, replace the `appId`, `clientSecret`, and `tenant` placeholder values in this code block with the values that you collected while completing the first step.

3. Don't forget your other configuration settings, such as the ones that are specific to Feathr in [Feathr Job Configuration during Run Time](./feathr-job-configuration.md).

4. Azure SQL Database Credential pass through is also supported. To achieve so you need to pass your token to environment variables and set `auth` parameter to `TOKEN` in `JdbcSource` or `JdbcSink`. For example:
```python
output_name = 'output'
sink = client.JdbcSink(name=output_name, url="some_jdbc_url", dbtable="table_name", auth="TOKEN")

os.environ[f"{output_name.upper()}_TOKEN"] = self.credential.get_token("https://management.azure.com/.default").token
client.get_offline_features(..., output_path=sink)
```

---
layout: default
title: Feathr Job Configuration during Run Time
parent: How-to Guides
---

# Feathr Job Configuration during Run Time

Since Feathr uses Spark as the underlying execution engine, there's a way to override Spark configuration by `FeathrClient.get_offline_features()` with `execution_configurations` parameters. The complete list of the available spark configuration is located in [Spark Configuration](https://spark.apache.org/docs/latest/configuration.html) (though not all of those are honored for cloud hosted Spark platforms such as Databricks), and there are a few Feathr specific ones that are documented here:

| Property Name                           | Default | Meaning                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | Since Version |
| --------------------------------------- | ------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------- |
| spark.feathr.inputFormat                | None    | Specify the input format if the file cannot be tell automatically. By default, Feathr will read files by parsing the file extension name; However the file/folder name doesn't have extension name, this configuration can be set to tell Feathr which format it should use to read the data. Currently can only be set for Spark built-in short names, including `json`, `parquet`, `jdbc`, `orc`, `libsvm`, `csv`, `text`. For more details, see ["Manually Specifying Options"](https://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#manually-specifying-options). Additionally, `delta` is also supported if users want to read delta lake. | 0.2.1         |
| spark.feathr.outputFormat               | None    | Specify the output format. "avro" is the default behavior if this value is not set. Currently can only be set for Spark built-in short names, including `json`, `parquet`, `jdbc`, `orc`, `libsvm`, `csv`, `text`. For more details, see ["Manually Specifying Options"](https://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html#manually-specifying-options). Additionally, `delta` is also supported if users want to write delta lake.                                                                                                                                                                                                          | 0.2.1         |
| spark.feathr.inputFormat.csvOptions.sep | None    | Specify the delimiter. For example, "," for commas or "\t" for tabs. (Supports both csv and tsv)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          | 0.6.0         |

## Examples on using job configurations

Examples when using the above job configurations when get offline features:

```python
client.get_offline_features(
                            observation_settings=settings,
                            feature_query=feature_query,
                            output_path=output_path,
                            execution_configurations=SparkExecutionConfiguration({"spark.feathr.inputFormat": "parquet", "spark.feathr.outputFormat": "parquet"}),
                            verbose=True
                    )
```

Examples when using the above job configurations when materializing features:

```python
client.materialize_features(settings, execution_configurations=SparkExecutionConfiguration({"spark.feathr.inputFormat": "parquet", "spark.feathr.outputFormat": "parquet"}))
```

## Config not applied issue
Please note that `execution_configurations` argument only works when using a new job cluster in Databricks : [Cluster spark config not applied](https://learn.microsoft.com/en-us/azure/databricks/kb/clusters/cluster-spark-config-not-applied)

If you are using an existing cluster, please manually add them to the cluster spark configuration. This can be done in Databrick Cluster UI : [Edit a cluster](https://learn.microsoft.com/en-us/azure/databricks/clusters/clusters-manage#--edit-a-cluster)




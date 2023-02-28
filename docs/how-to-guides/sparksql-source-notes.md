---
layout: default
title: Storing secrets in Azure Key Vault
parent: How-to Guides
---

## Use Databricks Tables as Data Source with `SparkSQLSource`

You may want to use tables as data source in Databricks. In this case, you can use SparkSQL to define a table and let Feathr read from it. 

There are two ways supported to define a SparkSQL table:
1. SparkSQL query
You can define a SparkSQL query as data source in Feathr job. The query should return a Spark DataFrame.

```python
from feathr.definition.source import SparkSqlSource

sql_source = SparkSqlSource(name="sparkSqlQuerySource", sql="SELECT * FROM green_tripdata_2020_04_with_index", event_timestamp_column="lpep_dropoff_datetime", timestamp_format="yyyy-MM-dd HH:mm:ss")

```

1. SparkSQL table
If your source is already defined as a table in Databricks, you can directly use its name as data source in Feathr job.

```python
from feathr.definition.source import SparkSqlSource

sql_source = SparkSqlSource(name="sparkSqlTableSource", table="green_tripdata_2020_04_with_index", event_timestamp_column="lpep_dropoff_datetime", timestamp_format="yyyy-MM-dd HH:mm:ss")
```

After defining the source, you can use it in the Feathr job as usual.

```python
agg_anchor = FeatureAnchor(name="aggregationFeatures",
                               source=sql_source,
                               features=agg_features)
```

When using SparkSQL table as data source, you need to make sure the table can be accessed by Spark session as the Feathr job.
---
layout: default
title: Using `SparkSQLSource` as Data Source
parent: How-to Guides
---

## Use Databricks Tables as Data Source with `SparkSQLSource`

You may want to use tables as data source in Databricks. The table can be "managed table" or external tables, but usually works best with managed tables. Please refer to the [databricks documentation](https://docs.databricks.com/data-governance/unity-catalog/create-tables.html#managed-tables) for more details on "managed table" vs "external table".

In this case, you can use SparkSQL to define a table and let Feathr read from it.

There are two ways supported to define a SparkSQL table:

### SparkSQL query

You can define a SparkSQL query as data source in Feathr job. The query should return a Spark DataFrame. The sample code is like below:

```python
from feathr.definition.source import SparkSqlSource

sql_source = SparkSqlSource(name="sparkSqlQuerySource", sql="SELECT * FROM green_tripdata_2020_04_with_index", event_timestamp_column="lpep_dropoff_datetime", timestamp_format="yyyy-MM-dd HH:mm:ss")

```

### SparkSQL table

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

Similarly, tables in Blob storages can also be used as this `SparkSQLSrouce` when using synapse as spark provider.

### Sample Code

Please refer to [this file](https://github.com/feathr-ai/feathr/blob/main/feathr_project/test/test_spark_sql_source.py) for more details on how to use this API.

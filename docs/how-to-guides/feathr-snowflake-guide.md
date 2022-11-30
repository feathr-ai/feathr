---
layout: default
title: Using Snowflake with Feathr
parent: Feathr How-to Guides
---

# Using Snowflake with Feathr

Currently, feathr supports using Snowflake as a source.

# Using Snowflake as a source

To use Snowflake as a source, we need to create a `SnowflakeSource` in projects.

```
source = feathr.SnowflakeSource(name: str, database: str, schema: str, dbtable: optional[str], query: Optional[str])
```

* `name` is the source name, same as other sources.
* `database` is SF database that stores the table of interest
* `schema` is SF schema that stores the table of interest
* `dbtable` or `query`, `dbtable` is the table name in the database and `query` is a SQL `SELECT` statement, only one of them should be specified at the same time.

For more information on how Snowflake uses Databases and Schemas to organize data, please refer to [Snowflake Datatabase and Schema](https://docs.snowflake.com/en/sql-reference/ddl-database.html)

There are some other parameters such as `preprocessing`, they're same as other sources like `HdfsSource`.

After creating the `SnowflakeSource`, you can use it in the same way as other kinds of sources.

# Specifying Snowflake Source in Observation Settings

`ObservationSettings` requires an observation path. In order to generate the snowflake path, feathr exposes client functionality that exposes the same arguments as SnowflakeSource. 

To generate snowflake path to pass into `ObservationSettings`, we need to call `client.get_snowflake_path()` functionality.

```
observation_path = client.get_snowflake_path(database: str, schema: str, dbtable: Optional[str], query: Optional[str])
```
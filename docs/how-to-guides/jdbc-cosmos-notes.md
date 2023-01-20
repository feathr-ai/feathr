---
layout: default
title: Using SQL databases, CosmosDb with Feathr
parent: Feathr How-to Guides
---

# Using SQL databases and CosmosDb with Feathr

Feathr supports using SQL databases as source or offline store, also supports using CosmosDb as online store.

## Using SQL databases as source

To use SQL database as source, we need to create `JdbcSource` instead of `HdfsSource` in projects.

A `JdbcSource` can be created with follow statement:

```python
source = feathr.JdbcSource(name, url, dbtable, query, auth)
```

* `name` is the source name, same as other sources.
* `url` is the database URL in JDBC format.
* `dbtable` or `query`, `dbtable` is the table name in the database and `query` is a SQL `SELECT` statement, only one of them should be specified at the same time.
* `auth` can be `None`, `'USERPASS'` or `'TOKEN'`.

There are some other parameters such as `preprocessing`, they're same as other sources like `HdfsSource`.

After creating the `JdbcSource`, you can use it in the same way as other kinds of sources.

## Auth

When the `auth` parameter is omitted or set to `None`, Feathr assumes that the database doesn't need any authentication.

When the `auth` parameter is set to `USERPASS`, you need to set following environment variables before submitting job:

* *name*_USER, the `name` is the source name and the value of this variable is the user name to log in to the database.

* *name*_PASSWORD, the `name` is the source name and the value of this variable is the password to log in to the database.

When the `auth` parameter is set to `TOKEN`, you need to set following environment variables before submitting job:

* *name*_TOKEN, used the `name` is the source name and the value of this variable is the token to log in to the database, currently only Azure SQL database supports this auth type.

I.e., if you created a source:

```python
src1_name="source1"
source1 = JdbcSource(name=src1_name, url="jdbc:...", dbtable="table1", auth="USERPASS")
anchor1 = FeatureAnchor(name="anchor_name",
                        source=source1,
                        features=[some_features, some_other_features])
```

You need to set 2 environment variables before submitting jobs:
```
os.environ[f"{src1_name.upper()}_USER"] = "some_user_name"
os.environ[f"{src1_name.upper()}_PASSWORD"] = "some_magic_word"

client.build_features(anchor_list=[anchor1, ...])
client.get_offline_features(...)
```

These values will be automatically passed to the Feathr core when submitting the job.

If you want to use token, the code will be like this:
Step 1: Define the source JdbcSource
```python
src_name="source_name"
source = JdbcSource(name=src_name, url="jdbc:...", dbtable="table_name", auth="TOKEN")
anchor = FeatureAnchor(name="anchor_name",
                        source=source,
                        features=[some_features, some_other_features])
```
Step 2: Set the environment variable before submitting the job
```python
os.environ[f"{src_name.upper()}_TOKEN"] = "some_token"
```
To enable Azure AD authentication in Azure SQL database, please refer to [this document](https://learn.microsoft.com/en-us/azure/azure-sql/database/authentication-aad-overview?view=azuresql#overview).

There are several ways to obtain Azure AD access token, please refer to [this document](https://docs.microsoft.com/en-us/azure/active-directory/develop/access-tokens) for more details.

If you want to leverage existing credential in python client, you could try:
```python
from azure.identity import DefaultAzureCredential

credential = DefaultAzureCredential()
token = credential.get_token("https://management.azure.com/.default").token()
```

## Using SQL database as the offline store

To use SQL database as the offline store, you can use `JdbcSink` as the `output_path` parameter of `FeathrClient.get_offline_features`, e.g.:
```python
name = 'output'
sink = client.JdbcSink(name, some_jdbc_url, dbtable, "USERPASS")
```

Then you need to set following environment variables before submitting job:
```python
os.environ[f"{name.upper()}_USER"] = "some_user_name"
os.environ[f"{name.upper()}_PASSWORD"] = "some_magic_word"
client.get_offline_features(..., output_path=sink)
```
"TOKEN" auth type is also supported in `JdbcSink`.

## Using SQL database as the online store

Same as the offline, create JDBC sink and add it to the `MaterializationSettings`, set corresponding environment variables, then use it with `FeathrClient.materialize_features`.
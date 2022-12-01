---
layout: default
title: Using SQL databases, CosmosDb, and ElasticSearch with Feathr
parent: Feathr How-to Guides
---

# Using SQL databases and CosmosDb with Feathr

Feathr supports using SQL databases as source or offline store, also supports using CosmosDb as online store.

## Using SQL databases as source

To use SQL database as source, we need to create `JdbcSource` instead of `HdfsSource` in projects.

A `JdbcSource` can be created with follow statement:

```
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

```
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
```python
src_name="source_name"
source = JdbcSource(name=src_name, url="jdbc:...", dbtable="table_name", auth="TOKEN")
anchor = FeatureAnchor(name="anchor_name",
                        source=source,
                        features=[some_features, some_other_features])
```
And you need to set 1 environment variable before submitting jobs:
```
os.environ[f"{src_name.upper()}_TOKEN"] = "some_token"
```
This method can be used to pass through AAD credentials to Azure SQL database.

## Using SQL database as the offline store

To use SQL database as the offline store, you can use `JdbcSink` as the `output_path` parameter of `FeathrClient.get_offline_features`, e.g.:
```
name = 'output'
sink = client.JdbcSink(name, some_jdbc_url, dbtable, "USERPASS")
```

Then you need to set following environment variables before submitting job:
```
os.environ[f"{name.upper()}_USER"] = "some_user_name"
os.environ[f"{name.upper()}_PASSWORD"] = "some_magic_word"
client.get_offline_features(..., output_path=sink)
```
"TOKEN" auth type is also supported in `JdbcSink`.

## Using SQL database as the online store

Same as the offline, create JDBC sink and add it to the `MaterializationSettings`, set corresponding environment variables, then use it with `FeathrClient.materialize_features`.

## Using CosmosDb as the online store

To use CosmosDb as the online store, create `CosmosDbSink` and add it to the `MaterializationSettings`, then use it with `FeathrClient.materialize_features`, e.g..

```
name = 'cosmosdb_output'
sink = CosmosDbSink(name, some_cosmosdb_url, some_cosmosdb_database, some_cosmosdb_collection)
os.environ[f"{name.upper()}_KEY"] = "some_cosmosdb_api_key"
client.materialize_features(..., materialization_settings=MaterializationSettings(..., sinks=[sink]))
```

Feathr client doesn't support getting feature values from CosmosDb, you need to use [official CosmosDb client](https://pypi.org/project/azure-cosmos/) to get the values:

```
from azure.cosmos import exceptions, CosmosClient, PartitionKey

client = CosmosClient(some_cosmosdb_url, some_cosmosdb_api_key)
db_client = client.get_database_client(some_cosmosdb_database)
container_client = db_client.get_container_client(some_cosmosdb_collection)
doc = container_client.read_item(some_key)
feature_value = doc['feature_name']
```

## Using ElasticSearch as online store

To use ElasticSearch as the online store, create `ElasticSearchSink` and add it to the `MaterializationSettings`, then use it with `FeathrClient.materialize_features`, e.g..

```
name = 'es_output'
sink = ElasticSearchSink(name, host="esnode1:9200", index="someindex", ssl=False, auth=True)
os.environ[f"{name.upper()}_USER"] = "some_user_name"
os.environ[f"{name.upper()}_PASSWORD"] = "some_magic_word"
client.materialize_features(..., materialization_settings=MaterializationSettings(..., sinks=[sink]))
```

Feathr client doesn't support getting feature values from ElasticSearch, you need to use [official ElasticSearch client](https://pypi.org/project/elasticsearch/) to get the values, e.g.:

```
from elasticsearch import Elasticsearch

es = Elasticsearch("http://esnode1:9200")
resp = es.get(index="someindex", id="somekey")
print(resp['_source'])
```

The feature generation job uses `upsert` mode to write data, so after the job the index may contain stale data, the recommended way is to create a new index each time, and use index alias to seamlessly switch over, detailed information can be found from [the official doc](https://www.elastic.co/guide/en/elasticsearch/reference/master/aliases.html), currently Feathr doesn't provide any helper to do this.

NOTE:
+ You can use no auth or basic auth only, no other authentication methods are supported.
+ If you enabled SSL, you need to make sure the certificate on ES nodes is trusted by the Spark cluster, otherwise the job will fail.

## Using ElasticSearch as offline store

To use ElasticSearch as the offline store, create `ElasticSearchSink` and use it with `FeathrClient.get_offline_features`, e.g..

```
name = 'es_output'
sink = ElasticSearchSink(name, host="esnode1", index="someindex", ssl=False, auth=True)
os.environ[f"{name.upper()}_USER"] = "some_user_name"
os.environ[f"{name.upper()}_PASSWORD"] = "some_magic_word"
client.get_offline_features(..., output_path=sink)
```

NOTE: The feature joining process doesn't generate meaningful keys for each document, you need to make sure the output dataset can be accessed/queried by some other ways such as full-text-search, otherwise you may have to fetch all the data from ES to get what you look for.
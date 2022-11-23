---
layout: default
title: Local Spark Provider Usage
parent: How-to Guides
---

## Background
Feathr Community has received several asks about how to use local spark environments. Based on Feathr contributors [local debug habit](#local-debug-guide), a local spark provider is added in Feathr Client now. 
This provider only support [limited functions](#supported-use-case) right now, and feedbacks on local spark use cases are welcomed to help us prioritize and improve features.

## Local Debug Guide
Local spark is frequently used when Feathr contributors test their code changes. The main purpose of it is not to include every features but make sure the basic e2e flow works.

The local spark provider only requires users to have a [local spark environment](#environment-setup) and set `spark_cluster` to `local` in [Feathr Config](#local-feathr-config) for feature join jobs. In case of feature gen job, users need to set online store params.


### Environment Setup
Please make sure that `Spark` and `feathr` are installed and the `SPARK_LOCAL_IP` is set. 
`JAVA_HOME` and Java environment is also required.

### Local Feathr Config
To use local spark environment, user need to set `spark_cluster: 'local'`. If `feathr_runtime_location` is not set, Feathr will use default Maven package instead.
```yaml
spark_config:
  # choice for spark runtime. Currently support: azure_synapse, databricks, local
  spark_cluster: 'local'
  spark_result_output_parts: '1'
  local:
    feathr_runtime_location:
```

### Sample spark-submit.sh
A spark-submit script will auto generated in your workspace under `debug` folder like below:
```sh
#!/bin/sh

spark-submit \
        --master local[*] \
        --name project_feathr_local_spark_test \
        --packages "org.apache.spark:spark-avro_2.12:3.3.0,com.microsoft.sqlserver:mssql-jdbc:10.2.0.jre8,com.microsoft.azure:spark-mssql-connector_2.12:1.2.0,org.apache.logging.log4j:log4j-core:2.17.2,com.typesafe:config:1.3.4,com.fasterxml.jackson.core:jackson-databind:2.12.6.1,org.apache.hadoop:hadoop-mapreduce-client-core:2.7.7,org.apache.hadoop:hadoop-common:2.7.7,org.apache.avro:avro:1.8.2,org.apache.xbean:xbean-asm6-shaded:4.10,org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.3,com.microsoft.azure:azure-eventhubs-spark_2.12:2.3.21,org.apache.kafka:kafka-clients:3.1.0,com.google.guava:guava:31.1-jre,it.unimi.dsi:fastutil:8.1.1,org.mvel:mvel2:2.2.8.Final,com.fasterxml.jackson.module:jackson-module-scala_2.12:2.13.3,com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.6,com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.12.6,com.jasonclawson:jackson-dataformat-hocon:1.1.0,com.redislabs:spark-redis_2.12:3.1.0,org.apache.xbean:xbean-asm6-shaded:4.10,com.google.protobuf:protobuf-java:3.19.4,net.snowflake:snowflake-jdbc:3.13.18,net.snowflake:spark-snowflake_2.12:2.10.0-spark_3.2,org.apache.commons:commons-lang3:3.12.0,org.xerial:sqlite-jdbc:3.36.0.3,com.github.changvvb:jackson-module-caseclass_2.12:1.1.1,com.azure.cosmos.spark:azure-cosmos-spark_3-1_2-12:4.11.1,org.eclipse.jetty:jetty-util:9.3.24.v20180605,commons-io:commons-io:2.6,org.apache.hadoop:hadoop-azure:2.7.4,com.microsoft.azure:azure-storage:8.6.4,com.linkedin.feathr:feathr_2.12:0.9.0" \
        --conf "spark.driver.extraClassPath=../target/scala-2.12/classes:jars/config-1.3.4.jar:jars/jackson-dataformat-hocon-1.1.0.jar:jars/jackson-module-caseclass_2.12-1.1.1.jar:jars/mvel2-2.2.8.Final.jar:jars/fastutil-8.1.1.jar" \
        --conf "spark.hadoop.fs.wasbs.impl=org.apache.hadoop.fs.azure.NativeAzureFileSystem" \
        --class com.linkedin.feathr.offline.job.FeatureJoinJob \
        ./noop-1.0.jar \
        --join-config feathr/feathr_project/test/test_user_workspace/feature_join_conf/feature_join_local.conf \
        --input ./green_tripdata_2020-04_with_index.csv \
        --output debug/test_output_20220903145015 \
        --feature-config feature_conf/auto_generated_request_features.conf,feature_conf/auto_generated_anchored_features.conf,feature_conf/auto_generated_derived_features.conf\
        --num-parts 1 \
```
You can also call the script directly:
```bash
sh debug/local_spark_feathr_feature_join_job20220914160251/command.sh
```
You may also refer to
[submitting-applications docs](https://spark.apache.org/docs/latest/submitting-applications.html) to customize your scripts.

### Logs
logs are automatically stored in `debug` folder of your workspace for further debugging.

### Usage
The usage of local spark provider is almost the same with cloud spark providers. You could refer to [test_local_spark_e2e.py](../../feathr_project/test/test_local_spark_e2e.py) for usage samples.

In short, the `submit_feathr_job()` in local spark mode will return a Popen object, which `stdout` and `stderr` are set to local log file. You can track the jobs with your custom code. Or, you could try the following python code which will process output for you.
```python
results = client.wait_job_to_finish()
```


## Supported Use Case
In this version of local spark provider, users are only able to test `get_offline_features()` with Feathr Client. 

`local-spark-provider` enable users to test features without deploying any cloud resources. However, please use it ONLY in test or trial scenarios. For production usage, cloud spark providers are highly recommended. 

### Tips:
- If you want to submit more customized params to Spark, a workaround is to generate a sample script and then update it with your own params.
- Cold start will be slow since it needs to download quite a few Maven packages to local environment. But after that it should be very fast to use it locally
- Windows is currently not supported. Linux/MacOS is fully tested. If you are on Windows machine, consider using WSL.
### Use Cases:
Following use cases are covered in CI test:
- `get_offline_features()` without UDFs
- `get_offline_features()` with UDFs
- `get_offline_features()` with local observation path
- `get_offline_features()` with wasb observation path

### Coming soon:
- `materialize_features()` into online store with local spark environment.
- advanced `udf` support
- more data sources


---
layout: default
title: Developing Customized Feathr Spark UDF
parent: How-to Guides
---

# Developing Customized Feathr Spark UDF

Feathr provides a lot of customized

Most of the content are a bit unrelated with Feathr, but we just document them here to make it easier for end users to develop

The thinking there is to register Spark UDFs in a permanent way so that those registered functions can be consumed in Feathr.

## Difference between Spark UDF scopes

There are two types - UDF available in session scope, and UDFs that are shared across different sessions (permanent functions)

the first one - example

second one -

```SQL
CREATE OR REPLACE FUNCTION simple_feathr_udf_add20_string AS 'org.example.SimpleFeathrUDFString' USING JAR 'dbfs:/FileStore/jars/SimpleFeathrUDF.jar';
```

## Step 1: Creating a JAR package for the UDF

https://spark.apache.org/docs/latest/sql-ref-syntax-ddl-create-function.html

Specifies the name of the class that provides the implementation for function to be created. The implementing class should extend one of the base classes as follows:

- Should extend UDF or UDAF in org.apache.hadoop.hive.ql.exec package.
- Should extend AbstractGenericUDAFResolver, GenericUDF, or GenericUDTF in org.apache.hadoop.hive.ql.udf.generic package.
- Should extend UserDefinedAggregateFunction in org.apache.spark.sql.expressions package.

Currently, only row level transformation is supported and tested. I.e. you should always extend `org.apache.hadoop.hive.ql.exec`.

## Write a simple UDF

```java
package org.example;

import org.apache.hadoop.hive.ql.exec.UDF;

public class SimpleFeathrUDFString extends UDF {
   public int evaluate(String value) {
        int number = Integer.parseInt(value);
        return number + 20;
    }
}
```

The corresponding pom.xml to have `org.apache.hive:hive-exec` and `org.apache.hadoop:hadoop-common` as dependencies:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>SimpleFeathrUDF</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>

        <dependency>
            <groupId>org.apache.hive</groupId>
            <artifactId>hive-exec</artifactId>
            <version>3.1.3</version>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>3.3.1</version>
        </dependency>

    </dependencies>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

</project>
```

Compile the result to a Jar file. For example, if you are using IDEs such as IntelliJ, you can create an artifact like this

![Feathr Spark UDF](../images/feathr-spark-udf-artifact.png)

After that, you should have a jar file containing your UDF code.

## Step 2: Upload the JAR to the cluster and register the functions

The second step is to upload the jar that we just compiled to a shared location, and have it registered in the Spark environment.

For databricks, you can upload it to the DBFS that your current spark cluster is using, like below:

![Feathr Spark Upload](../images/feathr-spark-udf-upload.png)

For Synapse, you can upload the jar to the default storage account that the spark cluster is associated with.

After uploading the JAR, you should register the UDF as a permanent UDF like this.

```SQL
CREATE OR REPLACE FUNCTION simple_feathr_udf_add20_string AS 'org.example.SimpleFeathrUDFString' USING JAR 'dbfs:/FileStore/jars/SimpleFeathrUDF.jar';
```

For more on the syntax, refer to the spark docs: https://spark.apache.org/docs/latest/sql-ref-syntax-ddl-create-function.html

You can test if the UDF is registered in Spark or not by typing those in the notebook environment:

```SQL
CREATE TABLE IF NOT EXISTS feathr_test_table(c1 INT);
INSERT INTO feathr_test_table VALUES (1), (2);
SELECT simple_feathr_udf_add20_string(c1) AS function_return_value FROM feathr_test_table;
```

![Feathr Spark UDF](../images/feathr-spark-udf-test.png)

Creates a temporary or permanent external function. Temporary functions are scoped at a session level where as permanent functions are created in the persistent catalog and are made available to all sessions. The resources specified in the USING clause are made available to all executors when they are executed for the first time.

https://docs.databricks.com/spark/latest/spark-sql/language-manual/sql-ref-syntax-ddl-create-function.html

## Step 3: Using the UDF in Feathr

The only caveat here is to disable Feathr from doing more optimizations. Since Feathr is designed to optimize for large scale workloads, and UDFs are black boxes to optimizer, so we need to disable some of the Feathr optimizations, such as bloom filters.

This is straightforward to do. In

```python
client.get_offline_features(observation_settings=settings,
                            feature_query=feature_query,
                            output_path=output_path,
                            execution_configurations={"spark.feathr.row.bloomfilter.maxThreshold":"0"}
                           )

```

```python
client.materialize_features(settings, execution_configurations={"spark.feathr.row.bloomfilter.maxThreshold":"0"})
```

That's it! Using the UDF just as regular functions. For example, we might want to define a feature like this:


```python
    Feature(name="f_udf_transform",
            feature_type=INT32,
            transform="simple_feathr_udf_add20_string(PULocationID)"),
```


![Feathr Spark UDF](../images/feathr-spark-udf-result.png)
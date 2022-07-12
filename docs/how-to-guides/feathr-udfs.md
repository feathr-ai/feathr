---
layout: default
title: Feathr User Defined Functions (UDFs)
parent: How-to Guides
---

# Feathr User Defined Functions (UDFs)

Feathr supports a wide range of user defined functions (UDFs) to allow flexible way of dealing with your data. There are two use cases that Feathr currently supports:

1. User defined functions at input sources (also known as preprocessing functions)
2. User defined functions at individual features (using the `transform` parameters).

## User defined functions at input sources (also known as preprocessing functions)

One of the example is as below:

```python
def add_new_dropoff_and_fare_amount_column(df: DataFrame):
    from pyspark.sql import SparkSession, DataFrame
    from pyspark.sql.functions import col
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    df = df.withColumn("fare_amount_cents", df.fare_amount.cast('double') * 100)
    return df

batch_source = HdfsSource(name="nycTaxiBatchSource",
                        path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                        preprocessing=add_new_dropoff_and_fare_amount_column,
                        event_timestamp_column="new_lpep_dropoff_datetime",
                        timestamp_format="yyyy-MM-dd HH:mm:ss")
```

As you can see, there will be parts:

1. A self-contained function
2. Calling that function in `preprocessing` parameter.

### What happened behind the scene and what is the limitation?

What's happening behind the scene is, Feathr will copy the function and execute the function in the corresponding Spark cluster. The execution order 

There are several limitations:

1. The functions should be "self-contained". I.e. Any package or functions you use, you should import it in the function body. Although Feathr imports all the built-in functions in `pyspark.sql.functions`, it is always a best practice to import the modules (might be other python modules you use, for example `time`, regex, etc.) in the UDF body, like the `from pyspark.sql.functions import dayofweek` line in the example below.
2. The function should accept only one Spark Dataframe as input, and return one Spark Dataframe as the result. Multiple DataFrames are not supported. There's no limitation on names, and by convention it is usually called `df`, but you can use any name you want.
3. As long as you give the UDF a dataframe as an input
4. For all the feature definitions, you should use the output of the UDF as the definition (if you have changed the name of the columns). For example, you have the following code: 
4. Currently, "chained" functions are not supported. I.e. the example below is not supported:

```python
def multiply_100(input_val):
  return input_val * 100

def add_new_dropoff_and_fare_amount_column(df: DataFrame):
    from pyspark.sql.functions import dayofweek
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    df = df.withColumn("fare_amount_cents",  multiply_100("fare_amount"))
    return df
```

Per the first limitation (i.e. the functions should be self-contained), you should consider the following way:

```python


def add_new_dropoff_and_fare_amount_column(df: DataFrame):
    from pyspark.sql.functions import dayofweek
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    
    def multiply_100(input_val):
      return input_val * 100
    
    df = df.withColumn("fare_amount_cents",  multiply_100("fare_amount"))
    return df
```

### PySpark Support Examples

Feathr supports using regular pyspark
```python
def add_new_dropoff_and_fare_amount_column(df: DataFrame):
    from pyspark.sql.functions import dayofweek
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    df = df.withColumn("fare_amount_cents", df.fare_amount.cast('double') * 100)
    return df
```
### Spark SQL Examples

Feathr also supports using a SQL dialect to deal with your data. Below is the template, note that:

1. you should declare a global "spark" session so that it can be called later
2. You should call `createOrReplaceTempView` so that you can refer to this view in your SQL code

```python
from pyspark.sql import SparkSession, DataFrame
def feathr_udf_filter_location_id(df: DataFrame) -> DataFrame:
  # if using Spark SQL, need to declare this default spark session, and create a temp view so that you can run Spark SQL on it.
  global spark
  df.createOrReplaceTempView("feathr_temp_table_feathr_udf_day_calc")
  sqlDF = spark.sql(
  """
  SELECT *
  FROM feathr_temp_table_feathr_udf_day_calc
  WHERE DOLocationID!= 100
  """
  )
  return sqlDF
```

### Pandas Examples

Feathr also supports using pandas to deal with the data. Behind the scene it's using pandas-on-spark so some limitation applies here. Please refer to [Pandas-on-Spark's Best Practice](https://spark.apache.org/docs/latest/api/python/user_guide/pandas_on_spark/best_practices.html#best-practices) for more details.

Below is the template, note that:

1. This is only available for Spark 3.2 and later, so make sure you submit to a spark cluster that has Spark 3.2 and later.
2. You need to call something like `psdf = df.to_pandas_on_spark()` to convert a Spark dataframe to "pandas dataframe", and call `psdf.to_spark()` to convert the "pandas dataframe" to Spark dataframe.


```python
def feathr_udf_pandas_spark(df: DataFrame) -> DataFrame:
  # using pandas on spark APIs. Fore more details, refer to the doc here: https://spark.apache.org/docs/latest/api/python/user_guide/pandas_on_spark/index.html
  # Note that this API is only available for Spark 3.2 and later, so make sure you submit to a spark cluster that has Spark 3.2 and later.
  psdf = df.to_pandas_on_spark()
  psdf['fare_amount_cents'] = psdf['fare_amount']*100
  # need to make sure converting the pandas-on-spark dataframe to Spark Dataframe.
  return psdf.to_spark()

batch_source = HdfsSource(name="nycTaxiBatchSource",
                        path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                        preprocessing=add_new_dropoff_and_fare_amount_column,
                        event_timestamp_column="new_lpep_dropoff_datetime",
                        timestamp_format="yyyy-MM-dd HH:mm:ss")
```


### When do I use UDFs?

Those UDFs are totally optional to use. For example, if you have an existing feature transformation pipeline, you don't have to use Feathr's preprocessing functions to rewrite your code. Instead, you can simply use your already transformed feature in Feathr, for point in time joins, or for feature registry and exploration.

But if you don't have an existing pipeline, Feathr's UDF does provide a good way for you to manage your feature engineering system from end to end. This decision is beyond the scope of this document.
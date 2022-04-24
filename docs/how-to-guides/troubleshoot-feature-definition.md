---
layout: default
title: Feature Definition Troubleshooting Guide
parent: Feathr How-to Guides
---

# Feature Definition Troubleshooting Guide

You may come across some errors while creating your feature definition config. This guide will help you troubleshoot those errors.

## Prerequisite

- [Local Feature Testing](local-feature-testing.md)

## How to Use This Guide

The guide is pretty comprehensive and covers quite diverse errors. To use this guide more efficiently, follow these steps:

- The error message may appear in either `feathr start` or `feathr test` terminal. Read outputs from both of them.
- The error message sometimes maybe pretty verbose. Just focus on the line that follows `Caused by: `.
- Read the headlines of each section and understand what problems each section is trying to troubleshoot.
- Use some keywords(one or two) followed by `Caused by` in your error message to search the error you want to troubleshoot.
- You don't have to read this guide all at once. But over time, try to read this guide a few times more throughly so it will be helpful to debug future issues.

# Basic Config Issue in Feature Fefinition

You may mis-spell a keyword, or a source etc in your feature config. When that happens, you will get a config error. For example, I mis-spelled my source as nycTaxiBatchSource3 and then I got:

```
: com.fasterxml.jackson.databind.JsonMappingException: Instantiation of [simple type, class com.linkedin.feathr.offline.config.FeathrConfig] value failed: [FEATHR_USER_ERROR] Source is not defined in source section Map(nycTaxiBatchSource -> path: abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv, sourceType:FIXED_PATH)
  at com.fasterxml.jackson.databind.deser.std.StdValueInstantiator.wrapException(StdValueInstantiator.java:399)
	...
Caused by: com.linkedin.feathr.common.exception.FeathrConfigException: [FEATHR_USER_ERROR] Source is not defined in source section Map(nycTaxiBatchSource -> path: abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv, sourceType:FIXED_PATH)
	at com.linkedin.feathr.offline.anchored.feature.FeatureAnchorWithSource$.getSource(FeatureAnchorWithSource.scala:82)
	...
```

So actually it should be nycTaxiBatchSource.

In other cases, some keyword may be misspelled, like key, aggregation, then you will got errors like this:

```
: com.fasterxml.jackson.databind.JsonMappingException: Instantiation of [simple type, class com.linkedin.feathr.offline.anchored.anchorExtractor.SimpleConfigurableAnchorExtractor] value failed: null (through reference chain: com.linkedin.feathr.offline.config.FeathrConfig["anchors"]->com.fasterxml.jackson.module.scala.deser.MapBuilderWrapper["nonAggFeatures"])
	at com.fasterxml.jackson.databind.deser.std.StdValueInstantiator.wrapException(StdValueInstantiator.java:399)
  ...
Caused by: java.lang.NullPointerException
	at com.linkedin.feathr.offline.anchored.anchorExtractor.SimpleConfigurableAnchorExtractor.<init>(SimpleConfigurableAnchorExtractor.scala:45)
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
	...
```

Let's look at one other exmaple. I come up with this feature definition:

```
  aggregationFeatures: {
    source: nycTaxiBatchSource
    key: DOLocationID
    features: {
      f_location_avg_fare: {
        def: "fare_amount"
        aggregation: SVG
        window: 90d
      }
    }
  }
```

And then I tested with `feathr test` but instead of getting features I want, I actually get the error message:

```
Caused by: com.linkedin.feathr.common.exception.FeathrConfigException: [FEATHR_USER_ERROR] Trying to deserialize the config into TimeWindowFeatureDefinition.Aggregation type AVG3 is not supported. Supported types are: SUM, COUNT, AVG, MAX, TIMESINCE.Please use a supported aggregation type.
```

You figure out that you mistyped your aggregfation as `SVG` but instead it should be `AVG`.

# Feature Transformation Expression Troubleshooting

The above errors are usually easier to troubleshoot. Feature transformation expression(`def`, `key` part) involves actual data transformation and thus is harder to troubleshoot. Based on the transformation types, we divide the troubleshooting into 3 parts that corresponds to 3 typical feature engineering scenarios: row-level features, aggregation features, and derived features.

## Row-lelve Transformation

For non-aggregation feature, the feature transformation part and the key part are just row-level transforamtion. For row-level transformations, you only transform one row at a time.

```
  nonAggFeatures: {
    source: nycTaxiBatchSource
    key: VendorID // key is also row-level transformation expression
    features: {

      f_trip_distance: "(double)trip_distance3" // non-agg feature is also row-level transformation expression

      f_day_of_month: "day_of_month(lpep_dropoff_datetime)"

      f_hour_of_day: "hour_of_day(lpep_dropoff_datetime)"
    }
  }
```

We will walk through a few typical scenarios.

### Simple Field Extraction

Extracting a field from a table is probably the most common scenario. You can just reference the field in the data table and transform it into a feature:

```
  nonAggFeatures: {
    source: nycTaxiBatchSource
    key: VendorID
    features: {
      f_trip_distance: "trip_distance_3"
    }
  }
```

Typical errors you see is `Field "x" does not exist`. You can see from the error message that x(here `trip_distance_3`) is not part of the data schema.

```
java.lang.RuntimeException: unable to access field
	at org.mvel2.optimizers.impl.refl.nodes.PropertyHandlerAccessor.getValue(PropertyHandlerAccessor.java:37)
	...
Caused by: java.lang.IllegalArgumentException: Field "trip_distance_3" does not exist.
Available fields: VendorID, lpep_pickup_datetime, lpep_dropoff_datetime, store_and_fwd_flag, RatecodeID, PULocationID, DOLocationID, passenger_count, trip_distance, fare_amount, extra, mta_tax, tip_amount, tolls_amount, ehail_fee, improvement_surcharge, total_amount, payment_type, trip_type, congestion_surcharge, comlinkedinfeathrofflineanchoredanchorExtractorSimpleConfigurableAnchorExtractor11644cfa_1
	at org.apache.spark.sql.types.StructType$$anonfun$fieldIndex$1.apply(StructType.scala:303)
	...
```

### Adding Some Transformations

Sometimes simply accessing the field is not enough. You may want some more transformations, like type casting, null handling etc. If you don't do that, you may not get the desired feature type. If you know your data type, then you can use type cast.

```
  nonAggFeatures: {
    source: nycTaxiBatchSource
    key: VendorID
    features: {
      f_trip_distance: "(double)trip_distance"  // cast to double
      ...
    }
```

If you want to peek into your data type, we provided a debug util(`getDataType`) to help you check the data type.

```
  nonAggFeatures: {
    source: nycTaxiBatchSource
    key: VendorID
    features: {
      f_trip_distance: "getDataType(trip_distance)"
      f_is_long_trip_distance: "get_type(Double.parseDouble(trip_distance))"
    }
```

Then in your `feathr test` terminal, you will see:

```
Your feature value is:
[2,[WrappedArray(java.lang.String),WrappedArray(1.0)]]
[22,[WrappedArray(java.lang.String),WrappedArray(1.0)]]

```

It means your data type for `trip_distance` is java.lang.Double. So maybe you don't want a feature of String type, then you can cast it into double or float.

## Aggregation Transformation

For aggregation transformation debug, usually the problem comes from the timestamped data intersection with the specified date in the generation config.

```
  aggregationFeatures: {
    source: nycTaxiBatchSource
    key: DOLocationID
    features: {
      f_location_avg_fare: {
        def: "float(fare_amount)"
        aggregation: AVG
        window: 3d
      }
    }
  }
```

For local feature testing, we set the generation config time to NOW. So you need to ensure that your data falls in [NOW - window, NOW]. For example, if NOW is 2022/02/20(yyyy-MM-dd). Then your local mock data(at least one row) should fall between [2022/02/17(yyyy-MM-dd), 2022/02/20(yyyy-MM-dd)] to ensure a window exsit and aggregation can then happen.

If there are no such overlapping, then you will see a error/warning message in your `feathre start` or `feathr test` like this:

```
There doesnt seem to have any data in the window you defined. Please check your window configurations.
```

If you see this, please try to fix your mock data timestamp fields or expand the window size so it falls into the right window.

For example, after checking the data, I see my data timestamp field shows it's all older than 2022/01/01. So to fix this, I can just simply increase my window to `window: 360d`(one year). After fix, I can see the features are produced again.

## Derived Feature Debug

Typical derived feature issues coming from the inputs.

```
derivations: {
   f_trip_time_distance: {
     definition: "f_trip_distance * f_trip_time_duration"
     type: NUMERIC
   }
}
```

### Checking Inputs

You can check your inputs by inspecting the debug message from `feathr start`.

```
Your input table to the derived feature is:
Your inputs to the derived feature({f_trip_distance * f_trip_time_duration}) with detailed type info: {Map(f_trip_distance -> Some(NumericFeatureValue{_floatValue=1.12}), f_trip_time_duration -> Some(NumericFeatureValue{_floatValue=5.0}))}
```

Here it's a happy case that all your inputs seem healthy.

In unlucky cases, you may see something like this:

```
Your inputs to the derived feature({f_trip_distance * f_trip_time_duration}) with detailed type info: {Map(f_trip_distance -> None, f_trip_time_duration -> Some(NumericFeatureValue{_floatValue=0.0}))}
...

```

One of your input(`f_trip_distance`) is None and then your derived feature transformation failed. Since you are adding None with a numeric value here. So you should try to fix the None from `f_trip_distance` feature. If the None is inevitable, try to fix your feature expression so it can handle None.

# FAQ

(TBD)

# Need More Help?

If you need more help, please reach out to us via our slack channel.

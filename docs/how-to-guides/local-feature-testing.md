---
layout: default
title: Feathr Local Feature Testing Guide
parent: Feathr How-to Guides
---

# Local Feature Testing Guide

> **Local testing supports .csv and .parquet source format.**

# What's Local Feature Testing

Local feature testing or experimentation tries to use some mock data to come up with the right feature definition and transformation locally without cluster job execution. Relying on cluster job execution(feathr join or feathr deploy) for feature definition iteration usually takes longer. However, for local testing, it's a matter of secs.

# How-to Local Feature Testing

## Set Up Local Feathr Engine

> java(version 8) installation is required so please install java if you haven't.
> Local feature testing requires a local feathr engine(a java jar) to be running to process the feature retrieval or transformation job. So let's setup local feathr engine first.

There are two ways to get the local feathr engine. You can manually download it from [Azure Blob Storage](https://azurefeathrstorage.blob.core.windows.net/public/feathr_local_engine.jar) or download and set it up in the terminal via:

`feathr start`

(if you download it yourself, you should also start the service by calling `feathr start` via terminal.)

The engine should stay running to accept and process requests from `feathr test`. Some detailed debug messages will be printed here.

```
(test_env) feathr_user_workspace % feathr start
There is no local feathr engine(jar) in the workspace. Will download the feathr jar.
Downloading feathr jar for local testing: feathr_local_engine.jar Bytes: 201190119 from https://azurefeathrstorage.blob.core.windows.net/public/feathr_local_engine.jar
Download feathr local engine jar  [####################################]  100%
Starting the local feathr engine: feathr_local_engine.jar.
Please keep this open and start another terminal to run feathr test. This terminal shows the debug message.

```

## Prepare Mock Data

Local feature testing will run the feature definition against your local mock data. Your local mock data should have same structure(ideally) or simliar structure to ensure that it will work the same way in the cluster. Usually you can take one small portion of your cluster data as your mock data and place to mockdata directory in your workspace in the following way.

If your source path in the features.conf is like :

```
nycTaxiBatchSource: {
    location: { path: "abfss://azure.net/my_data/green_tripdata_2020-04.csv" }
    ...
}
```

Then you should place the mock data to the path with the protocol prefix(here `abfss://`) stripped(`my_data/green_tripdata_2020-04.csv`) under the `feathr_user_workspace/mockdata` folder. That is: your mock data should be at:

```
feathr_user_workspace/mockdata/azure.net/my_data/green_tripdata_2020-04.csv
```

## Iterate Your Feature Definition

After your mock data is ready, you can start to iterate your features. For example, you first come with a feature definition like this:

```
anchors: {
  aggregationFeatures: {
    source: nycTaxiBatchSource
    key: DOLocationID
    features: {
      f_location_avg_fare: {
        def: "float(fare_amount)"
        aggregation: AVG
        window: 90d
      }
    }
  }

}

sources: {
  nycTaxiBatchSource: {
    location: { path: "abfss://azure.net/my_data/green_tripdata_2020-04.csv" }
    timeWindowParameters: {
      timestampColumn: "lpep_dropoff_datetime"
      timestampColumnFormat: "yyyy-MM-dd HH:mm:ss"
    }
  }
}
```

You can first test to see how this feature is like with `feathre test` and then type in your feature: `f_location_avg_fare`. You will see the feature results:

```
(test_env) feathr_user_workspace % feathr test
Your feature names, separated by comma: f_location_avg_fare

Producing feature values for requested features ...

Feature computation completed.

The following features are calculated:
f_location_avg_fare

Your feature schema is:
root
 |-- key0: string (nullable = false)
 |-- f_location_avg_fare: float (nullable = true)

Your feature value is:
[239,10.0]
[225,2.5]
[265,54.5]
[60,18.0]
[264,10.0]
[151,5.5]
[42,6.0]
[75,5.75]
[41,6.0]

```

But you may feel that the output feature aggregation is not ideal, then you can change aggregation from AVG to SUM to land on a different feature. After modify the features.conf, you can test it again with `feathr test`, then you will get your new results in a few seconds.

## Troubleshoot Feature Fefinition

Sometimes you might not get your feature definitions right at start. It may not produce feature values or may just not work. You can use local feature testing to help you troubleshoot as well. For example, I come up with this feature definition:

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

You figure out that you mistyped your aggregfation as `SVG` but instead it should be `AVG`. If this error message is not enough for you, you can also check out debug message provided by `feathr start` terminal.

# What's Next

After local feature testing, you should test your feature configs in your cluster via `feature join` or `feature deploy`.

# Need More Help?

If you need more help, please read the how-to-guides or reach out to us at our slack.

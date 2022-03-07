# Feature Definition

## Prerequisite
* [Feathr Expression Language](../how-to-guides/expression-language.md)

## Introduction
In Feathr, a feature is viewed as a function, mapping from entity id or key, and timestamp to a feature value. <br/>
1) The entity key (a.k.a. entity id) identifies the subject of feature, e.g. a user id, 123.<br/>
2) The feature name is the aspect of the entity that the feature is indicating, e.g. the age of the user.<br/>
3) The feature value is the actual value of that aspect at a particular time, e.g. the value is 30 at year 2022.<br/>

Feature is defined via Feature definition config in the format of [HOCON](https://github.com/lightbend/config/blob/main/HOCON.md)
The feature definition has three sections, including sources, anchors and derivations.

## Step1: Define Sources Section
A feature source is needed for anchored features that describes the raw data in which the feature values are computed from.
See an examples below:

```
sources: {
  nycTaxiBatchSource: {
     location: { path: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv" }
     timeWindowParameters: {
         timestampColumn: "lpep_dropoff_datetime"
         timestampColumnFormat: "yyyy-MM-dd HH:mm:ss"
    }
  }
}

```

| Field Name | Explanation | Required? | Example
| --- | --- | --- | --- |
| location | The location of the source data. | Yes | location: {  <br/>path: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv" <br/>} |
| timeWindowParameters | The time information of the source data. Include a `timestampColumn` and `timestampColumnFormat` | No | timeWindowParameters: { <br/> timestampColumn: "lpep_dropoff_datetime" <br/> timestampColumnFormat: "yyyy-MM-dd HH:mm:ss" <br/> } |

## Step2: Define Anchor Section
`anchors` contains all the anchored features. A feature is called an anchored feature when the feature is directly 
extracted from the source data, rather than computed on top of other features. 
The latter case is called derived feature.

`anchors` section is required in the feature definition config. You can simply write "anchors: {}" 
if you don't have any anchored features in the feature definition config.


See the anchored feature config specification below:

```
anchors: {
  <anchor name>: {    // This is an anchor,  <anchor name> needs to be unique across ALL feature config files. 
                      // It can be viewed as the feature namespace.
      ...  // feature definitions in this anchor goes here
  }
   
 <anchor name>: {     // This is another anchor, <anchor name> needs to be unique across ALL feature config files.
      ...  // feature definitions in this anchor goes here
  }
}
```

And an example:

```
anchors: {
    nonAggFeatures: {
        source: PASSTHROUGH
        key: NOT_NEEDED
        features: {

            f_trip_distance: "cast_double(trip_distance)"

            f_is_long_trip_distance: "cast_double(trip_distance) > 30"

            f_trip_time_duration: "time_duration(lpep_pickup_datetime, lpep_dropoff_datetime, 'minutes')"

            f_day_of_week: "dayofweek(lpep_dropoff_datetime)"

            f_day_of_month: "dayofmonth(lpep_dropoff_datetime)"

            f_hour_of_day: "hourofday(lpep_dropoff_datetime)"
        }
    }

    aggregationFeatures: {
        source: nycTaxiBatchSource
        key: DOLocationID
        features: {
            f_location_avg_fare: {
                def: "cast_float(fare_amount)"
                aggregation: AVG
                window: 3d
            }
            f_location_max_fare: {
                def: "cast_float(fare_amount)"
                aggregation: MAX
                window: 3d
            }
        }
    }
}

```

| Field Name | Explanation | Required? | Example
| --- | --- | --- | --- |
| source | The data source in which the features are anchored to. It could be PASSTHROUGH or a source name define in the `sources` section | Yes| source: nycTaxiBatchSource // source name defined in sources section <br/> source: PASSTHROUGH // using request data as feature source|
| key | An Feathr expression specifying how to extract the key for the features defined on this anchor. Usually it is a simple reference for the key field in the data record, e.g "entityId" but it can involve more complex expressions. | Yes | key: "entityId" // single key <br/> key: ["entityId","entityId2"] // multiple/compound key |
| features | Defines the features to be extracted from this anchor. It is a map of feature names and transformations using Feathr expression language. | Yes | features: { f_trip_distance: "(float)trip_distance" } |

For the features field above, there are two different types, simple anchored features and window aggregation features.

###1) For simple anchored features, see the supported fields below:

```
    features: {
      <feature name>: {
        def: <Feathr expression specifying how to extract the key for the feature value>
        type: <feature type>
        default: <default feature value when missing>
      }
    }
```

And an example:

```
    features: {
      f_is_long_trip_distance: {
        def: "trip_distance>30"
        type: BOOLEAN
        default: False
      }
    }
```

| Field Name | Explanation | Required? |
| --- | --- | --- |
| type | The type of the feature value. Possible feature types: BOOLEAN, NUMERIC, CATEGORICAL, CATEGORICAL_SET, TENSOR | No
| def | An Feathr expression specifying how to extract the key for the feature value | Yes |
| default | The default value constant when the feature value is missing| No |

Note that Feathr also support a simple form as a syntax sugar as: <br/>
`feature name`: `Feathr expression to extract the feature value`

###2) For window aggregation features, see the supported fields below:

```
    features: {
      <feature name>: {
        def: <Feathr expression specifying how to extract the feature value for source data row>
        aggregation: <aggregation type>
        window: <time window length to apply the aggregation>
        filter: <Feathr expression applied to each row as a filter>
        groupby: <Feathr expressions applied after the `def` transformation as groupby fields>
        default: <Constant default value when feature value is missing>
        type: <feature type>
      }
    }
```
And an example:

```
    features: {
        f_location_max_fare: {
            def: "float(fare_amount)"
            aggregation: MAX
            window: 3d
        }
    }
```


| Field Name | Explanation | Required? |
| --- | --- | --- |
| def | An Feathr expression specifying how to extract the feature value from the source data row| Yes |
| aggregation | Aggregation type. Supported aggregation types: SUM, COUNT, MAX, MIN, AVG, LATEST, MAX_POOLING, MIN_POOLING, AVG_POOLING. See table below for detailed explanation of each. | Yes |
| window | Time window length to apply the aggregation. support 4 type of units: d(day), h(hour), m(minute), s(second). The example value are "7d' or "5h" or "3m" or "1s" | Yes |
| filter | Feathr expression applied to each row as a filter **before** aggregation| No |
| groupby| Feathr expressions applied **after** the `def` transformation as groupby field, **before** aggregation| No|
| default | The default value constant when the feature value is missing| No |
| type | The type of the feature value. Possible feature types: BOOLEAN, NUMERIC, CATEGORICAL, CATEGORICAL_SET, TENSOR | No

Aggregation Function list:

| Aggregation Type | Input Type | Description |
| --- | --- | --- |
|SUM, COUNT, MAX, MIN, AVG	|Numeric|Applies the the numerical operation on the numeric inputs. |
|MAX_POOLING, MIN_POOLING, AVG_POOLING	| Numeric Vector | Applies the max/min/avg operation on a per entry bassis for a given a collection of numbers.|
|LATEST| Any |Returns the latest not-null values from within the defined time window |

## Step3: Derived Features Section
Derived features are the features that are computed from other features. They could be computed from anchored features, or other derived features.

To define a derived feature, create the "derivations" section in the feature definition configuration file.

```
derivations: {
  // This is a simple derived feature,  <derived feature name> needs to be unique across ALL feature config files.
  // It assume the 'key' of the derived feature to be the same as that of the dependent feature.
  <derived feature name>: {   
      definition: <Feathr expression to compute this derived feature, which can referece **ONLY one** other anchored 
                  feature or derived feature>
      type: <Feature type, same as anchored feature type>  
  }
   
 <derived feature name>: {     // This is a more comprehensive derived feature syntx
    key: <The name of the derived feature. Note that this is just an arbitrary name, similar to the parameter name of a function,
          rather than an Feathr expression>
    // All features with corresponding keys that this derived feature depends on
    inputs: {
      <feature_foo_alias>: {key: <key name of feature_foo>, feature: <feature_foo>}
      <feature_bar_alias>: {key: <key name of feature_bar>, feature: <feature_bar>}
      ...
      <other_feature_alias>: {key: <other feature key name>, feature: <other feature>}
    }
    definition: <feathr expression to produce the feature value, referencing by input features by their alias specifed in the inputs field above>
    type: <Feature type, same as anchored feature type>
}
}
```
And an example:

```

derivations: {
  f_trip_time_distance: {
    definition: "f_trip_distance * f_trip_time_duration"
    type: NUMERIC
  }
}

```

For the complex derived features, all supported fields are:

| Field Name | Explanation | Required? |
| --- | --- | --- | 
| key | The names of the derived feature. Note that this is just an arbitrary name, similar to the parameter name of a function, rather than an Feathr expression | Yes|
| inputs | All features with corresponding keys that this derived feature depends on |Yes| 
| definition | Feathr expression to produce the feature value, referencing by input features by their alias specifed in the inputs field above |Yes|
| type |Feature type, same as anchored feature type|Yes|

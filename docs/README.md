Feathr – An Enterprise-Grade, High Performance Feature Store
====================

**What is a Feature Store?** A Feature Store is a system that lets you
**define** and **deploy** ML features and then **access** them during model training and model inferencing.
The Feature Store is an essential component of a machine learning CI/CD platform, and is a prerequisite for being able
to deploy and serve ML models at scale – especially for applications that make predictions about **entities** like
users, documents, or ads.

**What is Feathr?** Feathr is a Feature Store that supports **time-aware feature computation**.
Feathr lets you **define features** based on raw data sources, using simple configuration.
Feathr then lets you **get those features by their names** during model training and model inferencing,
using simple APIs. Feathr automates the process of computing your features and joining them to your training
data, following point-in-time-correct semantics to avoid data leakage, and supports materializing and deploying
your features for use online in production.

## Defining Features in Feathr

Feathr enables defining features based on various data sources, including time-series data.
Feathr supports aggregations, transformations, time windowing, and a rich set of types including
vectors and tensors, making it easy to define many kinds of useful features based on your underlying data.

Feathr feature definitions can be replayed automatically over historical time-series data to compute
features at specific points in time, enabling point-in-time-correct feature computation during
training data generation.

### Simple Features
In **features.conf**:
```
anchors: {       // Feature anchors
    trip_features: {          // A feature anchor
      source: nycTaxiBatchSource
      key: DOLocationID
      features: {             // Feature names in this anchor
          f_is_long_trip: "trip_distance > 30"       // A feature by an expression
          f_day_of_week: "dayofweek(datetime)"     // A feature with built-in function
      }
    }
}
```

### Window Aggregation Features
```
anchors: {
    agg_features: {                    // A feature anchor (with aggregation)
        source: nyc_taxi_batch_source  // Features data source
        features: {
            f_location_avg_fare: {     // A feature with window aggregation
                aggregation: AVG           // Aggregation function
                def: "cast_float(fare_amount)"  // Aggregation expression
                window: 3d                 // Over a 3-day window
            }
        }
        key: LocationID                // Query/join key of the feature(group)
    }
}
        
sources: {                            // Named data sources
    nyc_taxi_batch_source: {          // A data source
        location: { path: "abfss://feathr@feathrazure.windows.net/demo_data/" }
        timeWindowParameters: {       // Time information of the data source
            timestampColumn: "dropoff_datetime"
            timestampColumnFormat: "yyyy-MM-dd HH:mm:ss"
        }
    }
}
```

### Derived Features: Beyond Features on Raw Data Sources
```
derivations: {    // Features that depend on other features instead of external raw data sources
  f_trip_time_distance: {    // Name of a derived feature
    definition: "f_trip_distance * f_trip_time_duration"
    type: NUMERIC
  }
}
```
## Accessing Features from Feathr

Feathr provides simple APIs for model pipelines and inferencing code to **get features by their names**.
Feathr automates the process of joining various data sources, point-in-time-correct computation for
training data generation, and pre-materializing feature data sets for online access.

Models may depend on a large number of features defined by multiple authors across a team or organization.
For such cases, Feathr seamlessly merges the multiple definitions into a combined join-and-compute workflow,
providing the consumer (the ML model) with the convenient appearance that the features are coming from a single table.

In **feature-join.conf**:
```
// Request dataset, used to join with features to become a training dataset
observationPath: "abfss://feathr@feathrazure.windows.net/demo_input/"
features: [      // Requested features to be joined  
    {
        featureList: [f_is_long_trip, f_day_of_week]
    }
]
outputPath: "abfss://feathr@feathrazure.windows.net/demo/demo_output/"
```

In **my_offline_training.py**:
```python
from  feathr import FeathrClient
client = FeathrClient()
# Prepare training data by joining features to the input (observation) data.
# feature-join.conf and features.conf are detected and used automatically.
result = client.get_offline_features()
result.sample(10)
```

In **my_online_model.py**:
```python
from  feathr import FeathrClient
client = FeathrClient()
# Get features for a locationId (key)
client.get_online_features(feature_table = "agg_features", 
                           key = "265",
                           feature_names = ['f_location_avg_fare', 'f_location_max_fare'])
# Batch get for multiple locationIds (keys)
client.multi_get_online_features(feature_table = "nycTaxiDemoFeature",
                                 key = ["239", "265"],
                                 feature_names = ['f_location_avg_fare', 'f_location_max_fare'])

```
# Next Steps
## Quickstart
* [Quickstart](quickstart.md)

## Concepts

* [Feature Definition](concepts/feature-definition.md)
* [Feature Generation](concepts/feature-generation.md)
* [Feature Join](concepts/feature-join.md)
* [Point-in-time Correctness](concepts/point-in-time-join.md)

## How-to-guides
* [Azure Deployment](how-to-guides/azure-deployment.md)
* [Local Feature Testing](how-to-guides/local-feature-testing.md)
* [Feature Definition Troubleshooting Guide](how-to-guides/troubleshoot-feature-definition.md)
* [Feathr Expression Language](how-to-guides/expression-language.md)



## Cloud Architecture
Feathr has native integration with Azure and other cloud services, and here's the high-level architecture to help you get started.
![Architecture](images/architecture.png)

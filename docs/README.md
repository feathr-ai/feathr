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
In **feathr_worksapce** folder:
```python
# Define the key for your feature
features = [
    Feature(name="f_trip_distance",                         # Ingest feature data as-is
            feature_type=FLOAT),      
    Feature(name="f_is_long_trip_distance",
            feature_type=BOOLEAN,
            transform="cast_float(trip_distance)>30"),      # SQL-like syntax to transform raw data into feature
    Feature(name="f_day_of_week",
            feature_type=INT32,
            transform="dayofweek(lpep_dropoff_datetime)")   # Provides built-in transformation
]

anchor = FeatureAnchor(name="request_features",             # Features anchored on same source
                       source=batch_source,
                       features=features)
```

### Window Aggregation Features
```python
# Define the key for your feature
location_id = TypedKey(key_column="DOLocationID",
                       key_column_type=ValueType.INT32,
                       description="location id in NYC",
                       full_name="nyc_taxi.location_id")

agg_features = [Feature(name="f_location_avg_fare",
                        key=location_id,                          # Query/join key of the feature(group)
                        feature_type=FLOAT,
                        transform=WindowAggTransformation(        # Window Aggregation transformation
                            agg_expr="cast_float(fare_amount)",
                            agg_func="AVG",                       # Apply average aggregation over the window
                            window="90d")),                       # Over a 90-day window
                ]

agg_anchor = FeatureAnchor(name="aggregationFeatures",
                           source=batch_source,
                           features=agg_features)
```

### Derived Features: Beyond Features on Raw Data Sources
```python
# Compute a new feature(a.k.a. derived feature) on top of an existing feature
derived_feature = DerivedFeature(name="f_trip_time_distance",
                                 feature_type=FLOAT,
                                 key=trip_key,
                                 input_features=[f_trip_distance, f_trip_time_duration],
                                 transform="f_trip_distance * f_trip_time_duration")

# Another example to compute embedding similarity
user_embedding = Feature(name="user_embedding", feature_type=DENSE_VECTOR, key=user_key)
item_embedding = Feature(name="item_embedding", feature_type=DENSE_VECTOR, key=item_key)

user_item_similarity = DerivedFeature(name="user_item_similarity",
                                      feature_type=FLOAT,
                                      key=[user_key, item_key],
                                      input_features=[user_embedding, item_embedding],
                                      transform="cosine_similarity(user_embedding, item_embedding)")
```

## Accessing Features from Feathr

Feathr provides simple APIs for model pipelines and inferencing code to **get features by their names**.
Feathr automates the process of joining various data sources, point-in-time-correct computation for
training data generation, and pre-materializing feature data sets for online access.

Models may depend on a large number of features defined by multiple authors across a team or organization.
For such cases, Feathr seamlessly merges the multiple definitions into a combined join-and-compute workflow,
providing the consumer (the ML model) with the convenient appearance that the features are coming from a single table.

```python
# Requested features to be joined 
feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=["DOLocationID"])

# Observation dataset settings
settings = ObservationSettings(
    observation_path="abfss://green_tripdata_2020-04.csv",    # Path to your observation data
    event_timestamp_column="lpep_dropoff_datetime",           # Event timepstamp field for your data, optional
    timestamp_format="yyyy-MM-dd HH:mm:ss")                   # Event timestamp format， optional

# Request the data for offline training
feathr_client.get_offline_features(observation_settings=settings,
                                   output_path="abfss://output.avro",
                                   feature_query=feature_query)
```

In **my_offline_training.py**:
```python
from feathr import FeathrClient
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

Feathr – An Enterprise-Grade, High Performance Feature Store
===========================================

## What is Feathr?

Feathr lets you:
* **define features** based on raw data sources, including time-series data, using simple [HOCON](https://github.com/lightbend/config/blob/main/HOCON.md) configuration 
* **get those features by their names** during model training and model inferencing,
using simple APIs
* **share features** across your team and company

Feathr automatically computes your feature values and joins them to your training
data, using point-in-time-correct semantics to avoid data leakage, and supports materializing and deploying
your features for use online in production.

Follow the [quick-start-guide](docs/quickstart.md) to try it out.
For more details, read our [documentation](https://linkedin.github.io/feathr/).

## Defining Features with Transformation
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

## (Optional) Deploy Features to Online (Redis) Store
With CLI tool: `feathr deploy`

## Accessing Features
In **my_offline_training.py**:
```python
from feathr import FeathrClient

# Requested features to be joined 
feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=[location_id])

# Observation dataset settings
settings = ObservationSettings(
  observation_path="abfss://green_tripdata_2020-04.csv",    # Path to your observation data
  event_timestamp_column="lpep_dropoff_datetime",           # Event timepstamp field for your data, optional
  timestamp_format="yyyy-MM-dd HH:mm:ss")                   # Event timestamp format， optional

# Prepare training data by joining features to the input (observation) data.
# feature-join.conf and features.conf are detected and used automatically.
feathr_client.get_offline_features(observation_settings=settings,
                                   output_path="abfss://output.avro",
                                   feature_query=feature_query)
```

In **my_online_model.py**:
```python
from feathr import FeathrClient
client = FeathrClient()
# Get features for a locationId (key)
client.get_online_features(feature_table = "agg_features",
                           key = "265",
                           feature_names = ['f_location_avg_fare', 'f_location_max_fare'])
# Batch get for multiple locationIds (keys)
client.multi_get_online_features(feature_table = "agg_features",
                                 key = ["239", "265"],
                                 feature_names = ['f_location_avg_fare', 'f_location_max_fare'])

```


# More on Defining Features

## Defining Window Aggregation Features
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

## Defining Named Raw Data Sources
```python
batch_source = HdfsSource(
    name="nycTaxiBatchSource",                              # Source name to enrich your metadata
    path="abfss://green_tripdata_2020-04.csv",              # Path to your data
    event_timestamp_column="lpep_dropoff_datetime",         # Event timestamp for point-in-time correctness
    timestamp_format="yyyy-MM-dd HH:mm:ss")                 # Supports various fromats inculding epoch
```

## Beyond Features on Raw Data Sources - Derived Features
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


## Roadmap
>`Public Preview` release doesn't guarantee API stability and may introduce API changes.

- [x] Private Preview release
- [x] Public Preview release
- [ ] Alpha version release
  - [ ] Support streaming and online transformation
  - [ ] Support feature versioning
  - [ ] Support more data sources




## Community Guidelines
Build for the community and build by the community. Check out [community guidelines](CONTRIBUTING.md).

Join our [slack](https://join.slack.com/t/feathrai/shared_invite/zt-14sxrbacj-7qo2bKL0LVG~4m0Z8gytZQ) for questions and discussions.

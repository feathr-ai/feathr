from feathr.anchor import FeatureAnchor
from feathr.feature import Feature
from feathr.dtype import BOOLEAN, INT32, ValueType
from feathr.typed_key import TypedKey
from feathr.source import HdfsSource
from pyspark.sql import SparkSession, DataFrame

batch_source = HdfsSource(name="nycTaxiBatchSource",
                          path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                          event_timestamp_column="lpep_dropoff_datetime",
                          timestamp_format="yyyy-MM-dd HH:mm:ss")

location_id = TypedKey(key_column="DOLocationID",
                       key_column_type=ValueType.INT32,
                       description="location id in NYC",
                       full_name="nyc_taxi.location_id")

def my_func(df: DataFrame):
    # give the user dataframe
    print("df: ")
    print(df)
    print(df.schema)
    # this is executed in spark
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    # df = df.filter("tolls_amount > 0.0")
    # df = df.withColumn("fare_amount", df.fare_amount.cast('double'))
    # df = df.withColumn("trip_distance", df.trip_distance.cast('double'))
    print(df)
    print(df.schema)
    df.show(10)
    return df

features333 = [
    Feature(name="f_is_long_trip_distance",
            feature_type=BOOLEAN,
            transform="cast_float(trip_distance)>30"),
    Feature(name="f_day_of_week",
            feature_type=INT32,
            transform="dayofweek(lpep_dropoff_datetime)"),
]


preprocessed_funcs = {
    "f_is_long_trip_distance": my_func
}

anchor = FeatureAnchor(name="nonAggFeatures",
                       source=batch_source,
                       features=features333)
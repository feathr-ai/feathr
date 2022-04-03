giimport os
from datetime import datetime, timedelta
from pathlib import Path
from pyspark.sql.functions import col,sum,avg,max
from feathr.job_utils import get_result_df
from feathr.anchor import FeatureAnchor
from feathr.dtype import BOOLEAN, FLOAT, INT32, ValueType
from feathr.feature import Feature
from feathr.query_feature_list import FeatureQuery
from feathr.settings import ObservationSettings
from feathr.source import INPUT_CONTEXT, HdfsSource
from feathr.typed_key import TypedKey
from feathr.transformation import WindowAggTransformation
from pyspark.sql import SparkSession, DataFrame
from test_fixture import basic_test_setup

def trip_distance_preprocessing(df: DataFrame):
    # df = df.withColumn("trip_distance", df.trip_distance.cast('double'))
    # df = df.withColumn("fare_amount", df.fare_amount.cast('string'))

    df = df.withColumn("trip_distance", df.trip_distance.cast('double') - 90000)
    df = df.withColumn("fare_amount", df.fare_amount.cast('double') - 90000)
    # df = df.withColumn("trip_distance", df.trip_distance - 90000)
    # df = df.withColumn("fare_amount", df.fare_amount - 90000)

    return df

def my_func2(df: DataFrame):
    df = df.withColumn("trip_distance", df.trip_distance.cast('double') - 90000)
    df = df.withColumn("fare_amount", df.fare_amount.cast('double') - 90000)
    return df

def feathr_udf_add_toll_amount(df: DataFrame) -> DataFrame:
    # df = df.filter("tolls_amount > 0.0")
    df = df.withColumn("CopiedColumn", col("tolls_amount") * -1)
    df = df.withColumn("fare_amount_new", col("fare_amount") + 100)
    return df

def feathr_udf_day_calc(df: DataFrame) -> DataFrame:
    # df = df.filter("tolls_amount > 0.0")
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    df = df.withColumn("f_day_of_year", dayofyear("lpep_dropoff_datetime"))
    return df

def test_feathr_get_offline_features():
    """
    Test get_offline_features() can get data correctly.
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    batch_source = HdfsSource(name="nycTaxiBatchSource",
                              path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                              event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    location_id = TypedKey(key_column="DOLocationID",
                           key_column_type=ValueType.INT32,
                           description="location id in NYC",
                           full_name="nyc_taxi.location_id")


    agg_features = [Feature(name="f_location_avg_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            # transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                            transform=WindowAggTransformation(agg_expr="fare_amount",
                                                              agg_func="AVG",
                                                              window="90d")),
                    Feature(name="f_location_max_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            # transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                            transform=WindowAggTransformation(agg_expr="fare_amount",
                                                              agg_func="MAX",
                                                              window="90d"))
                    ]

    agg_anchor = FeatureAnchor(name="aggregationFeatures",
                               source=batch_source,
                               features=agg_features,
                               preprocessing=my_func2)

    pickup_time_as_id = TypedKey(key_column="lpep_pickup_datetime",
                           key_column_type=ValueType.INT32,
                           description="location id in NYC",
                           full_name="nyc_taxi.location_id")

    features = [
        Feature(name="f_is_long_trip_distance",
                key=pickup_time_as_id,
                feature_type=FLOAT,
                # transform="trip_distance + 1000"),
                transform="fare_amount_new"),
        Feature(name="f_day_of_week",
                key=pickup_time_as_id,
                feature_type=INT32,
                transform="dayofweek(lpep_dropoff_datetime)"),
    ]

    request_anchor = FeatureAnchor(name="request_features",
                                   source=batch_source,
                                   features=features,
                                   # preprocessing=trip_distance_preprocessing)
                                   preprocessing=feathr_udf_add_toll_amount)

    client.build_features(anchor_list=[agg_anchor, request_anchor])

    feature_query = [FeatureQuery(
        feature_list=["f_is_long_trip_distance", "f_day_of_week"], key=pickup_time_as_id),
        FeatureQuery(
            feature_list=["f_location_avg_fare", "f_location_max_fare"], key=location_id)
    ]

    settings = ObservationSettings(
        observation_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")


    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro",
                                # udf_files=["./pyspark_client.py", "./client_udf_repo.py", "./__init__.py"]
                                # udf_files=["./client_udf_repo.py"]
                                udf_files=[]
                                )


    now = datetime.now()
    # set output folder based on different runtime
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".avro"])


    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path)

    # assuming the job can successfully run; otherwise it will throw exception
    client.wait_job_to_finish(timeout_sec=900)

    # download result and just assert the returned result is not empty
    res_df = get_result_df(client)
    assert res_df.shape[0] > 0

# def test_feathr_get_offline_features():
#     """
#     Test get_offline_features() can get data correctly.
#     """
#     test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
#
#     client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
#
#
#     batch_source = HdfsSource(name="nycTaxiBatchSource",
#                               path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
#                               event_timestamp_column="lpep_dropoff_datetime",
#                               timestamp_format="yyyy-MM-dd HH:mm:ss")
#
#     # location_id = TypedKey(key_column="DOLocationID",
#     location_id = TypedKey(key_column="lpep_pickup_datetime",
#                            key_column_type=ValueType.INT32,
#                            description="location id in NYC",
#                            full_name="nyc_taxi.location_id")
#
#     features = [
#         Feature(name="f_is_long_trip_distance",
#                 key=location_id,
#                 feature_type=FLOAT,
#                 # transform="trip_distance + 1000"),
#                 transform="fare_amount_new"),
#         Feature(name="f_day_of_week",
#                 key=location_id,
#                 feature_type=INT32,
#                 transform="dayofweek(lpep_dropoff_datetime)"),
#     ]
#
#     request_anchor = FeatureAnchor(name="request_features",
#                                    source=batch_source,
#                                    features=features,
#                                    # preprocessing=trip_distance_preprocessing)
#                                    preprocessing=feathr_udf_add_toll_amount)
#
#
#     client.build_features(anchor_list=[request_anchor])
#
#     feature_query = FeatureQuery(
#         feature_list=["f_is_long_trip_distance", "f_day_of_week"], key=location_id)
#
#     settings = ObservationSettings(
#         observation_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
#         event_timestamp_column="lpep_dropoff_datetime",
#         timestamp_format="yyyy-MM-dd HH:mm:ss")
#
#
#     client.get_offline_features(observation_settings=settings,
#                                 feature_query=feature_query,
#                                 output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro",
#                                 # udf_files=["./pyspark_client.py", "./client_udf_repo.py", "./__init__.py"]
#                                 # udf_files=["./client_udf_repo.py"]
#                                 udf_files=[]
#                                 )
#
#
#     now = datetime.now()
#     # set output folder based on different runtime
#     if client.spark_runtime == 'databricks':
#         output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
#     else:
#         output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".avro"])
#
#
#     client.get_offline_features(observation_settings=settings,
#                                 feature_query=feature_query,
#                                 output_path=output_path)
#
#     # assuming the job can successfully run; otherwise it will throw exception
#     client.wait_job_to_finish(timeout_sec=900)
#
#     # download result and just assert the returned result is not empty
#     res_df = get_result_df(client)
#     assert res_df.shape[0] > 0
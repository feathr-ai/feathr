import os
from datetime import datetime, timedelta
from pathlib import Path
from pyspark.sql.functions import col,sum,avg,max
from feathr.job_utils import get_result_df
from feathr.anchor import FeatureAnchor
from feathr.dtype import STRING, BOOLEAN, FLOAT, INT32, ValueType
from feathr.feature import Feature
from feathr.query_feature_list import FeatureQuery
from feathr.settings import ObservationSettings
from feathr.source import INPUT_CONTEXT, HdfsSource
from feathr.typed_key import TypedKey
from feathr.transformation import WindowAggTransformation
from pyspark.sql import SparkSession, DataFrame
from test_fixture import basic_test_setup
import pytest
from feathr import (BackfillTime, MaterializationSettings)
from feathr import RedisSink
from test_fixture import snowflake_test_setup

def trip_distance_preprocessing(df: DataFrame):
    df = df.withColumn("trip_distance", df.trip_distance.cast('double') - 90000)
    df = df.withColumn("fare_amount", df.fare_amount.cast('double') - 90000)

    return df

def add_new_dropoff_column(df: DataFrame):
    df = df.withColumn("new_lpep_dropoff_datetime", col("lpep_dropoff_datetime"))
    return df

def add_new_fare_amount(df: DataFrame) -> DataFrame:
    df = df.withColumn("fare_amount_new", col("fare_amount") + 1000000)

    return df

def feathr_udf_day_calc(df: DataFrame) -> DataFrame:
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    df = df.withColumn("f_day_of_year", dayofyear("lpep_dropoff_datetime"))
    return df


@pytest.mark.skip(reason="...")
def test_online_feature_with_offline_preprocessing():
    """
    Test feature gen with preprocessing
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    batch_source = HdfsSource(name="nycTaxiBatchSource",
                              path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                              preprocessing=add_new_fare_amount,
                              event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    pickup_time_as_id = TypedKey(key_column="lpep_pickup_datetime",
                                 key_column_type=ValueType.INT32,
                                 description="location id in NYC",
                                 full_name="nyc_taxi.location_id")

    features = [
        Feature(name="f_is_long_trip_distance",
                key=pickup_time_as_id,
                feature_type=FLOAT,
                transform="fare_amount_new"),
        Feature(name="f_day_of_week",
                key=pickup_time_as_id,
                feature_type=INT32,
                transform="dayofweek(lpep_dropoff_datetime)"),
    ]

    regular_anchor = FeatureAnchor(name="request_features",
                                   source=batch_source,
                                   features=features,
                                   )

    client.build_features(anchor_list=[regular_anchor])

    now = datetime.now()
    online_test_table = ''.join(['nycTaxiCITable', '_', str(now.minute), '_', str(now.second)])

    backfill_time = BackfillTime(start=datetime(
        2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings(name="py_udf",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_is_long_trip_distance",
                                           "f_day_of_week"
                                       ],
                                       backfill_time=backfill_time)
    client.materialize_features(settings)
    # just assume the job is successful without validating the actual result in Redis. Might need to consolidate
    # this part with the test_feathr_online_store test case
    client.wait_job_to_finish(timeout_sec=600)

    res = client.get_online_features(online_test_table, '2020-04-01 07:21:51', [
        'f_is_long_trip_distance', 'f_day_of_week'])
    assert res == [1000006.0, 3]


@pytest.mark.skip(reason="...")
def test_feature_swa_feature_gen_with_preprocessing():
    """
    Test SWA feature gen with preprocessing.
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    batch_source = HdfsSource(name="nycTaxiBatchSource",
                              path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                              preprocessing=add_new_dropoff_column,
                              event_timestamp_column="new_lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    location_id = TypedKey(key_column="DOLocationID",
                           key_column_type=ValueType.INT32,
                           description="location id in NYC",
                           full_name="nyc_taxi.location_id")


    agg_features = [Feature(name="f_location_avg_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="fare_amount",
                                                              agg_func="AVG",
                                                              window="90d")),
                    Feature(name="f_location_max_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="fare_amount",
                                                              agg_func="MAX",
                                                              window="90d"))
                    ]

    agg_anchor = FeatureAnchor(name="aggregationFeatures",
                               source=batch_source,
                               features=agg_features)

    client.build_features(anchor_list=[agg_anchor])

    now = datetime.now()
    online_test_table = ''.join(['nycTaxiCITable', '_', str(now.minute), '_', str(now.second)])

    backfill_time = BackfillTime(start=datetime(
        2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings(name="py_udf",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_location_avg_fare",
                                           "f_location_max_fare",
                                       ],
                                       backfill_time=backfill_time)
    client.materialize_features(settings)
    # just assume the job is successful without validating the actual result in Redis. Might need to consolidate
    # this part with the test_feathr_online_store test case
    client.wait_job_to_finish(timeout_sec=600)

    res = client.get_online_features(online_test_table, '265', ['f_location_avg_fare', 'f_location_max_fare'])
    assert res == [41.60617446899414, 97.23999786376953]

# @pytest.mark.skip(reason="...")
def test_feathr_get_offline_features_hdfs_source():
    """
    Test get offline features for blob storage
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    batch_source = HdfsSource(name="nycTaxiBatchSource",
                              path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                              preprocessing=add_new_dropoff_column,
                              event_timestamp_column="new_lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    batch_source2 = HdfsSource(name="nycTaxiBatchSource",
                              path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                              preprocessing=add_new_fare_amount,
                              event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")


    location_id = TypedKey(key_column="DOLocationID",
                           key_column_type=ValueType.INT32,
                           description="location id in NYC",
                           full_name="nyc_taxi.location_id")


    agg_features = [Feature(name="f_location_avg_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                                              agg_func="SUM",
                                                              window="90d")),
                    Feature(name="f_location_max_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                                              agg_func="MAX",
                                                              window="90d"))
                    ]

    agg_anchor = FeatureAnchor(name="aggregationFeatures",
                               source=batch_source,
                               features=agg_features,
                               )

    pickup_time_as_id = TypedKey(key_column="lpep_pickup_datetime",
                                 key_column_type=ValueType.INT32,
                                 description="location id in NYC",
                                 full_name="nyc_taxi.location_id")

    features = [
        Feature(name="f_is_long_trip_distance",
                key=pickup_time_as_id,
                feature_type=FLOAT,
                transform="fare_amount_new"),
        Feature(name="f_day_of_week",
                key=pickup_time_as_id,
                feature_type=INT32,
                transform="dayofweek(lpep_dropoff_datetime)"),
    ]

    request_anchor = FeatureAnchor(name="request_features",
                                   source=batch_source2,
                                   features=features,
                                   )

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


def snowflake_preprocessing(df: DataFrame) -> DataFrame:
    df = df.withColumn("NEW_CC_DIVISION_NAME", concat(col("CC_DIVISION_NAME"), lit("0000"), col("CC_DIVISION_NAME")))
    df = df.withColumn("NEW_CC_ZIP", concat(col("CC_ZIP"), lit("____"), col("CC_ZIP")))
    return df


@pytest.mark.skip(reason="...")
def test_feathr_get_offline_features_from_snowflake():
    """
    Test get_offline_features() can get feature data from Snowflake source correctly.
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
    client = snowflake_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    batch_source = HdfsSource(name="nycTaxiBatchSource",
                              path="jdbc:snowflake://dqllago-ol19457.snowflakecomputing.com/?user=feathrintegration"
                                   "&sfWarehouse=COMPUTE_WH&dbtable=CALL_CENTER&sfDatabase=SNOWFLAKE_SAMPLE_DATA"
                                   "&sfSchema=TPCDS_SF10TCL",
                              preprocessing=snowflake_preprocessing,
                              event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    call_sk_id = TypedKey(key_column="CC_CALL_CENTER_SK",
                          key_column_type=ValueType.STRING,
                          description="call center sk",
                          full_name="snowflake.CC_CALL_CENTER_SK")

    features = [
        Feature(name="f_snowflake_call_center_division_name_with_preprocessing",
                key=call_sk_id,
                feature_type=STRING,
                transform="NEW_CC_DIVISION_NAME"),
        Feature(name="f_snowflake_call_center_zipcode_with_preprocessing",
                key=call_sk_id,
                feature_type=STRING,
                transform="NEW_CC_ZIP"),
    ]

    feature_anchor = FeatureAnchor(name="snowflake_features",
                                   source=batch_source,
                                   features=features,
                                   )
    client.build_features(anchor_list=[feature_anchor])

    feature_query = FeatureQuery(
        feature_list=['f_snowflake_call_center_division_name_with_preprocessing', 'f_snowflake_call_center_zipcode_with_preprocessing'],
        key=call_sk_id)
    settings = ObservationSettings(
        observation_path='jdbc:snowflake://dqllago-ol19457.snowflakecomputing.com/?user=feathrintegration&sfWarehouse'
                         '=COMPUTE_WH&dbtable=CALL_CENTER&sfDatabase=SNOWFLAKE_SAMPLE_DATA&sfSchema=TPCDS_SF10TCL')

    now = datetime.now()
    # set output folder based on different runtime
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob_snowflake', '_', str(now.minute), '_', str(now.second), ".avro"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/snowflake_output','_', str(now.minute), '_', str(now.second), ".avro"])

    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path)

    # assuming the job can successfully run; otherwise it will throw exception
    client.wait_job_to_finish(timeout_sec=900)

    res = get_result_df(client)
    # just assume there are results.
    assert res.shape[0] > 1

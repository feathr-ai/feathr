import os
import asyncio
import unittest.mock as mock
import time
from subprocess import call
from datetime import datetime, timedelta

from pathlib import Path
from feathr import ValueType
from feathr import FeatureQuery
from feathr import ObservationSettings
from feathr import TypedKey
from test_fixture import get_online_test_table_name
from feathr.definition._materialization_utils import _to_materialization_config
from feathr import (BackfillTime, MaterializationSettings)
from feathr import (BackfillTime, MaterializationSettings, FeatureQuery, 
                    ObservationSettings, SparkExecutionConfiguration)
from feathr import RedisSink, HdfsSink
from feathr import (BOOLEAN, FLOAT, INPUT_CONTEXT, INT32, STRING,
                    DerivedFeature, Feature, FeatureAnchor, HdfsSource,
                    TypedKey, ValueType, WindowAggTransformation)
from feathr import FeathrClient

params = {"wait" : 0.1}
async def sample_callback(params):
    print(params)
    await asyncio.sleep(0.1)

callback = mock.MagicMock(return_value=sample_callback(params))


def basic_test_setup_with_callback(config_path: str, callback: callable):

    now = datetime.now()
    # set workspace folder by time; make sure we don't have write conflict if there are many CI tests running
    os.environ['SPARK_CONFIG__DATABRICKS__WORK_DIR'] = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), '_', str(now.microsecond)]) 
    os.environ['SPARK_CONFIG__AZURE_SYNAPSE__WORKSPACE_DIR'] = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/feathr_github_ci','_', str(now.minute), '_', str(now.second) ,'_', str(now.microsecond)]) 
    
    client = FeathrClient(config_path=config_path, callback=callback)
    batch_source = HdfsSource(name="nycTaxiBatchSource",
                              path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
                              event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    f_trip_distance = Feature(name="f_trip_distance",
                              feature_type=FLOAT, transform="trip_distance")
    f_trip_time_duration = Feature(name="f_trip_time_duration",
                                   feature_type=INT32,
                                   transform="(to_unix_timestamp(lpep_dropoff_datetime) - to_unix_timestamp(lpep_pickup_datetime))/60")

    features = [
        f_trip_distance,
        f_trip_time_duration,
        Feature(name="f_is_long_trip_distance",
                feature_type=BOOLEAN,
                transform="cast_float(trip_distance)>30"),
        Feature(name="f_day_of_week",
                feature_type=INT32,
                transform="dayofweek(lpep_dropoff_datetime)"),
    ]


    request_anchor = FeatureAnchor(name="request_features",
                                   source=INPUT_CONTEXT,
                                   features=features)

    f_trip_time_distance = DerivedFeature(name="f_trip_time_distance",
                                          feature_type=FLOAT,
                                          input_features=[
                                              f_trip_distance, f_trip_time_duration],
                                          transform="f_trip_distance * f_trip_time_duration")

    f_trip_time_rounded = DerivedFeature(name="f_trip_time_rounded",
                                         feature_type=INT32,
                                         input_features=[f_trip_time_duration],
                                         transform="f_trip_time_duration % 10")

    location_id = TypedKey(key_column="DOLocationID",
                           key_column_type=ValueType.INT32,
                           description="location id in NYC",
                           full_name="nyc_taxi.location_id")
    agg_features = [Feature(name="f_location_avg_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                                              agg_func="AVG",
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
                               features=agg_features)

    client.build_features(anchor_list=[agg_anchor, request_anchor], derived_feature_list=[
        f_trip_time_distance, f_trip_time_rounded])

    return client



def test_client_callback_offline_feature():
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
    client = basic_test_setup_with_callback(os.path.join(test_workspace_dir, "feathr_config.yaml"),callback)

    location_id = TypedKey(key_column="DOLocationID",
                            key_column_type=ValueType.INT32,
                            description="location id in NYC",
                            full_name="nyc_taxi.location_id")
    feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=location_id)
    
    settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")
    
    now = datetime.now()
    output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
    
    res = client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path, 
                                params=params)
    callback.assert_called_with(params)


def test_client_callback_materialization():
    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup_with_callback(os.path.join(test_workspace_dir, "feathr_config.yaml"),callback)
    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    client.materialize_features(settings, params=params)
    callback.assert_called_with(params)

def test_client_callback_monitor_features():
    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup_with_callback(os.path.join(test_workspace_dir, "feathr_config.yaml"),callback)
    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    client.monitor_features(settings, params=params)
    callback.assert_called_with(params)

def test_client_callback_get_online_features():
    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup_with_callback(os.path.join(test_workspace_dir, "feathr_config.yaml"),callback)
    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    client.materialize_features(settings)
    callback.assert_called_with(params)   
    client.wait_job_to_finish(timeout_sec=900)
    # wait for a few secs for the data to come in redis
    time.sleep(5)
    client.get_online_features('nycTaxiDemoFeature', '265', ['f_location_avg_fare', 'f_location_max_fare'], params=params)
    callback.assert_called_with(params)


def test_client_callback_multi_get_online_features():
    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup_with_callback(os.path.join(test_workspace_dir, "feathr_config.yaml"),callback)
    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    client.materialize_features(settings)
    callback.assert_called_with(params)   
    client.wait_job_to_finish(timeout_sec=900)
    # wait for a few secs for the data to come in redis
    time.sleep(5)
    client.multi_get_online_features('nycTaxiDemoFeature', ["239", "265"], ['f_location_avg_fare', 'f_location_max_fare'], params=params)
    callback.assert_called_with(params)
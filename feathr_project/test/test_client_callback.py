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
from test_fixture import basic_test_setup
from test_fixture import get_online_test_table_name
from feathr.definition._materialization_utils import _to_materialization_config
from feathr import (BackfillTime, MaterializationSettings)
from feathr import (BackfillTime, MaterializationSettings, FeatureQuery, 
                    ObservationSettings, SparkExecutionConfiguration)
from feathr import RedisSink, HdfsSink


params = {"wait" : 0.1}
async def sample_callback(params):
    print(params)
    await asyncio.sleep(0.1)

callback = mock.MagicMock(return_value=sample_callback(params))

def test_client_callback_offline_feature():
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

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
                                callback=callback, 
                                params=params)
    callback.assert_called_with(params)


def test_client_callback_materialization():
    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    client.materialize_features(settings, callback=callback, params=params)
    callback.assert_called_with(params)

def test_client_callback_monitor_features():
    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    client.monitor_features(settings, callback=callback, params=params)
    callback.assert_called_with(params)

def test_client_callback_get_online_features():
    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
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
    client.get_online_features('nycTaxiDemoFeature', '265', ['f_location_avg_fare', 'f_location_max_fare'], callback=callback, params=params)
    callback.assert_called_with(params)


def test_client_callback_multi_get_online_features():
    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
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
    client.multi_get_online_features('nycTaxiDemoFeature', ["239", "265"], ['f_location_avg_fare', 'f_location_max_fare'], callback=callback, params=params)
    callback.assert_called_with(params)
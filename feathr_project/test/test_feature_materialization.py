import os
from datetime import datetime, timedelta
from pathlib import Path

from feathr._materialization_utils import _to_materialization_config
from feathr import (BackfillTime, MaterializationSettings, FeatureQuery,
                    ObservationSettings, SparkExecutionConfiguration)
from feathr import RedisSink
from feathr import HdfsSink
from feathr.anchor import FeatureAnchor
from feathr.dtype import BOOLEAN, FLOAT, FLOAT_VECTOR, INT32, ValueType
from feathr.feature import Feature
from feathr.typed_key import TypedKey
from feathr import INPUT_CONTEXT
from test_fixture import basic_test_setup
from test_fixture import get_online_test_table_name

def test_feature_materialization_config():
    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5,20), step=timedelta(days=1))
    redisSink = RedisSink(table_name="nycTaxiDemoFeature")
    settings = MaterializationSettings("nycTaxiTable",
                                        sinks=[redisSink],
                                        feature_names=["f_location_avg_fare", "f_location_max_fare"],
                                        backfill_time=backfill_time)
    config = _to_materialization_config(settings)
    expected_config = """ 
        operational: {
            name: nycTaxiTable
            endTime: "2020-05-20 00:00:00"
            endTimeFormat: "yyyy-MM-dd HH:mm:ss"
            resolution: DAILY
            output:[
                {
                    name: REDIS
                    params: {
                        table_name: "nycTaxiDemoFeature"
                    }
                }
            ]
        }
        features: [f_location_avg_fare, f_location_max_fare]
        """
    assert ''.join(config.split()) == ''.join(expected_config.split())

def test_feature_materialization_offline_config():
    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5,20), step=timedelta(days=1))
    offlineSink = HdfsSink(output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output/hdfs_test.avro")
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[offlineSink],
                                       feature_names=["f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    config = _to_materialization_config(settings)
    expected_config = """ 
        operational: {
            name: nycTaxiTable
            endTime: "2020-05-20 00:00:00"
            endTimeFormat: "yyyy-MM-dd HH:mm:ss"
            resolution: DAILY
            output:[
                {
                    name: HDFS
                    params: {
                        path: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output/hdfs_test.avro"
                    }
                }
            ]
        }
        features: [f_location_avg_fare, f_location_max_fare]
        """
    assert ''.join(config.split()) == ''.join(expected_config.split())

def test_feature_materialization_daily_schedule():
    """Test back fill cutoff time for a daily range"""
    backfill_time = BackfillTime(start=datetime(2022, 3, 1), end=datetime(2022, 3, 5), step=timedelta(days=1))
    settings = MaterializationSettings("", [], [], backfill_time)
    expected = [datetime(2022, 3, day) for day in range(1, 6)]
    assert settings.get_backfill_cutoff_time() == expected


def test_feature_materialization_hourly_schedule():
    """Test back fill cutoff time for a hourly range"""
    backfill_time = BackfillTime(start=datetime(2022, 3, 1, 1), end=datetime(2022, 3, 1, 5), step=timedelta(hours=1))
    settings = MaterializationSettings("", [], [], backfill_time)
    expected = [datetime(2022,3, 1, hour) for hour in range(1, 6)]
    assert settings.get_backfill_cutoff_time() == expected


def test_feature_materialization_now_schedule():
    """Test back fill cutoff time without backfill."""
    settings = MaterializationSettings("", [], [])
    date = settings.get_backfill_cutoff_time()[0]
    expected = datetime.now()
    assert expected.year == date.year
    assert expected.month == date.month
    assert expected.day == date.day

def test_build_feature_verbose():
    """
    Test verbose for pretty printing features
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    # An anchor feature
    features = [
        Feature(name="trip_distance", feature_type=FLOAT),
        Feature(name="f_is_long_trip_distance",
                feature_type=BOOLEAN,
                transform="cast_float(trip_distance)>30"),
        Feature(name="f_day_of_week",
                feature_type=INT32,
                transform="dayofweek(lpep_dropoff_datetime)")
    ]

    anchor = FeatureAnchor(name="request_features",
                           source=INPUT_CONTEXT,
                           features=features)

    # Check pretty print
    client.build_features(anchor_list=[anchor], verbose=True)

def test_get_offline_features_verbose():
    """
    Test verbose for pretty printing feature query
    """

    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    location_id = TypedKey(key_column="DOLocationID",
                            key_column_type=ValueType.INT32)

    feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=location_id)

    settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss"
    )

    now = datetime.now()

    # set output folder based on different runtime
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".parquet"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".parquet"])

    # Check pretty print
    client.get_offline_features(
                                observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path,
                                execution_configuratons=SparkExecutionConfiguration({"spark.feathr.inputFormat": "parquet", "spark.feathr.outputFormat": "parquet"}),
                                verbose=True
                        )

def test_materialize_features_verbose():
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
    client.materialize_features(settings, verbose=True)
import os
from datetime import datetime, timedelta
from pathlib import Path

from feathr._materialization_utils import _to_materialization_config
from feathr import (BackfillTime, MaterializationSettings)
from feathr import RedisSink
from feathr.anchor import FeatureAnchor
from feathr.dtype import FLOAT, INT32, ValueType
from feathr.feature import Feature
from feathr.source import  HdfsSource
from feathr.typed_key import TypedKey
from pyspark.sql import DataFrame
from pyspark.sql.functions import col

from test_fixture import basic_test_setup

def add_new_fare_amount(df: DataFrame) -> DataFrame:
    df = df.withColumn("fare_amount_new", col("fare_amount") + 8000000)

    return df

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

def test_feature_materialization_now_schedule():
    """Test back fill cutoff time without backfill."""
    settings = MaterializationSettings("", [], [])
    date = settings.get_backfill_cutoff_time()[0]
    expected = datetime.now()
    assert expected.year == date.year
    assert expected.month == date.month
    assert expected.day == date.day

def test_build_feature_pretty_print_flag():
    """
    Test non-SWA feature gen with preprocessing
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    batch_source = HdfsSource(name="nycTaxiBatchSource_add_new_fare_amount",
                              path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
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

    regular_anchor = FeatureAnchor(name="request_features_add_new_fare_amount",
                                   source=batch_source,
                                   features=features,
                                   )

    client.build_features(anchor_list=[regular_anchor], pprint_flag=True)
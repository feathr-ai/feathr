from datetime import datetime, timedelta

from feathr._materialization_utils import _to_materialization_config
from feathr import (BackfillTime, MaterializationSettings)
from feathr import RedisSink


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

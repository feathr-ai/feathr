import pytest
from feathr.definition.settings import AbsoluteTimeRange, RelativeTimeRange, ObservationSettings


def test__observation_settings__with_use_latest_feature_data():
    settings = ObservationSettings(
        observation_path="some_observation_path",
        use_latest_feature_data=True,
    )
    config = settings.to_feature_config()
    expected_config = """
        settings: {
            joinTimeSettings: {
                useLatestFeatureData: true
            }
        }
        observationPath: "some_observation_path"
    """
    assert ''.join(config.split()) == ''.join(expected_config.split())


def test__observation_settings__with_use_latest_feature_data_and_join_time_settings__raise_exception():
    with pytest.raises(ValueError):
        ObservationSettings(
            observation_path="some_observation_path",
            event_timestamp_column="timestamp",
            use_latest_feature_data=True,
        )


def test__observation_settings__with_join_time_settings():
    settings = ObservationSettings(
        observation_path="some_observation_path",
        event_timestamp_column="timestamp",
        timestamp_format="yyyy-MM-dd",
    )
    config = settings.to_feature_config()
    expected_config = """
        settings: {
            joinTimeSettings: {
                timestampColumn: {
                    def: "timestamp"
                    format: "yyyy-MM-dd"
                }
            }
        }
        observationPath: "some_observation_path"
    """
    assert ''.join(config.split()) == ''.join(expected_config.split())


@pytest.mark.parametrize(
    "observation_time_range,expected_time_range_str", [
        (
            AbsoluteTimeRange(start_time="2020-01-01", end_time="2020-01-31", time_format="yyyy-MM-dd"),
            """absoluteTimeRange: {
                startTime: "2020-01-01"
                endTime: "2020-01-31"
                timeFormat: "yyyy-MM-dd"
            }""",
        ),
        (
            RelativeTimeRange(offset="1d", window="2d"),
            """relativeTimeRange: {
                offset: 1d
                window: 2d
            }""",
        )
    ],
)
def test__observation_settings__with_observation_data_time_settings(observation_time_range, expected_time_range_str):
    settings = ObservationSettings(
        observation_path="some_observation_path",
        observation_time_range=observation_time_range,
    )
    config = settings.to_feature_config()
    expected_config = f"""
        settings: {{
            observationDataTimeSettings: {{
                {expected_time_range_str}
            }}
        }}
        observationPath: "some_observation_path"
    """
    assert ''.join(config.split()) == ''.join(expected_config.split())


def test__observation_settings__without_join_and_observation_data_time_settings():
    settings = ObservationSettings(
        observation_path="some_observation_path",
    )
    config = settings.to_feature_config()
    expected_config = """
        observationPath: "some_observation_path"
    """
    assert ''.join(config.split()) == ''.join(expected_config.split())


def test__observation_settings():
    settings = ObservationSettings(
        observation_path="some_observation_path",
        event_timestamp_column="timestamp",
        timestamp_format="yyyy-MM-dd",
        observation_time_range=RelativeTimeRange(offset="1d", window="2d"),
    )
    config = settings.to_feature_config()
    expected_config = """
        settings: {
            observationDataTimeSettings: {
                relativeTimeRange: {
                    offset: 1d
                    window: 2d
                }
            }
            joinTimeSettings: {
                timestampColumn: {
                    def: "timestamp"
                    format: "yyyy-MM-dd"
                }
            }
        }
        observationPath: "some_observation_path"
    """

    assert ''.join(config.split()) == ''.join(expected_config.split())

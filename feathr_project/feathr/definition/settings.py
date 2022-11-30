from abc import ABC
from dataclasses import dataclass
from typing import Optional

from jinja2 import Template
from loguru import logger

from feathr.definition.feathrconfig import HoconConvertible


@dataclass
class BaseTimeRange(ABC):
    pass


@dataclass
class AbsoluteTimeRange(BaseTimeRange):
    start_time: str  # E.g. "2020-01-01"
    end_time: str    # E.g. "2020-01-31"
    time_format: str = "yyyy-MM-dd"


@dataclass
class RelativeTimeRange(BaseTimeRange):
    offset: str  # E.g. 1d
    window: str  # E.g. 3d


class ObservationSettings(HoconConvertible):
    """Time settings of the observation data. Used in feature join.

    Attributes:
        observation_path (str): Path to the observation dataset, i.e. input dataset to get with features.
        observation_data_time_settings (str): Settings which have parameters specifying how to load the observation data.
        join_time_settings (str): Settings which have parameters specifying how to join the observation data with the feature data.
    """
    observation_data_time_settings: str = None
    join_time_settings: str = None

    def __init__(
        self,
        observation_path: str,
        event_timestamp_column: Optional[str] = None,
        timestamp_format: Optional[str] = "epoch",
        use_latest_feature_data: Optional[bool] = False,
        observation_time_range: Optional[BaseTimeRange] = None,
    ) -> None:
        """Initialize observation settings.

        Args:
            observation_path: Path to the observation dataset, i.e. input dataset to get with features.
            event_timestamp_column: The timestamp field of your record as sliding window aggregation (SWA) feature
                assume each record in the source data should have a timestamp column.
            timestamp_format: The format of the timestamp field. Defaults to "epoch". Possible values are:
                - `epoch` (seconds since epoch), for example `1647737463`
                - `epoch_millis` (milliseconds since epoch), for example `1647737517761`
                - Any date formats supported by [SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html).
            use_latest_feature_data: Use the latest feature data when join. This should not be set together with event_timestamp_column
            observation_time_range: The time range of the observation data.
        """
        self.observation_path = observation_path
        if observation_path.startswith("http"):
            logger.warning("Your observation_path {} starts with http, which is not supported. Consider using paths starting with wasb[s]/abfs[s]/s3.", observation_path)

        # Settings which have parameters specifying how to load the observation data.
        if observation_time_range:
            if isinstance(observation_time_range, AbsoluteTimeRange):
                self.observation_data_time_settings = f"""{{
                            absoluteTimeRange: {{
                                startTime: "{observation_time_range.start_time}"
                                endTime: "{observation_time_range.end_time}"
                                timeFormat: "{observation_time_range.time_format}"
                            }}
                        }}"""
            elif isinstance(observation_time_range, RelativeTimeRange):
                self.observation_data_time_settings = f"""{{
                            relativeTimeRange: {{
                                offset: {observation_time_range.offset}
                                window: {observation_time_range.window}
                            }}
                        }}"""
            else:
                raise ValueError(f"Unsupported observation_time_range type {type(observation_time_range)}")

        # Settings which have parameters specifying how to join the observation data with the feature data.
        if use_latest_feature_data:
            if event_timestamp_column:
                raise ValueError("use_latest_feature_data cannot set together with event_timestamp_column")

            self.join_time_settings = """{
                        useLatestFeatureData: true
                    }"""
        elif event_timestamp_column:
            self.join_time_settings = f"""{{
                        timestampColumn: {{
                            def: "{event_timestamp_column}"
                            format: "{timestamp_format}"
                        }}
                    }}"""

        # TODO implement `simulateTimeDelay: 1d` -- This is the global setting, and should be applied to all the features
        # except those specified using timeDelayOverride (should introduce "timeDelayOverride" to Feature spec).

    def to_feature_config(self) -> str:
        tm = Template("""
                {% if (setting.observation_data_time_settings is not none) or (setting.join_time_settings is not none) %}
                settings: {
                    {% if setting.observation_data_time_settings is not none %}
                    observationDataTimeSettings: {{setting.observation_data_time_settings}}
                    {% endif %}
                    {% if setting.join_time_settings is not none %}
                    joinTimeSettings: {{setting.join_time_settings}}
                    {% endif %}
                }
                {% endif %}
                observationPath: "{{setting.observation_path}}"
            """)
        return tm.render(setting=self)

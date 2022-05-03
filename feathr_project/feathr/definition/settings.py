from typing import Optional
from jinja2 import Template
from loguru import logger
from feathr.frameconfig import HoconConvertible

class ObservationSettings(HoconConvertible):
    """Time settings of the observation data. Used in feature join.

    Attributes:
        observation_path: path to the observation dataset, i.e. input dataset to get with features
        event_timestamp_column (Optional[str]): The timestamp field of your record. As sliding window aggregation feature assume each record in the source data should have a timestamp column.
        timestamp_format (Optional[str], optional): The format of the timestamp field. Defaults to "epoch". Possible values are:
            - `epoch` (seconds since epoch), for example `1647737463`
            - `epoch_millis` (milliseconds since epoch), for example `1647737517761`
            - Any date formats supported by [SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html).
    """
    def __init__(self,
                 observation_path: str,
                 event_timestamp_column: Optional[str] = None,
                 timestamp_format: str = "epoch") -> None:
        self.event_timestamp_column = event_timestamp_column
        self.timestamp_format = timestamp_format
        self.observation_path = observation_path
        if observation_path.startswith("http"):
            logger.warning("Your observation_path {} starts with http, which is not supported. Consider using paths starting with wasb[s]/abfs[s]/s3.", observation_path)

    def to_feature_config(self) -> str:
        tm = Template("""
                {% if setting.event_timestamp_column is not none %}
                settings: {
                    joinTimeSettings: {
                        timestampColumn: {
                            def: "{{setting.event_timestamp_column}}"
                            format: "{{setting.timestamp_format}}"
                        }
                    }
                }
                {% endif %}
                observationPath: "{{setting.observation_path}}"
            """)
        return tm.render(setting=self)

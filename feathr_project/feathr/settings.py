from typing import Optional
from jinja2 import Template


class ObservationSettings:
    """Time settings of the observation data. Used in feature join.
    Attributes:
        observation_path: path to the observation dataset, i.e. input dataset to get with features
        event_timestamp_column: column name of the event timestamp
        timestamp_format: the format of the event_timestamp_column, e.g. yyyy/MM/DD.
    """
    def __init__(self,
                 observation_path: str,
                 event_timestamp_column: Optional[str] = None,
                 timestamp_format: str = "epoch") -> None:
        self.event_timestamp_column = event_timestamp_column
        self.timestamp_format = timestamp_format
        self.observation_path = observation_path

    def to_config(self) -> str:
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

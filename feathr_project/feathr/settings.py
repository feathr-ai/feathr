from typing import Optional
from jinja2 import Template


class ObservationSettings:
    """Time settings of the observation data. Used in feature join."""
    def __init__(self,
                 observation_path: str,
                 output_path: str,
                 event_timestamp_column: Optional[str] = None,
                 timestamp_format: str = "epoch") -> None:
        self.event_timestamp_column = event_timestamp_column
        self.timestamp_format = timestamp_format
        self.observation_path = observation_path
        self.output_path = output_path

    def to_config(self) -> str:
        tm = Template("""
            {% if event_timestamp_column is not none %}
            settings: {
                joinTimeSettings: {
                    timestampColumn: {
                        def: "{{setting.event_timestamp_column}}"
                        format: "{{setting.timestamp_format}}"
                    }
                }
            }
            
            observationPath: "{{setting.observation_path}}"
            outputPath: "{{setting.output_path}}"
            {% endif %}
        """)
        return tm.render(setting = self)

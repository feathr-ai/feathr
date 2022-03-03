from typing import Optional
from jinja2 import Template

class Settings:
    def __init__(self, event_timestamp_column: Optional[str] = None, timestamp_format: str = "epoch") -> None:
        self.event_timestamp_column = event_timestamp_column
        self.timestamp_format = timestamp_format

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
            {% endif %}
        """)
        return tm.render(setting = self)

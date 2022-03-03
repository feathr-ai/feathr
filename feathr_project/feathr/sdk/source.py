from typing import List, Optional
from jinja2 import Template

class SourceType:
    pass

class Source:
    def __init__(self,
                 name: str,
                 event_timestamp_column: Optional[str], 
                 timestamp_format: Optional[str] = "epoc") -> None:
        self.name = name
        self.event_timestamp_column = event_timestamp_column
        self.timestamp_format = timestamp_format

class PassthroughSource(Source):
    SOURCE_NAME = "PASSTHROUGH"
    def __init__(self) -> None:
        super().__init__(self.SOURCE_NAME, None, None)

    def to_feature_config(self) -> str:
        return "source: " + self.name

class ADLSSource(Source):
    def __init__(self, name: str, path: str, event_timestamp_column: Optional[str], timestamp_format: Optional[str] = "epoc") -> None:
        super().__init__(name, event_timestamp_column, timestamp_format)
        self.path = path

    def to_feature_config(self) -> str:
        tm = Template("""  
            {{source.name}}: {
                location: {path: "{{source.path}}"}
                {% if source.event_timestamp_column is defined %}
                    timeWindowParameters: {
                        timestampColumn: "{{source.event_timestamp_column}}"
                        timestampColumnFormat: "{{source.timestamp_format}}"
                    }
                {% endif %}
            } 
        """)
        msg = tm.render(source = self)
        return msg
    
PASSTHROUGH_SOURCE = PassthroughSource()
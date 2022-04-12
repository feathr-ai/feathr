from abc import ABC, abstractmethod
from typing import List, Optional
from jinja2 import Template


class Sink(ABC):
    @abstractmethod
    def to_write_config(self) -> str:
        pass


class RedisSink(Sink):
    """Redis-based sink use to store online feature data, can be used in batch job or streaming job.
    Attributes:
        table_name: output table name
        streaming: whether it is used in streaming mode
        streamingTimeoutMs: maximum running time for streaming mode. It is not used in batch mode.
    """
    def __init__(self, table_name: str, streaming: bool=False, streamingTimeoutMs: Optional[int]=None) -> None:
        self.table_name = table_name
        self.streaming = streaming
        self.streamingTimeoutMs = streamingTimeoutMs

    def to_write_config(self) -> str:
        """Produce the config used in feature materialization"""
        tm = Template("""  
            {
                name: REDIS
                params: {
                    table_name: "{{source.table_name}}"
                    {% if source.streaming %}
                    streaming: true
                    {% endif %}
                    {% if source.streamingTimeoutMs is defined %}
                    timeoutMs: {{source.streamingTimeoutMs}}
                    {% endif %}
                }
            }
        """)
        msg = tm.render(source=self)
        return msg
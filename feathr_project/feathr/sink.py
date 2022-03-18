from abc import ABC, abstractmethod
from typing import List, Optional
from jinja2 import Template


class Sink(ABC):
    @abstractmethod
    def to_write_config(self) -> str:
        pass


class RedisSink(Sink):
    def __init__(self, table_name: str) -> None:
        self.table_name = table_name

    def to_write_config(self) -> str:
        """Produce the config used in feature materialization"""
        tm = Template("""  
            {
                name: REDIS
                params: {
                    table_name: "{{source.table_name}}"
                }
            }
        """)
        msg = tm.render(source=self)
        return msg
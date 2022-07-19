from abc import abstractmethod
import json
from typing import Dict, List, Optional
from jinja2 import Template
from pymysql import paramstyle
from feathr.definition.feathrconfig import HoconConvertible


class Sink(HoconConvertible):
    """A data sink.
    """
    
    @abstractmethod
    def to_argument(self):
        pass

class MonitoringSqlSink(Sink):
    """SQL-based sink that stores feature monitoring results.

    Attributes:
        table_name: output table name
    """
    def __init__(self, table_name: str) -> None:
        self.table_name = table_name

    def to_feature_config(self) -> str:
        """Produce the config used in feature monitoring"""
        tm = Template("""  
            {
                name: MONITORING
                params: {
                    table_name: "{{source.table_name}}"
                }
            }
        """)
        msg = tm.render(source=self)
        return msg

    def to_argument(self):
        raise TypeError("MonitoringSqlSink cannot be used as output argument")

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

    def to_feature_config(self) -> str:
        """Produce the config used in feature materialization"""
        tm = Template("""  
            {
                name: REDIS
                params: {
                    table_name: "{{source.table_name}}"
                    {% if source.streaming %}
                    streaming: true
                    {% endif %}
                    {% if source.streamingTimeoutMs %}
                    timeoutMs: {{source.streamingTimeoutMs}}
                    {% endif %}
                }
            }
        """)
        msg = tm.render(source=self)
        return msg

    def to_argument(self):
        raise TypeError("RedisSink cannot be used as output argument")


class HdfsSink(Sink):
    """Offline Hadoop HDFS-compatible(HDFS, delta lake, Azure blog storage etc) sink that is used to store feature data.
    The result is in AVRO format.

    Attributes:
        output_path: output path
    """
    def __init__(self, output_path: str) -> None:
        self.output_path = output_path

    # Sample generated HOCON config:
    # operational: {
    #     name: testFeatureGen
    #     endTime: 2019-05-01
    #     endTimeFormat: "yyyy-MM-dd"
    #     resolution: DAILY
    #     output:[
    #         {
    #             name: HDFS
    #             params: {
    #                 path: "/user/featureGen/hdfsResult/"
    #             }
    #         }
    #     ]
    # }
    # features: [mockdata_a_ct_gen, mockdata_a_sample_gen]
    def to_feature_config(self) -> str:
        """Produce the config used in feature materialization"""
        tm = Template("""  
            {
                name: HDFS
                params: {
                    path: "{{sink.output_path}}"
                }
            }
        """)
        hocon_config = tm.render(sink=self)
        return hocon_config

    def to_argument(self):
        return self.output_path

    
class GenericSink(Sink):
    def __init__(self, format: str, mode: Optional[str] = None, options: Dict[str, str] = {}) -> None:
        self.format = format
        self.mode = mode
        self.options = dict([(o.replace(".", "__"), options[o]) for o in options])
    
    def to_feature_config(self) -> str:
        ret = {
            "name": "HDFS",
            "params": self._to_dict()
        }
        return json.dumps(ret, indent=4)
    
    def _to_dict(self) -> dict[str, str]:
        ret = self.options.copy()
        ret["type"] = "generic"
        ret["format"] = self.format
        if self.mode:
            ret["mode"] = self.mode
        return ret        
    
    def get_required_properties(self):
        ret = []
        for option in self.options:
            start = option.find("${")
            if start >= 0:
                end = option[start:].find("}")
                if end >= 0:
                    ret.append(option[start+2:start+end])
        return ret

    def to_argument(self):
        """
        One-line JSON string, used by job submitter
        """
        return json.dumps(self._to_dict())
    
class CosmosDbSink(GenericSink):
    def __init__(self, name: str, endpoint: str, database: str, collection: str):        
        super().__init__(format = "cosmos.oltp", mode="APPEND", options={
            "spark.cosmos.accountEndpoint": endpoint,
            'spark.cosmos.accountKey': "${%s_KEY}" % name,
            "spark.cosmos.database": database,
            "spark.cosmos.container": collection
        })
        self.name = name
        self.endpoint = endpoint
        self.database = database
        self.collection = collection
        
    def get_required_properties(self) -> List[str]:
        return [self.name + "_KEY"]

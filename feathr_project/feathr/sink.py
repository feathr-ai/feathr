from typing import List, Optional
from jinja2 import Template
from feathr.frameconfig import HoconConvertible


class Sink(HoconConvertible):
    """A data sink.
    """
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

from abc import abstractmethod
import copy
from typing import Callable, Dict, List, Optional
from feathr.definition.feathrconfig import HoconConvertible

from jinja2 import Template
from loguru import logger
import json


class SourceSchema(HoconConvertible):
    pass


class AvroJsonSchema(SourceSchema):
    """Avro schema written in Json string form."""

    def __init__(self, schemaStr: str):
        self.schemaStr = schemaStr

    def to_feature_config(self):
        """Convert the feature anchor definition into internal HOCON format."""
        tm = Template("""
        schema: {
            type = "avro"
            avroJson:{{avroJson}}
        }
        """)
        avroJson = json.dumps(self.schemaStr)
        msg = tm.render(schema=self, avroJson=avroJson)
        return msg


class Source(HoconConvertible):
    """External data source for feature. Typically a data 'table'.

    Attributes:
         name: name of the source. It's used to differentiate from other sources.
         event_timestamp_column: column name or field name of the event timestamp
         timestamp_format: the format of the event_timestamp_column, e.g. yyyy/MM/DD, or EPOCH
         registry_tags: A dict of (str, str) that you can pass to feature registry for better organization.
                        For example, you can use {"deprecated": "true"} to indicate this source is deprecated, etc.
    """

    def __init__(self,
                 name: str,
                 event_timestamp_column: Optional[str] = "0",
                 timestamp_format: Optional[str] = "epoch",
                 registry_tags: Optional[Dict[str, str]] = None,
                 ) -> None:
        self.name = name
        self.event_timestamp_column = event_timestamp_column
        self.timestamp_format = timestamp_format
        self.registry_tags = registry_tags

    def __eq__(self, other):
        """A source is equal to another if name is equal."""
        return self.name == other.name

    def __hash__(self):
        """A source can be identified with the name"""
        return hash(self.name)

    def __str__(self):
        return self.to_feature_config()
    
    @abstractmethod
    def to_argument(self):
        pass


class InputContext(Source):
    """'Passthrough' source, a.k.a. request feature source. Feature source data is from the observation data itself.
    For example, you have an observation data table t1 and another feature data table t2. Some of your feature data
    can be transformed from the observation data table t1 itself, like geo location, then you can define that feature
    on top of the InputContext.
    """
    __SOURCE_NAME = "PASSTHROUGH"

    def __init__(self) -> None:
        super().__init__(self.__SOURCE_NAME, None, None)

    def to_feature_config(self) -> str:
        return "source: " + self.name

    def to_argument(self):
        raise TypeError("InputContext cannot be used as observation source")


class HdfsSource(Source):
    """A data source(table) stored on HDFS-like file system. Data can be fetch through a POSIX style path.

    Attributes:
        name (str): name of the source
        path (str): The location of the source data.
        preprocessing (Optional[Callable]): A preprocessing python function that transforms the source data for further feature transformation.
        event_timestamp_column (Optional[str]): The timestamp field of your record. As sliding window aggregation feature assume each record in the source data should have a timestamp column.
        timestamp_format (Optional[str], optional): The format of the timestamp field. Defaults to "epoch". Possible values are:
                                                    - `epoch` (seconds since epoch), for example `1647737463`
                                                    - `epoch_millis` (milliseconds since epoch), for example `1647737517761`
                                                    - Any date formats supported by [SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html).
        registry_tags: A dict of (str, str) that you can pass to feature registry for better organization. For example, you can use {"deprecated": "true"} to indicate this source is deprecated, etc.
    """

    def __init__(self, name: str, path: str, preprocessing: Optional[Callable] = None, event_timestamp_column: Optional[str] = None, timestamp_format: Optional[str] = "epoch", registry_tags: Optional[Dict[str, str]] = None) -> None:
        super().__init__(name, event_timestamp_column,
                         timestamp_format, registry_tags=registry_tags)
        self.path = path
        self.preprocessing = preprocessing
        if path.startswith("http"):
            logger.warning(
                "Your input path {} starts with http, which is not supported. Consider using paths starting with wasb[s]/abfs[s]/s3.", path)

    def to_feature_config(self) -> str:
        tm = Template("""  
            {{source.name}}: {
                location: {path: "{{source.path}}"}
                {% if source.event_timestamp_column %}
                    timeWindowParameters: {
                        timestampColumn: "{{source.event_timestamp_column}}"
                        timestampColumnFormat: "{{source.timestamp_format}}"
                    }
                {% endif %}
            } 
        """)
        msg = tm.render(source=self)
        return msg

    def __str__(self):
        return str(self.preprocessing) + '\n' + self.to_feature_config()

    def to_argument(self):
        return self.path

class JdbcSource(Source):
    def __init__(self, name: str, url: str = "", dbtable: Optional[str] = None, query: Optional[str] = None, auth: Optional[str] = None, preprocessing: Optional[Callable] = None, event_timestamp_column: Optional[str] = None, timestamp_format: Optional[str] = "epoch", registry_tags: Optional[Dict[str, str]] = None) -> None:
        super().__init__(name, event_timestamp_column, timestamp_format, registry_tags)
        self.preprocessing = preprocessing
        self.url = url
        if dbtable is not None:
            self.dbtable = dbtable
        if query is not None:
            self.query = query
        if auth is not None:
            self.auth = auth.upper()
            if self.auth not in ["USERPASS", "TOKEN"]:
                raise ValueError(
                    "auth must be None or one of following values: ['userpass', 'token']")

    def get_required_properties(self):
        if not hasattr(self, "auth"):
            return []
        if self.auth == "USERPASS":
            return ["%s_USER" % self.name.upper(), "%s_PASSWORD" % self.name.upper()]
        elif self.auth == "TOKEN":
            return ["%s_TOKEN" % self.name.upper()]

    def to_feature_config(self) -> str:
        tm = Template("""  
            {{source.name}}: {
                location: {
                    type: "jdbc"
                    url: "{{source.url}}"
                    {% if source.dbtable is defined %}
                    dbtable: "{{source.dbtable}}"
                    {% endif %}
                    {% if source.query is defined %}
                    query: "{{source.query}}"
                    {% endif %}
                    {% if source.auth is defined %}
                        {% if source.auth == "USERPASS" %}
                    user: "${{ "{" }}{{source.name}}_USER{{ "}" }}"
                    password: "${{ "{" }}{{source.name}}_PASSWORD{{ "}" }}"
                        {% else %}
                    useToken: true
                    token: "${{ "{" }}{{source.name}}_TOKEN{{ "}" }}"
                        {% endif %}
                    {% else %}
                    anonymous: true
                    {% endif %}
                }
                {% if source.event_timestamp_column is defined %}
                    timeWindowParameters: {
                        timestampColumn: "{{source.event_timestamp_column}}"
                        timestampColumnFormat: "{{source.timestamp_format}}"
                    }
                {% endif %}
            } 
        """)
        source = copy.copy(self)
        source.name = self.name.upper()
        msg = tm.render(source=source)
        return msg

    def __str__(self):
        return str(self.preprocessing) + '\n' + self.to_feature_config()

    def to_argument(self):
        d = {
            "type": "jdbc",
            "url": self.url,
        }
        if hasattr(self, "dbtable"):
            d["dbtable"] = self.dbtable
        if hasattr(self, "query"):
            d["query"] = self.query
        if hasattr(self, "auth"):
            if self.auth == "USERPASS":
                d["user"] = "${" + self.name.upper() + "_USER}"
                d["password"] = "${" + self.name.upper() + "_PASSWORD}"
            elif self.auth == "TOKEN":
                d["useToken"] = True
                d["token"] = "${" + self.name.upper() + "_TOKEN}"
        else:
            d["anonymous"] = True
        return json.dumps(d)

class KafkaConfig:
    """Kafka config for a streaming source

    Attributes:
        brokers: broker/server address
        topics: Kafka topics
        schema: Kafka message schema
        """

    def __init__(self, brokers: List[str], topics: List[str], schema: SourceSchema):
        self.brokers = brokers
        self.topics = topics
        self.schema = schema


class KafKaSource(Source):
    """A kafka source object. Used in streaming feature ingestion."""

    def __init__(self, name: str, kafkaConfig: KafkaConfig):
        super().__init__(name)
        self.config = kafkaConfig

    def to_feature_config(self) -> str:
        tm = Template("""
{{source.name}}: {
    type: KAFKA
    config: {
        brokers: [{{brokers}}]
        topics: [{{topics}}]
        {{source.config.schema.to_feature_config()}}
    }
}
        """)
        brokers = '"'+'","'.join(self.config.brokers)+'"'
        topics = ','.join(self.config.topics)
        msg = tm.render(source=self, brokers=brokers, topics=topics)
        return msg

    def to_argument(self):
        raise TypeError("KafKaSource cannot be used as observation source")

class GenericSource(Source):
    """
    This class is corresponding to 'GenericLocation' in Feathr core, but only be used as Source.
    The class is not meant to be used by user directly, user should use its subclasses like `CosmosDbSource`
    """
    def __init__(self, name: str, format: str, mode: Optional[str] = None, options: Dict[str, str] = {}, preprocessing: Optional[Callable] = None, event_timestamp_column: Optional[str] = None, timestamp_format: Optional[str] = "epoch", registry_tags: Optional[Dict[str, str]] = None) -> None:
        super().__init__(name, event_timestamp_column,
                         timestamp_format, registry_tags=registry_tags)
        self.source_type = 'generic'
        self.format = format
        self.mode = mode
        self.preprocessing = preprocessing
        # Feathr Core uses HOCON format config, `.` will disrupt parsing.
        # In Feathr Core, GenericLocation will replace `__` back to `.`
        self.options = dict([(key.replace(".", "__"), options[key]) for key in options])

    def to_feature_config(self) -> str:
        tm = Template("""  
            {{source.name}}: {
                location: {
                    type: "generic"
                    format: "{{source.format}}"
                    {% if source.mode is defined %}
                    mode: "{{source.mode}}"
                    {% endif %}
                    {% for option in source.options %}
                    {{option.key}}: "{{option.value}}"
                    {% endfor %}
                }
                {% if source.event_timestamp_column is defined %}
                timeWindowParameters: {
                    timestampColumn: "{{source.event_timestamp_column}}"
                    timestampColumnFormat: "{{source.timestamp_format}}"
                }
                {% endif %}
            } 
        """)
        msg = tm.render(source=self)
        return msg

    def get_required_properties(self):
        ret = []
        for option in self.options:
            start = option.find("${")
            if start >= 0:
                end = option[start:].find("}")
                if end >= 0:
                    ret.append(option[start+2:start+end])
        return ret

    def to_dict(self) -> Dict[str, str]:
        ret = self.options.copy()
        ret["type"] = "generic"
        ret["format"] = self.format
        if self.mode:
            ret["mode"] = self.mode
        return ret        
    
    def to_argument(self):
        """
        One-line JSON string, used by job submitter
        """
        return json.dumps(self.to_dict())


class CosmosDbSource(GenericSource):
    """
    Use CosmosDb as the data source
    """
    def __init__(self,
                 name: str,
                 endpoint: str,
                 database: str,
                 container: str,
                 preprocessing: Optional[Callable] = None,
                 event_timestamp_column: Optional[str] = None,
                 timestamp_format: Optional[str] = "epoch",
                 registry_tags: Optional[Dict[str, str]] = None):
        options = {
            'spark.cosmos.accountEndpoint': endpoint,
            'spark.cosmos.accountKey': "${%s_KEY}" % name.upper(),
            'spark.cosmos.database': database,
            'spark.cosmos.container': container
        }
        super().__init__(name,
                         format='cosmos.oltp',
                         mode="APPEND",
                         options=options,
                         preprocessing=preprocessing,
                         event_timestamp_column=event_timestamp_column, timestamp_format=timestamp_format,
                         registry_tags=registry_tags)

class ElasticSearchSource(GenericSource):
    """
    Use ElasticSearch as the data source
    """
    def __init__(self,
                 name: str,
                 host: str,
                 index: str,
                 ssl: bool = True,
                 auth: bool = True,
                 preprocessing: Optional[Callable] = None,
                 event_timestamp_column: Optional[str] = None,
                 timestamp_format: Optional[str] = "epoch",
                 registry_tags: Optional[Dict[str, str]] = None):
        """
        name: The name of the sink.
        host: ElasticSearch node, can be `hostname` or `hostname:port`, default port is 9200.
        index: The index to read the data.
        ssl: Set to `True` to enable SSL.
        auth: Set to `True` to enable authentication, you need to provide username/password from environment or KeyVault.
        preprocessing/event_timestamp_column/timestamp_format/registry_tags: See `HdfsSource`
        """
        options = {
            'es.nodes': host,
            'es.ssl': str(ssl).lower(),
            'es.resource': index,
        }
        if auth:
            options["es.net.http.auth.user"] = "${%s_USER}" % name.upper(),
            options["es.net.http.auth.pass"] = "${%s_PASSWORD}" % name.upper(),
        super().__init__(name,
                         format='org.elasticsearch.spark.sql',
                         mode="APPEND",
                         options=options,
                         preprocessing=preprocessing,
                         event_timestamp_column=event_timestamp_column, timestamp_format=timestamp_format,
                         registry_tags=registry_tags)

INPUT_CONTEXT = InputContext()

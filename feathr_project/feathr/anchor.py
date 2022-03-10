from typing import List, Optional
from feathr.feature import Feature
from feathr.source import Source
from jinja2 import Template

# passthrough features do not need keys
class FeatureAnchor:
    """
    A feature anchor defines a set of features on top of a data source, a.k.a. a set of features anchored to a source.

    A feature can also be anchored to multiple sources via multiple anchors. e.g. in online, it anchors to
    data source A via anchor foo, while in offline, it anchors to data source B via anchor bar. It is possible
    to anchor a feature to multiple sources, in multiple environments(offline, online, streaming, etc.)
    in a single anchor.

    The feature producer writes multiple anchors for a feature, exposing the same feature name for the feature
    consumer to reference it.
    Attributes:
        full_name: Unique name of the anchor. Recommend using [project_name].[anchor_name], e.g. foo.bar
        source: data source that the features are anchored to.
        features: the feature definitions within this anchor.
    """
    def __init__(self,
                name: str,
                source: Source,
                features: List[Feature]):
        self.name = name
        self.features = features
        self.source = source

    def to_feature_config(self) -> str:
        tm = Template("""
            {{anchor_name}}: {
                source: {{source.name}}
                key: [{{key_list}}]
                features: {
                    {% for feature in features %}
                        {{feature.to_feature_config()}}
                    {% endfor %}
                }
            }
        """)     
        key_list = ','.join(key for key in self.features[0].key_alias) 
        return tm.render(anchor_name = self.name,
                        key_list = key_list,
                        features = self.features,
                        source = self.source)

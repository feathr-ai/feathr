from typing import List, Optional
from feathr.feature import Feature
from feathr.source import Source
from jinja2 import Template

# passthrough features do not need keys
class FeatureAnchor:
    """
    A feature anchor defines a set of features on top of a data source, a.k.a. a set of features anchored to a source.

    The feature producer writes multiple anchors for a feature, exposing the same feature name for the feature
    consumer to reference it.
    Attributes:
        name: Unique name of the anchor. 
        source: data source that the features are anchored to. Should be either of `INPUT_CONTEXT` or `feathr.source.Source`
        features: list of features within this anchor. 
    """
    def __init__(self,
                name: str,
                source: Source,
                features: List[Feature]):
        self.name = name
        self.features = features
        self.source = source
        self.validate_features()

    def validate_features(self):
        """Validate that anchor is non-empty and all its features share the same key"""
        assert len(self.features) > 0
        for feature in self.features:
            assert feature.key_alias == self.features[0].key_alias

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

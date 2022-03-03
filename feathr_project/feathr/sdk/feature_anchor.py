from typing import List, Optional
from feathr.sdk.feature import Feature
from feathr.sdk.source import Source
from jinja2 import Template

from pyhocon import ConfigFactory

# passthrough features do not need keys
DUMMY_KEY = ["NOT_NEEDED"]
class FeatureAnchor:
    """
    A feature anchor defines a set of features that are "anchored" to
    some source with specific schema.
    """
    def __init__(self,
                 name: str,
                 batch_source: Source,
                 features: List[Feature],
                 key_columns: Optional[List[str]] = None):
        self.name = name
        self.features = features
        self.key_columns = key_columns if key_columns else DUMMY_KEY
        self.batch_source = batch_source

    def to_feature_config(self) -> str:
        tm = Template("""
        // THIS FILE IS AUTO GENERATED. PLEASE DO NOT EDIT.
        anchors: {
            {{anchor_name}}: {
                source: {{source.name}}
                key: [{{key_list}}]
                features: {
                    {% for feature in features %}
                        {{feature.to_feature_config()}}
                    {% endfor %}
                }
            }
        }
        
        {% if not source.name == "PASSTHROUGH" %}
        sources: {
            {{source.to_feature_config()}}
        }
        {% endif %}
        """)
        key_list = ','.join(key for key in self.key_columns)
        return tm.render(anchor_name = self.name,
                        key_list = key_list,
                        features = self.features,
                        source = self.batch_source)


from typing import Dict, Optional
from feathr.sdk.dtype import ValueType, FeatureType
from feathr.sdk.transformation import RowTransformation
from feathr.sdk.transformation import ExprTransform

from jinja2 import Template

class Feature:
    def __init__(self,
                name: str,
                feature_type: FeatureType,
                transform: Optional[RowTransformation] = None):
        self.name = name
        self.feature_type = feature_type
        self.transform = transform if transform else ExprTransform(name)
    
    def to_feature_config(self) -> str:
        tm = Template("""
            {{feature.name}}: {
                {{feature.feature_type.to_feature_config()}} 
                {{feature.transform.to_feature_config()}}
            }
        """)
        return tm.render(feature = self)

from typing import Dict, Optional
from feathr.dtype import ValueType, FeatureType
from feathr.transformation import RowTransformation
from feathr.transformation import ExpressionTransformation

from jinja2 import Template

class Feature:
    """Represents a feature object, which has a name, feature type, and a transformation that
    produces its feature value."""
    def __init__(self,
                name: str,
                feature_type: FeatureType,
                transform: Optional[RowTransformation] = None):
        self.name = name
        self.feature_type = feature_type
        # If no transformation is specified, default to referencing the a field with the same name
        self.transform = transform if transform else ExpressionTransformation(name)
    
    def to_feature_config(self) -> str:
        tm = Template("""
            {{feature.name}}: {
                {{feature.feature_type.to_feature_config()}} 
                {{feature.transform.to_feature_config()}}
            }
        """)
        return tm.render(feature=self)
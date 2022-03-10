from copy import copy, deepcopy
from typing import List, Optional, Union

from jinja2 import Template

from feathr.dtype import FeatureType
from feathr.feature import FeatureBase
from feathr.transformation import RowTransformation
from feathr.typed_key import DUMMY_KEY, TypedKey


class DerivedFeature(FeatureBase):
    """A derived feature is a feature defined on top of other features, rather than external data source.
    Attributes:
        name: derived feature name
        feature_type: type of derived feature
        key: join key of the derived feature
        input_features: features that this derived features depends on
        transform: transformation that produces the derived feature value, based on the input_features
        feature_alias: Rename the derived feature to `feature_alias`. Default to None.
        key_alias: Rename the key of derived feature to `key`. Default to None.
    """

    def __init__(self,
                name: str,
                feature_type: FeatureType,
                input_features: Union[FeatureBase, List[FeatureBase]],
                transform: Union[str, RowTransformation],
                key: Optional[Union[TypedKey, List[TypedKey]]] = [DUMMY_KEY],
                feature_alias:Optional[str] = None,
                key_alias: Optional[List[str]] = None) -> None:
        super(DerivedFeature, self).__init__(name, feature_type, key=key, transform=transform, feature_alias=feature_alias, key_alias=key_alias)
        self.input_features = input_features if isinstance(input_features, List) else [input_features]

    def to_feature_config(self) -> str:
        tm = Template("""
            {{derived_feature.name}}: {
                key: [{{','.join(derived_feature.key_alias)}}]
                inputs: {
                    {% for feature in derived_feature.input_features %}
                        {{feature.feature_alias}}: {
                            key: [{{','.join(feature.key_alias)}}],
                            feature: {{feature.name}}
                        }
                    {% endfor %}
                }
                definition: {{derived_feature.transform.to_feature_config()}}
                {{derived_feature.feature_type.to_feature_config()}}
            }
        """)
        return tm.render(derived_feature=self)

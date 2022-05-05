from typing import List, Optional, Union, Dict

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
        key: All features with corresponding keys that this derived feature depends on
        input_features: features that this derived features depends on
        transform: transformation that produces the derived feature value, based on the input_features
        registry_tags: A dict of (str, str) that you can pass to feature registry for better organization. For example, you can For example, you can use {"deprecated": "true"} to indicate this feature is deprecated, etc.
    """

    def __init__(self,
                name: str,
                feature_type: FeatureType,
                input_features: Union[FeatureBase, List[FeatureBase]],
                transform: Union[str, RowTransformation],
                key: Optional[Union[TypedKey, List[TypedKey]]] = [DUMMY_KEY],
                registry_tags: Optional[Dict[str, str]] = None,
                ):
        super(DerivedFeature, self).__init__(name, feature_type, key=key, transform=transform, registry_tags=registry_tags)
        self.input_features = input_features if isinstance(input_features, List) else [input_features]
        self.validate_feature()

    def validate_feature(self):
        """Validate the derived feature is valid"""
        # Validate key alias
        input_feature_key_alias = []
        for feature in self.input_features:
            input_feature_key_alias.extend(feature.key_alias)
        for key_alias in self.key_alias:
            assert key_alias in input_feature_key_alias, "key alias {} in derived feature {} must come from " \
                   "its input features key alias list {}".format(key_alias, self.name, input_feature_key_alias)


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
                definition: {{derived_feature.transform.to_feature_config(False)}}
                {{derived_feature.feature_type.to_feature_config()}}
            }
        """)
        return tm.render(derived_feature=self)

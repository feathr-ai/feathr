from abc import ABC, abstractmethod
from copy import deepcopy
from typing import List, Optional, Union

from jinja2 import Template

from feathr.dtype import FeatureType
from feathr.transformation import ExpressionTransformation, Transformation
from feathr.typed_key import DUMMY_KEY, TypedKey


class FeatureBase(ABC):
    """The base class for features
    It has a feature name, feature type, and a convenient transformation used to produce its feature value.
    Attributes:
        name: Unique name of the feature. Only alphabet, numbers, and '_' are allowed in the name.
                It can not start with numbers. Note that '.' is NOT ALLOWED!
        feature_type: the feature value type. e.g. INT32, FLOAT, etc. feathr.dtype
        key: The key of this feature. e.g. user_id.
        transform: A transformation used to produce its feature value. e.g. amount * 10
    """
    def __init__(self,
                 name: str,
                 feature_type: FeatureType,
                 transform: Optional[Union[str, Transformation]] = None,
                 key: Optional[Union[TypedKey, List[TypedKey]]] = [DUMMY_KEY]):
        self.name = name
        self.feature_type = feature_type
        self.key = key if isinstance(key, List) else [key]
        # feature_alias: Rename the derived feature to `feature_alias`. Default to feature name.
        self.feature_alias = name
        # If no transformation is specified, default to referencing the a field with the same name
        if transform is None:
            self.transform = ExpressionTransformation(name)
        elif isinstance(transform, str):
            self.transform = ExpressionTransformation(transform)
        else:
            self.transform = transform
        # An alias for the key in this feature. Default to its key column alias. Useful in derived features.
        self.key_alias = [k.key_column_alias for k in self.key]

    def with_key(self, key_alias: Union[str, List[str]]):
        """Rename the feature key with the alias. This is useful in derived features that depends on
        the same feature with different keys."""
        cleaned_key_alias = [key_alias] if isinstance(key_alias, str) else key_alias
        assert(len(cleaned_key_alias) == len(self.key))
        new_key = []
        for i in range(0, len(cleaned_key_alias)):
            typed_key = deepcopy(self.key[i])
            typed_key.key_column_alias = cleaned_key_alias[i]
            new_key.append(typed_key)

        res = deepcopy(self)
        res.key = new_key
        res.key_alias = cleaned_key_alias
        return res

    def as_feature(self, feature_alias):
        """Provide the feature a different alias, which can be used to reference the feature in transformation
        expression. This is useful in derived features that depends on the same feature with different keys."""
        new_feature = deepcopy(self)
        new_feature.feature_alias = feature_alias
        return new_feature

    @abstractmethod
    def to_feature_config(self) -> str:
        pass


class Feature(FeatureBase):
    """A feature is an individual measurable property or characteristic of an entity.
    It has a feature name, feature type, and a convenient row transformation used to produce its feature value.
    Attributes:
        name: Unique name of the feature. Only alphabet, numbers, and '_' are allowed in the name.
                It can not start with numbers. Note that '.' is NOT ALLOWED!
        feature_type: the feature value type. e.g. INT32, FLOAT, etc. Should be part of `feathr.dtype`
        key: The key of this feature. e.g. user_id.
        transform: A row transformation used to produce its feature value. e.g. amount * 10
    """
    def __init__(self,
                name: str,
                feature_type: FeatureType,
                key: Optional[Union[TypedKey, List[TypedKey]]] = [DUMMY_KEY],
                transform: Optional[Union[str, Transformation]] = None):
        super(Feature, self).__init__(name, feature_type, transform, key)

    def to_feature_config(self) -> str:
        tm = Template("""
            {{feature.name}}: {
                def:{{feature.transform.to_feature_config()}}
                {{feature.feature_type.to_feature_config()}} 
            }
        """)
        return tm.render(feature=self)


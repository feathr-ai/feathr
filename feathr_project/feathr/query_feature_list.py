from typing import List, Optional, Union
from feathr.feature import RegisteredFeature

from jinja2 import Template

from feathr.typed_key import TypedKey
from feathr.frameconfig import HoconConvertible

class FeatureQuery(HoconConvertible):
    """A FeatureQuery contains a list of features

    Attributes:
        feature_list: a list of feature names
        key: key of `feature_list`, all features must share the same key
        """
    def __init__(self, feature_list: List[Union[str, RegisteredFeature]], key: Optional[Union[TypedKey, List[TypedKey]]] = None) -> None:
        self.key = key
        if isinstance(key, TypedKey):
            self.key = [key]
        self.feature_list = feature_list

    def to_feature_config(self) -> str:
        tm = Template("""
            {
                key: {{key_columns}}
                featureList: [{{feature_names}}]
            }
        """)
        key_columns = ", ".join(k.key_column for k in self.key) if self.key else ["NOT_NEEDED"]
        feature_names = []
        for f in self.feature_list:
            if isinstance(f, str):
                feature_names.append(f)
            else: # TODO handle same feature name from different project
                feature_names.append(f.name)
        feature_list = ", ".join(feature_names)
        return tm.render(key_columns = key_columns, feature_names = feature_list)

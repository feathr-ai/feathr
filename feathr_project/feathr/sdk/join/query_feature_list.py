from typing import Optional, List
from jinja2 import Template

class QueryFeatureList:
    def __init__(self, feature_list: List[str], key: Optional[List[str]] = None) -> None:
        self.key = key
        self.feature_list = feature_list

    def to_config(self) -> str:
        tm = Template("""
            {
                key: {{key_columns}}
                featureList: [{{feature_names}}]
            }
        """)
        key_columns = ", ".join(k for k in self.key) if self.key else ["NOT_NEEDED"]
        feature_list = ", ".join(f for f in self.feature_list)
        return tm.render(key_columns = key_columns, feature_names = feature_list)

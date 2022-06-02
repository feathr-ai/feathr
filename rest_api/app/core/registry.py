from typing import Optional
from feathr._feature_registry import _FeatureRegistry

def getRegistryClient(config_path : Optional[str]):
    return _FeatureRegistry(config_path = config_path)
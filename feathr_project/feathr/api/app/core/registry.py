from typing import Optional
from feathr.registry._feature_registry_purview import _FeatureRegistry

def getRegistryClient(config_path : Optional[str]):
    return _FeatureRegistry(config_path = config_path)
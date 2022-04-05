import os
from typing import Optional
from feathr._feature_registry import _FeatureRegistry

config_path = os.getenv("config_path")

def getRegistryClient(config_path : Optional[str]):
    return _FeatureRegistry(config_path = config_path)
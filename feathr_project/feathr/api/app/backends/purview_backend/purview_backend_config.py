import os
from typing import Dict, Optional
from feathr.registry._feature_registry_purview import _FeatureRegistry
from httplib2 import Credentials

def getRegistryClient(project_name: str, azure_purview_name: str, registry_delimiter: str, project_tags: Dict[str, str] = None, credential=None):
    return _FeatureRegistry(project_name,azure_purview_name,registry_delimiter,project_tags,credential)
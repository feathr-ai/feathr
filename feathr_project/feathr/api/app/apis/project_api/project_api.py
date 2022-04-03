import string
from typing import Optional
from app.core.registry import getRegistryClient

def list_features(project_name: string):
    registry_client = getRegistryClient()
    result = registry_client.list_registered_features(project_name)
    return result

def get_feature_by_name(feature_name:string, type_name: Optional[str] = None):
    registry_client = getRegistryClient()
    result = None
    if type_name: # Type is provided
        result = registry_client.get_feature_by_fqdn_type(feature_name, type_name)
    else:
        result = registry_client.get_feature_by_fqdn(feature_name)
    return result

def get_feature_lineage_by_name(feature_name:string):
    registry_client = getRegistryClient()
    guid = registry_client.get_feature_guid(feature_name)
    if guid:
        result = registry_client.get_feature_lineage(guid)
    return result
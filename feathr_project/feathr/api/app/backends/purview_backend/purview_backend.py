import configparser
from sys import getrefcount
from app.backends.backend_base import FeathrBackendAbstract
from typing import Optional
from app.core.error_code import ErrorCode
from feathr.constants import *

from app.core.feathr_api_exception import FeathrApiException
from app.backends.purview_backend.purview_backend_config import getRegistryClient


DEFAULT_DELIMETER = "__"

class PurviewBackend(FeathrBackendAbstract):
    '''
    Implementation for backend_base, specifically for purview backend.
    '''
    def __init__(self,purview_name):
        self.default_purview_name = purview_name
        self.default_delimeter = DEFAULT_DELIMETER
        self.default_project_name = "feathr_github_ci_synapse"

    # project-related
    def ListProjects(self):
        registry_client = getRegistryClient(self.default_project_name,self.default_purview_name,self.default_delimeter)

        raw_result = registry_client._list_registered_entities_with_details(entity_type=[TYPEDEF_FEATHR_PROJECT])
        result = [x['attributes']['name'] for x in raw_result]
        return result

    # feature-related
    def ListFeatures(self,project_name):
        registry_client = getRegistryClient(project_name,self.default_purview_name,self.default_delimeter)
        return registry_client.list_registered_features(project_name)

    def ListAllDataSources(self,project_name):
        registry_client = getRegistryClient(project_name,self.default_purview_name,self.default_delimeter)

        return registry_client._list_registered_entities_with_details(project_name,entity_type=[TYPEDEF_SOURCE])

    def RetrieveFeature(self,feature_fully_qualified_name,type_name):
        registry_client = getRegistryClient(self.default_project_name,self.default_purview_name,self.default_delimeter)
        result = None
        if type_name: # Type is provided
            result = registry_client.get_feature_by_fqdn_type(feature_fully_qualified_name, type_name)
        else:
            result = registry_client.get_feature_by_fqdn(feature_fully_qualified_name)
        return result

    def RetrieveFeatureByFQDN(self,project_name,feature_name):
        return self.registry_client.get_feature_by_fqdn(feature_name)

    def RetrieveFeatureLineage(self,entity_name):
        registry_client = getRegistryClient(self.default_project_name,self.default_purview_name,self.default_delimeter)
        guid = registry_client.get_feature_id(entity_name)
        if guid:
            result = registry_client.get_feature_lineage(guid)
        return result

import configparser
from sys import getrefcount
from app.backends.backend_base import FeathrBackendAbstract
from typing import Optional
from app.core.error_code import ErrorCode

from app.core.feathr_api_exception import FeathrApiException
from app.backends.purview_backend.purview_backend_config import config_path,getRegistryClient



class PurviewBackend(FeathrBackendAbstract):
    def __init__(self):
        self.registry_client = getRegistryClient(config_path)
    
    # project-related
    def CreateProject(self,name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)
    
    def RetrieveProject(self,name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def UpdateProject(self,name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def DeleteProject(self,name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    # entity-related
    def CreateEntity(self,project_name,entity_name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def RetrieveEntity(self,project_name,entity_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def UpdateEntity(self,project_name,entity_name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def DeleteEntity(self,project_name,entity_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    # feature-related
    def CreateFeature(self,project_name,feature_name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def RetrieveFeature(self,project_name,feature_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def ListFeatures(self,project_name):
        return self.registry_client.list_registered_features(project_name)

    def ListAllDataSources(self,project_name):
        return self.registry_client.list_registered_features(project_name)

    def RetrieveFeature(self,feature_fully_qualified_name,type_name):
        result = None
        if type_name: # Type is provided
            result = self.registry_client.get_feature_by_fqdn_type(feature_fully_qualified_name, type_name)
        else:
            result = self.registry_client.get_feature_by_fqdn(feature_fully_qualified_name)
        return result

    def RetrieveFeatureByFQDN(self,project_name,feature_name):
        return self.registry_client.get_feature_by_fqdn(feature_name)

    def RetrieveFeatureLineage(self,entity_name):
        guid = self.registry_client.get_feature_guid(entity_name)
        if guid:
            result = self.registry_client.get_feature_lineage(guid)
        return result

    def UpdateFeature(self,project_name,feature_name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def DeleteFeature(self,project_name,feature_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    #offline / online feature-related
    def CreateFeatureIngetst(self,project_name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)
    
    def RetrieveOfflineFeatures(self,project_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def RetrieveOnlineFeatures(self,project_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    #job-related
    def CreateJob(self,project_name,job_id,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def RetrieveJob(self,project_name,job_id):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def UpdateJob(self,project_name,job_id,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def DeleteJob(self,project_name,job_id):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    # feature recommendations
    def GetRecentFeatures(self,project_name,type,count):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def GetPopularFeatures(self,project_name,type,count):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def GetFavouriteFeatures(self,project_name,type,count,user_id):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    #search -related
    def AutoComplete(self,query):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def Suggestion(self,query):
        return self.registry_client.search_features(query)

    #transformation-related
    def RegisterTransformation(self,project_name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def GetTransformation(self,project_name,transformation_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)
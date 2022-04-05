from app.backends.backend_base import FeathrBackendAbstract
from app.core.error_code import ErrorCode
from app.core.feathr_api_exception import FeathrApiException


class FakeBackend(FeathrBackendAbstract):
    def __init__(self):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)
    
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

    def RetrieveFeatureByFQDN(self,project_name,feature_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def RetrieveFeatureLineage(self,project_name,entity_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

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
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    #transformation-related
    def RegisterTransformation(self,project_name,metadata):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)

    def GetTransformation(self,project_name,transformation_name):
        raise FeathrApiException("Backend Not Implemented.",ErrorCode.not_implemented)
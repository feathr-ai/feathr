from abc import abstractmethod,ABC


class FeathrBackendAbstract(ABC):
    # project-related
    @abstractmethod
    def CreateProject(self,name,metadata):
        pass
    
    @abstractmethod
    def RetrieveProject(self,name):
        pass

    @abstractmethod
    def UpdateProject(self,name,metadata):
        pass

    @abstractmethod
    def DeleteProject(self,name):
        pass

    # entity-related
    @abstractmethod
    def CreateEntity(self,project_name,entity_name,metadata):
        pass

    @abstractmethod
    def RetrieveEntity(self,project_name,entity_name):
        pass

    @abstractmethod
    def UpdateEntity(self,project_name,entity_name,metadata):
        pass

    @abstractmethod
    def DeleteEntity(self,project_name,entity_name):
        pass

    # feature-related
    @abstractmethod
    def CreateFeature(self,project_name,feature_name,metadata):
        pass

    @abstractmethod
    def RetrieveFeature(self,project_name,feature_name):
        pass

    @abstractmethod
    def RetrieveFeature(self,feature_fully_qualified_name,type_name):
        pass

    @abstractmethod
    def ListFeatures(self,project_name):
        pass

    @abstractmethod
    def RetrieveFeatureByFQDN(self,project_name,feature_name):
        pass

    @abstractmethod
    def RetrieveFeatureLineage(self,entity_name):
        pass

    @abstractmethod
    def ListAllDataSources(self,project_name):
        pass

    @abstractmethod
    def UpdateFeature(self,project_name,feature_name,metadata):
        pass

    @abstractmethod
    def DeleteFeature(self,project_name,feature_name):
        pass

    #offline / online feature-related
    @abstractmethod
    def CreateFeatureIngetst(self,project_name,metadata):
        pass
    
    @abstractmethod
    def RetrieveOfflineFeatures(self,project_name):
        pass

    @abstractmethod
    def RetrieveOnlineFeatures(self,project_name):
        pass

    #job-related
    @abstractmethod
    def CreateJob(self,project_name,job_id,metadata):
        pass

    @abstractmethod
    def RetrieveJob(self,project_name,job_id):
        pass

    @abstractmethod
    def UpdateJob(self,project_name,job_id,metadata):
        pass

    @abstractmethod
    def DeleteJob(self,project_name,job_id):
        pass

    # feature recommendations
    @abstractmethod
    def GetRecentFeatures(self,project_name,type,count):
        pass

    @abstractmethod
    def GetPopularFeatures(self,project_name,type,count):
        pass

    @abstractmethod
    def GetFavouriteFeatures(self,project_name,type,count,user_id):
        pass

    #search -related
    @abstractmethod
    def AutoComplete(self,query):
        pass

    @abstractmethod
    def Suggestion(self,query):
        pass

    #transformation-related
    @abstractmethod
    def RegisterTransformation(self,project_name,metadata):
        pass

    @abstractmethod
    def GetTransformation(self,project_name,transformation_name):
        pass
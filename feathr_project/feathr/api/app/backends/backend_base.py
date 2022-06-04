from abc import abstractmethod,ABC


class FeathrBackendAbstract(ABC):
    '''
    Definition for backend_base, a generic collection of backend interfaces
    '''
    # project-related
    @abstractmethod
    def ListProjects(self):
        '''
        List all projects (workspace) in default backend instance.
        '''
        pass
    
    # feature-related
    @abstractmethod
    def ListFeatures(self,project_name):
        '''
        List all features in a given project name.
        '''
        pass

    @abstractmethod
    def ListAllDataSources(self,project_name):
        '''
        List all datasources in a given project name.
        '''
        pass

    @abstractmethod
    def RetrieveFeature(self,feature_fully_qualified_name,type_name):
        '''
        Get feature by fully qualified name.
        '''
        pass

    @abstractmethod
    def RetrieveFeatureByFQDN(self,project_name,feature_name):
        '''
        Get feature by project name and its FQDN
        '''
        pass

    @abstractmethod
    def RetrieveFeatureLineage(self,entity_name):
        '''
        Get feature lineage (including parent links) for a given entity fully qualified name.
        '''
        pass
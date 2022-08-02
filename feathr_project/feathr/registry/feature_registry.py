from abc import ABC, abstractmethod
from pathlib import Path

from typing import Any, Dict, List, Optional, Tuple
from feathr.definition.feature_derivations import DerivedFeature
from feathr.definition.anchor import FeatureAnchor
from feathr.utils._envvariableutil import _EnvVaraibleUtil

class FeathrRegistry(ABC):
    """This is the abstract class for all the feature registries. All the feature registries should implement those interfaces.
    """

    @abstractmethod
    def register_features(self, anchor_list: List[FeatureAnchor] =[], derived_feature_list: List[DerivedFeature]=[]):
        """Registers features based on the current workspace

                Args:
                anchor_list: List of FeatureAnchors
                derived_feature_list: List of DerivedFeatures
        """
        pass


    @abstractmethod
    def list_registered_features(self, project_name: str) -> List[str]:
        """List all the already registered features under the given project.
        `project_name` must not be None or empty string because it violates the RBAC policy
        """
        pass

    @abstractmethod
    def get_features_from_registry(self, project_name: str) -> Tuple[List[FeatureAnchor], List[DerivedFeature]]:
        """[Sync Features from registry to local workspace, given a project_name, will write project's features from registry to to user's local workspace]

        Args:
            project_name (str): project name.

        Returns:
            bool: Returns true if the job completed successfully, otherwise False
        """
        pass
    
    @classmethod
    @abstractmethod
    def save_to_feature_config(self, workspace_path: Path, config_save_dir: Path):
        """Save feature definition within the workspace into HOCON feature config files"""
        pass

    @classmethod
    @abstractmethod
    def save_to_feature_config_from_context(self, anchor_list, derived_feature_list, local_workspace_dir: Path):
        """Save feature definition within the workspace into HOCON feature config files from current context, rather than reading from python files"""
        pass

def default_registry_client(project_name: str, config_path:str = "./feathr_config.yaml", project_registry_tag: Dict[str, str]=None, credential = None) -> FeathrRegistry:
    from feathr.registry._feathr_registry_client import _FeatureRegistry
    from feathr.registry._feature_registry_purview import _PurviewRegistry
    envutils = _EnvVaraibleUtil(config_path)
    registry_endpoint = envutils.get_environment_variable_with_default("feature_registry", "api_endpoint")
    if registry_endpoint:
        return _FeatureRegistry(project_name, endpoint=registry_endpoint, project_tags=project_registry_tag, credential=credential)
    else:
        registry_delimiter = envutils.get_environment_variable_with_default('feature_registry', 'purview', 'delimiter')
        azure_purview_name = envutils.get_environment_variable_with_default('feature_registry', 'purview', 'purview_name')
        # initialize the registry no matter whether we set purview name or not, given some of the methods are used there.
        return _PurviewRegistry(project_name, azure_purview_name, registry_delimiter, project_registry_tag, config_path = config_path, credential=credential)

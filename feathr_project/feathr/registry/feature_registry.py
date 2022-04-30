from abc import ABC, abstractmethod

from typing import Any, Dict, List, Optional, Tuple
from feathr.definition.feature_derivations import DerivedFeature
from feathr.definition.anchor import FeatureAnchor

class FeathrRegistry(ABC):
    """This is the abstract class for all the feature registries. All the feature registries should implement those interfaces.
    """

    @abstractmethod
    def register_features(self, from_context: bool = True):
        """Registers features based on the current workspace

                Args:
                from_context: If from_context is True (default), the features will be generated from
                    the current context, with the previous built features in client.build(). Otherwise, the features will be generated from
                    configuration files.
        """
        pass


    @abstractmethod
    def list_registered_features(self, project_name: str = None) -> List[str]:
        """List all the already registered features. If project_name is not provided or is None, it will return all
        the registered features; otherwise it will only return features under this project
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



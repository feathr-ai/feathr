import glob
import importlib
import inspect
import os
import sys
from graphlib import TopologicalSorter
from pathlib import Path
from tracemalloc import stop
from typing import Dict, List, Optional, Tuple, Union
from urllib.parse import urlparse



from feathr.definition.anchor import FeatureAnchor
from feathr.constants import *
from feathr.definition.feature import Feature, FeatureType
from feathr.definition.feature_derivations import DerivedFeature
from feathr.definition.repo_definitions import RepoDefinitions
from feathr.definition.source import HdfsSource, InputContext, Source
from feathr.definition.transformation import (ExpressionTransformation, Transformation,
                                   WindowAggTransformation)
from feathr.definition.typed_key import TypedKey
from feathr.registry.feature_registry import FeathrRegistry


class FileFeatureRegistry(FeathrRegistry):
    """
    Initializes the feature registry, doing the following:
    - Use an DefaultAzureCredential() to communicate with Azure Purview
    - Initialize an Azure Purview Client
    - Initialize the GUID tracker, project name, etc.
    """
    def __init__(self, project_name: str, azure_purview_name: str, registry_delimiter: str, project_tags: Dict[str, str] = None, credential=None, config_path=None,):
        self.project_name = project_name
        self.project_tags = project_tags


    def register_features(self, from_context: bool = True):
        """Registers features based on the current workspace

                Args:
                from_context: If from_context is True (default), the features will be generated from
                    the current context, with the previous built features in client.build(). Otherwise, the features will be generated from
                    configuration files.
        """
        pass


    def list_registered_features(self, project_name: str = None) -> List[str]:
        """List all the already registered features. If project_name is not provided or is None, it will return all
        the registered features; otherwise it will only return features under this project
        """
        pass

    def get_features_from_registry(self, project_name: str) -> Tuple[List[FeatureAnchor], List[DerivedFeature]]:
        """[Sync Features from registry to local workspace, given a project_name, will write project's features from registry to to user's local workspace]

        Args:
            project_name (str): project name.

        Returns:
            bool: Returns true if the job completed successfully, otherwise False
        """
        pass

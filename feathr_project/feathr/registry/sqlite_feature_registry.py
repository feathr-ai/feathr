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

from azure.identity import DefaultAzureCredential
from jinja2 import Template
from loguru import logger
from numpy import deprecate
from pyapacheatlas.auth import ServicePrincipalAuthentication
from pyapacheatlas.auth.azcredential import AzCredentialWrapper
from pyapacheatlas.core import (AtlasClassification, AtlasEntity, AtlasProcess,
                                PurviewClient, TypeCategory)
from pyapacheatlas.core.typedef import (AtlasAttributeDef,
                                        AtlasRelationshipEndDef, Cardinality,
                                        EntityTypeDef, RelationshipTypeDef)
from pyapacheatlas.core.util import GuidTracker
from pyhocon import ConfigFactory

from feathr._file_utils import write_to_file
from feathr.feathr_feature_definition.anchor import FeatureAnchor
from feathr.constants import *
from feathr.feathr_feature_definition.feature import Feature, FeatureType
from feathr.feathr_feature_definition.feature_derivations import DerivedFeature
from feathr.feathr_feature_definition.repo_definitions import RepoDefinitions
from feathr.feathr_feature_definition.source import HdfsSource, InputContext, Source
from feathr.feathr_feature_definition.transformation import (ExpressionTransformation, Transformation,
                                   WindowAggTransformation)
from feathr.feathr_feature_definition.typed_key import TypedKey
from feathr.registry.feature_registry import FeathrRegistry


class SQLiteFeatureRegistry(FeathrRegistry):
    """
    Initializes the feature registry, doing the following:
    - Use an DefaultAzureCredential() to communicate with Azure Purview
    - Initialize an Azure Purview Client
    - Initialize the GUID tracker, project name, etc.
    """
    def __init__(self, project_name: str, azure_purview_name: str, registry_delimiter: str, project_tags: Dict[str, str] = None, credential=None, config_path=None,):
        self.project_name = project_name
        self.registry_delimiter = registry_delimiter
        self.azure_purview_name = azure_purview_name
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

import sqlite3
con = sqlite3.connect('example.db')
print(sqlite3.sqlite_version)
cur = con.cursor()

# Create table
cur.execute('''CREATE TABLE stocks
               (date text, trans text, symbol text, qty real, price real)''')

# Insert a row of data
cur.execute("INSERT INTO stocks VALUES ('2006-01-05','BUY','RHAT',100,35.14)")

# Save (commit) the changes
con.commit()

# We can also close the connection if we are done with it.
# Just be sure any changes have been committed or they will be lost.
con.close()
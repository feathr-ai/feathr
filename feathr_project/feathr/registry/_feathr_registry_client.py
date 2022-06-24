from azure.identity import DefaultAzureCredential
from typing import List, Tuple

import requests
from feathr.definition.anchor import FeatureAnchor
from feathr.definition.feature import Feature
from feathr.definition.feature_derivations import DerivedFeature
from feathr.definition.source import Source
from feathr.registry.feature_registry import FeathrRegistry


class _FeathrRegistryClient(FeathrRegistry):
    def __init__(self, endpoint: str, credential=None, config_path=None):
        self.endpoint = endpoint
        self.credential = DefaultAzureCredential(exclude_interactive_browser_credential=False) if credential is None else credential
        
    
    def register_features(self, anchor_list: List[FeatureAnchor] =[], derived_feature_list: List[DerivedFeature]=[]):
        """Registers features based on the current workspace

                Args:
                anchor_list: List of FeatureAnchors
                derived_feature_list: List of DerivedFeatures
        """
        pass


    def list_registered_features(self, project_name: str) -> List[str]:
        """List all the already registered features. If project_name is not provided or is None, it will return all
        the registered features; otherwise it will only return features under this project
        """
        r = requests.get(f"{self.endpoint}/projects/{project_name}/features", headers=self._get_auth_header())
        if not r.ok():
            raise RuntimeError(f"Failed to retrieve features from registry, status is {r.status_code}, error is {r.text}")
        resp = r.json()
        return 
        for f in r:
            # r should be an array
            if f["typeName"] == "feathr_anchor_feature_v1":
                pass
                
        

    def get_features_from_registry(self, project_name: str) -> Tuple[List[FeatureAnchor], List[DerivedFeature]]:
        """
        [Sync Features from registry to local workspace, given a project_name, will write project's features from registry to to user's local workspace]

        Args:
            project_name (str): project name.

        Returns:
            bool: Returns true if the job completed successfully, otherwise False
        """
        pass

    def _get_auth_header(self) -> str:
        """
        Returns {"Authorization": "Bearer JWTToken"}
        """
        # TODO:
        return {}

def _parse_source(v: dict) -> Source:
    pass

def _parse_anchor(v: dict) -> FeatureAnchor:
    pass

def _parse_anchor_feature(v: dict) -> Feature:
    pass

def _parse_derived_feature(v: dict) -> DerivedFeature:
    pass
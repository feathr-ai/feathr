from typing import Dict
import json
import logging
import requests

from feathr.registry._feathr_registry_client import _FeatureRegistry


def check(r):
    if not r.ok:
        raise RuntimeError(f"Failed to call registry API, status is {r.status_code}, error is {r.text}")
    return r


class _FeatureRegistryAWS(_FeatureRegistry):
    def __init__(self, project_name: str, endpoint: str, project_tags: Dict[str, str] = None, credential=None,
                 config_path=None):
        self.project_name = project_name
        self.project_tags = project_tags
        self.endpoint = endpoint
        self.credential = credential
        self.project_id = None

    def _get(self, path: str) -> dict:
        logging.debug("PATH: ", path)
        return check(requests.get(f"{self.endpoint}{path}", auth=self.credential)).json()

    def _post(self, path: str, body: dict) -> dict:
        logging.debug("PATH: ", path)
        logging.debug("BODY: ", json.dumps(body, indent=2))
        return check(requests.post(f"{self.endpoint}{path}", auth=self.credential, json=body)).json()
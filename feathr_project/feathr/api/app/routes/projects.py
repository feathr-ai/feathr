
from __future__ import annotations
from typing import Optional

from fastapi import APIRouter, HTTPException, Response,status
from numpy import typename
from rsa import verify
from app.apis.project_api.project_api import get_feature_by_name, get_feature_lineage_by_name, list_features

from app.core.configs import *
from app.apis.project_api import *
from app.core.error_handling import verifyCode
from app.models.features import Features
router = APIRouter()

@router.get("/")
async def root(code):
    """
    Root endpoint
    """
    verifyCode(code)
    
    return {"message": "Welcome to Feature Store APIs. Please call specific APIs for your use case"}

@router.get("/projects/{project_name}/features", response_model=Features)
def list_registered_features(code, project_name: str, response: Response):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    verifyCode(code)
    
    response.status_code = status.HTTP_200_OK
    result = list_features(project_name)
    return {"features" : result}
    

@router.get("/projects/{project_name}/features/{feature_name}")
def get_feature_qualifiedName(code : str, project_name: str, feature_name: str, response: Response, type_name: Optional[str] = None):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    verifyCode(code)

    response.status_code = status.HTTP_200_OK
    result = get_feature_by_name(feature_name,type_name=type_name)
    
    return result
    

@router.get("/projects/{project_name}/features/lineage/{feature_name}")
def get_feature_qualifiedName(code : str, project_name: str, feature_name: str, response: Response):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    verifyCode(code)

    response.status_code = status.HTTP_200_OK
    result = get_feature_lineage_by_name(feature_name)
    return result
    
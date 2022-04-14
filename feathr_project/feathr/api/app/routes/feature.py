
from __future__ import annotations
from typing import Optional

from fastapi import APIRouter, HTTPException, Response,status
from app.backends.purview_backend.purview_backend import PurviewBackend

from app.core.configs import *
from app.core.error_handling import verifyCode
from app.models.features import Features

router = APIRouter()
backend = PurviewBackend()

@router.get("/projects/{project_name}/features", response_model=Features)
def list_registered_features(code, project_name: str, response: Response):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    verifyCode(code)
    
    response.status_code = status.HTTP_200_OK
    result = backend.ListFeatures(project_name)
    return {"features" : result}

@router.get("/features/{feature_name}")
def get_feature_by_qualifiedName(code : str, feature_fully_qualified_name: str, response: Response, type_name: Optional[str] = None):
    """Get the feature by its fully qualified name
    """
    verifyCode(code)

    response.status_code = status.HTTP_200_OK
    result = backend.RetrieveFeature(feature_fully_qualified_name,type_name)
    
    return result
    

@router.get("/features/lineage/{feature_fully_qualified_name}")
def get_feature_lineage(code : str, feature_fully_qualified_name:str,response: Response):
    """Get feature lineage by the fully qualified name
    """
    verifyCode(code)

    response.status_code = status.HTTP_200_OK
    result = backend.RetrieveFeatureLineage(feature_fully_qualified_name)
    return result
    
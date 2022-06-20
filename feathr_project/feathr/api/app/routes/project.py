
from __future__ import annotations
from typing import Optional

from fastapi import APIRouter, HTTPException, Response,status
from app.backends.purview_backend.purview_backend import PurviewBackend
from app.core.api_utils import remove_relationship_attributes

from app.core.configs import *
from app.core.error_handling import verifyCode
from app.models.features import Features

router = APIRouter()
backend = PurviewBackend(default_purview_name)
@router.get("/projects",tags=["Projects"])
def get_all_projects(response: Response):
    """Get all the projects
    """
    response.status_code = status.HTTP_200_OK
    result = backend.ListProjects()
    
    return result
@router.get("/projects/{project_name}", tags=["Projects"])
def TODO_get_project_by_name(project_name: str,response: Response):
    """[TO DO] Get Project entity
    """
    response.status_code = status.HTTP_200_OK
    result = backend.GetProject(project_name)
    return result

@router.get("/projects/{project_name}/features",tags=["Projects"])
def list_registered_features(project_name: str,response: Response):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    response.status_code = status.HTTP_200_OK
    result = backend.ListFeatures(project_name)
    [remove_relationship_attributes(x) for x in result]
    return result
    
@router.get("/projects/{project_name}/datasources",tags=["Projects"])
def get_feature_by_qualifiedName(project_name: str, response: Response):
    """Get all data sources within a project
    """
    response.status_code = status.HTTP_200_OK
    result = backend.ListAllDataSources(project_name)
    
    [remove_relationship_attributes(x) for x in result]
    return result
    
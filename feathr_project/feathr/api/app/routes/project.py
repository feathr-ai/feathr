
from __future__ import annotations
from typing import Optional

from fastapi import APIRouter, HTTPException, Response,status
from app.backends.purview_backend.purview_backend import PurviewBackend

from app.core.configs import *
from app.core.error_handling import verifyCode
from app.models.features import Features

router = APIRouter()

@router.get("/purview/{purview_name}/projects",tags=["Projects"])
def get_all_projects(code : str, purview_name:str, response: Response):
    """Get all the projects
    """
    verifyCode(code)
    backend = PurviewBackend(purview_name)

    response.status_code = status.HTTP_200_OK
    result = backend.ListProjects()
    
    return result

@router.get("/purview/{purview_name}/projects/{project_name}/datasources",tags=["Projects"])
def get_feature_by_qualifiedName(code : str, purview_name:str,project_name: str, response: Response):
    """Get all data sources within a project
    """
    verifyCode(code)
    backend = PurviewBackend(purview_name)

    response.status_code = status.HTTP_200_OK
    result = backend.ListAllDataSources(project_name)
    
    return result
    

from __future__ import annotations
from typing import Optional

from fastapi import APIRouter, HTTPException, Response,status
from app.backends.purview_backend.purview_backend import PurviewBackend

from app.core.configs import *
from app.core.error_handling import verifyCode
from app.models.features import Features

router = APIRouter()
backend = PurviewBackend()

@router.get("/projects/{project_name}/datasources")
def get_feature_by_qualifiedName(code : str, project_name: str, response: Response):
    """Get the feature by its fully qualified name
    """
    verifyCode(code)

    response.status_code = status.HTTP_200_OK
    result = backend.ListAllDataSources(project_name)
    
    return result
    
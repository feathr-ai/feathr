
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

@router.get("/features/{feature_fully_qualified_name}",tags=["Features"])
def get_feature_by_qualifiedName(feature_fully_qualified_name: str, response: Response, type_name: Optional[str] = None):
    """Get the feature by its fully qualified name
    """
    response.status_code = status.HTTP_200_OK
    result = backend.RetrieveFeature(feature_fully_qualified_name,type_name)['entity']
    remove_relationship_attributes(result)
    return result
    

@router.get("/purview/{purview_name}/features/lineage/{feature_fully_qualified_name}",tags=["Features"])
def get_feature_lineage(code : str, purview_name:str, feature_fully_qualified_name:str,response: Response):
    """Get feature lineage by the fully qualified name
    """
    verifyCode(code)
    backend = PurviewBackend(purview_name)

    response.status_code = status.HTTP_200_OK
    result = backend.RetrieveFeatureLineage(feature_fully_qualified_name)
    return result
    
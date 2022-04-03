from __future__ import annotations

from fastapi import APIRouter, HTTPException, Response,status

from app.core.configs import *
from app.apis.featurestore_api.feature_search import feature_search
from app.core.error_handling import verifyCode
router = APIRouter()

@router.get("/featurestore/search")
def get_feature_qualifiedName(code : str, query: str, response: Response):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    if code != appServiceKey:
        raise HTTPException(status_code=403, detail="You are not allowed to access this resource")  
        """
    verifyCode(code)
    result = feature_search(query)
    response.status_code = status.HTTP_200_OK
    return result
   
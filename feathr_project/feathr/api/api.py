import os
import logging
from typing import List, Optional
from fastapi import FastAPI, HTTPException, Response, status
from pyapacheatlas.core import (AtlasException)
from pydantic import BaseModel
from opencensus.ext.azure.log_exporter import AzureLogHandler
from feathr._feature_registry import _FeatureRegistry
from feathr.constants import *
from feathr._feature_registry import _FeatureRegistry


app = FastAPI()

# Log Level
log_level = os.getenv("logLevel", "INFO")

logger = logging.getLogger(__name__)
handler = logging.StreamHandler()
# Set the application insights connection string to enable the logger to write to Application Insight's directly
app_insights_connection_string = os.getenv("APPLICATIONINSIGHTS_CONNECTION_STRING")
formatter = logging.Formatter("[%(asctime)s] [%(name)s:%(lineno)s - %(funcName)5s()] %(levelname)s - %(message)s")
handler.setFormatter(formatter)
logger.addHandler(handler)
if app_insights_connection_string:
    azure_handler = AzureLogHandler(connection_string=app_insights_connection_string)
    logger.addHandler(azure_handler)
else:
    logger.warning("APPLICATIONINSIGHTS_CONNECTION_STRING is not set - will NOT log to AppInsights!!")
logger.setLevel(log_level)
logger.info("starting %s", __file__)

"""
This is to enable Authentication for API and not keep it wide open.
You can set the AppServiceKey variable as environment variable
and make sure to pass the variable as query parameter when you access the API.
eg - <apiserver>/projects/<project_name>/features?code=<your_api_code>
"""
appServiceKey = os.getenv("AppServiceKey")
project_name = os.getenv("ProjectName")
azure_purview_name = os.getenv("AZURE_PURVIEW_NAME")
registry_delimiter = os.getenv("REGISTRY_DELIMITER")


def getRegistry():
    return _FeatureRegistry(project_name=project_name, azure_purview_name= azure_purview_name, registry_delimiter = registry_delimiter)


@app.get("/")
async def root(code):
    """
    Root endpoint
    """
    if code != appServiceKey:
        raise HTTPException(status_code=403, detail="You are not allowed to access this resource")
    return {"message": "Welcome to Feature Store APIs. Please call specific APIs for your use case"}

class Features(BaseModel):
    """
    Defining contract for input field
    """

    features: List[str]


@app.get("/projects/{project_name}/features", response_model=Features)
def list_registered_features(code, project_name: str, response: Response):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    if code != appServiceKey:
        raise HTTPException(status_code=403, detail="You are not allowed to access this resource")  
    try:
        registry = getRegistry()
        logger.info("Retrieved registry client successfully")
        response.status_code = status.HTTP_200_OK
        result = registry.list_registered_features(project_name)
        return {"features" : result}
    except AtlasException as ae:
        logger.error("Error retrieving feature: %s", ae.args[0])
        raise HTTPException(status_code=400, detail="Error: " + ae.args[0])
    except Exception as err:
        logger.error("Error: %s", err.args[0])
        raise HTTPException(status_code=400, detail="Error: " + err.args[0])

@app.get("/projects/{project_name}/features/{feature_name}")
def get_feature_qualifiedName(code : str, project_name: str, feature_name: str, response: Response, type_name: Optional[str] = None):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    if code != appServiceKey:
        raise HTTPException(status_code=403, detail="You are not allowed to access this resource")  
    try:
        registry = getRegistry()
        logger.info("Retrieved registry client successfully")
        response.status_code = status.HTTP_200_OK
        result = None
        if type_name: # Type is provided
            result = registry.get_feature_by_fqdn_type(feature_name, type_name)
        else:
            result = registry.get_feature_by_fqdn(feature_name)
        return result
    except AtlasException as ae:
        logger.error("Error retrieving feature: %s", ae.args[0])
        raise HTTPException(status_code=400, detail=ae.args[0])
    except Exception as err:
        logger.error("Error: %s", err.args[0])
        raise HTTPException(status_code=400, detail=err.args[0])

@app.get("/projects/{project_name}/features/lineage/{feature_name}")
def get_feature_qualifiedName(code : str, project_name: str, feature_name: str, response: Response):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    if code != appServiceKey:
        raise HTTPException(status_code=403, detail="You are not allowed to access this resource")  
    try:
        registry = getRegistry()
        logger.info("Retrieved registry client successfully")
        response.status_code = status.HTTP_200_OK
        guid = registry.get_feature_guid(feature_name)
        if guid:
            result = registry.get_feature_lineage(guid)
            return result
    except AtlasException as ae:
        logger.error("Error retrieving feature: %s", ae.args[0])
        raise HTTPException(status_code=400, detail=ae.args[0])
    except Exception as err:
        logger.error("Error: %s", err.args[0])
        raise HTTPException(status_code=400, detail=err.args[0])

@app.get("/featurestore/search")
def get_feature_qualifiedName(code : str, query: str, response: Response):
    """List all the already registered features. If project_name is not provided or is None, it will return all
    the registered features; otherwise it will only return features under this project
    """
    if code != appServiceKey:
        raise HTTPException(status_code=403, detail="You are not allowed to access this resource")  
    try:
        registry = getRegistry()
        logger.info("Retrieved registry client successfully")
        response.status_code = status.HTTP_200_OK
        result = registry.search_features(query)
        return result
    except AtlasException as ae:
        logger.error("Error retrieving feature: %s", ae.args[0])
        raise HTTPException(status_code=400, detail=ae.args[0])
    except Exception as err:
        logger.error("Error: %s", err.args[0])
        raise HTTPException(status_code=400, detail=err.args[0])

@app.get("/projects/{project_name}/sources/{source_name}")
def get_source_by_qualifiedName(code : str, project_name: str, source_name: str, response: Response):
    """List all the sources. If project_name is not provided or is None, it will return all
    the sources; otherwise it will only return sources under this project
    """
    if code != appServiceKey:
        raise HTTPException(status_code=403, detail="You are not allowed to access this resource")  
    try:
        registry = getRegistry()
        logger.info("Retrieved registry client successfully")
        response.status_code = status.HTTP_200_OK
        type_name = "feathr_source_v1"
        result = registry.get_feature_by_fqdn_type(source_name, type_name)
        return result
    except AtlasException as ae:
        logger.error("Error retrieving feature: %s", ae.args[0])
        raise HTTPException(status_code=400, detail=ae.args[0])
    except Exception as err:
        logger.error("Error: %s", err.args[0])
        raise HTTPException(status_code=400, detail=err.args[0])

@app.get("/projects/{project_name}/anchors/{anchor_name}")
def get_anchor_by_qualifiedName(code : str, project_name: str, anchor_name: str, response: Response):
    """List all the anchors. If project_name is not provided or is None, it will return all
    the anchors; otherwise it will only return anchors under this project
    """
    if code != appServiceKey:
        raise HTTPException(status_code=403, detail="You are not allowed to access this resource")  
    try:
        registry = getRegistry()
        logger.info("Retrieved registry client successfully")
        response.status_code = status.HTTP_200_OK
        type_name = "feathr_anchor"
        result = registry.get_feature_by_fqdn_type(anchor_name, type_name)
        return result
    except AtlasException as ae:
        logger.error("Error retrieving feature: %s", ae.args[0])
        raise HTTPException(status_code=400, detail=ae.args[0])
    except Exception as err:
        logger.error("Error: %s", err.args[0])
        raise HTTPException(status_code=400, detail=err.args[0])

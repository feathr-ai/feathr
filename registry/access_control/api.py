import json
from typing import Optional
from fastapi import APIRouter, Depends
import requests
from rbac.access import *
from rbac.models import User
from rbac.db_rbac import DbRBAC
from rbac import config

router = APIRouter()
rbac = DbRBAC()
registry_url = config.RBAC_REGISTRY_URL


@router.get('/projects', name="Get a list of Project Names [No Auth Required]")
async def get_projects() -> list[str]:
    response = requests.get(
        url=f"{registry_url}/projects").content.decode('utf-8')
    return json.loads(response)


@router.get('/projects/{project}', name="Get My Project [Read Access Required]")
async def get_project(project: str, access: UserAccess  = Depends(project_read_access)):
    response = requests.get(url=f"{registry_url}/projects/{project}",
                            headers=get_api_header(access.user_name)).content.decode('utf-8')
    return json.loads(response)

@router.get("/dependent/{entity}", name="Get downstream/dependent entitites for a given entity [Read Access Required]")
def get_dependent_entities(entity: str, access: UserAccess = Depends(project_read_access)):
    response = requests.get(url=f"{registry_url}/dependent/{entity}",
                            headers=get_api_header(access.user_name)).content.decode('utf-8')
    return json.loads(response)

@router.get("/projects/{project}/datasources", name="Get data sources of my project [Read Access Required]")
def get_project_datasources(project: str, access: UserAccess = Depends(project_read_access)) -> list:
    response = requests.get(url=f"{registry_url}/projects/{project}/datasources",
                            headers=get_api_header(access.user_name)).content.decode('utf-8')
    return json.loads(response)


@router.get("/projects/{project}/datasources/{datasource}", name="Get a single data source by datasource Id [Read Access Required]")
def get_project_datasource(project: str, datasource: str, requestor: UserAccess = Depends(project_read_access)) -> list:
    response = requests.get(url=f"{registry_url}/projects/{project}/datasources/{datasource}",
                            headers=get_api_header(requestor.user_name)).content.decode('utf-8')
    return json.loads(response)


@router.get("/projects/{project}/features", name="Get features under my project [Read Access Required]")
def get_project_features(project: str, keyword: Optional[str] = None, access: UserAccess = Depends(project_read_access)) -> list:
    response = requests.get(url=f"{registry_url}/projects/{project}/features",
                            headers=get_api_header(access.user_name)).content.decode('utf-8')
    return json.loads(response)


@router.get("/features/{feature}", name="Get a single feature by feature Id [Read Access Required]")
def get_feature(feature: str, requestor: User = Depends(get_user)) -> dict:
    response = requests.get(url=f"{registry_url}/features/{feature}",
                            headers=get_api_header(requestor.username)).content.decode('utf-8')
    ret = json.loads(response)

    feature_qualifiedName = ret['attributes']['qualifiedName']
    validate_project_access_for_feature(
        feature_qualifiedName, requestor, AccessType.READ)
    return ret

@router.delete("/entity/{entity}", name="Deletes a single entity by qualified name [Write Access Required]")
def delete_entity(entity: str, access: UserAccess = Depends(project_write_access)) -> str:
    requests.delete(url=f"{registry_url}/entity/{feature}",
                               headers=get_api_header(access.user_name)).content.decode('utf-8')

@router.get("/features/{feature}/lineage", name="Get Feature Lineage [Read Access Required]")
def get_feature_lineage(feature: str, requestor: User = Depends(get_user)) -> dict:
    response = requests.get(url=f"{registry_url}/features/{feature}/lineage",
                            headers=get_api_header(requestor.username)).content.decode('utf-8')
    ret = json.loads(response)

    feature_qualifiedName = ret['guidEntityMap'][feature]['attributes']['qualifiedName']
    validate_project_access_for_feature(
        feature_qualifiedName, requestor, AccessType.READ)
    return ret


@router.post("/projects", name="Create new project with definition [Auth Required]")
def new_project(definition: dict, requestor: User = Depends(get_user)) -> dict:
    rbac.init_userrole(requestor.username, definition["name"])
    response = requests.post(url=f"{registry_url}/projects", json=definition,
                             headers=get_api_header(requestor.username)).content.decode('utf-8')
    return json.loads(response)


@router.post("/projects/{project}/datasources", name="Create new data source of my project [Write Access Required]")
def new_project_datasource(project: str, definition: dict, access: UserAccess = Depends(project_write_access)) -> dict:
    response = requests.post(url=f"{registry_url}/projects/{project}/datasources", json=definition, headers=get_api_header(
        access.user_name)).content.decode('utf-8')
    return json.loads(response)


@router.post("/projects/{project}/anchors", name="Create new anchors of my project [Write Access Required]")
def new_project_anchor(project: str, definition: dict, access: UserAccess = Depends(project_write_access)) -> dict:
    response = requests.post(url=f"{registry_url}/projects/{project}/anchors", json=definition, headers=get_api_header(
        access.user_name)).content.decode('utf-8')
    return json.loads(response)


@router.post("/projects/{project}/anchors/{anchor}/features", name="Create new anchor features of my project [Write Access Required]")
def new_project_anchor_feature(project: str, anchor: str, definition: dict, access: UserAccess = Depends(project_write_access)) -> dict:
    response = requests.post(url=f"{registry_url}/projects/{project}/anchors/{anchor}/features", json=definition, headers=get_api_header(
        access.user_name)).content.decode('utf-8')
    return json.loads(response)


@router.post("/projects/{project}/derivedfeatures", name="Create new derived features of my project [Write Access Required]")
def new_project_derived_feature(project: str, definition: dict, access: UserAccess = Depends(project_write_access)) -> dict:
    response = requests.post(url=f"{registry_url}/projects/{project}/derivedfeatures",
                             json=definition, headers=get_api_header(access.user_name)).content.decode('utf-8')
    return json.loads(response)

# Below are access control management APIs
@router.get("/userroles", name="List all active user role records [Project Manage Access Required]")
def get_userroles(requestor: User = Depends(get_user)) -> list:
    return rbac.list_userroles(requestor.username)


@router.post("/users/{user}/userroles/add", name="Add a new user role [Project Manage Access Required]")
def add_userrole(project: str, user: str, role: str, reason: str, access: UserAccess = Depends(project_manage_access)):
    return rbac.add_userrole(access.project_name, user, role, reason, access.user_name)


@router.delete("/users/{user}/userroles/delete", name="Delete a user role [Project Manage Access Required]")
def delete_userrole(user: str, role: str, reason: str, access: UserAccess= Depends(project_manage_access)):
    return rbac.delete_userrole(access.project_name, user, role, reason, access.user_name)

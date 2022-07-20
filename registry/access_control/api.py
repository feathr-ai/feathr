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
    response = requests.get(registry_url + "/projects").content.decode('utf-8')
    return json.loads(response)


@router.get('/projects/{project}', name="Get My Project [Read Access Required]")
async def get_project(project: str, requestor: User = Depends(project_read_access)):
    response = requests.get(registry_url + "/projects/" + project, headers = get_api_header(requestor)).content.decode('utf-8')
    return json.loads(response)


@router.get("/projects/{project}/datasources", name="Get data sources of my project [Read Access Required]")
def get_project_datasources(project: str, requestor: User = Depends(project_read_access)) -> list:
    response = requests.get(registry_url + "/projects/" + project + "/datasources", headers = get_api_header(requestor)).content.decode('utf-8')
    return json.loads(response)


@router.get("/projects/{project}/features", name="Get features under my project [Read Access Required]")
def get_project_features(project: str, keyword: Optional[str] = None, requestor: User = Depends(project_read_access)) -> list:
    response = requests.get(registry_url + "/projects/" + project + "/features", headers = get_api_header(requestor)).content.decode('utf-8')
    return json.loads(response)

@router.get("/features/{feature}", name="Get a single feature by feature Id [Read Access Required]")
def get_feature(feature: str, requestor: User = Depends(get_user)) -> dict:
    response = requests.get(registry_url + "/features/" + feature, headers = get_api_header(requestor)).content.decode('utf-8')
    ret = json.loads(response)

    feature_qualifiedName = ret['attributes']['qualifiedName']
    validate_project_access_for_feature(feature_qualifiedName, requestor, AccessType.READ)
    return ret

@router.get("/features/{feature}/lineage", name="Get Feature Lineage [Read Access Required]")
def get_feature_lineage(feature: str, requestor: User = Depends(get_user)) -> dict:
    response = requests.get(registry_url + "/features/" + feature + "/lineage", headers = get_api_header(requestor)).content.decode('utf-8')
    ret = json.loads(response)

    feature_qualifiedName = ret['guidEntityMap'][feature]['attributes']['qualifiedName']
    validate_project_access_for_feature(feature_qualifiedName, requestor, AccessType.READ)
    return ret

@router.post("/projects", name="Create new project with definition [Auth Required]")
def new_project(definition: dict, requestor: User = Depends(get_user)) -> dict:
    rbac.init_userrole(requestor, definition["name"])
    response = requests.post(url = registry_url + "/projects", params=definition, headers = get_api_header(requestor)).content.decode('utf-8')
    return json.loads(response)

@router.post("/projects/{project}/datasources", name="Create new data source of my project [Write Access Required]")
def new_project_datasource(project: str, definition: dict, requestor: User = Depends(project_write_access)) -> dict:
    response = requests.post(url = registry_url + "/projects/" + project + '/datasources', params=definition, headers = get_api_header(requestor)).content.decode('utf-8')
    return json.loads(response)

@router.post("/projects/{project}/anchors", name="Create new anchors of my project [Write Access Required]")
def new_project_anchor(project: str, definition: dict, requestor: User = Depends(project_write_access)) -> dict:
    response = requests.post(url = registry_url + "/projects/" + project + '/datasources', params=definition, headers = get_api_header(requestor)).content.decode('utf-8')
    return json.loads(response)


@router.post("/projects/{project}/anchors/{anchor}/features", name="Create new anchor features of my project [Write Access Required]")
def new_project_anchor_feature(project: str, anchor: str, definition: dict, requestor: User = Depends(project_write_access)) -> dict:
    response = requests.post(url = registry_url + "/projects/" + project + '/anchors/' + anchor + '/features', params=definition, headers = get_api_header(requestor)).content.decode('utf-8')
    return json.loads(response)


@router.post("/projects/{project}/derivedfeatures", name="Create new derived features of my project [Write Access Required]")
def new_project_derived_feature(project: str,definition: dict, requestor: User = Depends(project_write_access)) -> dict:
    response = requests.post(url = registry_url + "/projects/" + project + '/derivedfeatures', params=definition, headers = get_api_header(requestor)).content.decode('utf-8')
    return json.loads(response)

# Below are access control management APIs
@router.get("/userroles", name="List all active user role records [Global Admin Required]")
def get_userroles(requestor: User = Depends(global_admin_access)) -> list:
    return list([r.to_dict() for r in rbac.userroles])


@router.post("/users/{user}/userroles/add", name="Add a new user role [Global Admin Required]")
def add_userrole(project: str, user: str, role: str, reason: str, requestor: User = Depends(global_admin_access)):
    return rbac.add_userrole(project, user, role, reason, requestor.username)


@router.delete("/users/{user}/userroles/delete", name="Delete a user role [Global Admin Required]")
def delete_userrole(project: str, user: str, role: str, reason: str, requestor: User = Depends(global_admin_access)):
    return rbac.delete_userrole(project, user, role, reason, requestor.username)


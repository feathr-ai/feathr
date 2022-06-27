import json
from typing import Optional
from fastapi import APIRouter, Depends
import requests
from access_control.access import global_admin_access, get_user, project_read_access, project_write_access
from access_control.models import User
from access_control.db_rbac import DbRBAC
from access_control import config

router = APIRouter()
rbac = DbRBAC()
registry_url = config.REGISTRY_URL

@router.get('/projects', name="Get a list of Project Names [No Auth Required]")
async def get_projects() -> list[str]:
    response = requests.get(registry_url + "/projects").content.decode('utf-8')
    return json.loads(response)


@router.get('/projects/{project}', name="Get My Project [Read Access Required]")
async def get_project(project: str, requestor: User = Depends(project_read_access)):
    response = requests.get(registry_url + "/projects/" + project).content.decode('utf-8')
    return json.loads(response)



@router.get("/projects/{project}/datasources", name="Get data sources of my project [Read Access Required]")
def get_project_datasources(project: str, requestor: User = Depends(project_read_access)) -> list:
    response = requests.get(registry_url + "/projects/" + project + "/datasources").content.decode('utf-8')
    return json.loads(response)


@router.get("/projects/{project}/features", name="Get features under my project [Read Access Required]")
def get_project_features(project: str, keyword: Optional[str] = None, requestor: User = Depends(project_read_access)) -> list:
    response = requests.get(registry_url + "/projects/" + project + "/features").content.decode('utf-8')
    return json.loads(response)


@router.get("/features/{feature}/{project}", name="Get a single feature by feature Id [Read Access Required]")
def get_feature(feature: str, requestor: User = Depends(project_read_access)) -> dict:
    response = requests.get(registry_url + "/features/" + feature).content.decode('utf-8')
    return json.loads(response)


@router.get("/features/{feature}/lineage/{project}", name="Get Feature Lineage [Read Access Required]")
def get_feature_lineage(feature: str, requestor: User = Depends(project_read_access)) -> dict:
    response = requests.get(registry_url + "/features/" + feature + "/lineage").content.decode('utf-8')
    return json.loads(response)


@router.post("/projects", name="Create new project with definition [Auth Required]")
def new_project(definition: dict, requestor: User = Depends(get_user)) -> dict:
    rbac.init_userrole(requestor, definition["name"])
    response = requests.post(url = registry_url + "/projects", params=definition).content.decode('utf-8')
    return json.loads(response)

@router.post("/projects/{project}/datasources", name="Create new data source of my project [Write Access Required]")
def new_project_datasource(project: str, definition: dict, requestor: User = Depends(project_write_access)) -> dict:
    response = requests.post(url = registry_url + "/projects/" + project + '/datasources', params=definition).content.decode('utf-8')
    return json.loads(response)

@router.post("/projects/{project}/anchors", name="Create new anchors of my project [Write Access Required]")
def new_project_anchor(project: str, definition: dict, requestor: User = Depends(project_write_access)) -> dict:
    response = requests.post(url = registry_url + "/projects/" + project + '/datasources', params=definition).content.decode('utf-8')
    return json.loads(response)


@router.post("/projects/{project}/anchors/{anchor}/features", name="Create new anchor features of my project [Write Access Required]")
def new_project_anchor_feature(project: str, anchor: str, definition: dict, requestor: User = Depends(project_write_access)) -> dict:
    response = requests.post(url = registry_url + "/projects/" + project + '/anchors/' + anchor + '/features', params=definition).content.decode('utf-8')
    return json.loads(response)


@router.post("/projects/{project}/derivedfeatures", name="Create new derived features of my project [Write Access Required]")
def new_project_derived_feature(project: str,definition: dict, requestor: User = Depends(project_write_access)) -> dict:
    response = requests.post(url = registry_url + "/projects/" + project + '/derivedfeatures', params=definition).content.decode('utf-8')
    return json.loads(response)

# Below are access control management APIs


@router.get("/userroles", name="List all active user role records [Global Admin Required]")
def get_userroles(requestor: User = Depends(global_admin_access)) -> list:
    return list([r.to_dict() for r in rbac.userroles])


@router.post("/users/{user}/userroles/add", name="Add a new user role [Global Admin Required]")
def add_userrole(project: str, user: str, role: str, reason: str, requestor: User = Depends(global_admin_access)):
    return rbac.add_userrole(project, user, role, reason, requestor.preferred_username)


@router.delete("/users/{user}/userroles/delete", name="Delete a user role [Global Admin Required]")
def delete_userrole(project: str, user: str, role: str, reason: str, requestor: User = Depends(global_admin_access)):
    return rbac.delete_userrole(project, user, role, reason, requestor.preferred_username)


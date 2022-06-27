from http.client import OK
from typing import List, Optional

from fastapi import APIRouter, Body, Depends, status

from access_control.access import global_admin_access, get_user, project_read_access
from access_control.models import User
from access_control.db_rbac import DbRBAC

router = APIRouter()
rbac = DbRBAC()

# no authentication needed
# even anonymous users can check project names
@router.get('/projects', status_code=status.HTTP_200_OK, name="Get a list of Project Names [NO AUTH REQUIRED]")
async def get_projects(requestor: User = Depends(get_user)):
    return ['project 1','Project 2']

# requires user to be authenticated, only returns items for users with project read access
# that the user is authenticated is verified by Depends(get_user)
@router.get('/projects/{project}', status_code=status.HTTP_200_OK, name="Get My Project [Read Access Required]")
async def get_project(project: str, requestor: User = Depends(project_read_access)):
    return OK

@router.get("/projects/{project}/features")
def get_project_features(project: str, keyword: Optional[str] = None, requestor: User = Depends(project_read_access)) -> list:
    return OK

@router.get("/userroles")
def get_userroles(requestor: User = Depends(global_admin_access)) -> list:
        return list([r.to_dict() for r in rbac.userroles])


@router.post("/users/{user}/userroles/add")
def add_userrole(project: str, user: str, role: str, reason: str, requestor: User = Depends(global_admin_access)):
        return rbac.add_userrole(project, user, role, reason, requestor.preferred_username)


@router.delete("/users/{user}/userroles/delete")
def delete_userrole(project: str, user: str, role: str, reason: str, requestor: User = Depends(global_admin_access)):
    rbac.delete_userrole(project, user, role, reason, requestor.preferred_username)
    return OK
from typing import Any, Union
from uuid import UUID
from fastapi import Depends, HTTPException, status
from rbac.db_rbac import DbRBAC

from rbac.models import AccessType, User, UserAccess,_to_uuid
from rbac.auth import authorize

"""
All Access Validation Functions. Used as FastAPI Dependencies.
"""

rbac = DbRBAC()


class ForbiddenAccess(HTTPException):
    def __init__(self, detail: Any = None) -> None:
        super().__init__(status_code=status.HTTP_403_FORBIDDEN,
                         detail=detail, headers={"WWW-Authenticate": "Bearer"})


def get_user(user: User = Depends(authorize)) -> User:
    return user


def project_read_access(project: str, user: User = Depends(authorize)) -> UserAccess:
    return _project_access(project, user, AccessType.READ)


def project_write_access(project: str, user: User = Depends(authorize)) -> UserAccess:
    return _project_access(project, user, AccessType.WRITE)


def project_manage_access(project: str, user: User = Depends(authorize)) -> UserAccess:
    return _project_access(project, user, AccessType.MANAGE)


def _project_access(project: str, user: User, access: str) -> UserAccess:
    project = _get_project_name(project)
    if rbac.validate_project_access_users(project, user.username, access):
        return UserAccess(user.username, project)
    else:
        raise ForbiddenAccess(
            f"{access} access for project {project} is required for user {user.username}")


def global_admin_access(user: User = Depends(authorize)):
    if user.username in rbac.get_global_admin_users():
        return user
    else:
        raise ForbiddenAccess('Admin privileges required')

def validate_project_access_for_feature(feature:str, user:User, access:str):
    project = _get_project_from_feature(feature)
    _project_access(project, user, access)

def _get_project_from_feature(feature: str):
    feature_delimiter = "__"
    return feature.split(feature_delimiter)[0]

def get_api_header(username: str):
    return {
        "x-registry-requestor": username
    }

def _get_project_name(id_or_name: Union[str, UUID]):
    try:
        _to_uuid(id_or_name)
        if id_or_name not in rbac.projects_ids:
            # refresh project id map if id not found
            rbac.get_projects_ids()
        return rbac.projects_ids[id_or_name]
    except KeyError:
        raise ForbiddenAccess(f"Project Id {id_or_name} not found in Registry")
    except ValueError:
        pass
    # It is a name
    return id_or_name
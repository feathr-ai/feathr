from typing import Any
from fastapi import Depends, HTTPException, status
from rbac.db_rbac import DbRBAC

from rbac.models import AccessType, User
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


def project_read_access(project: str, user: User = Depends(authorize)) -> User:
    return _project_access(project, user, AccessType.READ)


def project_write_access(project: str, user: User = Depends(authorize)) -> User:
    return _project_access(project, user, AccessType.WRITE)


def project_manage_access(project: str, user: User = Depends(authorize)) -> User:
    return _project_access(project, user, AccessType.MANAGE)


def _project_access(project: str, user: User, access: str):
    if rbac.validate_project_access_users(project, user.username, access):
        return user
    else:
        raise ForbiddenAccess(
            f"{access} privileges for project {project} required for user {user.username}")


def global_admin_access(user: User = Depends(authorize)):
    if user.username in rbac.get_global_admin_users():
        return user
    else:
        raise ForbiddenAccess('Admin privileges required')

def validate_project_access_for_feature(feature:str, user:str, access:str):
    project = _get_project_from_feature(feature)
    _project_access(project, user, access)


def _get_project_from_feature(feature: str):
    feature_delimiter = "__"
    return feature.split(feature_delimiter)[0]

def get_api_header(requestor: User):
    return {
        "x-registry-requestor": requestor.username
    }
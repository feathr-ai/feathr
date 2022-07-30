from typing import List, Optional
from pydantic import BaseModel
from datetime import datetime
from enum import Enum

class User(BaseModel):
    id: str
    name: str
    username: str
    type: str
    roles: List[str]

class UserType(str, Enum):
    AAD_USER = "aad_user",
    USER_IMPERSONATION = "user_impersonation"
    AAD_APP = "aad_application",
    COMMON_USER = "common_user",
    UNKNOWN = "unknown",

SUPER_ADMIN_SCOPE = "global"


class AccessType(str, Enum):
    READ = "read",
    WRITE = "write",
    MANAGE = "manage",


class RoleType(str, Enum):
    ADMIN = "admin",
    CONSUMER = "consumer",
    PRODUCER = "producer",
    DEFAULT = "default",


RoleAccessMapping = {
    RoleType.ADMIN: ["read", "write", "manage"],
    RoleType.CONSUMER: ["read"],
    RoleType.PRODUCER: ["read", "write"],
    RoleType.DEFAULT: []
}


class UserRole():
    def __init__(self,
                 record_id: int,
                 project_name: str,
                 user_name: str,
                 role_name: str,
                 create_by: str,
                 create_reason: str,
                 create_time: datetime,
                 delete_by: Optional[str] = None,
                 delete_reason: Optional[str] = None,
                 delete_time: Optional[datetime] = None,
                 **kwargs):
        self.record_id = record_id
        self.project_name = project_name.lower()
        self.user_name = user_name.lower()
        self.role_name = role_name.lower()
        self.create_by = create_by.lower()
        self.create_reason = create_reason
        self.create_time = create_time
        self.delete_by = delete_by
        self.delete_reason = delete_reason
        self.delete_time = delete_time
        self.access = RoleAccessMapping[RoleType(self.role_name)]

    def to_dict(self) -> dict:
        return {
            "id": str(self.record_id),
            "scope": self.project_name,
            "userName": self.user_name,
            "roleName": str(self.role_name),
            "createBy": self.create_by,
            "createReason": self.create_reason,
            "createTime": str(self.create_time),
            "deleteBy": str(self.delete_by),
            "deleteReason": self.delete_reason,
            "deleteTime": str(self.delete_time),
            "access": self.access
        }


class Access():
    def __init__(self,
                 record_id: int,
                 project_name: str,
                 access_name: str) -> None:
        self.record_id = record_id
        self.project_name = project_name
        self.access_name = access_name

    def to_dict(self) -> dict:
        return {
            "record_id": str(self.record_id),
            "project_name": self.project_name,
            "access": self.access_name,
        }

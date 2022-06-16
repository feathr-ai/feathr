from datetime import datetime
from enum import Enum
from typing import Optional
from numpy import number
from registry.models import ToDict

SUPER_ADMIN_SCOPE = "global"


class RoleType(str, Enum):
    ADMIN = "admin",
    CONSUMER = "consumer",
    PRODUCER = "producer"
    DEFAULT = "default"


RoleAccessMapping = {
    RoleType.ADMIN: ["read", "write", "manage"],
    RoleType.CONSUMER: ["read"],
    RoleType.PRODUCER: ["read", "write"],
    RoleType.DEFAULT: []
}


class UserRole(ToDict):
    def __init__(self,
                 record_id: number,
                 project_name: str,
                 user_name: str,
                 role_name: str,
                 create_reason: str,
                 create_time: datetime,
                 delete_reason: Optional[str] = None,
                 delete_time: Optional[datetime] = None,
                 **kwargs):
        self.record_id = record_id
        self.project_name = project_name.lower()
        self.user_name = user_name.lower()
        self.role_name = role_name.lower()
        self.create_reason = create_reason
        self.create_time = create_time
        self.delete_reason = delete_reason
        self.delete_time = delete_time
        self.access = RoleAccessMapping[RoleType(self.role_name)]

    def to_dict(self) -> dict:
        return {
            "id": str(self.record_id),
            "scope": self.project_name,
            "userName": self.user_name,
            "roleName": str(self.role_name),
            "createReason": self.create_reason,
            "createTime": str(self.create_time),
            "deleteReason": self.delete_reason,
            "deleteTime": str(self.delete_time),
            "access": self.access
        }


class Access(ToDict):
    def __init__(self,
                 record_id: number,
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

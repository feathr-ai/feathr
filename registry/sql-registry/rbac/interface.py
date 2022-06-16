from abc import ABC, abstractmethod
from registry.database import DbConnection
from rbac.models import Access, UserRole
from uuid import UUID


class RBAC(ABC):
    @abstractmethod
    def get_userroles(self) -> list[UserRole]:
        """Get List of All User Role Records
        """
        pass

    @abstractmethod
    def add_userrole(self, userrole: UserRole):
        """Add a Role to a User
        """
        pass

    @abstractmethod
    def delete_userrole(self, userrole: UserRole):
        """Delete a Role of a User
        """
        pass

    @abstractmethod
    def init_userrole(self, creator_name: str, project_name: str):
        """Default User Role Relationship when a new project is created
        """
        pass

    @abstractmethod
    def get_userroles_by_user(self, user_name: str) -> list[UserRole]:
        """Get List of All User Role Records for a User
        """
        pass

    @abstractmethod
    def get_userroles_by_project(self, project_name: str) -> list[UserRole]:
        """Get List of All User Role Records for a Project
        """
        pass

    @abstractmethod
    def validate_access(self, userrole: UserRole, access: Access):
        """Validate if a Role has certian access
        """
        pass

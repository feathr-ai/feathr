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
    def get_userrole(self, user_name: str) -> list[UserRole]:
        """Get List of All User Role Records for a User
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
    def validate_access(self, userrole: UserRole, access: Access):
        """Validate if a Role has certian access
        """
        pass

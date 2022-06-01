from abc import ABC, abstractmethod
from typing import Union
from uuid import UUID
from registry.database import DbConnection

from registry.models import *

class Registry(ABC):
    @abstractmethod
    def get_projects(self) -> list[str]:
        pass

    @abstractmethod
    def get_entity(self, id_or_name: Union[str, UUID]) -> Entity:
        pass

    @abstractmethod
    def get_entities(self, ids: list[UUID]) -> list[Entity]:
        pass

    @abstractmethod
    def get_entity_id(self, id_or_name: Union[str, UUID]) -> UUID:
        pass
    
    @abstractmethod
    def get_neighbors(self, id_or_name: Union[str, UUID], relationship: RelationshipType) -> list[Edge]:
        pass

    @abstractmethod
    def get_lineage(self, id_or_name: Union[str, UUID]) -> EntitiesAndRelations:
        pass

    @abstractmethod
    def get_project(self, id_or_name: Union[str, UUID]) -> EntitiesAndRelations:
        pass

    @abstractmethod
    def search_entity(self,
                      keyword: str,
                      type: list[EntityType],
                      project: Optional[Union[str, UUID]] = None) -> list[EntityRef]:
        pass


if __name__ == '__main__':
    print("foo bar")
else:
    print("spam")

from typing import Optional, Tuple, Union
from uuid import UUID
from registry import Registry
from registry import connect
from registry.models import Edge, EntitiesAndRelations, Entity, EntityRef, EntityType, RelationshipType, to_type, to_uuid
import json


def quote(id):
    if isinstance(id, str):
        return f"'{id}'"
    else:
        return ",".join([f"'{i}'" for i in id])


class DbRegistry(Registry):
    def __init__(self):
        self.conn = connect()

    def get_projects(self) -> list[str]:
        ret = self.conn.execute(
            f"select qualified_name from entities where entity_type='{EntityType.Project}'")
        return list([r["qualified_name"] for r in ret])

    def get_entity(self, id_or_name: Union[str, UUID]) -> Entity:
        return self._fill_entity(self._get_entity(id_or_name))

    def get_entities(self, ids: list[UUID]) -> list[Entity]:
        return list([self._fill_entity(e) for e in self._get_entities(ids)])

    def get_entity_id(self, id_or_name: Union[str, UUID]) -> UUID:
        try:
            id = to_uuid(id_or_name)
            return id
        except ValueError:
            pass
        # It is a name
        ret = self.conn.execute(
            f"select entity_id from entities where qualified_name='{id_or_name}'")
        return ret[0]["entity_id"]

    def get_neighbors(self, id_or_name: Union[str, UUID], relationship: RelationshipType) -> list[Edge]:
        rows = self.conn.execute(fr'''
            select edge_id, from_id, to_id, conn_type
            from edges
            where from_id = '{self.get_entity_id(id_or_name)}'
            and conn_type = '{relationship.name}'
        ''')
        return list([Edge(**row) for row in rows])

    def get_lineage(self, id_or_name: Union[str, UUID]) -> EntitiesAndRelations:
        """
        Get feature lineage on both upstream and downstream
        Returns [entity_id:entity] map and list of edges have been traversed.
        """
        id = self.get_entity_id(id_or_name)
        upstream_entities, upstream_edges = self._bfs(
            id, RelationshipType.Consumes)
        downstream_entities, downstream_edges = self._bfs(
            id, RelationshipType.Produces)
        return EntitiesAndRelations(
            upstream_entities + downstream_entities,
            upstream_edges + downstream_edges)

    def get_project(self, id_or_name: Union[str, UUID]) -> EntitiesAndRelations:
        project = self._get_entity(id_or_name)
        edges = set(self.get_neighbors(id_or_name, RelationshipType.Contains))
        ids = list([e.to_id for e in edges])
        children = self._get_entities(ids)
        child_map = dict([(e.id, e) for e in children])
        project.attributes.children = children
        for anchor in project.attributes.anchors:
            conn = self.get_neighbors(anchor.id, RelationshipType.Contains)
            feature_ids = [e.to_id for e in conn]
            edges = edges.union(conn)
            features = list([child_map[id] for id in feature_ids])
            anchor.attributes.features = features
            source_id = self.get_neighbors(anchor.id, RelationshipType.Consumes)[0].to_id
            anchor.attributes.source = child_map[source_id]
        for df in project.attributes.derived_features:
            conn = self.get_neighbors(anchor.id, RelationshipType.Consumes)
            input_ids = [e.to_id for e in conn]
            edges = edges.union(conn)
            features = list([child_map[id] for id in input_ids])
            df.attributes.input_features = features
        all_edges = self._get_edges(ids)
        return EntitiesAndRelations([project] + children, list(edges.union(all_edges)))
    
    def _fill_entity(self, e: Entity) -> Entity:
        if e.entity_type == EntityType.Project:
            edges = self.get_neighbors(e.id, RelationshipType.Contains)
            ids = list([e.to_id for e in edges])
            children = self._get_entities(ids)
            e.attributes.children = children
            return e
        if e.entity_type == EntityType.Anchor:
            conn = self.get_neighbors(e.id, RelationshipType.Contains)
            feature_ids = [e.to_id for e in conn]
            features = self._get_entities(feature_ids)
            e.attributes.features = features
            source_id = self.get_neighbors(e.id, RelationshipType.Consumes)[0].to_id
            source = self.get_entity(source_id)
            e.attributes.source = source
            return e
        if e.entity_type == EntityType.DerivedFeature:
            conn = self.get_neighbors(e.id, RelationshipType.Consumes)
            feature_ids = [e.to_id for e in conn]
            features = self._get_entities(feature_ids)
            e.attributes.input_features = features
            return e
        return e
    
    def _get_edges(self, ids: list[UUID], types: list[RelationshipType] = []) -> list[Edge]:
        sql = fr"""select edge_id, from_id, to_id, conn_type from edges
        where from_id in ({quote(ids)})
        and to_id in ({quote(ids)})"""
        if len(types)>0:
            sql = fr"""select edge_id, from_id, to_id, conn_type from edges
            where conn_type in ({quote(types)})
            and from_id in ({quote(ids)})
            and to_id in ({quote(ids)})"""
        rows = self.conn.execute(sql)
        return list([to_type(row, Edge) for row in rows])
    
    def _get_entity(self, id_or_name: Union[str, UUID]) -> Entity:
        row = self.conn.execute(fr'''
            select entity_id, qualified_name, entity_type, attributes
            from entities
            where entity_id = '{self.get_entity_id(id_or_name)}'
        ''')[0]
        row["attributes"] = json.loads(row["attributes"])
        return to_type(row, Entity)

    def _get_entities(self, ids: list[UUID]) -> list[Entity]:
        rows = self.conn.execute(fr'''
            select entity_id, qualified_name, entity_type, attributes
            from entities
            where entity_id in ({quote(ids)})
        ''')
        ret = []
        for row in rows:
            row["attributes"] = json.loads(row["attributes"])
            ret.append(Entity(**row))
        return ret

    def _bfs(self, id: UUID, conn_type: RelationshipType) -> Tuple[list[Entity], list[Edge]]:
        """
        Breadth first traversal
        Starts from `id`, follow edges with `conn_type` only.
        """
        connections = []
        to_ids = [{
            "to_id": id,
        }]
        # BFS over SQL
        while len(to_ids) != 0:
            to_ids = self._bfs_step(to_ids, conn_type)
            connections.extend(to_ids)
        ids = set([id])
        for r in connections:
            ids.add(r["from_id"])
            ids.add(r["to_id"])
        entities = self.get_entities(ids)
        edges = list([Edge(**c) for c in connections])
        return (entities, edges)

    def _bfs_step(self, ids: list[UUID], conn_type: RelationshipType) -> set[dict]:
        """
        One step of the BFS process
        Returns all edges that connect to node ids the next step
        """
        ids = list([id["to_id"] for id in ids])
        sql = fr"""select edge_id, from_id, to_id, conn_type from edges where conn_type = '{conn_type.name}' and from_id in ({quote(ids)})"""
        return self.conn.execute(sql)

    def search_entity(self,
                      keyword: str,
                      type: list[EntityType]) -> list[EntityRef]:
        types = ",".join([quote(str(t)) for t in type])
        sql = fr'''select entity_id as id, qualified_name, entity_type as type from entities where qualified_name like %s and entity_type in ({types})'''
        rows = self.conn.execute(sql, ('%' + keyword + '%', ))
        return list([EntityRef(**row) for row in rows])

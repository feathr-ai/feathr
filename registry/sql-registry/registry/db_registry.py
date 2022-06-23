from typing import Optional, Tuple, Union
from uuid import UUID, uuid4

from pydantic import UUID4
from registry import Registry
from registry import connect
from registry.models import AnchorAttributes, AnchorDef, AnchorFeatureAttributes, AnchorFeatureDef, DerivedFeatureAttributes, DerivedFeatureDef, Edge, EntitiesAndRelations, Entity, EntityRef, EntityType, ProjectAttributes, ProjectDef, RelationshipType, SourceAttributes, SourceDef, _to_type, _to_uuid
import json


def quote(id):
    if isinstance(id, str):
        return f"'{id}'"
    if isinstance(id, UUID):
        return f"'{str(id)}'"
    else:
        return ",".join([quote(i) for i in id])


class DbRegistry(Registry):
    def __init__(self):
        self.conn = connect()

    def get_projects(self) -> list[str]:
        ret = self.conn.query(
            f"select qualified_name from entities where entity_type='{EntityType.Project}'")
        return list([r["qualified_name"] for r in ret])

    def get_entity(self, id_or_name: Union[str, UUID]) -> Entity:
        return self._fill_entity(self._get_entity(id_or_name))

    def get_entities(self, ids: list[UUID]) -> list[Entity]:
        return list([self._fill_entity(e) for e in self._get_entities(ids)])

    def get_entity_id(self, id_or_name: Union[str, UUID]) -> UUID:
        try:
            id = _to_uuid(id_or_name)
            return id
        except ValueError:
            pass
        # It is a name
        ret = self.conn.query(
            f"select entity_id from entities where qualified_name='{id_or_name}'")
        return ret[0]["entity_id"]

    def get_neighbors(self, id_or_name: Union[str, UUID], relationship: RelationshipType) -> list[Edge]:
        rows = self.conn.query(fr'''
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
        """
        This function returns not only the project itself, but also everything in the project
        """
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
            source_id = self.get_neighbors(
                anchor.id, RelationshipType.Consumes)[0].to_id
            anchor.attributes.source = child_map[source_id]
        for df in project.attributes.derived_features:
            conn = self.get_neighbors(anchor.id, RelationshipType.Consumes)
            input_ids = [e.to_id for e in conn]
            edges = edges.union(conn)
            features = list([child_map[id] for id in input_ids])
            df.attributes.input_features = features
        all_edges = self._get_edges(ids)
        return EntitiesAndRelations([project] + children, list(edges.union(all_edges)))

    def search_entity(self,
                      keyword: str,
                      type: list[EntityType]) -> list[EntityRef]:
        """
        WARN: This search function is implemented via `like` operator, which could be extremely slow.
        """
        types = ",".join([quote(str(t)) for t in type])
        sql = fr'''select entity_id as id, qualified_name, entity_type as type from entities where qualified_name like %s and entity_type in ({types})'''
        rows = self.conn.query(sql, ('%' + keyword + '%', ))
        return list([EntityRef(**row) for row in rows])

    def create_project(self, definition: ProjectDef) -> UUID:
        with self.conn.transaction() as c:
            c.execute(f'''select entity_id, entity_type, attributes from entities where qualified_name = %s''',
                      definition.qualified_name)
            r = c.fetchall()
            if r:
                if len(r) > 1:
                    assert False, "Data inconsistency detected, %d entities have same qualified_name %s" % (
                        len(r), definition.qualified_name)
                # The entity with same name already exists but with different type
                if _to_type(r[0]["entity_type"], EntityType) != EntityType.Project:
                    raise ValueError("Entity %s already exists" %
                                     definition.qualified_name)
                # Just return the existing project id
                return r[0]["entity_id"]
            id = uuid4()
            c.execute(f"insert into entities (entity_id, entity_type, qualified_name, attributes) values (%s, 'feathr_workspace_v1', %s, %s)",
                      (str(id),
                       definition.qualified_name,
                       definition.to_attr().to_json()))
            return id

    def create_project_datasource(self, project_id: UUID, definition: SourceDef) -> UUID:
        with self.conn.transaction() as c:
            c.execute(f'''select entity_id, entity_type, attributes from entities where qualified_name = %s''',
                      definition.qualified_name)
            r = c.fetchall()
            if r:
                if len(r) > 1:
                    assert False, "Data inconsistency detected, %d entities have same qualified_name %s" % (
                        len(r), definition.qualified_name)
                # The entity with same name already exists but with different type
                if _to_type(r[0]["entity_type"], EntityType) != EntityType.Source:
                    raise ValueError("Entity %s already exists" %
                                     definition.qualified_name)
                attr: SourceAttributes = _to_type(
                    r[0]["attributes"], SourceAttributes)
                if attr.name == definition.name \
                        and attr.type == definition.type \
                        and attr.path == definition.path \
                        and attr.preprocessing == definition.preprocessing \
                        and attr.event_timestamp_column == definition.event_timestamp_column \
                        and attr.timestamp_format == definition.timestamp_format:
                    # Creating exactly same entity
                    # Just return the existing id
                    return r[0]["entity_id"]
                raise ValueError("Entity %s already exists" %
                                 definition.qualified_name)
            id = uuid4()
            c.execute(f"insert into entities (entity_id, entity_type, qualified_name, attributes) values (%s, 'feathr_source_v1', %s, %s)",
                      (str(id),
                       definition.qualified_name,
                       definition.to_attr().to_json()))
            self._create_edge(c, project_id, id, RelationshipType.Contains)
            self._create_edge(c, id, project_id, RelationshipType.BelongsTo)
            return id

    def create_project_anchor(self, project_id: UUID, definition: AnchorDef) -> UUID:
        with self.conn.transaction() as c:
            c.execute(f'''select entity_id, entity_type, attributes from entities where qualified_name = %s''',
                      definition.qualified_name)
            r = c.fetchall()
            if r:
                if len(r) > 1:
                    assert False, "Data inconsistency detected, %d entities have same qualified_name %s" % (
                        len(r), definition.qualified_name)
                # The entity with same name already exists but with different type
                if _to_type(r[0]["entity_type"], EntityType) != EntityType.Anchor:
                    raise ValueError("Entity %s already exists" %
                                     definition.qualified_name)
                attr: AnchorAttributes = _to_type(
                    r[0]["attributes"], AnchorAttributes)
                if attr.name == definition.name \
                        and attr.source.id == definition.source_id:
                    # Creating exactly same entity
                    # Just return the existing id
                    return r[0]["entity_id"]
                raise ValueError("Entity %s already exists" %
                                 definition.qualified_name)
            c.execute("select entity_id, qualified_name from entities where entity_id = %s and entity_type = 'feathr_source_v1'", str(
                definition.source_id))
            r = c.fetchall()
            if not r:
                raise ValueError("Source %s does not exist" %
                                 definition.source_id)
            ref = EntityRef(r[0]["entity_id"],
                            EntityType.Source, r[0]["qualified_name"])
            id = uuid4()
            c.execute(f"insert into entities (entity_id, entity_type, qualified_name, attributes) values (%s, 'feathr_anchor_v1', %s, %s)",
                      (str(id),
                       definition.qualified_name,
                       definition.to_attr(ref).to_json()))
            self._create_edge(c, project_id, id, RelationshipType.Contains)
            self._create_edge(c, id, project_id, RelationshipType.BelongsTo)
            self._create_edge(c, id, definition.source_id,
                              RelationshipType.Consumes)
            self._create_edge(c, definition.source_id, id,
                              RelationshipType.Produces)
            return id

    def create_project_anchor_feature(self, project_id: UUID, anchor_id: UUID, definition: AnchorFeatureDef) -> UUID:
        with self.conn.transaction() as c:
            c.execute(f'''select entity_id, entity_type, attributes from entities where qualified_name = %s''',
                      definition.qualified_name)
            r = c.fetchall()
            if r:
                if len(r) > 1:
                    assert False, "Data inconsistency detected, %d entities have same qualified_name %s" % (
                        len(r), definition.qualified_name)
                # The entity with same name already exists but with different type
                if _to_type(r[0]["entity_type"], EntityType) != EntityType.AnchorFeature:
                    raise ValueError("Entity %s already exists" %
                                     definition.qualified_name)
                attr: AnchorFeatureAttributes = _to_type(
                    r[0]["attributes"], AnchorFeatureAttributes)
                if attr.name == definition.name \
                        and attr.type == definition.feature_type \
                        and attr.transformation == definition.transformation \
                        and attr.key == definition.key:
                    # Creating exactly same entity
                    # Just return the existing id
                    return r[0]["entity_id"]
                raise ValueError("Entity %s already exists" %
                                 definition.qualified_name)
            anchor: AnchorAttributes = self.get_entity(anchor_id).attributes
            source_id = anchor.source.id
            id = uuid4()
            c.execute(f"insert into entities (entity_id, entity_type, qualified_name, attributes) values (%s, 'feathr_anchor_feature_v1', %s, %s)",
                      (str(id),
                       definition.qualified_name,
                       definition.to_attr().to_json()))
            self._create_edge(c, project_id, id, RelationshipType.Contains)
            self._create_edge(c, id, project_id, RelationshipType.BelongsTo)
            self._create_edge(c, anchor_id, id, RelationshipType.Contains)
            self._create_edge(c, id, anchor_id, RelationshipType.BelongsTo)
            self._create_edge(c, id, source_id, RelationshipType.Consumes)
            self._create_edge(c, source_id, id, RelationshipType.Produces)
            return id

    def create_project_derived_feature(self, project_id: UUID, definition: DerivedFeatureDef) -> UUID:
        with self.conn.transaction() as c:
            c.execute(f'''select entity_id, entity_type, attributes from entities where qualified_name = %s''',
                      definition.qualified_name)
            r = c.fetchall()
            if r:
                if len(r) > 1:
                    assert False, "Data inconsistency detected, %d entities have same qualified_name %s" % (
                        len(r), definition.qualified_name)
                # The entity with same name already exists but with different type
                if _to_type(r[0]["entity_type"], EntityType) != EntityType.DerivedFeature:
                    raise ValueError("Entity %s already exists" %
                                     definition.qualified_name)
                attr: DerivedFeatureAttributes = _to_type(
                    r[0]["attributes"], DerivedFeatureAttributes)
                if attr.name == definition.name \
                        and attr.type == definition.feature_type \
                        and attr.transformation == definition.transformation \
                        and attr.key == definition.key:
                    # Creating exactly same entity
                    # Just return the existing id
                    return r[0]["entity_id"]
                raise ValueError("Entity %s already exists" %
                                 definition.qualified_name)
            r1 = []
            if definition.input_anchor_features:
                c.execute(
                    fr'''select entity_id, entity_type, qualified_name from entities where entity_id in ({quote(definition.input_anchor_features)}) and entity_type = 'feathr_anchor_feature_v1' ''')
                r1 = c.fetchall()
                if len(r1) != len(definition.input_anchor_features):
                    # TODO: More detailed error
                    raise(ValueError("Missing input anchor features"))
            r2 = []
            if definition.input_derived_features:
                c.execute(
                    fr'''select entity_id, entity_type, qualified_name from entities where entity_id in ({quote(definition.input_derived_features)}) and entity_type = 'feathr_derived_feature_v1' ''')
                r2 = c.fetchall()
                if len(r2) != len(definition.input_derived_features):
                    # TODO: More detailed error
                    raise(ValueError("Missing input derived features"))
            refs = list([EntityRef(r["entity_id"], r["entity_type"], r["qualified_name"]) for r in r1+r2])
            id = uuid4()
            c.execute(f"insert into entities (entity_id, entity_type, qualified_name, attributes) values (%s, 'feathr_anchor_feature_v1', %s, %s)",
                      (str(id),
                       definition.qualified_name,
                       definition.to_attr(refs).to_json()))
            self._create_edge(c, project_id, id, RelationshipType.Contains)
            self._create_edge(c, id, project_id, RelationshipType.BelongsTo)
            for r in r1+r2:
                input_feature_id = r["entity_id"]
                self._create_edge(c, id, input_feature_id,
                                  RelationshipType.Consumes)
                self._create_edge(c, input_feature_id, id,
                                  RelationshipType.Produces)
            return id

    def _create_edge(self, cursor, from_id: UUID, to_id: UUID, type: RelationshipType):
        sql = r'''
        IF NOT EXISTS (SELECT 1 FROM edges WHERE from_id=%(from_id)s and to_id=%(to_id)s and conn_type=%(type)s)
                BEGIN
                    INSERT INTO edges
                    (edge_id, from_id, to_id, conn_type)
                    values
                    (%(edge_id)s, %(from_id)s, %(to_id)s, %(type)s)
                END'''
        cursor.execute(sql, {
            "edge_id": str(uuid4()),
            "from_id": str(from_id),
            "to_id": str(to_id),
            "type": type.name
        })

    def _fill_entity(self, e: Entity) -> Entity:
        """
        Entities in the DB contains only attributes belong to itself, but the returned
        data model contains connections/contents, so we need to fill this gap
        """
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
            source_id = self.get_neighbors(
                e.id, RelationshipType.Consumes)[0].to_id
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
        if len(types) > 0:
            sql = fr"""select edge_id, from_id, to_id, conn_type from edges
            where conn_type in ({quote(types)})
            and from_id in ({quote(ids)})
            and to_id in ({quote(ids)})"""
        rows = self.conn.query(sql)
        return list([_to_type(row, Edge) for row in rows])

    def _get_entity(self, id_or_name: Union[str, UUID]) -> Entity:
        row = self.conn.query(fr'''
            select entity_id, qualified_name, entity_type, attributes
            from entities
            where entity_id = '{self.get_entity_id(id_or_name)}'
        ''')[0]
        row["attributes"] = json.loads(row["attributes"])
        return _to_type(row, Entity)

    def _get_entities(self, ids: list[UUID]) -> list[Entity]:
        if not ids:
            return []
        rows = self.conn.query(fr'''select entity_id, qualified_name, entity_type, attributes
            from entities
            where entity_id in ({quote(ids)})
        ''')
        ret = []
        for row in rows:
            row["attributes"] = json.loads(row["attributes"])
            print("XXX", row)
            ret.append(Entity(**row))
        return ret

    def _bfs(self, id: UUID, conn_type: RelationshipType) -> Tuple[list[Entity], list[Edge]]:
        """
        Breadth first traversal
        Starts from `id`, follow edges with `conn_type` only.

        WARN: There is no depth limit.
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
        return self.conn.query(sql)

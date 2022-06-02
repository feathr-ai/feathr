from abc import ABC, abstractmethod
from enum import Enum
from typing import Optional, Union
from uuid import UUID
import json
import re


def to_snake(d):
    if isinstance(d, str):
        return re.sub('([A-Z]\w+$)', '_\\1', d).lower()
    if isinstance(d, list):
        return [to_snake(i) if isinstance(i, (dict, list)) else i for i in d]
    return {to_snake(a): to_snake(b) if isinstance(b, (dict, list)) else b for a, b in d.items()}


def to_type(value, type):
    if isinstance(value, type):
        return value
    if isinstance(value, list):
        return list([to_type(v, type) for v in value])
    if isinstance(value, dict):
        if hasattr(type, "new"):
            try:
                return type.new(**to_snake(value))
            except TypeError:
                pass
        return type(**to_snake(value))
    if issubclass(type, Enum):
        try:
            n = int(value)
            return type(n)
        except ValueError:
            pass
        if hasattr(type, "new"):
            try:
                return type.new(value)
            except KeyError:
                pass
        return type[value]
    return type(value)


def to_uuid(value):
    return to_type(value, UUID)


class ValueType(Enum):
    UNSPECIFIED = 0
    BOOLEAN = 1
    INT = 2
    LONG = 3
    FLOAT = 4
    DOUBLE = 5
    STRING = 6
    BYTES = 7


class VectorType(Enum):
    TENSOR = 0


class TensorCategory(Enum):
    DENSE = 0
    SPARSE = 1


class EntityType(Enum):
    Project = 1
    Source = 2
    Anchor = 3
    AnchorFeature = 4
    DerivedFeature = 5

    @staticmethod
    def new(v):
        return {
            "feathr_workspace_v1": EntityType.Project,
            "feathr_source_v1": EntityType.Source,
            "feathr_anchor_v1": EntityType.Anchor,
            "feathr_anchor_feature_v1": EntityType.AnchorFeature,
            "feathr_derived_feature_v1": EntityType.DerivedFeature,
        }[v]

    def __str__(self):
        return {
            EntityType.Project: "feathr_workspace_v1",
            EntityType.Source: "feathr_source_v1",
            EntityType.Anchor: "feathr_anchor_v1",
            EntityType.AnchorFeature: "feathr_anchor_feature_v1",
            EntityType.DerivedFeature: "feathr_derived_feature_v1",
        }[self]


class RelationshipType(Enum):
    Contains = 1
    BelongsTo = 2
    Consumes = 3
    Produces = 4


class ToDict(ABC):
    @abstractmethod
    def to_dict(self) -> dict:
        pass

    def to_json(self, indent=None) -> str:
        return json.dumps(self.to_dict(), indent=indent)


class FeatureType(ToDict):
    def __init__(self,
                 type: Union[str, VectorType],
                 tensor_category: Union[str, TensorCategory],
                 dimension_type: list[Union[str, ValueType]],
                 val_type: Union[str, ValueType]):
        self.type = to_type(type, VectorType)
        self.tensor_category = to_type(tensor_category, TensorCategory)
        self.dimension_type = to_type(dimension_type, ValueType)
        self.val_type = to_type(val_type, ValueType)

    def to_dict(self) -> dict:
        return {
            "type": self.type.name,
            "tensorCategory": self.tensor_category.name,
            "dimensionType": [t.name for t in self.dimension_type],
            "valType": self.val_type.name,
        }


class TypedKey(ToDict):
    def __init__(self,
                 key_column: str,
                 key_column_type: ValueType,
                 full_name: Optional[str] = None,
                 description: Optional[str] = None,
                 key_column_alias: Optional[str] = None):
        self.key_column = key_column
        self.key_column_type = to_type(key_column_type, ValueType)
        self.full_name = full_name
        self.description = description
        self.key_column_alias = key_column_alias

    def to_dict(self) -> dict:
        ret = {
            "key_column": self.key_column,
            "key_column_type": self.key_column_type.name,
        }
        if self.full_name is not None:
            ret["full_name"] = self.full_name
        if self.description is not None:
            ret["description"] = self.full_name
        if self.key_column_alias is not None:
            ret["key_column_alias"] = self.key_column_alias
        return ret


class Transformation(ToDict):
    @staticmethod
    def new(**kwargs):
        if "transform_expr" in kwargs:
            return ExpressionTransformation(**kwargs)
        elif "def_expr" in kwargs:
            return WindowAggregationTransformation(**kwargs)
        elif "name" in kwargs:
            return UdfTransformation(**kwargs)
        else:
            raise ValueError(kwargs)


class ExpressionTransformation(Transformation):
    def __init__(self, transform_expr: str):
        self.transform_expr = transform_expr

    def to_dict(self) -> dict:
        return {
            "transform_expr": self.transform_expr
        }


class WindowAggregationTransformation(Transformation):
    def __init__(self,
                 def_expr: str,
                 agg_func: Optional[str] = None,
                 window: Optional[str] = None,
                 group_by: Optional[str] = None,
                 filter: Optional[str] = None,
                 limit: Optional[int] = None):
        self.def_expr = def_expr
        self.agg_func = agg_func
        self.window = window
        self.group_by = group_by
        self.filter = filter
        self.limit = limit

    def to_dict(self) -> dict:
        ret = {
            "def_expr": self.def_expr,
        }
        if self.agg_func is not None:
            ret["agg_func"] = self.agg_func
        if self.window is not None:
            ret["window"] = self.window
        if self.group_by is not None:
            ret["group_by"] = self.group_by
        if self.filter is not None:
            ret["filter"] = self.filter
        if self.limit is not None:
            ret["limit"] = self.limit
        return ret


class UdfTransformation(Transformation):
    def __init__(self, name: str):
        self.name = name

    def to_dict(self) -> dict:
        return {
            "name": self.name
        }


class EntityRef(ToDict):
    def __init__(self,
                 id: UUID,
                 type: Union[str, EntityType],
                 qualified_name: Optional[str] = None,
                 uniq_attr: dict = {}):
        self.id = id
        self.type = to_type(type, EntityType)
        if qualified_name is not None:
            self.uniq_attr = {"qualifiedName": qualified_name}
        else:
            self.uniq_attr = uniq_attr

    @property
    def entity_type(self) -> EntityType:
        return self.type

    @property
    def qualified_name(self) -> EntityType:
        return self.uniq_attr['qualifiedName']

    def get_ref(self):
        return self

    def to_dict(self) -> dict:
        return {
            "guid": str(self.id),
            "typeName": str(self.type),
            "uniqueAttributes": self.uniq_attr,
        }


class Attributes(ToDict):
    @staticmethod
    def new(entity_type: Union[str, EntityType], **kwargs):
        return {
            EntityType.Project: ProjectAttributes,
            EntityType.Source: SourceAttributes,
            EntityType.Anchor: AnchorAttributes,
            EntityType.AnchorFeature: AnchorFeatureAttributes,
            EntityType.DerivedFeature: DerivedFeatureAttributes,
        }[to_type(entity_type, EntityType)](**kwargs)


class Entity(ToDict):
    def __init__(self,
                 entity_id: Union[str, UUID],
                 qualified_name: str,
                 entity_type: Union[str, EntityType],
                 attributes: Union[dict, Attributes],
                 **kwargs):
        self.id = to_uuid(entity_id)
        self.qualified_name = qualified_name
        self.entity_type = to_type(entity_type, EntityType)
        if isinstance(attributes, Attributes):
            self.attributes = attributes
        else:
            self.attributes = Attributes.new(
                entity_type, **to_snake(attributes))

    def get_ref(self) -> EntityRef:
        return EntityRef(self.id,
                         self.attributes.entity_type,
                         self.qualified_name)

    def to_dict(self) -> dict:
        return {
            "guid": str(self.id),
            "lastModifiedTS": "1",
            "status": "ACTIVE",
            "displayText": self.attributes.name,
            "typeName": str(self.attributes.entity_type),
            "attributes": self.attributes.to_dict(),
        }


class ProjectAttributes(Attributes):
    def __init__(self,
                 name: str,
                 children: list[Union[dict, Entity]] = [],
                 tags: dict = {},
                 **kwargs):
        self.name = name
        self.tags = tags
        self._children = []
        if len(children) > 0:
            self.children = children

    @property
    def entity_type(self) -> EntityType:
        return EntityType.Project

    @property
    def children(self):
        return self._children

    @children.setter
    def children(self, v: list[Union[dict, Entity]]):
        for f in v:
            if isinstance(f, Entity):
                self._children.append(f)
            elif isinstance(f, dict):
                self._children.append(to_type(f, Entity))
            else:
                raise TypeError(f)

    @property
    def sources(self):
        return [
            e for e in self.children if e.entity_type == EntityType.Source]

    @property
    def anchors(self):
        return [
            e for e in self.children if e.entity_type == EntityType.Anchor]

    @property
    def anchor_features(self):
        return [
            e for e in self.children if e.entity_type == EntityType.AnchorFeature]

    @property
    def derived_features(self):
        return [
            e for e in self.children if e.entity_type == EntityType.DerivedFeature]

    def to_dict(self) -> dict:
        return {
            "qualifiedName": self.name,
            "name": self.name,
            "sources": list([e.get_ref().to_dict() for e in self.sources]),
            "anchors": list([e.get_ref().to_dict() for e in self.anchors]),
            "anchor_features": list([e.get_ref().to_dict() for e in self.anchor_features]),
            "derived_features": list([e.get_ref().to_dict() for e in self.derived_features]),
            "tags": self.tags,
        }


class SourceAttributes(Attributes):
    def __init__(self,
                 qualified_name: str,
                 name: str,
                 type: str,
                 path: str,
                 preprocessing: Optional[str] = None,
                 event_timestamp_column: Optional[str] = None,
                 timestamp_format: Optional[str] = None,
                 tags: dict = {}):
        self.qualified_name = qualified_name
        self.name = name
        self.type = type
        self.path = path
        self.preprocessing = preprocessing
        self.event_timestamp_column = event_timestamp_column
        self.timestamp_format = timestamp_format
        self.tags = tags

    @property
    def entity_type(self) -> EntityType:
        return EntityType.Source

    def to_dict(self) -> dict:
        ret = {
            "qualifiedName": self.qualified_name,
            "name": self.name,
            "type": self.type,
            "path": self.path,
            "tags": self.tags,
        }
        if self.preprocessing is not None:
            ret["preprocessing"] = self.preprocessing
        if self.event_timestamp_column is not None:
            ret["eventTimestampColumn"] = self.event_timestamp_column
        if self.timestamp_format is not None:
            ret["timestampFormat"] = self.timestamp_format
        return ret


class AnchorAttributes(Attributes):
    def __init__(self,
                 qualified_name: str,
                 name: str,
                 # source: Optional[Union[dict, EntityRef, Entity]] = None,
                 # features: list[Union[dict, EntityRef, Entity]] = [],
                 tags: dict = {},
                 **kwargs):
        self.qualified_name = qualified_name
        self.name = name
        self._source = None
        self._features = []
        # if source is not None:
        #     self._source = source.get_ref()
        # if len(features)>0:
        #     self._set_feature(features)
        self.tags = tags

    @property
    def entity_type(self) -> EntityType:
        return EntityType.Anchor

    @property
    def source(self) -> EntityRef:
        return self._source

    @source.setter
    def source(self, s):
        if isinstance(s, Entity):
            self._source = s.get_ref()
        elif isinstance(s, EntityRef):
            self._source = s
        elif isinstance(s, dict):
            self._source = to_type(s, Entity).get_ref()
        else:
            raise TypeError(s)

    @property
    def features(self):
        return self._features

    @features.setter
    def features(self, features):
        self._features = []
        for f in features:
            if isinstance(f, Entity):
                self._features.append(f.get_ref())
            elif isinstance(f, EntityRef):
                self._features.append(f)
            elif isinstance(f, dict):
                self._features.append(to_type(f, Entity).get_ref())
            else:
                raise TypeError(f)

    def to_dict(self) -> dict:
        ret = {
            "qualifiedName": self.qualified_name,
            "name": self.name,
            "features": list([e.get_ref().to_dict() for e in self.features]),
            "tags": self.tags,
        }
        if self.source is not None:
            ret["source"] = self.source.get_ref().to_dict()
        return ret


class AnchorFeatureAttributes(Attributes):
    def __init__(self,
                 qualified_name: str,
                 name: str,
                 type: Union[dict, FeatureType],
                 transformation: Union[dict, Transformation],
                 key: list[Union[dict, TypedKey]],
                 tags: dict = {}):
        self.qualified_name = qualified_name
        self.name = name
        self.type = to_type(type, FeatureType)
        self.transformation = to_type(transformation, Transformation)
        self.key = to_type(key, TypedKey)
        self.tags = tags

    @property
    def entity_type(self) -> EntityType:
        return EntityType.AnchorFeature

    def to_dict(self) -> dict:
        return {
            "qualifiedName": self.qualified_name,
            "name": self.name,
            "type": self.type.to_dict(),
            "transformation": self.transformation.to_dict(),
            "key": list([k.to_dict() for k in self.key]),
            "tags": self.tags,
        }


class DerivedFeatureAttributes(Attributes):
    def __init__(self,
                 qualified_name: str,
                 name: str,
                 type: Union[dict, FeatureType],
                 transformation: Union[dict, Transformation],
                 key: list[Union[dict, TypedKey]],
                 # input_anchor_features: list[Union[dict, EntityRef, Entity]] = [],
                 # input_derived_features: list[Union[dict, EntityRef, Entity]] = [],
                 tags: dict = {},
                 **kwargs):
        self.qualified_name = qualified_name
        self.name = name
        self.type = to_type(type, FeatureType)
        self.transformation = to_type(transformation, Transformation)
        self.key = to_type(key, TypedKey)
        self._input_anchor_features = []
        self._input_derived_features = []
        self.tags = tags
        # self._set_input_anchor_features(input_anchor_features)
        # self._set_input_derived_features(input_derived_features)

    @property
    def entity_type(self) -> EntityType:
        return EntityType.DerivedFeature

    @property
    def input_features(self):
        return self._input_anchor_features + self._input_derived_features

    @input_features.setter
    def input_features(self, v: Union[dict, Entity]):
        self._input_anchor_features = []
        self._input_derived_features = []
        for f in v:
            e = None
            if isinstance(f, Entity):
                e = f
            elif isinstance(f, dict):
                e = to_type(f, Entity)
            else:
                raise TypeError(f)

            if e.entity_type == EntityType.AnchorFeature:
                self._input_anchor_features.append(e)
            elif e.entity_type == EntityType.DerivedFeature:
                self._input_derived_features.append(e)
            else:
                pass

    @property
    def input_anchor_features(self):
        return self._input_anchor_features

    # @input_anchor_features.setter
    # def input_anchor_features(self, v):
    #     self._input_anchor_features = []
    #     for f in v:
    #         if isinstance(f, Entity):
    #             self._input_anchor_features.append(f.get_ref())
    #         elif isinstance(f, EntityRef):
    #             self._input_anchor_features.append(f)
    #         elif isinstance(f, dict):
    #             self._input_anchor_features.append(
    #                 to_type(f, Entity).get_ref())
    #         else:
    #             raise TypeError(f)

    @property
    def input_derived_features(self):
        return self._input_derived_features

    # @input_derived_features.setter
    # def input_derived_features(self, v):
    #     self._input_derived_features = []
    #     for f in v:
    #         if isinstance(f, Entity):
    #             self._input_derived_features.append(f.get_ref())
    #         elif isinstance(f, EntityRef):
    #             self._input_derived_features.append(f)
    #         elif isinstance(f, dict):
    #             self._input_derived_features.append(
    #                 to_type(f, Entity).get_ref())
    #         else:
    #             raise TypeError(f)

    def to_dict(self) -> dict:
        return {
            "qualifiedName": self.qualified_name,
            "name": self.name,
            "type": self.type.to_dict(),
            "transformation": self.transformation.to_dict(),
            "key": list([k.to_dict() for k in self.key]),
            "input_anchor_features": [e.get_ref().to_dict() for e in self.input_anchor_features],
            "input_derived_features": [e.get_ref().to_dict() for e in self.input_derived_features],
            "tags": self.tags,
        }


class Edge(ToDict):
    def __init__(self,
                 edge_id: Union[str, UUID],
                 from_id: Union[str, UUID],
                 to_id: Union[str, UUID],
                 conn_type: Union[str, RelationshipType]):
        self.id = to_uuid(edge_id)
        self.from_id = to_uuid(from_id)
        self.to_id = to_uuid(to_id)
        self.conn_type = to_type(conn_type, RelationshipType)

    def __eq__(self, o: object) -> bool:
        # Edge ID is kinda useless
        return self.from_id == o.from_id and self.to_id == o.to_id and self.conn_type == o.conn_type

    def __hash__(self) -> int:
        return hash((self.from_id, self.to_id, self.conn_type))

    def to_dict(self) -> dict:
        return {
            "relationshipId": str(self.id),
            "fromEntityId": str(self.from_id),
            "toEntityId": str(self.to_id),
            "relationshipType": self.conn_type.name,
        }


class EntitiesAndRelations(ToDict):
    def __init__(self, entities: list[Entity], edges: list[Edge]):
        self.entities = dict([(e.id, e) for e in entities])
        self.edges = set(edges)

    def to_dict(self) -> dict:
        return {
            "guidEntityMap": dict([(str(id), self.entities[id].to_dict()) for id in self.entities]),
            "relations": list([e.to_dict() for e in self.edges]),
        }


class ProjectDef:
    def __init__(self, qualified_name: str, tags: dict = {}):
        self.qualified_name = qualified_name
        self.name = qualified_name
        self.tags = tags


class SourceDef:
    def __init__(self,
                 qualified_name: str,
                 name: str,
                 path: str,
                 type: str,
                 preprocessing: Optional[str] = None,
                 event_timestamp_column: Optional[str] = None,
                 timestamp_format: Optional[str] = None,
                 tags: dict = {}):
        self.qualified_name = qualified_name
        self.name = name
        self.path = path
        self.type = type
        self.preprocessing = preprocessing
        self.event_timestamp_column = event_timestamp_column
        self.timestamp_format = timestamp_format
        self.tags = tags


class AnchorDef:
    def __init__(self,
                 qualified_name: str,
                 name: str,
                 source_id: Union[str, UUID],
                 tags: dict = {}):
        self.qualified_name = qualified_name
        self.name = name
        self.source_id = to_uuid(source_id)
        self.tags = tags


class AnchorFeatureDef:
    def __init__(self,
                 qualified_name: str,
                 name: str,
                 feature_type: Union[dict, FeatureType],
                 transformation: Union[dict, Transformation],
                 key: list[Union[dict, TypedKey]],
                 tags: dict = {}):
        self.qualified_name = qualified_name
        self.name = name
        self.feature_type = to_type(feature_type, FeatureType)
        self.transformation = to_type(transformation, Transformation)
        self.key = to_type(key, TypedKey)
        self.tags = tags


class DerivedFeatureDef:
    def __init__(self,
                 qualified_name: str,
                 name: str,
                 feature_type: Union[dict, FeatureType],
                 transformation: Union[dict, Transformation],
                 key: list[Union[dict, TypedKey]],
                 input_anchor_features: list[Union[str, UUID]],
                 input_derived_features: list[Union[str, UUID]],
                 tags: dict = {}):
        self.qualified_name = qualified_name
        self.name = name
        self.feature_type = to_type(feature_type, FeatureType)
        self.transformation = to_type(transformation, Transformation)
        self.key = to_type(key, TypedKey)
        self.input_anchor_features = to_uuid(input_anchor_features)
        self.input_derived_features = to_uuid(input_derived_features)
        self.tags = tags

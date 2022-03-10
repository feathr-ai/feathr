from __future__ import annotations
from copy import copy, deepcopy

from typing import List, Optional
from feathr.dtype import ValueType, FeatureType


class TypedKey:
    """A feature is an individual measurable property or characteristic of an 'Entity'.
      e.g. the price is a feature of a car, and the car is an 'entity'.
      Attributes:
        key_column: The id column name(s) of this entity. e.g. 'product_id'. The value(s)
                    of the id column(s) should identify a unique instance of the entity.
        key_column_type: Types of the key_column
        full_name: Unique name of the entity. Recommend using [project_name].[entity_name], e.g. foo.bar
        description: Documentation for the entity.
        key_column_alias: Used in relation feature. Default to the full_name with its '.' replaced with '_'.
    """
    def __init__(self,
                 key_column: str,
                 key_column_type: ValueType,
                 full_name: str,
                 description: str,
                 key_column_alias: Optional[str] = None) -> None:
        self.key_column = key_column
        self.key_column_type = key_column_type
        self.full_name = full_name
        self.description = description
        self.key_column_alias = key_column_alias if key_column_alias else self.key_column

    def as_key(self, key_column_alias: str) -> TypedKey:
        """Rename the key alias. This is useful in derived features that depends on the same feature
            with different keys.
        """
        new_key = deepcopy(self)
        new_key.key_column_alias = key_column_alias
        return new_key


DUMMY_KEY = TypedKey(key_column="NOT_NEEDED",
                     key_column_type=ValueType.UNSPECIFIED,
                     full_name="feathr.dummy_typedkey",
                     description="A dummy typed key for passthrough/request feature.")
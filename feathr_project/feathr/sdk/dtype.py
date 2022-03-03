import enum
from typing import Type, Union, List
from abc import ABC, abstractmethod

from jinja2 import Template

class ValueType(enum.Enum):
    """
    Basic value type. Used as feature key type.
    """
    UNSPECIFIED = 0
    BOOL = 1
    INT32 = 2
    INT64 = 3
    FLOAT = 4
    DOUBLE = 5
    STRING = 6
    BYTES = 7


class FeatureType(ABC):
    @abstractmethod
    def to_feature_config(self) -> str:
        pass

class BooleanFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
            type: BOOLEAN
        """

class Int32FeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
            type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: []
                valType: INT
            }
        """

class Int64FeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
            type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: []
                valType: LONG
            }
        """

class FloatFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
            type: NUMERIC
        """

class DoubleFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
            type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: []
                valType: DOUBLE
            }
        """

class StringFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
            type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: []
                valType: STRING
            }
        """

class BytesFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
            type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: []
                valType: BYTES
            }
        """

class DenseVectorFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
            type: DENSE_VECTOR
        """


# tensor dimension/axis
class Dimension:
    def __init__(self, shape: int, dType: ValueType = ValueType.INT32):
        self.shape = shape
        self.dType = dType
 
class TensorCategory(enum.Enum):
    DENSE = 0
    SPARSE = 1
    RAGGED = 2

class TensorFeatureType(FeatureType):
    def __init__(self,
                 dimensions: List[Dimension],
                 dType: ValueType,
                 tensor_category: TensorCategory = TensorCategory.DENSE) -> None:
        self.dimensions = dimensions
        self.dType = dType
        self.tensor_category = tensor_category

    def to_feature_config(self) -> str:
        tm = Template("""
            type: {
                type: TENSOR
                tensorCategory: {{tensor_catgory.name}}
                shape: [{{shape_list}}]
                dimensionType: [{{type_list}}]
                valType: {{val_type}}
            }
        """)

        shape_list = ','.join(str(dimension.shape) for dimension in self.dimensions)
        type_list = ','.join(dimension.dType.name for dimension in self.dimensions)
        return tm.render(tensor_catgory = self.tensor_category,
                shape_list = shape_list,
                type_list = type_list,
                val_type = self.dType.name)

BOOLEAN = BooleanFeatureType()
INT32 = Int32FeatureType()
INT64 = Int64FeatureType()
FLOAT = FloatFeatureType()
DOUBLE = DoubleFeatureType()
STRING = StringFeatureType()
BYTES = BytesFeatureType()
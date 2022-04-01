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
    """Base class for all feature types"""
    @abstractmethod
    def to_feature_config(self) -> str:
        pass

class BooleanFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
           type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: []
                valType: BOOLEAN
            }
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
            type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: []
                valType: FLOAT
            }
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

class FloatVectorFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
           type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: [INT]
                valType: FLOAT
            }
        """


class Int32VectorFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
           type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: [INT]
                valType: INT
            }
        """


class Int64VectorFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
           type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: [INT]
                valType: LONG
            }
        """


class DoubleVectorFeatureType(FeatureType):
    def to_feature_config(self) -> str:
        return """
           type: {
                type: TENSOR
                tensorCategory: DENSE
                dimensionType: [INT]
                valType: DOUBLE
            }
        """
# tensor dimension/axis
class Dimension:
    def __init__(self, shape: int, dType: ValueType = ValueType.INT32):
        self.shape = shape
        self.dType = dType


BOOLEAN = BooleanFeatureType()
INT32 = Int32FeatureType()
INT64 = Int64FeatureType()
FLOAT = FloatFeatureType()
DOUBLE = DoubleFeatureType()
STRING = StringFeatureType()
BYTES = BytesFeatureType()
FLOAT_VECTOR = FloatVectorFeatureType()
INT32_VECTOR = Int32VectorFeatureType()
INT64_VECTOR = Int64VectorFeatureType()
DOUBLE_VECTOR = DoubleVectorFeatureType()
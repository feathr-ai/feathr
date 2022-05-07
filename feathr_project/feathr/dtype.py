import enum
from feathr.frameconfig import HoconConvertible

class ValueType(enum.Enum):
    """Data type to describe feature keys or observation keys.

    Attributes:
        UNSPECIFIED: key data type is unspecified.
        BOOL: key data type is boolean, either true or false
        INT32: key data type is 32-bit integer, for example, an invoice id, 93231.
        INT64: key data type is 64-bit integer, for example, an invoice id, 93231.
        FLOAT: key data type is float, for example, 123.4f.
        DOUBLE: key data type is double, for example, 123.4d.
        STRING: key data type is string, for example, a user name, 'user_joe'
        BYTES: key data type is bytes.
    """
    UNSPECIFIED = 0
    BOOL = 1
    INT32 = 2
    INT64 = 3
    FLOAT = 4
    DOUBLE = 5
    STRING = 6
    BYTES = 7


class FeatureType(HoconConvertible):
    """Base class for all feature types"""
    pass

class BooleanFeatureType(FeatureType):
    """Boolean feature value, either true or false.
    """
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
    """32-bit integer feature value, for example, 123, 98765.
    """
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
    """64-bit integer(a.k.a. Long in some system) feature value, for example, 123, 98765 but stored in 64-bit integer.
    """
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
    """Float feature value, for example, 1.3f, 2.4f.
    """
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
    """Double feature value, for example, 1.3d, 2.4d. Double has better precision than float.
    """
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
    """String feature value, for example, 'apple', 'orange'.
    """
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
    """Bytes feature value.
    """
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
    """Float vector feature value, for example, [1,3f, 2.4f, 3.9f]
    """
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
    """32-bit integer vector feature value, for example, [1, 3, 9]
    """
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
    """64-bit integer vector feature value, for example, [1, 3, 9]
    """
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
    """Double vector feature value, for example, [1.3d, 3.3d, 9.3d]
    """
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
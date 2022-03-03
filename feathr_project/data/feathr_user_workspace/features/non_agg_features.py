from feathr.sdk.feature_anchor import FeatureAnchor
from feathr.sdk.feature import Feature
from feathr.sdk.dtype import BOOLEAN, FLOAT, TensorFeatureType, Dimension, ValueType
from feathr.sdk.transformation import ExprTransform
from feathr.sdk.source import PASSTHROUGH_SOURCE

features = [
    Feature(name = "trip_distance", feature_type = FLOAT),
    Feature(name = "f_is_long_trip_distance",
            feature_type = BOOLEAN,
            transform = ExprTransform("trip_distance>30")),
    Feature(name = "trip_embedding",
            feature_type = TensorFeatureType(dimensions=[Dimension(-1)], dType= ValueType.INT32)
            )
]

feature_anchor = FeatureAnchor(name = "nonAggFeatures",
                               batch_source = PASSTHROUGH_SOURCE,
                               features = features)
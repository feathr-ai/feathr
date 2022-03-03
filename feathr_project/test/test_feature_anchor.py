from feathr.sdk.feature_anchor import FeatureAnchor
from feathr.sdk.source import ADLSSource
from feathr.sdk.feature import Feature
from feathr.sdk.dtype import BOOLEAN, FLOAT
from feathr.sdk.transformation import ExprTransform
from feathr.sdk.dtype import Dimension, TensorFeatureType, ValueType
from feathr.sdk.source import PASSTHROUGH_SOURCE
from feathr.sdk.transformation import WindowAggTransform

def test_feature_anchor_to_config():

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
    print (feature_anchor.to_feature_config())

    batch_source = ADLSSource(name = "nycTaxiBatchSource",
                    path = "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data",
                    event_timestamp_column = "lpep_dropoff_datetime",
                    timestamp_format = "yyyy-MM-dd HH:mm:ss")
    agg_features = [Feature(name = "f_location_avg_fare",
                            feature_type=FLOAT,
                            transform=WindowAggTransform(agg_expr="float(fare_amount)",
                                                         agg_func="AVG",
                                                         window="3d")),
                    Feature(name = "f_location_max_fare",
                            feature_type=FLOAT,
                            transform=WindowAggTransform(agg_expr="float(fare_amount)",
                                                         agg_func="MAX",
                                                         window="3d"))
                    ]
    agg_feature_anchor = FeatureAnchor(name = "aggregationFeatures",
                                   batch_source = batch_source,
                                   features = agg_features,
                                   key_columns = ["DOLocationID"])
    print (agg_feature_anchor.to_feature_config())
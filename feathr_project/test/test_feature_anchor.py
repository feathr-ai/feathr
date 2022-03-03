from feathr.sdk.feature_anchor import FeatureAnchor
from feathr.sdk.source import HDFSSource
from feathr.sdk.feature import Feature
from feathr.sdk.dtype import BOOLEAN, FLOAT
from feathr.sdk.transformation import ExprTransform
from feathr.sdk.dtype import Dimension, TensorFeatureType, ValueType
from feathr.sdk.source import PASSTHROUGH_SOURCE
from feathr.sdk.transformation import WindowAggTransform

def test_non_agg_feature_anchor_to_config():

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
    expected_non_agg_feature_config = """
    // THIS FILE IS AUTO GENERATED.PLEASE DO NOT EDIT.
    anchors: {
        nonAggFeatures: {
            source: PASSTHROUGH
            key: [NOT_NEEDED]
            features: {
                trip_distance: {
                    type: NUMERIC
                    def: "trip_distance"
                }
                f_is_long_trip_distance: {
                    type: BOOLEAN
                    def: "trip_distance>30"
                }
                trip_embedding: {
                    type: {
                        type: TENSOR
                        tensorCategory: DENSE
                        shape: [-1]
                        dimensionType: [INT32]
                        valType: INT32
                    }
                    def: "trip_embedding"
                }
            }
        }
    }
    """
    assert ''.join(feature_anchor.to_feature_config().split()) == ''.join(expected_non_agg_feature_config.split())

def test_agg_feature_anchor_to_config():
    batch_source = HDFSSource(name = "nycTaxiBatchSource",
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

    expected_agg_feature_config = """
        // THIS FILE IS AUTO GENERATED.PLEASE DO NOT EDIT.
        anchors: {
            aggregationFeatures: {
                source: nycTaxiBatchSource
                key: [DOLocationID]
                features: {
                    f_location_avg_fare: {
                        type: NUMERIC
                        def: "float(fare_amount)"
                        window: 3d
                        agg: AVG
                    }
                    f_location_max_fare: {
                        type: NUMERIC
                        def: "float(fare_amount)"
                        window: 3d
                        agg: MAX
                    }
                }
            }
        }
        sources: {
            nycTaxiBatchSource: {
                location: {path: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data"}
                    timeWindowParameters: {
                        timestampColumn: "lpep_dropoff_datetime"
                        timestampColumnFormat: "yyyy-MM-dd HH:mm:ss"
                    }
            } 
        }
        """
    assert ''.join(agg_feature_anchor.to_feature_config().split()) == ''.join(expected_agg_feature_config.split())

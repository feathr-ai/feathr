from feathr.sdk.feature_anchor import FeatureAnchor
from feathr.sdk.source import HDFSSource
from feathr.sdk.feature import Feature
from feathr.sdk.dtype import BOOLEAN, FLOAT
from feathr.sdk.transformation import WindowAggTransform

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

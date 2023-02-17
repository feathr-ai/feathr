# Samples for feature naming conflicts check and handle
import pytest

from feathr import (TypedKey, ValueType, FeatureQuery, ObservationSettings, HdfsSource,
                    Feature,WindowAggTransformation, FLOAT)
from feathr import (FeathrClient, FeatureAnchor, ConflictsAutoCorrection)
from feathr.utils.job_utils import get_result_df
from datetime import datetime

# Example for feature naming conflicts check from python client side 
# with no 'auto-correction' enabled

# replace by your own config path
client = client = FeathrClient("feathr_config.yaml")
    
location_id = TypedKey(key_column="DOLocationID",
                key_column_type=ValueType.INT32,
                description="location id in NYC",
                full_name="nyc_taxi.location_id")
    
feature_query = FeatureQuery(
        feature_list=["trip_distance","fare_amount"], key=location_id)

# Defined feature names conflict with observation data set column names
settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04_with_index.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")
output_path = "wasbs://fake_path"
with pytest.raises(RuntimeError) as e:
    client.get_offline_features(observation_settings=settings,
                feature_query=feature_query,
                output_path=output_path
    )
assert str(e.value) == "Feature names exist conflicts with dataset column names: trip_distance,fare_amount"

# Defined feature names conflict with provided column names        
settings = ObservationSettings(
    observation_path="wasbs://public@fake_file",
    event_timestamp_column="lpep_dropoff_datetime",
    timestamp_format="yyyy-MM-dd HH:mm:ss")
output_path = "wasbs://fakepath"
with pytest.raises(RuntimeError) as e:
    client.get_offline_features(observation_settings=settings,
            feature_query=feature_query,
            output_path=output_path,
            dataset_column_names=set(('trip_distance','fare_amount'))
    )
assert str(e.value) == "Feature names exist conflicts with dataset column names: trip_distance,fare_amount"

# Example for feature naming conflicts when auto-correction is enabled

# replace by yout own confi path
client = FeathrClient(config_path='feathr_config.yaml', local_workspace_dir="conflicts_test")
batch_source = HdfsSource(name="nycTaxiBatchSource",
                    path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04_with_index.csv",
                    event_timestamp_column="lpep_dropoff_datetime",
                    timestamp_format="yyyy-MM-dd HH:mm:ss")
location_id = TypedKey(key_column="DOLocationID",
                    key_column_type=ValueType.INT32,
                    description="location id in NYC",
                    full_name="nyc_taxi.location_id")
pu_location_id = TypedKey(key_column="PULocationID",
                    key_column_type=ValueType.INT32,
                    description="location id in NYC",
                    full_name="nyc_taxi.location_id")

agg_features = [Feature(name="tip_amount",
                    key=[location_id, pu_location_id],
                    feature_type=FLOAT,
                    transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                                          agg_func="AVG",
                                                          window="3d")),
                Feature(name="total_amount",
                        key=[location_id, pu_location_id],
                        feature_type=FLOAT,
                        transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                                          agg_func="MAX",
                                                          window="3d")),
                ]

agg_anchor = FeatureAnchor(name="aggregationFeatures",
                           source=batch_source,
                           features=agg_features)
    
client.build_features(anchor_list=[agg_anchor])

now = datetime.now()

# Feature names 'tip_amount' and 'total_amount' are conflicted with dataset columns
# they will be renamed to 'tip_amount_test' and 'total_amoun_test' in the result
feature_query = FeatureQuery(
feature_list=["tip_amount", "total_amount"], key=location_id)
settings = ObservationSettings(
    observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04_with_index.csv",
    event_timestamp_column="lpep_dropoff_datetime",
    timestamp_format="yyyy-MM-dd HH:mm:ss",
    conflicts_auto_correction=ConflictsAutoCorrection(rename_features=True, suffix="test"))

# replace by your own output path 
output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
    
client.get_offline_features(observation_settings=settings,
                        feature_query=feature_query,
                        output_path=output_path
                        )
client.wait_job_to_finish(timeout_sec=500)

res_df = get_result_df(client, data_format="avro", res_url = output_path)
assert res_df.shape[0] > 0

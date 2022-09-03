
import glob
import shutil
import pandas as pd
import pandavro as pdx
import pytest
from pathlib import Path
import os
from datetime import datetime
from feathr import FeathrClient, ObservationSettings, FeatureQuery, TypedKey, HdfsSource, Feature, FeatureAnchor, INPUT_CONTEXT, FLOAT, INT32, BOOLEAN, DerivedFeature, WindowAggTransformation, ValueType
from feathr import BOOLEAN, FLOAT, INT32, ValueType


def test_local_spark_get_offline_features():
    """
    """
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"

    os.chdir(test_workspace_dir)

    client = local_test_setup(test_workspace_dir)

    assert client.spark_runtime == 'local'

    location_id = TypedKey(key_column="DOLocationID",
                               key_column_type=ValueType.INT32,
                               description="location id in NYC",
                               full_name="nyc_taxi.location_id")

    feature_query = FeatureQuery(
            feature_list=["f_location_avg_fare"], key=location_id)
    
    settings = ObservationSettings(
        observation_path="./green_tripdata_2020-04_with_index.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")

    now = datetime.now().strftime("%Y%m%d%H%M%S")
    output_path = os.path.join("debug", f"test_output_{now}")
    result = client.get_offline_features(observation_settings=settings,
                            feature_query=feature_query,
                            output_path=output_path,
                            verbose=False)
    assert result.returncode == 0

    df = parse_avro_result(output_path)
    assert df.__len__() == 35612

def test_local_spark_materialization():
    #TODO: add test for materialization
    return

def local_test_setup(local_workspace:str):

    now = datetime.now()

    client = FeathrClient(os.path.join(local_workspace, "feathr_config_local.yaml"), local_workspace_dir=local_workspace)
    batch_source = HdfsSource(name="nycTaxiBatchSource",
                              path="./green_tripdata_2020-04_with_index.csv",
                              event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    f_trip_distance = Feature(name="f_trip_distance",
                              feature_type=FLOAT, transform="trip_distance")
    f_trip_time_duration = Feature(name="f_trip_time_duration",
                                   feature_type=INT32,
                                   transform="(to_unix_timestamp(lpep_dropoff_datetime) - to_unix_timestamp(lpep_pickup_datetime))/60")

    features = [
        f_trip_distance,
        f_trip_time_duration,
        Feature(name="f_is_long_trip_distance",
                feature_type=BOOLEAN,
                transform="cast_float(trip_distance)>30"),
        Feature(name="f_day_of_week",
                feature_type=INT32,
                transform="dayofweek(lpep_dropoff_datetime)"),
    ]


    request_anchor = FeatureAnchor(name="request_features",
                                   source=INPUT_CONTEXT,
                                   features=features)

    f_trip_time_distance = DerivedFeature(name="f_trip_time_distance",
                                          feature_type=FLOAT,
                                          input_features=[
                                              f_trip_distance, f_trip_time_duration],
                                          transform="f_trip_distance * f_trip_time_duration")

    f_trip_time_rounded = DerivedFeature(name="f_trip_time_rounded",
                                         feature_type=INT32,
                                         input_features=[f_trip_time_duration],
                                         transform="f_trip_time_duration % 10")

    location_id = TypedKey(key_column="DOLocationID",
                           key_column_type=ValueType.INT32,
                           description="location id in NYC",
                           full_name="nyc_taxi.location_id")
    agg_features = [Feature(name="f_location_avg_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                                              agg_func="AVG",
                                                              window="90d",
                                                              )),
                    Feature(name="f_location_max_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                                              agg_func="MAX",
                                                              window="90d"))
                    ]

    agg_anchor = FeatureAnchor(name="aggregationFeatures",
                               source=batch_source,
                               features=agg_features)

    client.build_features(anchor_list=[agg_anchor, request_anchor], derived_feature_list=[
        f_trip_time_distance, f_trip_time_rounded])
    return client 

def parse_avro_result(output_path):
    print(output_path)
    dataframe_list = []
    # assuming the result are in avro format
    for file in glob.glob(os.path.join(output_path, '*.avro')):
        print(file)
        dataframe_list.append(pdx.read_avro(file))
    
    # output folder will be cleaned up.
    shutil.rmtree(output_path)
    vertical_concat_df = pd.concat(dataframe_list, axis=0)
    return vertical_concat_df

if __name__ == "__main__":
    test_local_spark_get_offline_features()


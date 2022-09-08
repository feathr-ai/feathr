import time
import pytest
import glob
import shutil
import pandas as pd
import pandavro as pdx
from pathlib import Path
import os
from datetime import datetime
from feathr import FeathrClient, ObservationSettings, FeatureQuery, TypedKey, HdfsSource, Feature, FeatureAnchor, INPUT_CONTEXT, FLOAT, INT32, BOOLEAN, DerivedFeature, WindowAggTransformation, ValueType
from feathr import BOOLEAN, FLOAT, INT32, ValueType

from pyspark.sql import DataFrame
from pyspark.sql.functions import col

from subprocess import Popen


def test_local_spark_get_offline_features():
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"

    client = _local_client_setup(test_workspace_dir)

    #This Test is for Local Spark only
    if client.spark_runtime != 'local':
        return
    
    output_path, proc1 = _non_udf_features(client)
    result = client.feathr_spark_launcher.wait_for_completion()
    assert result == True

    df = parse_avro_result(output_path)
    assert df.__len__() == 35612
    shutil.rmtree('debug')
    return 

def test_local_spark_pyudf_get_offline_features():
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"

    client = _local_client_setup(test_workspace_dir)

    #This Test is for Local Spark only
    if client.spark_runtime != 'local':
        return
    
    output_path, proc = _udf_features(client)

    result = client.feathr_spark_launcher.wait_for_completion()
    assert result == True
    df = parse_avro_result(output_path)
    assert df.__len__() == 35612
    shutil.rmtree('debug')
    return 

def test_local_spark_materialization():
    #TODO: add test for materialization
    return

def _local_client_setup(local_workspace:str):
    os.chdir(local_workspace)
    client = FeathrClient(os.path.join(local_workspace, "feathr_config_local.yaml"), local_workspace_dir=local_workspace)
    return client

def _non_udf_features(client:FeathrClient):
    
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

    location_id = TypedKey(key_column="DOLocationID",
                               key_column_type=ValueType.INT32,
                               description="location id in NYC",
                               full_name="nyc_taxi.location_id")

    feature_query = FeatureQuery(
            feature_list=["f_location_avg_fare"], key=location_id)
    
    settings = ObservationSettings(
        observation_path="./green_tripdata_2020-04_with_index.csv",
        #observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")

    now = datetime.now().strftime("%Y%m%d%H%M%S")
    output_path = os.path.join("debug", f"test_output_{now}")
    proc = client.get_offline_features(observation_settings=settings,
                            feature_query=feature_query,
                            output_path=output_path,
                            config_file_name = "feature_join_conf/feature_join_local.conf",
                            verbose=False)
    return output_path, proc

def _udf_features(client:FeathrClient):
    batch_source1 = HdfsSource(name="nycTaxiBatchSource_add_new_dropoff_and_fare_amount_column",
                              path="./green_tripdata_2020-04_with_index.csv",
                              preprocessing=add_new_dropoff_and_fare_amount_column,
                              event_timestamp_column="new_lpep_dropoff_datetime",
                              # event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    batch_source2 = HdfsSource(name="nycTaxiBatchSource_add_new_fare_amount",
                              path="./green_tripdata_2020-04_with_index.csv",
                              preprocessing=add_new_fare_amount,
                              event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")


    location_id = TypedKey(key_column="DOLocationID",
                           key_column_type=ValueType.INT32,
                           description="location id in NYC",
                           full_name="nyc_taxi.location_id")


    agg_features = [Feature(name="f_location_avg_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="new_fare_amount",
                                                              agg_func="SUM",
                                                              window="90d")),
                    Feature(name="f_location_max_fare",
                            key=location_id,
                            feature_type=FLOAT,
                            transform=WindowAggTransformation(agg_expr="new_fare_amount",
                                                              agg_func="MAX",
                                                              window="90d"))
                    ]

    agg_anchor = FeatureAnchor(name="aggregationFeatures",
                               source=batch_source1,
                               features=agg_features,
                               )

    pickup_time_as_id = TypedKey(key_column="lpep_pickup_datetime",
                                 key_column_type=ValueType.INT32,
                                 description="location id in NYC",
                                 full_name="nyc_taxi.location_id")

    features = [
        Feature(name="f_is_long_trip_distance",
                key=pickup_time_as_id,
                feature_type=FLOAT,
                transform="fare_amount_new"),
        Feature(name="f_day_of_week",
                key=pickup_time_as_id,
                feature_type=INT32,
                transform="dayofweek(lpep_dropoff_datetime)"),
    ]

    regular_anchor = FeatureAnchor(name="regular_anchor",
                                   source=batch_source2,
                                   features=features,
                                   )

    client.build_features(anchor_list=[agg_anchor, regular_anchor])

    feature_query = [FeatureQuery(
        feature_list=["f_is_long_trip_distance", "f_day_of_week"], key=pickup_time_as_id),
        FeatureQuery(
            feature_list=["f_location_avg_fare", "f_location_max_fare"], key=location_id)
    ]

    settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")

    now = datetime.now().strftime("%Y%m%d%H%M%S")
    output_path = os.path.join("debug", f"test_output_{now}")
    proc = client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path)

    return output_path, proc

def add_new_dropoff_and_fare_amount_column(df: DataFrame):
    df = df.withColumn("new_lpep_dropoff_datetime", col("lpep_dropoff_datetime"))
    df = df.withColumn("new_fare_amount", col("fare_amount") + 1000000)
    return df

def add_new_fare_amount(df: DataFrame) -> DataFrame:
    df = df.withColumn("fare_amount_new", col("fare_amount") + 8000000)

    return df

def parse_avro_result(output_path):
    print(output_path)
    dataframe_list = []
    # assuming the result are in avro format
    for file in glob.glob(os.path.join(output_path, '*.avro')):
        dataframe_list.append(pdx.read_avro(file))
    
    vertical_concat_df = pd.concat(dataframe_list, axis=0)
    return vertical_concat_df
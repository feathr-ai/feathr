from feathr import FeatureAnchor
from feathr.client import FeathrClient
from feathr import BOOLEAN, FLOAT, INT32, STRING, ValueType
from feathr import Feature
from feathr import DerivedFeature
from feathr import INPUT_CONTEXT, HdfsSource
from feathr import WindowAggTransformation
from feathr import TypedKey
from datetime import datetime, timedelta
import os

def basic_test_setup(config_path: str):

    now = datetime.now()
    # set workspace folder by time; make sure we don't have write conflict if there are many CI tests running
    os.environ['SPARK_CONFIG__DATABRICKS__WORK_DIR'] = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), '_', str(now.microsecond)]) 
    os.environ['SPARK_CONFIG__AZURE_SYNAPSE__WORKSPACE_DIR'] = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/feathr_github_ci','_', str(now.minute), '_', str(now.second) ,'_', str(now.microsecond)]) 
    
    client = FeathrClient(config_path=config_path)
    batch_source = HdfsSource(name="nycTaxiBatchSource",
                              path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                              event_timestamp_column="lpep_dropoff_datetime",
                              timestamp_format="yyyy-MM-dd HH:mm:ss")

    f_trip_distance = Feature(name="f_trip_distance",
                              feature_type=FLOAT, transform="trip_distance")
    f_trip_time_duration = Feature(name="f_trip_time_duration",
                                   feature_type=INT32,
                                   transform="time_duration(lpep_pickup_datetime, lpep_dropoff_datetime, 'minutes')")

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
                                                              window="90d")),
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




def snowflake_test_setup(config_path: str):
    now = datetime.now()
    # set workspace folder by time; make sure we don't have write conflict if there are many CI tests running
    os.environ['SPARK_CONFIG__DATABRICKS__WORK_DIR'] = ''.join(['dbfs:/feathrazure_cijob_snowflake','_', str(now.minute), '_', str(now.second), '_', str(now.microsecond)]) 
    os.environ['SPARK_CONFIG__AZURE_SYNAPSE__WORKSPACE_DIR'] = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/feathr_github_ci_snowflake','_', str(now.minute), '_', str(now.second), '_', str(now.microsecond)]) 

    client = FeathrClient(config_path=config_path)
    batch_source = HdfsSource(name="snowflakeSampleBatchSource",
                              path="jdbc:snowflake://dqllago-ol19457.snowflakecomputing.com/?user=feathrintegration&sfWarehouse=COMPUTE_WH&dbtable=CALL_CENTER&sfDatabase=SNOWFLAKE_SAMPLE_DATA&sfSchema=TPCDS_SF10TCL",
                              )
    call_sk_id = TypedKey(key_column="CC_CALL_CENTER_SK",
                          key_column_type=ValueType.INT32,
                          description="call center sk",
                          full_name="snowflake.CC_CALL_CENTER_SK")

    f_snowflake_call_center_division_name = Feature(name="f_snowflake_call_center_division_name",feature_type=STRING, transform="CC_DIVISION_NAME", key=call_sk_id)

    f_snowflake_call_center_zipcode = Feature(name="f_snowflake_call_center_zipcode",feature_type=STRING, transform="CC_ZIP", key=call_sk_id)



    features = [f_snowflake_call_center_division_name, f_snowflake_call_center_zipcode ]


    snowflakeFeatures = FeatureAnchor(name="snowflakeFeatures",
                                   source=batch_source,
                                   features=features)




    client.build_features(anchor_list=[snowflakeFeatures])

    return client

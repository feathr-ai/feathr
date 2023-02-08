import os
import time
from datetime import datetime
from pathlib import Path

from feathr import (FeatureQuery, ObservationSettings, SparkExecutionConfiguration, TypedKey, 
                    ValueType, Feature, FeatureAnchor, BackfillTime, timedelta, MaterializationSettings, 
                    CosmosDbSource, WindowAggTransformation, RedisSink, FLOAT)
from feathr.client import FeathrClient
from feathr.constants import OUTPUT_FORMAT
from feathr.utils.job_utils import get_result_df
from test_fixture import basic_test_setup
from test_utils.constants import Constants


# test parquet file read/write without an extension name
def test_feathr_get_offline_features_with_parquet():
    """
    Test if the program can read and write parquet files
    """

    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"

    client: FeathrClient = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    location_id = TypedKey(key_column="DOLocationID",
                            key_column_type=ValueType.INT32)

    feature_query = FeatureQuery(
        feature_list=["f_location_avg_fare"], key=location_id)
    settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")

    now = datetime.now()
    # set output folder based on different runtime
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second),'_', str(now.microsecond),  ".parquet"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".parquet"])


    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path,
                                execution_configurations=SparkExecutionConfiguration({"spark.feathr.inputFormat": "parquet", "spark.feathr.outputFormat": "parquet"})
                                )

    # assuming the job can successfully run; otherwise it will throw exception
    client.wait_job_to_finish(timeout_sec=Constants.SPARK_JOB_TIMEOUT_SECONDS)

    # download result and just assert the returned result is not empty
    res_df = get_result_df(client)
    assert res_df.shape[0] > 0


# test delta lake read/write without an extension name
def test_feathr_get_offline_features_with_delta_lake():
    """
    Test if the program can read and write delta lake
    """

    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    location_id = TypedKey(key_column="DOLocationID",
                            key_column_type=ValueType.INT32)

    feature_query = FeatureQuery(
        feature_list=["f_location_avg_fare"], key=location_id)
    settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/feathr_delta_table",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")

    now = datetime.now()
    # set output folder based on different runtime
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), "_deltalake"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), "_deltalake"])


    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path,
                                execution_configurations=SparkExecutionConfiguration({"spark.feathr.inputFormat": "delta", "spark.feathr.outputFormat": "delta"})
                                )

    # assuming the job can successfully run; otherwise it will throw exception
    client.wait_job_to_finish(timeout_sec=Constants.SPARK_JOB_TIMEOUT_SECONDS)

    # wait for a few secs for the resource to come up in the databricks API
    time.sleep(5)

    # download result and just assert the returned result is not empty
    # if users are using delta format in synapse, skip this check, due to issue https://github.com/delta-io/delta-rs/issues/582
    result_format: str = client.get_job_tags().get(OUTPUT_FORMAT, "")
    if not (client.spark_runtime == 'azure_synapse' and result_format == 'delta'):
        res_df = get_result_df(client)
        assert res_df.shape[0] > 0
        
def test_feathr_materialize_features_with_cosmosDb():
    """
    Test if the program can read from cosmos DB
    """
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    client: FeathrClient = FeathrClient(config_path=os.path.join(test_workspace_dir, "feathr_config_bak.yaml"))

    container = "test_cosmosDbSource_output"
    batch_source = CosmosDbSource(name='cosmos1', endpoint='https://feathrazuretest3-cosmosdb.documents.azure.com:443/', database='feathr', container=table_name)
    key = TypedKey(key_column="key0",
               key_column_type=ValueType.INT32)
    agg_features = [
        Feature(name="f_loc_avg_output",
            key=[key],
            feature_type=FLOAT,
            transform=WindowAggTransformation(agg_expr="f_location_avg_fare",
                                              agg_func="AVG",
                                              window="3d")),
        Feature(name="f_loc_max_output",
            feature_type=FLOAT,
            key=[key],
            transform=WindowAggTransformation(agg_expr="f_location_max_fare",
                                              agg_func="MAX",
                                              window="3d")),
        ]

    agg_anchor = FeatureAnchor(name="testTimePartitionFeatures",
                           source=batch_source,
                           features=agg_features)
    client.build_features(anchor_list=[agg_anchor])

    now = datetime.now()
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
    else:
        output_path = ''.join(['abfss://eyxfs@eyxdls.dfs.core.windows.net/cosmosdb'])
          

    backfill_time = BackfillTime(start=datetime(
            2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))

    offline_redis_sink = RedisSink('feathr_test_table')
    settings = MaterializationSettings("nycTaxiTable",
                sinks=[offline_redis_sink],
                feature_names=[
                    "f_loc_avg_output"],
                backfill_time=backfill_time)
   
    client.materialize_features(settings)
    client.wait_job_to_finish(timeout_sec=500)

    res_df = get_result_df(client, data_format="avro", res_url = output_path)
    res_df.to_csv('cosmosdb.csv')

import os
from datetime import datetime, timedelta
from pathlib import Path

from feathr.client import FeathrClient
from feathr import ValueType
from feathr.job_utils import get_result_df
from feathr import (BackfillTime, MaterializationSettings)
from feathr import FeatureQuery
from feathr import ObservationSettings
from feathr import RedisSink
from feathr import TypedKey
from test_fixture import snowflake_test_setup


def test_feathr_online_store_agg_features():
    """
    Test FeathrClient() get_online_features and batch_get can get feature data correctly.
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
    
    client = snowflake_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    backfill_time = BackfillTime(start=datetime(2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name="snowflakeSampleDemoFeature")
    settings = MaterializationSettings(name="snowflakeSampleDemoFeature",
                                   sinks=[redisSink],
                                   feature_names=['f_snowflake_call_center_division_name',
                                                  'f_snowflake_call_center_zipcode'],
                                   backfill_time=backfill_time)
    client.materialize_features(settings)
    # just assume the job is successful without validating the actual result in Redis. Might need to consolidate
    # this part with the test_feathr_online_store test case
    client.wait_job_to_finish(timeout_sec=900)

    res = client.get_online_features('snowflakeSampleDemoFeature', '1',
                                     ['f_snowflake_call_center_division_name', 'f_snowflake_call_center_zipcode'])

    assert len(res) == 2
    assert res[0] != None
    assert res[1] != None
    res = client.multi_get_online_features('snowflakeSampleDemoFeature',
                                    ['1', '2'],
                                    ['f_snowflake_call_center_division_name', 'f_snowflake_call_center_zipcode'])
    assert res['1'][0] != None
    assert res['1'][1] != None
    assert res['2'][0] != None
    assert res['2'][1] != None


def test_feathr_get_offline_features():
    """
    Test get_offline_features() can get feature data from Snowflake source correctly.
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
    
    
    client = snowflake_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    call_sk_id = TypedKey(key_column="CC_CALL_CENTER_SK",
                          key_column_type=ValueType.INT32,
                          description="call center sk",
                          full_name="snowflake.CC_CALL_CENTER_SK")

    feature_query = FeatureQuery(
        feature_list=['f_snowflake_call_center_division_name', 'f_snowflake_call_center_zipcode'],
        key=call_sk_id)
    settings = ObservationSettings(
        observation_path='jdbc:snowflake://dqllago-ol19457.snowflakecomputing.com/?user=feathrintegration&sfWarehouse'
                         '=COMPUTE_WH&dbtable=CALL_CENTER&sfDatabase=SNOWFLAKE_SAMPLE_DATA&sfSchema=TPCDS_SF10TCL')
    
    now = datetime.now()
     # set output folder based on different runtime
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob_snowflake','_', str(now.minute), '_', str(now.second), ".avro"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/snowflake_output','_', str(now.minute), '_', str(now.second), ".avro"])
    
    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path)

    # assuming the job can successfully run; otherwise it will throw exception
    client.wait_job_to_finish(timeout_sec=900)

    res = get_result_df(client)
    # just assume there are results.
    assert res.shape[0] > 1

import os
from datetime import datetime, timedelta
from pathlib import Path

from feathr.client import FeathrClient
from feathr.dtype import ValueType
from feathr.job_utils import get_result_df
from feathr.materialization_settings import (BackfillTime, MaterializationSettings)
from feathr.query_feature_list import FeatureQuery
from feathr.settings import ObservationSettings
from feathr.sink import RedisSink
from feathr.typed_key import TypedKey


def test_feathr_online_store_agg_features():
    """
    Test FeathrClient() get_online_features and batch_get can get feature data correctly.
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
    os.chdir(test_workspace_dir)
    client = FeathrClient()
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
    client.wait_job_to_finish(timeout_sec=600)

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
    os.chdir(test_workspace_dir)
    client = FeathrClient()

    call_sk_id = TypedKey(key_column="CC_CALL_CENTER_SK",
                          key_column_type=ValueType.INT32,
                          description="call sk",
                          full_name="snowflake.CC_CALL_CENTER_SK")
    feature_query = FeatureQuery(
        feature_list=['f_snowflake_call_center_division_name', 'f_snowflake_call_center_zipcode'],
        key=call_sk_id)
    settings = ObservationSettings(
        observation_path='jdbc:snowflake://dqllago-ol19457.snowflakecomputing.com/?user=feathrintegration&sfWarehouse'
                         '=COMPUTE_WH&dbtable=CALL_CENTER&sfDatabase=SNOWFLAKE_SAMPLE_DATA&sfSchema=TPCDS_SF10TCL')
    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net"
                                            "/demo_data/snowflake/output.avro")

    res = get_result_df(client)
    # just assume there are results. Need to think about this test and make sure it captures the result
    assert res.shape[0] > 1

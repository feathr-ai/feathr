import os
from datetime import datetime, timedelta
from pathlib import Path

from click.testing import CliRunner
from feathr import BOOLEAN, FLOAT, INT32, ValueType
from feathr.client import FeathrClient
from feathr import ValueType
from feathr.job_utils import get_result_df
from feathr import (BackfillTime, MaterializationSettings)
from feathr import FeatureQuery
from feathr import ObservationSettings
from feathr import RedisSink, HdfsSink
from feathr import TypedKey
from feathrcli.cli import init
import pytest

from test_fixture import (basic_test_setup, get_online_test_table_name)
# make sure you have run the upload feature script before running these tests
# the feature configs are from feathr_project/data/feathr_user_workspace
def test_feathr_materialize_to_offline():
    """
    Test FeathrClient() HdfsSink.
    """

    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    # os.chdir(test_workspace_dir)

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    backfill_time = BackfillTime(start=datetime(
        2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))

    now = datetime.now()
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob_materialize_offline_','_', str(now.minute), '_', str(now.second), ""])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/feathrazure_cijob_materialize_offline_','_', str(now.minute), '_', str(now.second), ""])
    offline_sink = HdfsSink(output_path=output_path)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[offline_sink],
                                       feature_names=[
                                           "f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    client.materialize_features(settings)
    # assuming the job can successfully run; otherwise it will throw exception
    client.wait_job_to_finish(timeout_sec=900)

    # download result and just assert the returned result is not empty
    # by default, it will write to a folder appended with date
    res_df = get_result_df(client, "avro", output_path + "/df0/daily/2020/05/20")
    assert res_df.shape[0] > 0

def test_feathr_online_store_agg_features():
    """
    Test FeathrClient() get_online_features and batch_get can get data correctly.
    """

    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    # os.chdir(test_workspace_dir)

    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    backfill_time = BackfillTime(start=datetime(
        2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[redisSink],
                                       feature_names=[
                                           "f_location_avg_fare", "f_location_max_fare"],
                                       backfill_time=backfill_time)
    client.materialize_features(settings)
    # just assume the job is successful without validating the actual result in Redis. Might need to consolidate
    # this part with the test_feathr_online_store test case
    client.wait_job_to_finish(timeout_sec=900)

    res = client.get_online_features(online_test_table, '265', [
                                     'f_location_avg_fare', 'f_location_max_fare'])
    # just assme there are values. We don't hard code the values for now for testing
    # the correctness of the feature generation should be garunteed by feathr runtime.
    # ID 239 and 265 are available in the `DOLocationID` column in this file:
    # https://s3.amazonaws.com/nyc-tlc/trip+data/green_tripdata_2020-04.csv
    # View more detials on this dataset: https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page
    assert len(res) == 2
    assert res[0] != None
    assert res[1] != None
    res = client.multi_get_online_features(online_test_table,
                                           ['239', '265'],
                                           ['f_location_avg_fare', 'f_location_max_fare'])
    assert res['239'][0] != None
    assert res['239'][1] != None
    assert res['265'][0] != None
    assert res['265'][1] != None

@pytest.mark.skip(reason="Add back when complex types are supported in python API")
def test_feathr_online_store_non_agg_features():
    """
    Test FeathrClient() online_get_features and batch_get can get data correctly.
    """
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))

    online_test_table = get_online_test_table_name('nycTaxiCITable')
    backfill_time = BackfillTime(start=datetime(
        2020, 5, 20), end=datetime(2020, 5, 20), step=timedelta(days=1))
    redisSink = RedisSink(table_name=online_test_table)
    settings = MaterializationSettings("nycTaxiTable",
                                       sinks=[redisSink],
                                       feature_names=["f_gen_trip_distance", "f_gen_is_long_trip_distance", "f1", "f2", "f3", "f4", "f5", "f6"],
                                       backfill_time=backfill_time)

    client.materialize_features(settings)
    # just assume the job is successful without validating the actual result in Redis. Might need to consolidate
    # this part with the test_feathr_online_store test case
    client.wait_job_to_finish(timeout_sec=900)

    res = client.get_online_features(online_test_table, '111', ['f_gen_trip_distance', 'f_gen_is_long_trip_distance',
                                                                   'f1', 'f2', 'f3', 'f4', 'f5', 'f6'])
    # just assme there are values. We don't hard code the values for now for testing
    # the correctness of the feature generation should be garunteed by feathr runtime.
    # ID 239 and 265 are available in the `DOLocationID` column in this file:
    # https://s3.amazonaws.com/nyc-tlc/trip+data/green_tripdata_2020-04.csv
    # View more detials on this dataset: https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page

    assert len(res) == 8
    assert res[0] != None
    assert res[1] != None
    # assert constant features
    _validate_constant_feature(res)
    res = client.multi_get_online_features(online_test_table,
                                           ['239', '265'],
                                           ['f_gen_trip_distance', 'f_gen_is_long_trip_distance', 'f1', 'f2', 'f3', 'f4', 'f5', 'f6'])
    _validate_constant_feature(res['239'])
    assert res['239'][0] != None
    assert res['239'][1] != None
    _validate_constant_feature(res['265'])
    assert res['265'][0] != None
    assert res['265'][1] != None


def _validate_constant_feature(feature):
    assert feature[2] == [10.0, 20.0, 30.0]
    assert feature[3] == ['a', 'b', 'c']
    assert feature[4] == ([1, 2, 3], ['10', '20', '30'])
    assert feature[5] == ([1, 2, 3], [True, False, True])
    assert feature[6] == ([1, 2, 3], [1.0, 2.0, 3.0])
    assert feature[7] == ([1, 2, 3], [1, 2, 3])


def test_dbfs_path():
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    if client.spark_runtime.casefold() == "databricks":
        # expect this raise an error since the result path is not in dbfs: format
        with pytest.raises(RuntimeError):
            client.feathr_spark_laucher.download_result(result_path="wasb://res_url", local_folder="/tmp")


def test_feathr_get_offline_features():
    """
    Test get_offline_features() can get data correctly.
    """
    runner = CliRunner()
    with runner.isolated_filesystem():
        runner.invoke(init, [])
        client = basic_test_setup(
            "./feathr_user_workspace/feathr_config.yaml")

        location_id = TypedKey(key_column="DOLocationID",
                               key_column_type=ValueType.INT32,
                               description="location id in NYC",
                               full_name="nyc_taxi.location_id")

        feature_query = FeatureQuery(
            feature_list=["f_location_avg_fare"], key=location_id)
        settings = ObservationSettings(
            observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
            event_timestamp_column="lpep_dropoff_datetime",
            timestamp_format="yyyy-MM-dd HH:mm:ss")

        now = datetime.now()
        # set output folder based on different runtime
        if client.spark_runtime == 'databricks':
            output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
        else:
            output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".avro"])


        client.get_offline_features(observation_settings=settings,
                                    feature_query=feature_query,
                                    output_path=output_path)

        # assuming the job can successfully run; otherwise it will throw exception
        client.wait_job_to_finish(timeout_sec=900)

        # download result and just assert the returned result is not empty
        res_df = get_result_df(client)
        assert res_df.shape[0] > 0

import os
from click.testing import CliRunner
from pathlib import Path
from feathr.client import FeathrClient
from feathr.dtype import ValueType
from feathr.query_feature_list import FeatureQuery
from feathr.settings import ObservationSettings
from feathr.typed_key import TypedKey
from feathrcli.cli import init


def test_feathr_online_store_databricks():
    """
    Test FeathrClient() get_online_features and batch_get can get data correctly.
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"
    os.chdir(test_workspace_dir)
    client = FeathrClient()

    client.materialize_features("feature_gen_conf/test_feature_gen_1.conf")
    # just assume the job is successful without validating the actual result in Redis. Might need to consolidate
    # this part with the test_feathr_online_store test case
    client.wait_job_to_finish(timeout_sec=900)
    res = client.get_online_features('nycTaxiDemoFeature', '265', ['f_location_avg_fare', 'f_location_max_fare'])
    # just assume there are values. We don't hard code the values for now for testing
    # the correctness of the feature generation should be garunteed by feathr runtime.
    # ID 239 and 265 are available in the `DOLocationID` column in this file:
    # https://s3.amazonaws.com/nyc-tlc/trip+data/green_tripdata_2020-04.csv
    # View more detials on this dataset: https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page
    assert len(res) == 2
    assert res[0] != None
    assert res[1] != None
    res = client.multi_get_online_features('nycTaxiDemoFeature',
                                           ['239', '265'],
                                           ['f_location_avg_fare', 'f_location_max_fare'])
    assert res['239'][0] != None
    assert res['239'][1] != None
    assert res['265'][0] != None
    assert res['265'][1] != None


def test_get_offline_features_databricks():
    """
    Test FeathrClient() to make sure offline features can be get successfully
    """
    runner = CliRunner()
    with runner.isolated_filesystem():
        runner.invoke(init, [])
        os.chdir('feathr_user_workspace')
        client = FeathrClient()

        location_id = TypedKey(key_column="DOLocationID",
                        key_column_type=ValueType.INT32,
                        description="location id in NYC",
                        full_name="nyc_taxi.location_id")
        feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=location_id)
        settings = ObservationSettings(
            observation_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
            event_timestamp_column="lpep_dropoff_datetime",
            timestamp_format="yyyy-MM-dd HH:mm:ss")
        client.get_offline_features(observation_settings=settings,
            feature_query=feature_query,
            output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro")

        res_url = client.wait_job_to_finish(timeout_sec=900)
        # just assume the job is successful. if not, the test will fail

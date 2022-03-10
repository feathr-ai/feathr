from feathrcli.cli import init
from click.testing import CliRunner
from feathr.client import FeathrClient
from feathr.job_utils import get_result_df
from feathr.query_feature_list import FeatureQuery
from feathr.settings import ObservationSettings
import os


# make sure you have run the upload feature script before running these tests
# the feature configs are from feathr_project/feathrcli/data/feathr_user_workspace
def test_feathr_online_store():
    """
    Test FeathrClient() get_online_features and batch_get can get data correctly.
    """
    runner = CliRunner()
    with runner.isolated_filesystem():
        runner.invoke(init, [])
        os.chdir('feathr_user_workspace')
        client = FeathrClient()
        job_res = client.materialize_features()
        # just assume the job is successful without validating the actual result in Redis. Might need to consolidate
        # this part with the test_feathr_online_store test case
        client.wait_job_to_finish(timeout_sec=600)
        res = client.get_online_features('nycTaxiDemoFeature', '265', ['f_location_avg_fare', 'f_location_max_fare'])
        # just assme there are values. We don't hard code the values for now for testing
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


def test_feathr_get_offline_features():
    """
    Test get_offline_features() can get data correctly.
    """
    runner = CliRunner()
    with runner.isolated_filesystem():
        runner.invoke(init, [])
        os.chdir('feathr_user_workspace')
        client = FeathrClient()

        feature_query = FeatureQuery(feature_list=["f_location_avg_fare"], key=["DOLocationID"])
        settings = ObservationSettings(
            observation_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
            output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro",
            event_timestamp_column="lpep_dropoff_datetime", timestamp_format="yyyy-MM-dd HH:mm:ss")
        client.get_offline_features(observation_settings=settings, feature_query=feature_query)
      
        vertical_concat_df = get_result_df(client)
        # just assume there are results. Need to think about this test and make sure it captures the result
        assert vertical_concat_df.shape[0] > 1

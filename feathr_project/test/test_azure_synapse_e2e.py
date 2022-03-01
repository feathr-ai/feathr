from feathrcli.cli import init
from click.testing import CliRunner
from feathr.client import FeathrClient
import os
import glob
import pandavro as pdx
import pandas as pd
import tempfile


# make sure you have run the upload feature script before running these tests
# the feature configs are from feathr_project/data/feathr_user_workspace
def test_feathr_online_store():
    """
    Test FeathrClient() online_get_features and batch_get can get data correctly.
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
        res = client.online_get_features('nycTaxiDemoFeature', '265', ['f_location_avg_fare', 'f_location_max_fare'])
        # just assme there are values. We don't hard code the values for now for testing
        # the correctness of the feature generation should be garunteed by feathr runtime.
        # ID 239 and 265 are available in the `DOLocationID` column in this file:
        # https://s3.amazonaws.com/nyc-tlc/trip+data/green_tripdata_2020-04.csv
        # View more detials on this dataset: https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page
        assert len(res) == 2
        assert res[0] != None
        assert res[1] != None
        res = client.online_batch_get_features('nycTaxiDemoFeature',
                                        ['239', '265'],
                                        ['f_location_avg_fare', 'f_location_max_fare'])
        assert res['239'][0] != None
        assert res['239'][1] != None
        assert res['265'][0] != None
        assert res['265'][1] != None


def test_feathr_get_historical_features():
    """
    Test FeathrClient() get_features and batch_get can get data correctly.
    """
    runner = CliRunner()
    with runner.isolated_filesystem():
        runner.invoke(init, [])
        os.chdir('feathr_user_workspace')
        client = FeathrClient()

        returned_spark_job = client.join_offline_features()
        res_url = client.get_job_result_uri(block=True, timeout_sec=600)
        tmp_dir = tempfile.TemporaryDirectory()
        client.feathr_spark_laucher.download_result(result_path=res_url, local_folder=tmp_dir.name)
        dataframe_list = []
        # assuming the result are in avro format
        for file in glob.glob(os.path.join(tmp_dir.name, '*.avro')):
            dataframe_list.append(pdx.read_avro(file))
        vertical_concat_df = pd.concat(dataframe_list, axis=0)
        tmp_dir.cleanup()
        # just assume there are results. Need to think about this test and make sure it captures the result
        assert vertical_concat_df.shape[0] > 1





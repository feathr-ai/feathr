import os
from datetime import datetime, timedelta
from pathlib import Path

from feathr import (BackfillTime, MaterializationSettings)
from feathr import RedisSink
from test_fixture import (basic_test_setup, get_online_test_table_name)
from test_utils.constants import Constants

def test_feathr_online_store_agg_features():
    """
    Test FeathrClient() get_online_features and batch_get can get data correctly.
    """

    online_test_table = get_online_test_table_name("nycTaxiCITable")
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    # os.chdir(test_workspace_dir)

    # The `feathr_runtime_location` was commented out in this config file, so feathr should use
    # Maven package as the dependency and `noop.jar` as the main file
    client = basic_test_setup(os.path.join(test_workspace_dir, "feathr_config_maven.yaml"))

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
    client.wait_job_to_finish(timeout_sec=Constants.SPARK_JOB_TIMEOUT_SECONDS)

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
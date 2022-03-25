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
from feathr import RedisSink
from feathr import TypedKey
from feathrcli.cli import init
import pytest

from test_fixture import basic_test_setup

# test parquet file read/write without an extension name
def test_feathr_get_offline_features_with_parquet():
    """
    Test if the program can read and write parquet files
    """
    runner = CliRunner()
    with runner.isolated_filesystem():
        runner.invoke(init, [])
        client = basic_test_setup(
            "./feathr_user_workspace/feathr_config.yaml")

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
            output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".parquet"])
        else:
            output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".parquet"])

        
        client.get_offline_features(observation_settings=settings,
                                    feature_query=feature_query,
                                    output_path=output_path,
                                    execution_configuratons={"spark.feathr.inputFormat": "parquet", "spark.feathr.outputFormat": "parquet"}
                                    )

        # assuming the job can successfully run; otherwise it will throw exception
        client.wait_job_to_finish(timeout_sec=900)
        
        # download result and just assert the returned result is not empty
        res_df = get_result_df(client)
        assert res_df.shape[0] > 0

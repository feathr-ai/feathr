# TODO with, without optional args
# TODO test with no data files exception and unsupported format exception
from pathlib import Path
from tempfile import NamedTemporaryFile
from unittest.mock import MagicMock

import pandas as pd
import pytest
from pytest_mock import MockerFixture
from pyspark.sql import DataFrame, SparkSession

from feathr import FeathrClient
from feathr.utils.job_utils import (
    get_result_df,
    get_result_pandas_df,
    get_result_spark_df,
)


def test__get_result_pandas_df(mocker: MockerFixture):
    # Assert if the base function, get_result_df, called w/ proper args
    mocked_get_result_df = mocker.patch("feathr.utils.job_utils.get_result_df")
    client = MagicMock()
    data_format = "some_data_format"
    res_url = "some_res_url"
    local_cache_path = "some_local_cache_path"
    get_result_pandas_df(client, data_format, res_url, local_cache_path)
    mocked_get_result_df.assert_called_once_with(client, data_format, res_url, local_cache_path)


def test__get_result_spark_df(mocker: MockerFixture):
    # Assert if the base function, get_result_df, called w/ proper args
    mocked_get_result_df = mocker.patch("feathr.utils.job_utils.get_result_df")
    client = MagicMock()
    spark = MagicMock()
    data_format = "some_data_format"
    res_url = "some_res_url"
    local_cache_path = "some_local_cache_path"
    get_result_spark_df(spark, client, data_format, res_url, local_cache_path)
    mocked_get_result_df.assert_called_once_with(client, data_format, res_url, local_cache_path, spark=spark)


# Local spark is expected to use a local filepath for res_url. Therefore, we mark this test to run with databricks.
@pytest.mark.databricks
def test__get_result_df__with_local_cache_path(feathr_client_databricks: FeathrClient):
    # TODO Assert there is a local copy of the file in the given local_cache_path
    pass


def test__get_result_df__exceptions():
    client = MagicMock()
    client.get_job_result_uri = MagicMock(return_value=None)

    # Test ValueError when res_url is None
    with pytest.raises(ValueError):
        get_result_df(client)


@pytest.mark.parametrize(
    "data_format,output_filename,expected_count", [
        ("csv", "output.csv", 5),
        ("csv", "output_dir.csv", 4),  # TODO add a header to the csv file and change expected_count to 5 after fixing the bug https://github.com/feathr-ai/feathr/issues/811
        ("parquet", "output.parquet", 5),
        ("avro", "output.avro", 5),
        ("delta", "output-delta", 5),
    ]
)
def test__get_result_df(
    workspace_dir: str,
    feathr_client_local: FeathrClient,
    data_format: str,
    output_filename: str,
    expected_count: int,
):
    # Note: make sure the output file exists in the test_user_workspace
    res_url = str(Path(workspace_dir, "mock_results", output_filename))
    df = get_result_df(
        client=feathr_client_local,
        data_format=data_format,
        res_url=res_url,
    )
    assert isinstance(df, pd.DataFrame)
    assert len(df) == expected_count


@pytest.mark.parametrize(
    "data_format,output_filename,expected_count", [
        ("csv", "output.csv", 5),
        ("csv", "output_dir.csv", 4),  # TODO add a header to the csv file and change expected_count = 5 after fixing the bug https://github.com/feathr-ai/feathr/issues/811
        ("parquet", "output.parquet", 5),
        ("avro", "output.avro", 5),
        ("delta", "output-delta", 5),
    ]
)
def test__get_result_df__with_spark_session(
    workspace_dir: str,
    feathr_client_local: FeathrClient,
    spark: SparkSession,
    data_format: str,
    output_filename: str,
    expected_count: int,
):
    # Note: make sure the output file exists in the test_user_workspace
    res_url = str(Path(workspace_dir, "mock_results", output_filename))
    df = get_result_df(
        client=feathr_client_local,
        data_format=data_format,
        res_url=res_url,
        spark=spark,
    )
    assert isinstance(df, DataFrame)
    assert df.count() == expected_count

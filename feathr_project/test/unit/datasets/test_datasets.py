from pathlib import Path
from unittest.mock import MagicMock

from pyspark.sql import SparkSession
import pytest
from pytest_mock import MockerFixture

from feathr.datasets import nyc_taxi


TEST_DATASET_DIR = Path(__file__).parent.parent.parent.joinpath("test_user_workspace")
NYC_TAXI_FILE_PATH = str(TEST_DATASET_DIR.joinpath("green_tripdata_2020-04_with_index.csv").resolve())


@pytest.fixture(scope="module")
def spark() -> SparkSession:
    """Generate a spark session for tests."""
    # Set ui port other than the default one (4040) so that feathr spark job may not fail.
    spark_session = (
        SparkSession
        .builder
        .appName("tests")
        .config("spark.ui.port", "8080")
        .getOrCreate()
    )
    yield spark_session
    spark_session.stop()


@pytest.mark.parametrize(
    "local_cache_path",
    [
        None,                                   # default temporary directory
        NYC_TAXI_FILE_PATH,                     # full filepath
        str(Path(NYC_TAXI_FILE_PATH).parent),   # directory
    ],
)
def test__nyc_taxi__get_pandas_df(
    mocker: MockerFixture,
    local_cache_path: str,
):
    """Test if nyc_taxi.get_pandas_df returns pd.DataFrame. Also check if the proper modules are being called.
    """
    # Mock maybe_download and TempDirectory
    mocked_maybe_download = mocker.patch("feathr.datasets.nyc_taxi.maybe_download")
    mocked_tmpdir = MagicMock()
    mocked_tmpdir.name = NYC_TAXI_FILE_PATH
    mocked_TemporaryDirectory = mocker.patch("feathr.datasets.nyc_taxi.TemporaryDirectory", return_value=mocked_tmpdir)

    pdf = nyc_taxi.get_pandas_df(local_cache_path=local_cache_path)
    assert len(pdf) == 35612

    # Assert mock called
    if local_cache_path:
        mocked_TemporaryDirectory.assert_not_called()
    else:
        mocked_TemporaryDirectory.assert_called_once()

    mocked_maybe_download.assert_called_once()


@pytest.mark.parametrize(
    "local_cache_path",
    [
        NYC_TAXI_FILE_PATH,                     # full filepath
        str(Path(NYC_TAXI_FILE_PATH).parent),   # directory
    ],
)
def test__nyc_taxi__get_spark_df(
    spark,
    mocker: MockerFixture,
    local_cache_path: str,
):
    """Test if nyc_taxi.get_spark_df returns spark.sql.DataFrame.
    """
    # Mock maybe_download
    mocked_maybe_download = mocker.patch("feathr.datasets.nyc_taxi.maybe_download")

    df = nyc_taxi.get_spark_df(spark=spark, local_cache_path=local_cache_path)
    assert df.count() == 35612

    # Assert mock called
    mocked_maybe_download.assert_called_once()

from pathlib import Path
from tempfile import TemporaryDirectory
from threading import local
from urllib.parse import urlparse

import pandas as pd
from pyspark.sql import DataFrame, SparkSession

from feathr.datasets.utils import maybe_download
from feathr.utils.platform import is_databricks


NYC_TAXI_SMALL_URL = "https://azurefeathrstorage.blob.core.windows.net/public/sample_data/green_tripdata_2020-04_with_index.csv"

def get_pandas_df(
    local_cache_path: str = None,
) -> pd.DataFrame:
    """Get NYC taxi fare prediction data samples as a pandas DataFrame.

    Refs:
        https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page

    Args:
        local_cache_path (optional): Local cache file path to download the data set.

    Returns:
        pandas DataFrame
    """
    # Use tmpdir if not provided
    tmpdir = None
    if local_cache_path is None:
        tmpdir = TemporaryDirectory()
        local_cache_path = tmpdir.name

    # If local_cache_path is a directory, add the source file name.
    src_filepath = Path(urlparse(NYC_TAXI_SMALL_URL).path)
    dst_filepath = Path(local_cache_path)
    if dst_filepath.suffix != src_filepath.suffix:
        local_cache_path = str(dst_filepath.joinpath(src_filepath.name))

    maybe_download(src_url=NYC_TAXI_SMALL_URL, dst_filepath=local_cache_path)

    pdf = pd.read_csv(local_cache_path)

    # Clean up if we used tmpdir
    if tmpdir:
        tmpdir.cleanup()

    return pdf


def get_spark_df(
    spark: SparkSession,
    local_cache_path: str,
) -> DataFrame:
    """Get NYC taxi fare prediction data samples as a spark DataFrame.

    Refs:
        https://www1.nyc.gov/site/tlc/about/tlc-trip-record-data.page

    Args:
        spark: Spark session.
        local_cache_path: Local cache file path to download the data set.

    Returns:
        Spark DataFrame
    """
    # If local_cache_path is a directory, add the source file name.
    src_filepath = Path(urlparse(NYC_TAXI_SMALL_URL).path)
    dst_filepath = Path(local_cache_path)
    if dst_filepath.suffix != src_filepath.suffix:
        local_cache_path = str(dst_filepath.joinpath(src_filepath.name))

    if is_databricks():
        # Databricks uses "dbfs:/" prefix for spark paths
        if not local_cache_path.startswith("dbfs:/"):
            local_cache_path = str(Path("dbfs:/", local_cache_path))
        # Databricks uses "/dbfs/" prefix for python paths
        python_local_cache_path = local_cache_path.replace("dbfs:/", "/dbfs/")
    # TODO add "if is_synapse()"
    else:
        python_local_cache_path = local_cache_path

    maybe_download(src_url=NYC_TAXI_SMALL_URL, dst_filepath=python_local_cache_path)

    df = spark.read.option("header", True).csv(local_cache_path)

    return df

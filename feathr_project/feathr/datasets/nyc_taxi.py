from tempfile import TemporaryDirectory
from urllib.parse import urlparse

import pandas as pd
from pyspark.sql import DataFrame, SparkSession

from .utils import maybe_download


NYC_TAXI_SMALL_URL = "https://azurefeathrstorage.blob.core.windows.net/public/sample_data/green_tripdata_2020-04_with_index.csv"

def get_pandas_df(
    local_cache_path: str = None,
) -> pd.DataFrame:
    """_summary_

    Args:
        local_cache_path (str, optional): _description_. Defaults to None.

    Returns:
        pd.DataFrame: _description_
    """
    # Use tmpdir if not provided
    tmpdir = None
    if local_cache_path is None:
        tmpdir = TemporaryDirectory()
        local_cache_path = tmpdir.name

    maybe_download(src_url=NYC_TAXI_SMALL_URL, dst_path=local_cache_path)

    pdf = pd.read_csv(local_cache_path)

    # Clean up if we used tmpdir
    if tmpdir:
        tmpdir.cleanup()

    return pdf


def get_spark_df(
    spark: SparkSession,
    local_cache_path: str = None,
) -> DataFrame:
    """_summary_

    Args:
        spark (_type_): _description_
        local_cache_path (str, optional): _description_. Defaults to None.

    Returns:
        DataFrame: _description_
    """
    # Use tmpdir if not provided
    tmpdir = None
    if local_cache_path is None:
        tmpdir = TemporaryDirectory()
        local_cache_path = tmpdir.name

    maybe_download(src_url=NYC_TAXI_SMALL_URL, dst_path=local_cache_path)

    df = spark.read.option("header", True).csv(local_cache_path)

    # Clean up if we used tmpdir
    if tmpdir:
        tmpdir.cleanup()

    return df

from multiprocessing.sharedctypes import Value
from pathlib import Path
from tempfile import TemporaryDirectory
from typing import Union

from loguru import logger
import pandas as pd
from pyspark.sql import DataFrame, SparkSession

from feathr.client import FeathrClient
from feathr.constants import OUTPUT_FORMAT


def get_result_pandas_df(
    client: FeathrClient,
    data_format: str = None,
    res_url: str = None,
    local_cache_path: str = None,
) -> pd.DataFrame:
    """Download the job result dataset from cloud as a Pandas DataFrame.

    Args:
        client: Feathr client
        data_format: Format to read the downloaded files. Currently support `parquet`, `delta`, `avro`, and `csv`.
            Default to `avro` if not specified.
        res_url: Result URL to download files from. Note that this will not block the job so you need to make sure
            the job is finished and the result URL contains actual data.
        local_cache_path (optional): Specify the absolute download path. if the user does not provide this,
            the function will create a temporary directory.

    Returns:
        pandas DataFrame
    """
    return get_result_df(client, data_format, res_url, local_cache_path)


def get_result_spark_df(
    spark: SparkSession,
    client: FeathrClient,
    data_format: str = None,
    res_url: str = None,
    local_cache_path: str = None,
) -> DataFrame:
    """Download the job result dataset from cloud as a Spark DataFrame.

    Args:
        spark: Spark session
        client: Feathr client
        data_format: Format to read the downloaded files. Currently support `parquet`, `delta`, `avro`, and `csv`.
            Default to `avro` if not specified.
        res_url: Result URL to download files from. Note that this will not block the job so you need to make sure
            the job is finished and the result URL contains actual data.
        local_cache_path (optional): Specify the absolute download path. if the user does not provide this,
            the function will create a temporary directory.

    Returns:
        Spark DataFrame
    """
    return get_result_df(client, data_format, res_url, local_cache_path, spark=spark)


def get_result_df(
    client: FeathrClient,
    data_format: str = None,
    res_url: str = None,
    local_cache_path: str = None,
    spark: SparkSession = None,
) -> Union[DataFrame, pd.DataFrame]:
    """Download the job result dataset from cloud as a Spark DataFrame or pandas DataFrame.

    Args:
        client: Feathr client
        data_format: Format to read the downloaded files. Currently support `parquet`, `delta`, `avro`, and `csv`.
            Default to `avro` if not specified.
        res_url: Result URL to download files from. Note that this will not block the job so you need to make sure
            the job is finished and the result URL contains actual data.
        local_cache_path (optional): Specify the absolute download path. if the user does not provide this,
            the function will create a temporary directory.
        spark (optional): Spark session. If provided, the function returns spark Dataframe.
            Otherwise, it returns pd.DataFrame.

    Returns:
        Either Spark or pandas DataFrame.
    """
    # use a result url if it's provided by the user, otherwise use the one provided by the job
    res_url: str = res_url or client.get_job_result_uri(block=True, timeout_sec=1200)
    if res_url is None:
        raise ValueError(
            "`res_url` is None. Please make sure either you provide a res_url or make sure the job finished in FeathrClient has a valid result URI."
        )

    if client.spark_runtime == "local":
        if local_cache_path is not None:
            logger.warning(
                "In local spark mode, the result files are expected to be stored at a local storage and thus `local_cache_path` argument will be ignored."
            )
        local_cache_path = res_url
    elif client.spark_runtime == "databricks":
        if res_url.startswith("dbfs:"):
            if local_cache_path is not None:
                logger.warning(
                    "Result files are already in DBFS and thus `local_cache_path` will be ignored."
                )
            local_cache_path = res_url
        else:
            # if local_cache_path params is not provided then create a temporary folder
            if local_cache_path is None:
                # We'll just use the name of a local TemporaryDirectory to cache the data into DBFS.
                local_cache_path = TemporaryDirectory().name

            # Databricks uses "dbfs:/" prefix for spark paths
            if not local_cache_path.startswith("dbfs:"):
                local_cache_path = str(Path("dbfs:", local_cache_path.lstrip("/")))
    else:
        logger.warning("This utility function currently supports local spark and databricks. You may encounter unexpected results on other platforms.")
    # TODO elif azure_synapse

    if local_cache_path != res_url:
        logger.info(f"{res_url} files will be downloaded into {local_cache_path}")
        client.feathr_spark_launcher.download_result(result_path=res_url, local_folder=local_cache_path)

    # use user provided format, if there isn't one, then otherwise use the one provided by the job;
    # if none of them is available, "avro" is the default format.
    data_format: str = data_format or client.get_job_tags().get(OUTPUT_FORMAT, "")
    if data_format is None or data_format == "":
        data_format = "avro"

    result_df = None

    if spark is not None:
        if data_format == "csv":
            result_df = spark.read.option("header", True).csv(local_cache_path)
        else:
            result_df = spark.read.format(data_format).load(local_cache_path)
    else:
        result_df = _load_files_to_pandas_df(
            dir_path=local_cache_path.replace("dbfs:", "/dbfs"),  # replace to python path if spark path is provided.
            data_format=data_format,
        )

    return result_df


def _load_files_to_pandas_df(dir_path: str, data_format: str = "avro") -> pd.DataFrame:

    if data_format == "parquet":
        return pd.read_parquet(dir_path)

    elif data_format == "delta":
        from deltalake import DeltaTable

        delta = DeltaTable(dir_path)
        # if client.spark_runtime != "azure_synapse":
        # don't detect for synapse result with Delta as there's a problem with underlying system
        # Issues are tracked here: https://github.com/delta-io/delta-rs/issues/582
        return delta.to_pyarrow_table().to_pandas()
        # else:
        # TODO -- Proper warning messages. Is this applied to all the other formats?
        # raise RuntimeError(
        #     "Please use Azure Synapse to read the result in the Azure Synapse cluster. Reading local results is not supported for Azure Synapse."
        # )

    elif data_format == "avro":
        import pandavro as pdx
        if Path(dir_path).is_file():
            return pdx.read_avro(dir_path)
        else:
            try:
                return pd.concat([pdx.read_avro(f) for f in Path(dir_path).glob("*.avro")]).reset_index(drop=True)
            except ValueError:  # No object to concat when the dir is empty
                return pd.DataFrame()

    elif data_format == "csv":
        if Path(dir_path).is_file():
            return pd.read_csv(dir_path)
        else:
            try:
                return pd.concat([pd.read_csv(f) for f in Path(dir_path).glob("*.csv")]).reset_index(drop=True)
            except ValueError:  # No object to concat when the dir is empty
                return pd.DataFrame()

    else:
        raise ValueError(
            f"{data_format} is currently not supported in get_result_df. Currently only parquet, delta, avro, and csv are supported, please consider writing a customized function to read the result."
        )

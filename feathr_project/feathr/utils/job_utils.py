import glob
import os
import tempfile
from typing import Union
from warnings import warn

import pandas as pd
from pandas.errors import EmptyDataError
from pyspark.sql import DataFrame, SparkSession

from feathr.client import FeathrClient
from feathr.constants import OUTPUT_FORMAT


def get_result_pandas_df(
    client: FeathrClient,
    data_format: str = None,
    res_url: str = None,
    local_folder: str = None,
) -> pd.DataFrame:
    """Download the job result dataset from cloud as a Pandas DataFrame.

    Args:
        client: Feathr client
        data_format: Format to read the downloaded files. Currently support `parquet`, `delta`, `avro`, and `csv`. Default to `avro` if not specified.
        res_url: Output URL to download files. Note that this will not block the job so you need to make sure the job is finished and result URL contains actual data.
        local_folder (Optional): Specify the absolute download path. if the user does not provide this, function will create a temporary directory and delete it after reading the dataframe.

    Returns:
        pandas DataFrame
    """
    return get_result_df(client, data_format, res_url, local_folder)


def get_result_spark_df(
    spark: SparkSession,
    client: FeathrClient,
    data_format: str = None,
    res_url: str = None,
    local_folder: str = None,
) -> DataFrame:
    """Download the job result dataset from cloud as a Spark DataFrame.

    Args:
        spark: Spark session
        client: Feathr client
        data_format: Format to read the downloaded files. Currently support `parquet`, `delta`, `avro`, and `csv`. Default to `avro` if not specified.
        res_url: Output URL to download files. Note that this will not block the job so you need to make sure the job is finished and result URL contains actual data.
        local_folder (Optional): Specify the absolute download path. if the user does not provide this, function will create a temporary directory and delete it after reading the dataframe.

    Returns:
        Spark DataFrame
    """
    return get_result_df(client, data_format, res_url, local_folder, spark=spark)


def get_result_df(
    client: FeathrClient,
    data_format: str = None,
    res_url: str = None,
    local_folder: str = None,
    spark: SparkSession = None,
) -> Union[DataFrame, pd.DataFrame]:
    """Download the job result dataset from cloud as a Spark DataFrame or pandas DataFrame.

    Args:
        client: Feathr client
        data_format: Format to read the downloaded files. Currently support `parquet`, `delta`, `avro`, and `csv`. Default to `avro` if not specified.
        res_url: Output URL to download files. Note that this will not block the job so you need to make sure the job is finished and result URL contains actual data.
        local_folder (Optional): Specify the absolute download path. if the user does not provide this, function will create a temporary directory and delete it after reading the dataframe.
        spark (Optional): Spark session. If provided, the function returns spark Dataframe. Otherwise, it returns pd.DataFrame.

    Returns:
        Either Spark or pandas DataFrame.
    """
    # use a result url if it's provided by the user, otherwise use the one provided by the job
    res_url: str = res_url or client.get_job_result_uri(block=True, timeout_sec=1200)
    if res_url is None:
        raise RuntimeError(
            "res_url is None. Please make sure either you provide a res_url or make sure the job finished in FeathrClient has a valid result URI."
        )

    tmp_dir = None

    if client.spark_runtime == "local":
        local_dir_path = res_url
        if local_folder is not None:
            warn(
                "In local spark mode, the result files are expected to be stored at a local storage and thus `local_folder` argument will be ignored."
            )
    else:
        # if local_folder params is not provided then create a temporary folder
        if local_folder is not None:
            local_dir_path = local_folder
        else:
            tmp_dir = tempfile.TemporaryDirectory()
            local_dir_path = tmp_dir.name
        client.feathr_spark_launcher.download_result(
            result_path=res_url, local_folder=local_dir_path
        )

    # use user provided format, if there isn't one, then otherwise use the one provided by the job;
    # if none of them is available, "avro" is the default format.
    data_format: str = data_format or client.get_job_tags().get(OUTPUT_FORMAT, "")
    if data_format is None or data_format == "":
        data_format = "avro"

    result_df = None

    if spark is not None:
        result_df = spark.read.format(data_format).load(local_dir_path)
    else:
        result_df = _read_files_to_pandas_df(dir_path=local_dir_path, data_format=data_format)

    if tmp_dir is not None:
        tmp_dir.cleanup()

    return result_df


def _read_files_to_pandas_df(dir_path: str, data_format: str = "avro") -> pd.DataFrame:

    if data_format == "parquet":
        from pyarrow.parquet import ParquetDataset

        files = glob.glob(os.path.join(dir_path, "*.parquet"))
        ds = ParquetDataset(files)
        return ds.read().to_pandas()

    elif data_format == "delta":
        from deltalake import DeltaTable

        delta = DeltaTable(dir_path)
        # if client.spark_runtime != "azure_synapse":
        # don't detect for synapse result with Delta as there's a problem with underlying system
        # Issues are tracked here: https://github.com/delta-io/delta-rs/issues/582
        return delta.to_pyarrow_table().to_pandas()
        # else:
        # TODO -- Proper warning messages. Is this applied for all the other formats?
        # raise RuntimeError(
        #     "Please use Azure Synapse to read the result in the Azure Synapse cluster. Reading local results is not supported for Azure Synapse."
        # )

    elif data_format == "avro":
        import pandavro as pdx

        dataframe_list = [
            pdx.read_avro(file) for file in glob.glob(os.path.join(dir_path, "*.avro"))
        ]
        return pd.concat(dataframe_list, axis=0)

    elif data_format == "csv":
        dataframe_list = []
        for file in glob.glob(os.path.join(dir_path, "*.csv")):
            try:
                dataframe_list.append(pd.read_csv(file, index_col=None, header=None))
            except EmptyDataError:
                # in case there are empty files
                pass

        if dataframe_list:
            # Reset index to avoid duplicated indices -- TODO don't we need reset_index when reading avro too?
            return pd.concat(dataframe_list, axis=0).reset_index(drop=True)
        else:
            raise ValueError(f"Empty files in {dir_path}.")

    else:
        raise ValueError(
            f"{data_format} is currently not supported in get_result_df. Currently only parquet, delta, avro, and csv are supported, please consider writing a customized function to read the result."
        )

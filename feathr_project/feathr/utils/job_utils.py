from feathr.client import FeathrClient
import os
import glob
from feathr.constants import OUTPUT_FORMAT
from loguru import logger
import pandas as pd
import tempfile


def get_result_df(client: FeathrClient, format: str = None, res_url: str = None, local_folder: str = None) -> pd.DataFrame:
    """Download the job result dataset from cloud as a Pandas dataframe.

    format: format override, could be "parquet", "delta", etc.
    res_url: output URL to download files. Note that this will not block the job so you need to make sure the job is finished and result URL contains actual data.
    local_folder: optional parameter to specify the absolute download path. if the user does not provide this, function will create a temporary directory and delete it after reading the dataframe.
    """
    res_url: str = res_url or client.get_job_result_uri(block=True, timeout_sec=1200)
    format: str = format or client.get_job_tags().get(OUTPUT_FORMAT, "")
    # if local_folder params is not provided then create a temporary folder
    if local_folder is not None:
        local_dir_path = local_folder
    else:
        tmp_dir = tempfile.TemporaryDirectory()
        local_dir_path = tmp_dir.name
        
    client.feathr_spark_launcher.download_result(result_path=res_url, local_folder=local_dir_path)
    dataframe_list = []
    # by default the result are in avro format
    if format:
        # helper function for only parquet and avro
        if format.casefold()=="parquet":
            files =  glob.glob(os.path.join(local_dir_path, '*.parquet'))
            from pyarrow.parquet import ParquetDataset
            ds = ParquetDataset(files)
            result_df = ds.read().to_pandas()
        elif format.casefold()=="delta":
            from deltalake import DeltaTable
            delta = DeltaTable(local_dir_path)
            if not client.spark_runtime == 'azure_synapse':
                # don't detect for synapse result with Delta as there's a problem with underlying system
                # Issues are trached here: https://github.com/delta-io/delta-rs/issues/582
                result_df = delta.to_pyarrow_table().to_pandas()
            else:
                logger.info("Please use Azure Synapse to read the result in the Azure Synapse cluster. Reading local results is not supported for Azure Synapse. Emtpy DataFrame is returned.")
                result_df = pd.DataFrame()
        elif format.casefold()=="avro":
            import pandavro as pdx
            for file in glob.glob(os.path.join(local_dir_path, '*.avro')):
                dataframe_list.append(pdx.read_avro(file))
            result_df = pd.concat(dataframe_list, axis=0)
    else:
        # by default use avro
        import pandavro as pdx
        for file in glob.glob(os.path.join(local_dir_path, '*.avro')):
            dataframe_list.append(pdx.read_avro(file))
        result_df = pd.concat(dataframe_list, axis=0)
    if local_folder is None:
        tmp_dir.cleanup()
    return result_df
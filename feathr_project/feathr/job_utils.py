from feathr.client import FeathrClient
import os
import glob
from feathr.constants import OUTPUT_FORMAT

import pandas as pd
import tempfile


def get_result_df(client: FeathrClient) -> pd.DataFrame:
    """Download the job result dataset from cloud as a Pandas dataframe."""
    res_url = client.get_job_result_uri(block=True, timeout_sec=600)
    format: str = client.get_job_tags().get(OUTPUT_FORMAT, "")
    tmp_dir = tempfile.TemporaryDirectory()
    client.feathr_spark_laucher.download_result(result_path=res_url, local_folder=tmp_dir.name)
    dataframe_list = []
    # by default the result are in avro format
    if format:
        # helper function for only parquet and avro
        if format.lower()=="parquet":
            files =  glob.glob(os.path.join(tmp_dir.name, '*.parquet'))
            from pyarrow.parquet import ParquetDataset
            ds = ParquetDataset(files)
            result_df = ds.read().to_pandas()
        if format.lower()=="delta":
            from deltalake import DeltaTable
            delta = DeltaTable(tmp_dir.name)
            result_df = delta.to_pyarrow_table().to_pandas()
    else:
        import pandavro as pdx
        for file in glob.glob(os.path.join(tmp_dir.name, '*.avro')):
            dataframe_list.append(pdx.read_avro(file))
        result_df = pd.concat(dataframe_list, axis=0)
    tmp_dir.cleanup()
    return result_df
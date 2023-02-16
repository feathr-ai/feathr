# Code samples for time partition pattern
from feathr import AvroJsonSchema
from feathr import KafKaSource
from feathr import KafkaConfig
from typing import List
import os
import random
from datetime import datetime, timedelta

from feathr import (BOOLEAN, FLOAT, INPUT_CONTEXT, INT32, STRING,
                    FeatureQuery, ObservationSettings, Feature, FeatureAnchor, HdfsSource,
                    TypedKey, ValueType, WindowAggTransformation, BackfillTime,
                    MaterializationSettings,HdfsSink, Constants)
from feathr import FeathrClient
from feathr.utils.job_utils import get_result_df

# replace by your config path
config_path = "feathr_config.yaml" 
# replace by your own data source path
data_source_path = 'dbfs:/timePartitionPattern_postfix_test/df0/daily/'
# replace by your own postfix path
postfix_path = "postfixPath" 

# Example for materialize job with 'time_partition_pattern'
client = FeathrClient(config_path=config_path)

batch_source = HdfsSource(name="testTimePartitionSource",
                          path=data_source_path,
                          time_partition_pattern="yyyy/MM/dd",
                          postfix_path=postfix_path
                        )
key = TypedKey(key_column="key0",
               key_column_type=ValueType.INT32)
agg_features = [
    Feature(name="f_loc_avg_output",
            key=[key],
            feature_type=FLOAT,
            transform=WindowAggTransformation(agg_expr="f_location_avg_fare",
                                              agg_func="AVG",
                                              window="3d")),
    Feature(name="f_loc_max_output",
            feature_type=FLOAT,
            key=[key],
            transform=WindowAggTransformation(agg_expr="f_location_max_fare",
                                              agg_func="MAX",
                                              window="3d")),
    ]

agg_anchor = FeatureAnchor(name="testTimePartitionFeatures",
                           source=batch_source,
                           features=agg_features)
client.build_features(anchor_list=[agg_anchor])

backfill_time_pf = BackfillTime(start=datetime(
        2020, 5, 2), end=datetime(2020, 5, 2), step=timedelta(days=1))
now = datetime.now()
# replace by your own output path
output_path_pf = ''.join(['dbfs:/feathrazure_cijob_materialize_offline_','_', str(now.minute), '_', str(now.second), ""])
offline_sink_pf = HdfsSink(output_path=output_path_pf)
settings_pf = MaterializationSettings("nycTaxiTable",
                                       sinks=[offline_sink_pf],
                                       feature_names=[
                                           "f_loc_avg_output", "f_loc_max_output"],
                                       backfill_time=backfill_time_pf)
client.materialize_features(settings_pf)
client.wait_job_to_finish(timeout_sec=Constants.SPARK_JOB_TIMEOUT_SECONDS)

res_df = get_result_df(client, data_format="avro", res_url=output_path_pf + "/df0/daily/2020/05/02")
assert res_df.shape[0] > 0
    
# Example for get offline job with 'time_partition_pattern'    
client = FeathrClient(config_path=config_path)

batch_source = HdfsSource(name="testTimePartitionSource",
                          path=data_source_path,
                          time_partition_pattern="yyyy/MM/dd",
                          postfix_path=postfix_path
                        )
tpp_key = TypedKey(key_column="f_location_max_fare",
                    key_column_type=ValueType.FLOAT)
tpp_features = [
    Feature(name="key0",
            key=tpp_key,
            feature_type=FLOAT,
            transform=WindowAggTransformation(agg_expr="key0",
                                              agg_func="LATEST",
                                              window="3d"
                    ))
    ]
tpp_anchor = FeatureAnchor(name="tppFeatures",
                            source=batch_source,
                            features=tpp_features)
client.build_features(anchor_list=[tpp_anchor])
    
feature_query = FeatureQuery(feature_list=["key0"], key=tpp_key)
settings = ObservationSettings(
        observation_path='wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/tpp_source.csv',
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")
# replace by your own output path
output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
client.get_offline_features(observation_settings=settings,
                            feature_query=feature_query,
                            output_path=output_path)
client.wait_job_to_finish(timeout_sec=Constants.SPARK_JOB_TIMEOUT_SECONDS)

res_df = get_result_df(client, data_format="avro", res_url = output_path)
assert res_df.shape[0] > 0
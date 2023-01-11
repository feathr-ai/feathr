# initialize Feathr execution environment
# Mostly it should initialize the SQLite database schema, as well as run a basic Feathr job so the container can cache the required maven pacakges

# Also consider adding some sample code

import glob
import os
import tempfile
from datetime import datetime, timedelta
from math import sqrt

import pandas as pd
import pandavro as pdx
from feathr import FeathrClient
from feathr import BOOLEAN, FLOAT, INT32, ValueType
from feathr import Feature, DerivedFeature, FeatureAnchor
from feathr import BackfillTime, MaterializationSettings
from feathr import FeatureQuery, ObservationSettings
from feathr import RedisSink
from feathr import INPUT_CONTEXT, HdfsSource
from feathr import WindowAggTransformation
from feathr import TypedKey
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import train_test_split
from azure.identity import AzureCliCredential
from azure.keyvault.secrets import SecretClient
from pyspark.ml import Pipeline
from pyspark.ml.evaluation import RegressionEvaluator
from pyspark.ml.feature import VectorAssembler
from pyspark.ml.regression import GBTRegressor
from pyspark.sql import DataFrame, SparkSession
import pyspark.sql.functions as F


import feathr
print(feathr.__version__)


os.environ['SPARK_LOCAL_IP'] = "127.0.0.1"

import tempfile
yaml_config = f"""
api_version: 1
project_config:
  project_name: 'local_spark'
  required_environment_variables:
  optional_environment_variables:
    # the environemnt variables are optional, however you will need them if you want to use some of the services:
    - ADLS_ACCOUNT
    - ADLS_KEY
    - WASB_ACCOUNT
    - WASB_KEY
    - S3_ACCESS_KEY
    - S3_SECRET_KEY
    - JDBC_TABLE
    - JDBC_USER
    - JDBC_PASSWORD
    - KAFKA_SASL_JAAS_CONFIG


spark_config:
  # choice for spark runtime. Currently support: azure_synapse, databricks, local
  spark_cluster: 'local'
  spark_result_output_parts: '1'
  local:
    master: 'local[*]'
    feathr_runtime_location:

online_store:
  redis:
    # Redis configs to access Redis cluster
    host: '<redis_host_name>'
    port: 6380
    ssl_enabled: True

feature_registry:
  # The API endpoint of the registry service
  api_endpoint: "https://feathr-sql-registry.azurewebsites.net/api/v1"
"""

tmp = tempfile.NamedTemporaryFile(mode='w', delete=False)
with open(tmp.name, "w") as text_file:
    text_file.write(yaml_config)


client = FeathrClient(tmp.name)

DATA_FILE_PATH = "/tmp/green_tripdata_2020-04_with_index.csv"

import pandas as pd
df_raw = pd.read_csv("https://azurefeathrstorage.blob.core.windows.net/public/sample_data/green_tripdata_2020-04_with_index.csv")
df_raw.to_csv(DATA_FILE_PATH, index=False)


TIMESTAMP_COL = "lpep_dropoff_datetime"
TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss"


def preprocessing(df: DataFrame) -> DataFrame:
    import pyspark.sql.functions as F
    df = df.withColumn("fare_amount_cents", (F.col("fare_amount") * 100.0).cast("float"))
    return df

batch_source = HdfsSource(
    name="nycTaxiBatchSource",
    path=DATA_FILE_PATH,
    event_timestamp_column=TIMESTAMP_COL,
    preprocessing=preprocessing,
    timestamp_format=TIMESTAMP_FORMAT,
)


# We define f_trip_distance and f_trip_time_duration features separately
# so that we can reuse them later for the derived features.
f_trip_distance = Feature(
    name="f_trip_distance",
    feature_type=FLOAT,
    transform="trip_distance",
)
f_trip_time_duration = Feature(
    name="f_trip_time_duration",
    feature_type=FLOAT,
    transform="cast_float((to_unix_timestamp(lpep_dropoff_datetime) - to_unix_timestamp(lpep_pickup_datetime)) / 60)",
)



features = [
    f_trip_distance,
    f_trip_time_duration,
    Feature(
        name="f_is_long_trip_distance",
        feature_type=BOOLEAN,
        transform="trip_distance > 30.0",
    ),
    Feature(
        name="f_day_of_week",
        feature_type=INT32,
        transform="dayofweek(lpep_dropoff_datetime)",
    ),
    Feature(
        name="f_day_of_month",
        feature_type=INT32,
        transform="dayofmonth(lpep_dropoff_datetime)",
    ),
    Feature(
        name="f_hour_of_day",
        feature_type=INT32,
        transform="hour(lpep_dropoff_datetime)",
    ),
]

# After you have defined features, bring them together to build the anchor to the source.
feature_anchor = FeatureAnchor(
    name="feature_anchor",
    source=INPUT_CONTEXT,  # Pass through source, i.e. observation data.
    features=features,
)

agg_key = TypedKey(
    key_column="DOLocationID",
    key_column_type=ValueType.INT32,
    description="location id in NYC",
    full_name="nyc_taxi.location_id",
)

agg_window = "90d"

# Anchored features with aggregations
agg_features = [
    Feature(
        name="f_location_avg_fare",
        key=agg_key,
        feature_type=FLOAT,
        transform=WindowAggTransformation(
            agg_expr="fare_amount_cents",
            agg_func="AVG",
            window=agg_window,
        ),
    ),
    Feature(
        name="f_location_max_fare",
        key=agg_key,
        feature_type=FLOAT,
        transform=WindowAggTransformation(
            agg_expr="fare_amount_cents",
            agg_func="MAX",
            window=agg_window,
        ),
    ),
]

agg_feature_anchor = FeatureAnchor(
    name="agg_feature_anchor",
    source=batch_source,  # External data source for feature. Typically a data table.
    features=agg_features,
)


f_trip_time_distance = DerivedFeature(name="f_trip_time_distance",
                                          feature_type=FLOAT,
                                          input_features=[
                                              f_trip_distance, f_trip_time_duration],
                                          transform="f_trip_distance * f_trip_time_duration")

f_trip_time_rounded = DerivedFeature(name="f_trip_time_rounded",
                                         feature_type=INT32,
                                         input_features=[f_trip_time_duration],
                                         transform="f_trip_time_duration % 10")

derived_feature = [f_trip_time_distance, f_trip_time_rounded]


client.build_features(
    anchor_list=[feature_anchor, agg_feature_anchor],
    derived_feature_list=derived_feature,
)


feature_names = [feature.name for feature in features + agg_features]
feature_names


now = datetime.now().strftime("%Y%m%d%H%M%S")
output_path = os.path.join("debug", f"test_output_{now}")

offline_features_path = output_path


# Features that we want to request. Can use a subset of features
query = FeatureQuery(
    feature_list=feature_names,
    key=agg_key,
)
settings = ObservationSettings(
    observation_path=DATA_FILE_PATH,
    event_timestamp_column=TIMESTAMP_COL,
    timestamp_format=TIMESTAMP_FORMAT,
)
# client.get_offline_features(
#     observation_settings=settings,
#     feature_query=query,
#     output_path=offline_features_path,
# )

# client.wait_job_to_finish(timeout_sec=5000)


materialized_feature_names = [feature.name for feature in agg_features]
materialized_feature_names

FEATURE_TABLE_NAME = "nycTaxiDemoFeature"

backfill_time = BackfillTime(start=datetime(
    2020, 4, 1), end=datetime(2020, 4, 1), step=timedelta(days=1))
redisSink = RedisSink(table_name=FEATURE_TABLE_NAME)
settings = MaterializationSettings(FEATURE_TABLE_NAME + ".job",
                                    sinks=[redisSink],
                                    feature_names=[
                                        "f_location_avg_fare", "f_location_max_fare"],
                                    backfill_time=backfill_time)
client.materialize_features(settings)

client.wait_job_to_finish(5000)
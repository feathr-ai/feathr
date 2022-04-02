import os
from feathr.anchor import FeatureAnchor
from feathr.client import FeathrClient
from feathr.dtype import BOOLEAN, FLOAT, INT32, ValueType
from feathr.feature import Feature
from feathr.query_feature_list import FeatureQuery
from feathr.settings import ObservationSettings
from feathr.source import INPUT_CONTEXT, HdfsSource
from feathr.transformation import WindowAggTransformation
from feathr.typed_key import TypedKey
from pyspark.sql import SparkSession, DataFrame


os.environ['REDIS_PASSWORD'] = 'Li7Nn63iNB0x731VTnnz2Vr29WYJHx7JlAzCaH9lbHw='
os.environ['AZURE_CLIENT_ID'] = "b40e49c0-75c7-4959-ad25-896118cd79e8"
os.environ['AZURE_TENANT_ID'] = '72f988bf-86f1-41af-91ab-2d7cd011db47'
os.environ['AZURE_CLIENT_SECRET'] = 'kAB5ps6yvo_f08n-4Av~.IDwHFL_xl_63I'
os.environ['AZURE_PURVIEW_NAME'] = 'feathrazuretest3-purview1'

# This is like a managed notebook

yaml_config = """
# DO NOT MOVE OR DELETE THIS FILE

# This file contains the configurations that are used by Feathr
# All the configurations can be overwritten by environment variables with concatenation of `__` for different layers of this config file.
# For example, `feathr_runtime_location` for databricks can be overwritten by setting this environment variable:
# SPARK_CONFIG__DATABRICKS__FEATHR_RUNTIME_LOCATION
# Another example would be overwriting Redis host with this config: `ONLINE_STORE__REDIS__HOST`
# For example if you want to override this setting in a shell environment:
# export ONLINE_STORE__REDIS__HOST=feathrazure.redis.cache.windows.net

# version of API settings
api_version: 1
project_config:
  project_name: 'Hangfei_udf_feathr_testing1'
  # Information that are required to be set via environment variables.
  required_environment_variables:
    # the environemnt variables are required to run Feathr
    # Redis password for your online store
    - 'REDIS_PASSWORD'
    # client IDs and client Secret for the service principal. Read the getting started docs on how to get those information.
    - 'AZURE_CLIENT_ID'
    - 'AZURE_TENANT_ID'
    - 'AZURE_CLIENT_SECRET'
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

offline_store:
  # paths starts with abfss:// or abfs://
  # ADLS_ACCOUNT and ADLS_KEY should be set in environment variable if this is set to true
  adls:
    adls_enabled: true

  # paths starts with wasb:// or wasbs://
  # WASB_ACCOUNT and WASB_KEY should be set in environment variable
  wasb:
    wasb_enabled: true

  # paths starts with s3a://
  # S3_ACCESS_KEY and S3_SECRET_KEY should be set in environment variable
  s3:
    s3_enabled: true
    # S3 endpoint. If you use S3 endpoint, then you need to provide access key and secret key in the environment variable as well.
    s3_endpoint: 's3.amazonaws.com'

  # jdbc endpoint
  jdbc:
    jdbc_enabled: true
    jdbc_database: 'feathrtestdb'
    jdbc_table: 'feathrtesttable'
  
  # snowflake endpoint
  snowflake:
    url: "dqllago-ol19457.snowflakecomputing.com"
    user: "feathrintegration"
    role: "ACCOUNTADMIN"

# reading from streaming source is coming soon
# streaming_source:
#   kafka_connection_string: ''

spark_config:
  # choice for spark runtime. Currently support: azure_synapse, databricks
  # The `databricks` configs will be ignored if `azure_synapse` is set and vice versa.
  spark_cluster: 'azure_synapse'
  # configure number of parts for the spark output for feature generation job
  spark_result_output_parts: '1'

  azure_synapse:
    dev_url: 'https://feathrazuretest3synapse.dev.azuresynapse.net'
    pool_name: 'spark3'
    # workspace dir for storing all the required configuration files and the jar resources
    workspace_dir: 'abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/feathr_getting_started'
    executor_size: 'Small'
    executor_num: 4
    # Feathr Job configuration. Support local paths, path start with http(s)://, and paths start with abfs(s)://
    # this is the default location so end users don't have to compile the runtime again.
    # feathr_runtime_location: wasbs://public@azurefeathrstorage.blob.core.windows.net/feathr-assembly-0.1.0-SNAPSHOT.jar
    feathr_runtime_location: ../target/scala-2.12/feathr-assembly-0.1.0.jar
  databricks:
    # workspace instance
    workspace_instance_url: 'https://adb-6885802458123232.12.azuredatabricks.net/'
    workspace_token_value: ''
    # config string including run time information, spark version, machine size, etc.
    # the config follows the format in the databricks documentation: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs
    config_template: {'run_name':'','new_cluster':{'spark_version':'9.1.x-scala2.12','node_type_id':'Standard_D3_v2','num_workers':2,'spark_conf':{}},'libraries':[{'jar':''}],'spark_jar_task':{'main_class_name':'','parameters':['']}}
    # Feathr Job location. Support local paths, path start with http(s)://, and paths start with dbfs:/
    work_dir: 'dbfs:/feathr_getting_started'
    # this is the default location so end users don't have to compile the runtime again.
    feathr_runtime_location: 'https://azurefeathrstorage.blob.core.windows.net/public/feathr-assembly-0.1.0-SNAPSHOT.jar'

online_store:
  redis:
    # Redis configs to access Redis cluster
    host: 'feathrazuretest3redis.redis.cache.windows.net'
    port: 6380
    ssl_enabled: True

feature_registry:
  purview:
    # Registry configs
    # register type system in purview during feathr client initialization. This is only required to be executed once.
    type_system_initialization: false
    # configure the name of the purview endpoint
    purview_name: 'feathrazuretest3-purview1'
    # delimiter indicates that how the project/workspace name, feature names etc. are delimited. By default it will be '__'
    # this is for global reference (mainly for feature sharing). For exmaple, when we setup a project called foo, and we have an anchor called 'taxi_driver' and the feature name is called 'f_daily_trips'
    # the feature will have a globally unique name called 'foo__taxi_driver__f_daily_trips'
    delimiter: '__'

"""


with open("/tmp/feathr_config.yaml", "w") as text_file:
    text_file.write(yaml_config)

def trip_distance_preprocessing(df: DataFrame):
    # give the user dataframe
    # user want to cast some fields to certain types
    print("This is my_func1: ")
    print(df)
    print(df.schema)
    # this is executed in spark
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    # df = df.filter("tolls_amount > 0.0")
    print("cast to double in my_func UDF!")
    print("cast to double in my_func UDF!")
    print("cast to double in my_func UDF!")
    print("cast to double in my_func UDF!")
    # df = df.withColumn("trip_distance", df.trip_distance.cast('double'))
    # df = df.withColumn("fare_amount", df.fare_amount.cast('string'))

    df = df.withColumn("trip_distance", df.trip_distance.cast('double') - 90000)
    df = df.withColumn("fare_amount", df.fare_amount.cast('double') - 90000)
    # df = df.withColumn("trip_distance", df.trip_distance - 90000)
    # df = df.withColumn("fare_amount", df.fare_amount - 90000)

    print(df)
    print(df.schema)
    df.show(10)
    return df

def my_func2(df: DataFrame):
    # give the user dataframe
    # user want to cast some fields to certain types
    print("This is my_func2: ")
    print(df)
    print(df.schema)
    # this is executed in spark
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    # df = df.filter("tolls_amount > 0.0")
    print("cast to double in my_func UDF!")
    print("cast to double in my_func UDF!")
    print("cast to double in my_func UDF!")
    print("cast to double in my_func UDF!")
    # df = df.withColumn("trip_distance", df.trip_distance.cast('string'))
    # df = df.withColumn("fare_amount", df.fare_amount.cast('double'))
    # df = df.withColumn("trip_distance", df.trip_distance + 90000)
    # df = df.withColumn("fare_amount", df.fare_amount + 90000)
    df = df.withColumn("trip_distance", df.trip_distance.cast('double') + 90000)
    df = df.withColumn("fare_amount", df.fare_amount.cast('double') + 90000)
    print(df)
    print(df.schema)
    df.show(10)
    return df

from pyspark.sql import DataFrame
def feathr_udf_add_toll_amount(df: DataFrame) -> DataFrame:
    from pyspark.sql.functions import col,sum,avg,max
    # df = df.filter("tolls_amount > 0.0")
    df = df.withColumn("CopiedColumn", col("tolls_amount") * -1)
    df = df.withColumn("fare_amount_new", col("fare_amount") + 100)
    return df

def feathr_udf_day_calc(df: DataFrame) -> DataFrame:
    from pyspark.sql.functions import col,sum,avg,max,dayofweek,dayofmonth, dayofyear
    # df = df.filter("tolls_amount > 0.0")
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    df = df.withColumn("f_day_of_year", dayofyear("lpep_dropoff_datetime"))
    return df

client = FeathrClient(config_path="/tmp/feathr_config.yaml")


batch_source = HdfsSource(name="nycTaxiBatchSource",
                          path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
                          event_timestamp_column="lpep_dropoff_datetime",
                          timestamp_format="yyyy-MM-dd HH:mm:ss")

location_id = TypedKey(key_column="lpep_pickup_datetime",
                       key_column_type=ValueType.INT32,
                       description="location id in NYC",
                       full_name="nyc_taxi.location_id")

# preprocessing
# figure out pyfile
# give the py func the input source data
# return the input source data back to Frame for further processing
f_fare_amount = Feature(name="f_fare_amount",
                        key=location_id,
                        feature_type=FLOAT, transform="fare_amount + 1000")
f_trip_time_duration = Feature(name="f_trip_time_duration",
                               key=location_id,
                               feature_type=INT32,
                               transform="time_duration(lpep_pickup_datetime, lpep_dropoff_datetime, 'minutes')")



features = [
    Feature(name="f_is_long_trip_distance",
            key=location_id,
            feature_type=FLOAT,
            # transform="trip_distance + 1000"),
            transform="fare_amount_new"),
    Feature(name="f_day_of_week",
            key=location_id,
            feature_type=INT32,
            transform="dayofweek(lpep_dropoff_datetime)"),
]

request_anchor = FeatureAnchor(name="request_features",
                               source=batch_source,
                               features=features,
                               # preprocessing=trip_distance_preprocessing)
                               preprocessing=feathr_udf_add_toll_amount)

features2222 = [
    f_fare_amount,
    f_trip_time_duration
]

feature_anchor2222 = FeatureAnchor(name="feature_anchor2222",
                                   source=batch_source,
                                   features=features2222,
                                   preprocessing=my_func2)

# f_trip_time_distance = DerivedFeature(name="f_trip_time_distance",
#                                       feature_type=FLOAT,
#                                       input_features=[
#                                           f_trip_distance, f_trip_time_duration],
#                                       transform="f_trip_distance * f_trip_time_duration")
#
# f_trip_time_rounded = DerivedFeature(name="f_trip_time_rounded",
#                                      feature_type=INT32,
#                                      input_features=[f_trip_time_duration],
#                                      transform="f_trip_time_duration % 10")


agg_features = [Feature(name="f_location_avg_fare",
                        key=location_id,
                        feature_type=FLOAT,
                        # transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                        transform=WindowAggTransformation(agg_expr="fare_amount",
                                                          agg_func="AVG",
                                                          window="90d")),
                Feature(name="f_location_max_fare",
                        key=location_id,
                        feature_type=FLOAT,
                        transform=WindowAggTransformation(agg_expr="cast_float(fare_amount)",
                                                          agg_func="MAX",
                                                          window="90d"))
                ]

agg_anchor = FeatureAnchor(name="aggregationFeatures",
                           source=batch_source,
                           features=agg_features,
                           preprocessing=my_func2)




client.build_features(anchor_list=[agg_anchor, request_anchor, feature_anchor2222])
# client.build_features(anchor_list=[agg_anchor, request_anchor], derived_feature_list=[
#     f_trip_time_distance, f_trip_time_rounded])


# TODO
# anchor name and requested feature name might be different?
# {'f_location_avg_fare,f_location_max_fare': JavaObject id=o204, 'f_trip_distance,f_trip_time_duration,f_is_long_trip_distance,f_day_of_week': JavaObject id=o220}


feature_query = FeatureQuery(
    # This breaks. fixed
    feature_list=["f_is_long_trip_distance", "f_fare_amount", "f_trip_time_duration"], key=location_id)
# this works
# feature_list=["f_is_long_trip_distance", "f_day_of_week", "f_fare_amount", "f_trip_time_duration"], key=location_id)
# feature_list=["f_location_avg_fare"], key=location_id)

settings = ObservationSettings(
    observation_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv",
    event_timestamp_column="lpep_dropoff_datetime",
    timestamp_format="yyyy-MM-dd HH:mm:ss")


client.get_offline_features(observation_settings=settings,
                            feature_query=feature_query,
                            output_path="abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro",
                            # udf_files=["./pyspark_client.py", "./client_udf_repo.py", "./__init__.py"]
                            # udf_files=["./client_udf_repo.py"]
                            udf_files=[]
                            )
from pyspark.sql import SparkSession
from client_udf_repo import *

spark = SparkSession.builder.appName('SparkByExamples.com').getOrCreate()
spark._jvm.com.linkedin.feathr.offline.job.SimpleApp.hello()

# This is executed in Spark driver
offline_job_arguments = ['--join-config', 'abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net'
                                          '/feathr_getting_started/feature_join.conf', '--input',
                         'abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data'
                         '/green_tripdata_2020-04.csv', '--output',
                         'abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output'
                         '.avro', '--feature-config',
                         'abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net'
                         '/feathr_getting_started/auto_generated_request_features.conf,'
                         'abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net'
                         '/feathr_getting_started/auto_generated_anchored_features.conf,'
                         'abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net'
                         '/feathr_getting_started/auto_generated_derived_features.conf', '--num-parts', '1',
                         '--s3-config', '\n            S3_ENDPOINT: s3.amazonaws.com\n            S3_ACCESS_KEY: '
                                        '"None"\n            S3_SECRET_KEY: "None"\n            ', '--adls-config',
                         '\n            ADLS_ACCOUNT: None\n            ADLS_KEY: "None"\n            ',
                         '--blob-config', '\n            BLOB_ACCOUNT: None\n            BLOB_KEY: "None"\n           '
                                          ' ', '--sql-config', '\n            JDBC_TABLE: None\n            '
                                                               'JDBC_USER: None\n            JDBC_PASSWORD: None\n    '
                                                               '        JDBC_DRIVER: None\n            '
                                                               'JDBC_AUTH_FLAG: None\n            JDBC_TOKEN: None\n  '
                                                               '          ', '--snowflake-config', '\n            '
                                                                                                   'JDBC_SF_URL: '
                                                                                                   'dqllago-ol19457.snowflakecomputing.com\n            JDBC_SF_USER: feathrintegration\n            JDBC_SF_ROLE: ACCOUNTADMIN\n            JDBC_SF_PASSWORD: None\n            ']




def toJStringArray(arr):
    jarr = spark._sc._gateway.new_array(spark._sc._jvm.java.lang.String, len(arr))
    for i in range(len(arr)):
        jarr[i] = arr[i]
    return jarr

def load_dataframe(source_path):
    source_df = spark.read.option('header', 'true').csv(source_path)
    return source_df

def submit_spark_job(user_func_map):
    preprocessed_df_map = {}
    for source_path, user_func in user_func_map.items():
        print("Source_path is: ")
        print(source_path)
        source_df = load_dataframe(source_path)
        print("Corresponding DataFrame: ")
        print(source_df)
        # preprocess the DataFrae via UDF
        preprocessed_udf = user_func(source_df)
        preprocessed_udf.show(10)

        print("preprocessed_udf: ")
        print(preprocessed_udf.schema)

        preprocessed_df_map = {
            source_path: preprocessed_udf._jdf
        }
    print("Preprocessed DataFrame map to pass to Scala Spark is: ")
    print(preprocessed_df_map)
    print("Starting Scala Spark FeatureJoinJob.")
    # Need to convert to proper Java array otherwise it won't work
    job_param_java_array = toJStringArray(offline_job_arguments)
    spark._jvm.com.linkedin.feathr.offline.job.FeatureJoinJob.mainWithMap(job_param_java_array, df_map)
    return None

print("Preprocessing UDFs.")


submit_spark_job(preprocessed_funcs)

print("Feathr Pyspark job completed.")




from pyspark import SparkContext, SparkConf
from pyspark.sql import SparkSession, DataFrame
from pyspark.sql import SparkSession
from feathr.typed_key import TypedKey
from feathr.dtype import BOOLEAN, FLOAT, INT32, ValueType
# if __name__ == "__main__":
#     # get the feature definitions from spark
#     # use python pickle or send the whole file over
#     # let's mock a python definition for now
#     # then i get the source
spark = SparkSession.builder.appName('SparkByExamples.com').getOrCreate()
spark._jvm.com.linkedin.feathr.offline.job.SimpleApp.hello()

# this is executed in spark
print("1111111111111111111111111")
print("1111111111111111111111111")
print("1111111111111111111111111")
print("1111111111111111111111111")
print("1111111111111111111111111")
print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!3334444444444444444!")
    # df = df.filter("tolls_amount > 0.0")
    # df.show(10)
    # df.write.mode('overwrite').csv('abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/UDF_output.csv')

# feathrClient
# read in the dataframe into a global_map
# pass the dataframe to the UDF
# decorator

# spark._jvm.com.linkedin.feathr.offline.job.SimpleApp.feathrDataframe(global_df._jdf)

from py4j.java_collections import SetConverter, MapConverter, ListConverter
from py4j.java_gateway import (
    JavaGateway, CallbackServerParameters, GatewayParameters,
    launch_gateway)

def toJStringArray(arr):
    jarr = spark._sc._gateway.new_array(spark._sc._jvm.java.lang.String, len(arr))
    for i in range(len(arr)):
        jarr[i] = arr[i]
    return jarr

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



# spark._jvm.com.linkedin.feathr.offline.job.SimpleApp.hello(my_java_arr)
# spark._jvm.com.linkedin.feathr.offline.job.FeatureJoinJob.mainWithList(my_list)



print("return wrapper_func!!!!!")

print("outside of main!!!!!")
from non_agg_features import *
print(features333)
print(anchor)

def feathr_feature(user_func_map):
    from sklearn import preprocessing
    import numpy as np
    print("scikit learn: ")
    X_train = np.array([[ 1., -1.,  2.],
                        [ 2.,  0.,  0.],
                        [ 0.,  1., -1.]])
    scaler = preprocessing.StandardScaler().fit(X_train)
    print(scaler)
    print("my_decorator_func!!!!!")
    # global_map.get(dataframe)
    location_id = TypedKey(key_column="DOLocationID",
                           key_column_type=ValueType.INT32,
                           description="location id in NYC",
                           full_name="nyc_taxi.location_id")
    print("location_id: ")
    print(location_id)

    my_java_arr = toJStringArray(offline_job_arguments)
    print(my_java_arr)
    print(type(my_java_arr))

    # get path
    df_map = {}
    for feature_name, user_func in user_func_map.items():
        # user_func = user_func_map['f_is_long_trip_distance']
        source_path = anchor.source.path
        print("source_path: ")
        print(source_path)
        # global_df = spark.read.option('header', 'true').csv('wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv')
        source_df = spark.read.option('header', 'true').csv(source_path)
        print("source_df: ")
        print(source_df)
        preprocessed_udf = user_func(source_df)
        preprocessed_udf.show(10)

        print("preprocessed_udf: ")
        print(preprocessed_udf.schema)
        # def wrapper_func(*args, **kwargs):
        #     print("wrapper_func!!!!!")
        #     # Do something before the function.
        #     func(*args, **kwargs)
        #     # Do something after the function.
        print("return wrapper_func!!!!!")


        print("Using my_java_arr:")
        # spark._jvm.com.linkedin.feathr.offline.job.SimpleApp.feathrDataframe(global_df._jdf)
        df_map = {
            source_path: preprocessed_udf._jdf
        }
    print("dfMap to pass to Scala Spark: ")
    print(df_map)
    spark._jvm.com.linkedin.feathr.offline.job.FeatureJoinJob.mainWithMap(my_java_arr, df_map)
    return None

# Use this file to read from other files
# What if i am in managed notebook



print("preprocessed_funcs.preprocess_func !!!!!")



print("preprocessed_funcs.preprocess_func !!!!!")
print(preprocessed_funcs)
feathr_feature(preprocessed_funcs)
print("end of pyspark client code")
print("end of pyspark client code")
print("end of pyspark client code")
# from pysparkudf import *
# print(feathr_feature)
# feathr_feature(my_func)

# pick a user function to execute

# from pyspark import SparkFiles
#
# spark.sparkContext.addPyFile(SparkFiles.get("pysparkudf.py"))
#





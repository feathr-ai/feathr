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


# spark._jvm.com.linkedin.feathr.offline.job.SimpleApp.hello(my_java_arr)
# spark._jvm.com.linkedin.feathr.offline.job.FeatureJoinJob.mainWithList(my_list)


# print("outside of main!!!!!")
# from non_agg_features import *
# print(features333)
# print(anchor)

# def feathr_feature(user_func):
#     from sklearn import preprocessing
#     import numpy as np
#     print("scikit learn: ")
#     X_train = np.array([[ 1., -1.,  2.],
#                         [ 2.,  0.,  0.],
#                         [ 0.,  1., -1.]])
#     scaler = preprocessing.StandardScaler().fit(X_train)
#     print(scaler)
#     print("my_decorator_func!!!!!")
#     # global_map.get(dataframe)
#     location_id = TypedKey(key_column="DOLocationID",
#                            key_column_type=ValueType.INT32,
#                            description="location id in NYC",
#                            full_name="nyc_taxi.location_id")
#     print("location_id: ")
#     print(location_id)
#     preprocessed_udf = user_func(global_df)
#     preprocessed_udf.show(10)
#     print("preprocessed_udf: ")
#     print(preprocessed_udf.schema)
#     # def wrapper_func(*args, **kwargs):
#     #     print("wrapper_func!!!!!")
#     #     # Do something before the function.
#     #     func(*args, **kwargs)
#     #     # Do something after the function.
#     print("return wrapper_func!!!!!")
#     my_java_arr = toJStringArray(offline_job_arguments)
#     print(my_java_arr)
#     print(type(my_java_arr))
#
#     print("Using my_java_arr:")
#     # spark._jvm.com.linkedin.feathr.offline.job.SimpleApp.feathrDataframe(global_df._jdf)
#     dfMap = {
#         "f1": global_df._jdf
#     }
#     spark._jvm.com.linkedin.feathr.offline.job.FeatureJoinJob.mainWithMap(my_java_arr, dfMap)
#     return None
#
# # Use this file to read from other files
# # What if i am in managed notebook
#
# @feathr_feature
def my_func(df: DataFrame):
    # give the user dataframe
    print("df: ")
    print(df)
    print(df.schema)
    # this is executed in spark
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    print("hi @feathr_udf!!!!!!!!!!!!!!!!!!!!!")
    # df = df.filter("tolls_amount > 0.0")
    df = df.withColumn("fare_amount", df.fare_amount.cast('double'))
    df = df.withColumn("trip_distance", df.trip_distance.cast('double'))
    print(df)
    print(df.schema)
    df.show(10)
    return df

print("return wrapper_func!!!!!3333")
from pyspark.sql import SparkSession, DataFrame, SQLContext
from client_udf_repo import *
import sys

# This is executed in Spark driver
print("Feathr Pyspark job started.")
spark = SparkSession.builder.appName('FeathrPyspark').getOrCreate()


def toJStringArray(arr):
    jarr = spark._sc._gateway.new_array(spark._sc._jvm.java.lang.String, len(arr))
    for i in range(len(arr)):
        jarr[i] = arr[i]
    return jarr


def submit_spark_job(feature_names_funcs):
    # Prepare job parameters
    # sys.argv has all the arguments passed by submit job.
    # In pyspark job, the first param is the python file.
    # For example: ['pyspark_client.py', '--join-config', 'abfss://...', ...]
    job_param_java_array = toJStringArray(sys.argv)

    print("submit_spark_job: feature_names_funcs: ")
    print(feature_names_funcs)
    print("submit_spark_job: user_func_map feature nams to source data: ")
    print(preprocessed_funcs)
    print("set(feature_names_funcs.keys()): ")
    print(set(feature_names_funcs.keys()))

    print("submit_spark_job: Load DataFrame from Scala engine.")
    py4j_featureJoinJob = spark._jvm.com.linkedin.feathr.offline.job.FeatureJoinJob
    dataframeFromSpark = py4j_featureJoinJob.loadDataframe(job_param_java_array, set(feature_names_funcs.keys()))
    print("Submit_spark_job: dataframeFromSpark: ")
    print(dataframeFromSpark)

    sql_ctx = SQLContext(spark)
    new_preprocessed_df_map = {}
    for feature_names, scala_dataframe in dataframeFromSpark.items():
        print(feature_names)
        print(scala_dataframe)
        # Need to convert java DataFrame into python DataFrame
        py_df = DataFrame(scala_dataframe, sql_ctx)
        print("Corresponding py_df: ")
        print(py_df)
        py_df.show(10)
        # Preprocess the DataFrame via UDF
        user_func = feature_names_funcs[feature_names]
        preprocessed_udf = user_func(py_df)
        preprocessed_udf.show(10)
        new_preprocessed_df_map[feature_names] = preprocessed_udf._jdf

    print("submit_spark_job: running Feature job with preprocessed DataFrames:")
    print("Preprocessed DataFrames are: ")
    print(new_preprocessed_df_map)

    py4j_featureJoinJob.mainWithMap(job_param_java_array, new_preprocessed_df_map)
    return None


print("pyspark_client.py: Preprocessing via UDFs and submit Spark job.")
submit_spark_job(feature_names_funcs)

print("pyspark_client.py: Feathr Pyspark job completed.")




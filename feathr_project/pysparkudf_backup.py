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

@feathr_feature
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

print("return wrapper_func!!!!!")
from pyspark.sql import SparkSession, DataFrame
from pyspark.sql.functions import col,sum,avg,max,dayofweek,dayofyear,concat,lit

def trip_distance_preprocessing(df: DataFrame):
    df = df.withColumn("trip_distance", df.trip_distance.cast('double') - 90000)
    df = df.withColumn("fare_amount", df.fare_amount.cast('double') - 90000)

    return df

def add_new_dropoff_and_fare_amount_column(df: DataFrame):
    df = df.withColumn("new_lpep_dropoff_datetime", col("lpep_dropoff_datetime"))
    df = df.withColumn("new_fare_amount", col("fare_amount") + 1000000)
    return df

def add_new_fare_amount(df: DataFrame) -> DataFrame:
    df = df.withColumn("fare_amount_new", col("fare_amount") + 8000000)

    return df

def add_new_surcharge_amount_and_pickup_column(df: DataFrame) -> DataFrame:
    df = df.withColumn("new_improvement_surcharge", col("improvement_surcharge") + 1000000)
    df = df.withColumn("new_tip_amount", col("tip_amount") + 1000000)
    df = df.withColumn("new_lpep_pickup_datetime", col("lpep_pickup_datetime"))

    return df

def add_old_lpep_dropoff_datetime(df: DataFrame) -> DataFrame:
    df = df.withColumn("old_lpep_dropoff_datetime", col("lpep_dropoff_datetime"))

    return df

def feathr_udf_day_calc(df: DataFrame) -> DataFrame:
    df = df.withColumn("f_day_of_week", dayofweek("lpep_dropoff_datetime"))
    df = df.withColumn("f_day_of_year", dayofyear("lpep_dropoff_datetime"))
    return df

def snowflake_preprocessing(df: DataFrame) -> DataFrame:
    df = df.withColumn("NEW_CC_DIVISION_NAME", concat(col("CC_DIVISION_NAME"), lit("0000"), col("CC_DIVISION_NAME")))
    df = df.withColumn("NEW_CC_ZIP", concat(col("CC_ZIP"), lit("____"), col("CC_ZIP")))
    return df


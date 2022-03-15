# Feature Join
## Intuitions of Frame Join
Observation dataset has 2 records as below, and we want to use it as the 'spine' dataset, joining two 
features onto it:

1) Feature 'page_view_count' from dataset 'page_view_data'

2) Feature 'like_count' from dataset 'like_count_data' 

The Feathr feature join in this case, will use the field 'id' as join key of the observation data,
and also consider the timestamp of each row during the join, making sure the joined feature values are 
collected **before** the observation_time of each row.

| id | observe_time | Label 
| --- | --- | --- 
| 1 | 2022-01-01 | Yes 
| 1 | 2022-01-02 | Yes 
| 2 | 2022-01-02 | No 


Dataset 'page_view_data' contains “page_view_count” of each user at a given time:

| UserId | log_time | page_view_count |
| --- | --- | --- | 
|1 | 2022-01-01 | 101 |
|1 | 2022-01-02 | 102 |
|1 | 2022-01-03 | 103 |
|2 | 2022-01-02 | 200 |
|3 | 2022-01-02 | 300 |


Dataset 'like_count_data' contains "like_count" of each user at a given time:

| UserId | updated_time | 'like_count' |
| --- | --- | --- | 
|1 | 2022-01-01 | 11 |
|1 | 2022-01-02 | 12 |
|1 | 2022-01-03 | 13 |
|2 | 2022-01-02 | 20 |
|3 | 2022-01-02 | 30 |

The expected joined output, a.k.a. training dataset would be assuming feature:

| id | observe_time | Label | f_page_view_count | f_like_count|
| --- | --- | --- | --- | --- |
|1 | 2022-01-01 | Yes | 101 | 11 |
|1 | 2022-01-02 | Yes | 102 | 12 |
|2 | 2022-01-02 | No | 200 | 20

Note: In the above example, feature f_page_view_count and f_like_count are defined as simply a reference of field
'page_view_count' and 'like_count' respectively. Timestamp in these 3 datasets are considered automatically.

## Feature join config

Feature join config syntax spec:

```

observationPath: <path to the observation dataset>
        
outputPath: <path of the output dataset, i.e. observation dataset with features joined to it, a.k.a. training dataset>
        
// Time information of the observation data used to join with feature datsets
settings: {
    joinTimeSettings: {
        timestampColumn: {
            def: <Feathr expression that extract the timestamp from observation row>
            format: <format of the timestamp format returned in the above def expression>
        }
    }
}

featureList: [
    {
        key: <join key of observation data>
        featureList: <list of feature names to be joined to the observation data using the above key>
    }
    {
        key: <other join key of observation data>
        featureList: <list of feature names to be joined to the observation data using the above other key>
    }
]


```

Feature join config example:

```

observationPath: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv"
        
outputPath: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output.avro"
        
settings: {
    joinTimeSettings: {
        timestampColumn: {
            def: "lpep_dropoff_datetime"
            format: "yyyy-MM-dd HH:mm:ss"
        }
    }
}

featureList: [
    {
        key: DOLocationID
        featureList: [f_location_avg_fare, f_trip_time_distance, f_trip_distance, f_trip_time_duration, f_is_long_trip_distance, f_day_of_week, f_day_of_month, f_hour_of_day]
    }
]



```



Create training dataset via feature join with the above feature join config:
```bash
feathr join
```
Or with Python:
```python
returned_spark_job = client.get_offline_features()
df_res = client.get_job_result()
```



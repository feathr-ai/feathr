# Feature Generation
User could utilize feature generation config to pre-compute and materialize the pre-defined features in the feature
definition configs, to online and/or offline storage. This is a common practice when the feature transformation is 
computation intensive.

Example in Python:
```python

from datetime import datetime, timedelta
from feathr.client import FeathrClient
from feathr.materialization_settings import (BackfillTime,
                                             MaterializationSettings)
from feathr.sink import RedisSink

client = FeathrClient()
redisSink = RedisSink(table_name="nycTaxiDemoFeature")
# Materialize two features into a redis table.
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[redisSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"])
client.materialize_features(settings)

```
If it is also possible to backfill the features for a previous time range:
```python
# Simulate running the materialization job every day for a time range between 2/1/22 and 2/20/22
backfill_time = BackfillTime(start=datetime(2022, 2, 1), end=datetime(2022, 2, 20), step=timedelta(days=1))
settings = MaterializationSettings("nycTaxiMaterializationJob",
                                   sinks=[redisSink],
                                   feature_names=["f_location_avg_fare", "f_location_max_fare"],
                                   backfill_time=backfill_time)
client.materialize_features(settings)
```
Alternatively, this can be done by using feature generation config and CLI:

### Feature generation config specification


```
operational: {
    name: <arbitrary name of the job, given by the user>
    endTime: <cutoff time in computing the feature value>
    endTimeFormat: <cutoff timestamp expression format, e.g. "yyyy-MM-dd">
    resolution: <resolution of the generation job, could be DAILY or HOURLY>
    output:[
        {
            name: <output storage type>
            params: {
              <arbitrary parameter name>: <Parameter value >
              <parbitrary arameter name>: <Parameter value >
            }
        }
    ]
}
features: <list of feature names to be materilized, making sure that they all share the same feature key>
```

And an example(feature_gen_conf/feature_gen.conf):
```
operational: {
    name: nycTaxiMaterializationJob
    endTime: 2021-01-02
    endTimeFormat: "yyyy-MM-dd"
    resolution: DAILY
    output:[
        {
            name: REDIS
            params: {
              table_name: "nycTaxiDemoFeature"
            }
        }
    ]
}
features: [f_location_avg_fare, f_location_max_fare]

```

The feature generation config will be used by the command line:

```bash
feathr deploy
```

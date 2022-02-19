# Feature Generation
User could utilize feature generation config to pre-compute and materialize the pre-defined features in the feature
definition configs, to online and/or offline storage. This is a common practice when the feature transformation is 
computation intensive.

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

And an example:
```
operational: {
    name: generateWithDefaultParams
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

The feature generation config can be used in the command line or Python code:

```bash
feathr deploy
```

Or Python:
```python
job_res = client.materialize_features()

job_res.state
```
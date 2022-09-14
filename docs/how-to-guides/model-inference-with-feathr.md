---
layout: default
title: Online Model Inference with Features from Feathr
parent: How-to Guides
---

# Online Model Inference with Features from Feathr

After you have materialized features in online store such as Redis or Azure Cosmos DB, usually end users want to consume those features in production environment for model inference.

With Feathr's [online client](https://feathr.readthedocs.io/en/latest/#feathr.FeathrClient.get_online_features), it is quite straightforward to do that. The sample code is as below, where users only need to configure the online store endpoint (if using Redis), and call `client.get_online_features()` to get the features for a particular key.

```python

## put the section below into the initialization handler
import os
from feathr import FeathrClient

# Set Redis endpoint
os.environ['online_store__redis__host'] = "<replace_with_your_redis_name>.redis.cache.windows.net"
os.environ['online_store__redis__port'] = "6380"
os.environ['online_store__redis__ssl_enabled'] = "True"
os.environ['REDIS_PASSWORD'] = "<put-your-key-here>"

client = FeathrClient()


# put this section in the model inference handler
feature = client.get_online_features(feature_table="nycTaxiCITable",
                                 key='2020-04-15',
                                 feature_names=['f_is_long_trip_distance', 'f_day_of_week'])
# `res` will be an array representing the features of that particular key.


# `model` will be a ML model that is loaded previously.
result = model.predict(feature)
```

## Best Practices

Usually for ML platforms such as Azure Machine Learning, Sagemaker, or DataRobot, there are options where you can "bring your own container" or using "container inference". Basically it requires end users to write an "entry script" and provide a few functions. In those cases, there are usually two handlers:

- an initialization handler to allow users to load configurations. For example, in Azure Machine Learning, it is a function called `init()`, and in Sagemaker, it is `model_fn()`.
- a model inference handler to do the model inference. For example, in Azure Machine Learning, it is called `init()`, and in Sagemaker, it is called `predict_fn()`.

In the initialization handler, initialize the environment variables and initialize `FeathrClient` as shown in the above script; in the inference handler, call this line:

```python
# put this section in the model inference handler
feature = client.get_online_features(feature_table="nycTaxiCITable",
                                 key='2020-04-15',
                                 feature_names=['f_is_long_trip_distance', 'f_day_of_week'])
# `res` will be an array representing the features of that particular key.
# `model` will be a ML model that is loaded previously.
result = model.predict(feature)
```

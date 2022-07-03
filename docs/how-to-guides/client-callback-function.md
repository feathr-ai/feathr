---
layout: default
title: How to use callback function in feathr client
parent: Feathr How-to Guides
---

## What is a callback function

A callback function is a function that is sent to another function as an argument. It can be used to extend the function as per the user needs.

## How to use callback functions

We can pass a callback function when initializing the feathr client.

```python
client = FeathrClient(config_path, callback)
```

The below functions accept an optional parameters named **params**. params is a dictionary where user can pass the arguments for the callback function.

- get_online_features
- multi_get_online_features
- get_offline_features
- monitor_features
- materialize_features

An example on how to use it:

```python
# inside notebook
client = FeathrClient(config_path, callback)
params = {"param1":"value1", "param2":"value2"}
client.get_offline_features(observation_settings,feature_query,output_path, params)

# users can define their own callback function
async def callback(params):
    import httpx
    async with httpx.AsyncClient() as requestHandler:
        response = await requestHandler.post('https://some-endpoint', json = params)
        return response

```

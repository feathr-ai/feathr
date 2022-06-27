---
layout: default
title: How to use callback function in feathr client
parent: Feathr How-to Guides
---

## What is a callback function

A callback function is a function that is sent to another function as an argument. It can be used to extend the function as per the user needs.

## How to use callback functions

Currently the below functions in feathr client support passing a callback as an argument:

- get_online_features
- multi_get_online_features
- get_offline_features
- monitor_features
- materialize_features

These functions accept two optional parameters named **callback** and **params**.
callback is of type function and params is a dictionary where user can pass the arguments for the callback function.

An example on how to use it:

```python
async def callback(params):
    import httpx
    async with httpx.AsyncClient() as client:
        response = await client.post('https://some-endpoint', json = params)
        return response

params = {"param1":"value1", "param2":"value2"}

# inside the notebook
client = FeathrClient(config_path)
client.get_offline_features(observation_settings,feature_query,output_path, callback, params)

```

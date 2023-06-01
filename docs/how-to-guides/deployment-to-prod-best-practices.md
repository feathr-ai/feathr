---
layout: default
title: Implementation Recommendations for Deployment
parent: Instructional Guides
---

## Preferred Practices for Feathr Deployment 

### Distinguishing Production and Non-Production Environments

We highly suggest maintaining a clear distinction between production and non-production environments, encompassing the Feathr registry, computational resources (such as Spark clusters), and certain data sources.

While the optimal arrangement involves total segregation, practically, the fundamental requirement is to maintain distinct registries for production and non-production. For Spark, a shared environment could suffice, supporting both development/testing and production, contingent on the setup. Likewise, data sources might be shared across different environments, circumventing data duplication.

Implementation of this in Feathr is straightforward. Typically, you would utilize configurations as shown below:

```yaml
spark_config:
  spark_cluster: "databricks"
  spark_result_output_parts: "1"
  azure_synapse:
    dev_url: "https://feathrazuretest3synapse.dev.azuresynapse.net"
    pool_name: "spark3"
    workspace_dir: "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/feathr_test_workspace"
    executor_size: "Small"
    executor_num: 1
  databricks:
    workspace_instance_url: "https://adb-2474129336842816.16.azuredatabricks.net/"
    workspace_token_value: ""
    config_template:
      {
        "run_name": "FEATHR_FILL_IN",
        "new_cluster":
          {
            "spark_version": "9.1.x-scala2.12",
            "num_workers": 1,
            "spark_conf": { "FEATHR_FILL_IN": "FEATHR_FILL_IN" },
            "instance_pool_id": "0403-214809-inlet434-pool-l9dj3kwz",
          },
        "libraries": [{ "jar": "FEATHR_FILL_IN" }],
        "spark_jar_task":
          {
            "main_class_name": "FEATHR_FILL_IN",
            "parameters": ["FEATHR_FILL_IN"],
          },
      }
    work_dir: "dbfs:/feathr_getting_started"

online_store:
  redis:
    host: "feathrazuretest3redis.redis.cache.windows.net"
    port: 6380
    ssl_enabled: True

feature_registry:
  api_endpoint: "https://feathr-registry-purview-rbac.azurewebsites.net/api/v1"
```

Just modify the configuration to different endpoints (like one for production registry, and another for development/testing registry), and retain the rest of the code uniform across different environments.

### Establishing CI/CD Pipelines

Setting up CI/CD pipelines is also quite direct. The most convenient method involves storing all the features in a python file and utilizing your existing CI/CD pipeline in your IT infrastructure. This could involve Jenkins, GitHub Actions, or Azure DevOps. Here, the features would be a python file indicating the relevant environment, then you can retrieve the results and verify their accuracy.

You can refer to the [test folder](https://github.com/feathr-ai/feathr/tree/main/feathr_project/test) for Feathr to see a list of tests that we run. Also please refer to the [GitHub Workflow Folder](https://github.com/feathr-ai/feathr/tree/main/.github/workflows) for how we setup the GitHub CI and CD pipelines.

### Materializing Features When Necessary to Save Cost

Consider using materialization features when appropriate. The `get_offline_features` API will compute features on the fly, which will retrieve the most up-to-date features. But sometimes you probably don't need this, so in that case you can simply materialize the features to an offline store (the `materialize_features()` API), then read the materialized features.

### Reducing Single Points of Failure and Having a Backup Plan

The registry backend supports both Azure Purview and SQL databases. For Purview, as it is a managed service, Business Continuity and Disaster Recovery (BCDR) is overseen by Azure and Microsoft. Conversely, if you choose SQL as the feature registry backend, it would be prudent to design a BCDR plan.

Backing up your Feathr registry follows the same procedures as backing up other databases. We recommend referring to the relevant documentation for your database system.

### Using Registry Tags for Easier Feature Status Tracking

It is recommended to use registry tags to indicate a feature's life cycle, such as marking it as "ready to use," "production," or "deprecated." Every feature store object (feature, anchor, project, etc.) all have a field called "tag" where it's a key-value pair and you can literally put anything you want. We recommend putting something like `status: test`, `status: prod` for the status of the feature, and `description: This feature is used to calculate the total purchase for a certain user` for the usage of the feature, or put something like `owner: foo@bar.com` to indicate the owner of the feature.

There is also a roadmap to use ChatGPT to help generate descriptions for features.

### Using Different Projects for Different Teams and Projects

It is recommended to have separate projects for different teams (for example, one project for anti-abuse and another project for recommendation). Currently, a project is an independent namespace and all the access control are on project level.

### Implementing Role-Based Access Control (RBAC) for Enhanced Security

Setting up RBAC for the environments is always recommended. Usually, only a few selected users (or service accounts) have access to production environments. So the pipeline is usually that someone (an authorized user or a CD pipeline account, they usually have write access) pushes the feature to production, and the rest of the users/service accounts will just read from the production registry and do the calculation.
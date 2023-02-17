---
layout: default
title: Quick Start Guide with Databricks
---

Announce Feathr 1.0.0

Feathr 1.0.0-rc1 is released on February 16, 2023 with following new features

New Features:

- Feathr sandbox: The Feathr sandbox is a pre-configured environment that you can use to learn how to use Feathr, experiment with features, and build proof-of-concept applications locally, without setting up complex infrastructure on the cloud. Pleaes check out [Quick Start Guide with Local Sandbox | Feathr (feathr-ai.github.io)](https://feathr-ai.github.io/feathr/quickstart_local_sandbox.html) to get started.

- Online Transform: Online Transform is a feature that enables real-time feature transformations as part of the feature retrieval process, here are some use cases examples:

  - Featurization source is only available at inference time​.

  - Using offline transform might be a waste of storage and compute resources. 
  - Users would like to decouple featurization work off up-stream online system.
  - Users would like to define transformation once for both online and offline consumption.  
  Please checkout [feathr-ai/feathr-online (github.com)](https://github.com/feathr-ai/feathr-online#readme) to get started

- Use Azure Cosmos DB as online store. Please see following documentation for provisioning and materilization 
  
  - Provisioning: [Azure Resource Provisioning through Azure Resource Manager | Feathr (feathr-ai.github.io)](https://feathr-ai.github.io/feathr/how-to-guides/azure-deployment-arm.html#azure-resource-provisioning)
  
  - Materilization: [Using SQL databases, CosmosDb with Feathr | Feathr (feathr-ai.github.io)](https://feathr-ai.github.io/feathr/how-to-guides/jdbc-cosmos-notes.html#using-cosmosdb-as-the-online-store)

- Feathr Notebook samples now provide 5 getting started tutorial with Jupyter notebooks to help users get started, these notebooks cover various use cases commonly used in ML work.
  
  | Name                                                                                                                                 | Description                                                                                                                           | Platform                               |
  | ------------------------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------- |
  | [NYC Taxi Demo](https://github.com/feathr-ai/feathr/blob/main/docs/samples/nyc_taxi_demo.ipynb)                                      | Quickstart notebook that showcases how to define, materialize, and register features with NYC taxi-fare prediction sample data.       | Azure Synapse, Databricks, Local Spark |
  | [Databricks Quickstart NYC Taxi Demo](https://github.com/feathr-ai/feathr/blob/main/docs/samples/nyc_taxi_demo.ipynb)                | Quickstart Databricks notebook with NYC taxi-fare prediction sample data.                                                             | Databricks                             |
  | [Feature Embedding](https://github.com/feathr-ai/feathr/blob/main/docs/samples/feature_embedding.ipynb)                              | Feathr UDF example showing how to define and use feature embedding with a pre-trained Transformer model and hotel review sample data. | Databricks                             |
  | [Fraud Detection Demo](https://github.com/feathr-ai/feathr/blob/main/docs/samples/fraud_detection_demo.ipynb)                        | An example to demonstrate Feature Store using multiple data sources such as user account and transaction data.                        | Azure Synapse, Databricks, Local Spark |
  | [Product Recommendation Demo](https://github.com/feathr-ai/feathr/blob/main/docs/samples/product_recommendation_demo_advanced.ipynb) | Feathr Feature Store example notebook with a product recommendation scenario                                                          | Azure Synapse, Databricks, Local Spark |

- Web UI has been enhanced in following areas
  - Support register feature
  - Support delete feature
  - Support display version
  - Lineage has been added to top manu for quick access
  You can try these new changes on Feathr live demo site at [Feathr Feature Store (feathr-registry-sql.azurewebsites.net)](https://feathr-registry-sql.azurewebsites.net/projects)](https://feathr-registry-sql.azurewebsites.net/)

- Time pattern support in data source path.  [Input File for Feathr | Feathr (feathr-ai.github.io)](https://feathr-ai.github.io/feathr/how-to-guides/feathr-input-format.html#timepartitionpattern-for-input-files)

- Feature names conflicts check and auto correction [Getting Offline Features using Feature Query | Feathr (feathr-ai.github.io)](https://feathr-ai.github.io/feathr/concepts/get-offline-features.html#feature-names-conflicts-check)

- Composite key improvement , to be updated by @enya

- Use SparkSQL as DataSource, to be updated by @yuqing

For more information, please refer to the official Feathr documentation.

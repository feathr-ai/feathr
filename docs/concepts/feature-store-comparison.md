---
layout: default
title: Comparison between Feature Stores
parent: Feathr Concepts
---

| Feature Store            | Open-Source | Point-in-time Support                                                                                              | Data Source                                                                       | Feature Transformation                                                                                                                                                          | Feature materialization                                                                                            | Performance                                                        | Feature Type                                         |
| ------------------------ | ----------- | ------------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------ | ------------------------------------------------------------------ | ---------------------------------------------------- |
| Feathr                   | Open-source | Point-in-time. Supports various timestamp formats.                                                                 | Supports most major sources and file formats(csv, parquet, avro, orc, delta lake) | Native transformation support with declarative framework •Row-level transformation, window aggregation transformation. •Supports offline, streaming and online transformations. | Supports feature materialization via both Python API and configuration files + CLI Redis, CosmosDB, AeroSpike, SQL | Scales. Performant. with built-in, low-level Spark optimizations   | Tensor type (for deep learning/ML) + Primitive Types |
| Databricks Feature Store | Proprietary | Only time-travel (No point-in-time support).                                                                       | Limited. Delta Lake tables for offline and Amazon Aurora for online.              | No native transformation support. •Only general data processing with PySpark notebook. •Users must know PySpark. •No online feature transformation. •Vendor locked to Spark.    | Manually managed by notebook                                                                                       | Doesn’t have Spark optimizations but still scales because of Spark | Primitive Types                                      |
| Feast                    | Open-source | •Point-in-time and requires a fixed timestamp format. •Timestamp is always required even for non-time-series data. | Supports most major sources. Doesn’t support CSV.                                 | Only row-level transformation with Pandas (Python library)                                                                                                                      | Supports feature materialization via CLI                                                                           | Single node. In-memory. Doesn’t scale.                             | Primitive Types                                      |
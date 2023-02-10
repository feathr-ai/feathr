---
layout: default
title: Feathr Online Transformation
parent: How-to Guides
---

# Feathr Online Transformation

Feathr contains an online transformation service, as well as a Python package for further development and customization.

The detailed document about the online transformation service can be found at the project repository: [feathr-online-transformation](https://github.com/feathr-ai/feathr-online).

## Feathr Online Transformation Integration

In Feather project, a set of functions are provided to integrate with the online transformation service. The usage of these functions include following steps:
1. Define the data sources and the features as documented in ["Feature Definition"](../concepts/feature-definition.md)
2. Use `feathr.dsl_generator.gen_dsl` function to get the DSL script returned as a string, e.g.:
    ```
    dsl = feathr.dsl_generator.gen_dsl("pipeline_name", [list_of_output_features])
    ```
    The 1st parameter is the output pipeline name, and the 2nd parameter is a list of output features. Both Anchor and Derived features are supported.
3. Save the DSL script to a file, e.g. `pipeline.conf`:
    ```
    with open("pipeline.conf", "w") as f:
        f.write(dsl)
    ```
4. Start the service:
    ```
    /path/to/piper -p "pipeline.conf"
    ```
5. Or you can use the DSL script with your own service implementation based on the Online Transformation Python package. See the [Online Transformation Python Package](https://github.com/feathr-ai/feathr-online/tree/main/python) for more details.

6. Details about the deployment of the service is out of the scope of this document. Please refer to the [Feathr Helm Charts](https://github.com/feathr-ai/helm-charts) for K8S/AKS deployment, or [Feathr Online Transformation Project](https://github.com/feathr-ai/feathr-online) for Docker deployment and local test runs.

## Limitations

Due to the different nature of the online service and the offline batch processing, some Spark features are not supported in the online service:

* Aggregation functions [^1]
* Joining [^2]
* Window functions, so features that uses `WindowAggTransformation` are not supported.
* UDFs [^3]
* Higher-order functions that take other functions as the parameter
* `binary`, `decimal`, `time`, `timestamp`, `struct` data types. [^4]
* Some other Spark-specific functions

Most of the incompatibilities are due to the fact that the online service is designed to be process input row by row then return the result immediately, in this context the aggregation/windowing makes no sense.

The online service does support exploding and (limited) left joining, then some limited aggregation functions are supported. But they have different syntax and semantics from the Spark ones so cannot be translated from the feature definitions directly. For more information about the joining and aggregation functions in the online transformation service, please refer to the [Online Transformation Doc](https://github.com/feathr-ai/feathr-online).


[^1]: The online service has its own aggregation functions, but it's different from the Spark semantics.

[^2]: The online service has its own limited joining implementation, but with different syntax.

[^3]: UDFs are not supported in the online service because Feathr UDFs are the mappings between DataFrames, which is Spark/Pandas specific. The online transformation has its own UDF support, details can be found at the [Online Transformation Python Package Doc](https://github.com/feathr-ai/feathr-online/tree/main/python).

[^4]: The `time` and `timestamp` types are not supported, use `datetime` instead, the `binary` type is not supported, use `string` instead. The `struct` type is not supported, use `map` instead when possible. There is no `decimal` type in the online service, use `double` instead.
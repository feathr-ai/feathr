---
layout: default
title: Cloud Integration Test/CI Pipeline
parent: Developer Guides
---
# Cloud Integration Test/CI Pipeline

We use [GitHub Actions](https://github.com/feathr-ai/feathr/tree/main/.github/workflows) to do cloud integration test. Currently the integration test has 4 jobs:

- running `./gradlew test` to verify if the scala/spark related code has passed all the test
- running `flake8` to lint python scripts and make sure there are no obvious syntax errors
- running the built jar in databricks environment with end to end test to make sure it passed the end to end test
- running the built jar in Azure Synapse environment with end to end test to make sure it passed the end to end test

The above 4 jobs will ran in parallel, and if any one of them fails, the integration test will fail.

## Cloud Testing Pipelines

Since there are many cloud integration testing jobs that could be run in parallel, currently the workflow is like this:

- For each spark runtime (databricks or Azure Synapse), it will first compile the Feathr jar file
- CI pipeline will upload the jar to a location which is specific to this CI workflow. i.e. For the subsequent spark jobs, they will use the same jars in a shared cloud location (so that those spark jobs don't have to upload jars again); However the jar will be in a different location for different CI workflow (for example you have a new push for an PR, the CI pipeline will upload the jar into a different location)
- For each spark job, they will use a different "workspace folder" so that all the required configurations and cloud resources won't conflict. 
    ```python
    os.environ['SPARK_CONFIG__DATABRICKS__WORK_DIR'] = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), '_', str(now.microsecond)]) 
    os.environ['SPARK_CONFIG__AZURE_SYNAPSE__WORKSPACE_DIR'] = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/feathr_github_ci','_', str(now.minute), '_', str(now.second) ,'_', str(now.microsecond)]) 
    ```
- They will also use different output paths to make sure there's no writing conflict for the same output file.


    ```python
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob_snowflake','_', str(now.minute), '_', str(now.second), ".avro"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/snowflake_output','_', str(now.minute), '_', str(now.second), ".avro"])
    ```  

## Optimizing Parallel Runs

Since Feathr is using cloud resources to do CI testing, we have those optimizations in place:

- set `pytest -n 4` to run 4 tests in parallel
- Use pre-exist spark pools to reduce the setup time. All the spark jobs are running on "instance pools" that has certain idle compute instances so the setup time will be short. For example, for Databricks:

`"instance_pool_id":"0403-214809-inlet434-pool-l9dj3kwz"`


## More on GitHub Actions

The integration test will be triggered once there are push or for new pull requests.

The integration test will also skip the files in the `/docs` folder and for files that are ending with `md`.

For more info on GitHub actions, refer to the documentation [here](https://docs.github.com/en/actions/using-workflows/events-that-trigger-workflows).

```yaml
push:
  branches: [main]
  paths-ignore:
    - "docs/**"
    - "**/README.md"
pull_request:
  branches: [main]
  paths-ignore:
    - "docs/**"
    - "**/README.md"
```
from copy import deepcopy
import os
import json
from tempfile import NamedTemporaryFile
from typing import Dict
import yaml

from feathr.utils.platform import is_databricks


DEFAULT_FEATHR_CONFIG = {
    "api_version": 1,
    "project_config": {},  # "project_name"
    "feature_registry": {},  # "api_endpoint"
    "spark_config": {
        # "spark_cluster". Currently support 'azure_synapse', 'databricks', and 'local'
        "spark_result_output_parts": "1",
    },
    "offline_store": {
        "adls": {"adls_enabled": "true"},
        "wasb": {"wasb_enabled": "true"},
    },
    "online_store": {
        "redis": {
            # "host"
            "port": "6380",
            "ssl_enabled": "true",
        }
    }
}


# New databricks job cluster config
DEFAULT_DATABRICKS_CLUSTER_CONFIG = {
    "spark_version": "11.2.x-scala2.12",
    "node_type_id": "Standard_D3_v2",
    "num_workers": 2,
    "spark_conf": {
        "FEATHR_FILL_IN": "FEATHR_FILL_IN",
        # Exclude conflicting packages if use feathr <= v0.8.0:
        "spark.jars.excludes": "commons-logging:commons-logging,org.slf4j:slf4j-api,com.google.protobuf:protobuf-java,javax.xml.bind:jaxb-api",
    },
}


# New Azure Synapse spark pool config
DEFAULT_AZURE_SYNAPSE_SPARK_POOL_CONFIG = {
    "executor_size": "Small",
    "executor_num": 2,
}


def generate_config(
    resource_prefix: str,
    project_name: str,
    spark_cluster: str,
    cluster_name: str = None,
    databricks_url: str = None,
    output_filepath: str = None,
    use_env_vars: bool = True,
) -> str:
    """Generate a feathr config yaml file. Note, if environment variables are set, they will be used instead of the
    provided arguments.

    Some credential variables are intentionally not included in the argument and the outut config file
    to avoid leaking secrets. E.g. DATABRICKS_WORKSPACE_TOKEN_VALUE and REDIS_PASSWORD.
    Those values should be passed via the environment variables regardless of the `use_env_vars` flag.

    Note:
        This utility function assumes Azure resources are deployed using the Azure Resource Manager (ARM) template,
        and infers resource names based on the given `resource_prefix`. If you deploy resources manually, you may need
        to create the config file manually.

    Args:
        resource_prefix: Resource name prefix.
        project_name: Project name.
        spark_cluster: Spark cluster to use. Either 'local', 'databricks', or 'azure_synapse'.
        cluster_name (optional): Synapse spark pool name or Databricks cluster id if applicable.
            If not provided, a new (job) cluster will be created and used.
        databricks_url (optional): Databricks workspace url if applicable.
        output_filepath (optional): Output filepath.
        use_env_vars (optional): Whether to use environment variables if they are set.

    Returns:
        str: Generated config file path. output_filepath if provided. Otherwise, NamedTemporaryFile path.
    """
    if use_env_vars:
        spark_cluster = os.getenv("SPARK_CONFIG__SPARK_CLUSTER", spark_cluster)

    config = deepcopy(DEFAULT_FEATHR_CONFIG)
    config["project_config"]["project_name"] = project_name
    config["feature_registry"]["api_endpoint"] = f"https://{resource_prefix}webapp.azurewebsites.net/api/v1"
    config["spark_config"]["spark_cluster"] = spark_cluster
    config["online_store"]["redis"]["host"] = f"{resource_prefix}redis.redis.cache.windows.net"

    # Set platform specific configurations
    if spark_cluster == "local":
        _set_local_spark_config()
    elif spark_cluster == "azure_synapse":
        _set_azure_synapse_config(
            config=config,
            resource_prefix=resource_prefix,
            project_name=project_name,
            cluster_name=cluster_name,
            use_env_vars=use_env_vars,
        )
    elif spark_cluster == "databricks":
        _set_databricks_config(
            config=config,
            project_name=project_name,
            workspace_url=databricks_url,
            cluster_name=cluster_name,
            use_env_vars=use_env_vars,
        )

    if not output_filepath:
        output_filepath = NamedTemporaryFile(mode="w", delete=False).name

    with open(output_filepath, "w") as f:
        yaml.dump(config, f, default_flow_style=False)

    return output_filepath


def _set_local_spark_config():
    """Set environment variables for local spark cluster."""
    os.environ["SPARK_LOCAL_IP"] = os.getenv(
        "SPARK_LOCAL_IP",
        "127.0.0.1",
    )


def _set_azure_synapse_config(
    config: Dict,
    resource_prefix: str,
    project_name: str,
    cluster_name: str = None,
    use_env_vars: bool = True,
):
    """Set environment variables for Azure Synapse spark cluster.
    One may need to set ADLS_KEY"""

    dev_url = f"https://{resource_prefix}syws.dev.azuresynapse.net"
    workspace_dir = f"abfss://{resource_prefix}fs@{resource_prefix}dls.dfs.core.windows.net/{project_name}"

    if use_env_vars:
        dev_url = os.getenv("SPARK_CONFIG__AZURE_SYNAPSE__DEV_URL", dev_url)
        cluster_name = os.getenv("SPARK_CONFIG__AZURE_SYNAPSE__POOL_NAME", cluster_name)
        workspace_dir = os.getenv("SPARK_CONFIG__AZURE_SYNAPSE__WORKSPACE_DIR", workspace_dir)

    if not cluster_name:
        raise ValueError("Azure Synapse spark pool name is not provided.")

    config["spark_config"]["azure_synapse"] = {
        "dev_url": dev_url,
        "pool_name": cluster_name,
        "workspace_dir": workspace_dir,
        **DEFAULT_AZURE_SYNAPSE_SPARK_POOL_CONFIG,
    }


def _set_databricks_config(
    config: Dict,
    project_name: str,
    workspace_url: str,
    cluster_name: str = None,
    use_env_vars: bool = True,
):
    if is_databricks():
        # If this functions is being called in Databricks, we may use the context to override the provided arguments.
        ctx = dbutils.notebook.entry_point.getDbutils().notebook().getContext()
        workspace_url = "https://" + ctx.tags().get("browserHostName").get()
        workspace_token = ctx.apiToken().get()
    else:
        workspace_token = os.getenv("DATABRICKS_WORKSPACE_TOKEN_VALUE", None)

    work_dir = f"dbfs:/{project_name}"
    databricks_config = {
        "run_name": "FEATHR_FILL_IN",
        "libraries": [{"jar": "FEATHR_FILL_IN"}],
        "spark_jar_task": {
            "main_class_name": "FEATHR_FILL_IN",
            "parameters": ["FEATHR_FILL_IN"],
        },
    }
    if cluster_name is None:
        databricks_config["new_cluster"] = DEFAULT_DATABRICKS_CLUSTER_CONFIG
    else:
        databricks_config["existing_cluster_id"] = cluster_name
    config_template = json.dumps(databricks_config)

    if use_env_vars:
        work_dir = os.getenv("SPARK_CONFIG__DATABRICKS__WORK_DIR", work_dir)
        workspace_url = os.getenv("SPARK_CONFIG__DATABRICKS__WORKSPACE_INSTANCE_URL", workspace_url)
        workspace_token = os.getenv("DATABRICKS_WORKSPACE_TOKEN_VALUE", workspace_token)
        config_template = os.getenv("SPARK_CONFIG__DATABRICKS__CONFIG_TEMPLATE", config_template)

    if not workspace_url:
        raise ValueError("Databricks workspace url is not provided.")

    if not workspace_token:
        raise ValueError("Databricks workspace token is not provided.")

    os.environ["DATABRICKS_WORKSPACE_TOKEN_VALUE"] = workspace_token
    config["spark_config"]["databricks"] = {
        "work_dir": work_dir,
        "workspace_instance_url": workspace_url,
        "config_template": config_template,
    }

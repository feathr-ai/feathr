from tempfile import NamedTemporaryFile


FEATHR_CONFIG_TEMPLATE = """
api_version: 1

project_config:
  project_name: {project_name}

feature_registry:
  api_endpoint: 'https://{resource_prefix}webapp.azurewebsites.net/api/v1'

spark_config:
  # Currently support: 'azure_synapse', 'databricks', and 'local'
  spark_cluster: {spark_cluster}
  spark_result_output_parts: '1'

offline_store:
  wasb:
    wasb_enabled: true

online_store:
  # You can skip this part if you don't have Redis and skip materialization later in this notebook.
  redis:
    host: '{resource_prefix}redis.redis.cache.windows.net'
    port: 6380
    ssl_enabled: true
"""


def generate_config(
    resource_prefix: str,
    project_name: str,
    spark_cluster: str,
    output_filepath: str = None,
) -> str:
    """Generate a feathr config yaml file

    Args:
        resource_prefix: Resource name prefix.
        project_name: Project name.
        spark_cluster: Spark cluster to use. Either 'local', 'databricks', or 'azure_synapse'.
        output_filepath: Output filepath.

    Returns:
        str: Generated config file path. output_filepath if provided. Otherwise, NamedTemporaryFile path.
    """

    conf_str = FEATHR_CONFIG_TEMPLATE.format(
        resource_prefix=resource_prefix,
        project_name=project_name,
        spark_cluster=spark_cluster,
    )

    if not output_filepath:
        output_filepath = NamedTemporaryFile(mode="w", delete=False).name

    with open(output_filepath, "w") as conf_file:
        conf_file.write(conf_str)

    return output_filepath

from copy import deepcopy
import os
from pathlib import Path
import yaml

import pytest

from feathr import FeathrClient
from feathr.utils.config import generate_config


@pytest.mark.parametrize(
    "output_filepath", [None, "config.yml"],
)
def test__generate_config__output_filepath(
    output_filepath: str,
    tmp_path: Path,
):
    resource_prefix = "test_prefix"
    project_name = "test_project"
    spark_cluster = "local"

    # Use tmp_path so that the test files get cleaned up after the tests
    if output_filepath:
        output_filepath = str(tmp_path / output_filepath)

    if "DATABRICKS_WORKSPACE_TOKEN_VALUE" not in os.environ:
        os.environ["DATABRICKS_WORKSPACE_TOKEN_VALUE"] = "test_token"

    config_filepath = generate_config(
        resource_prefix=resource_prefix,
        project_name=project_name,
        spark_cluster=spark_cluster,
        output_filepath=output_filepath,
        use_env_vars=False,
    )

    # Assert if the config file was generated in the specified output path.
    if output_filepath:
        assert output_filepath == config_filepath

    # Assert the generated config string is correct.
    with open(config_filepath, "r") as f:
        config = yaml.safe_load(f)

    assert config["project_config"]["project_name"] == project_name
    assert config["feature_registry"]["api_endpoint"] == f"https://{resource_prefix}webapp.azurewebsites.net/api/v1"
    assert config["spark_config"]["spark_cluster"] == spark_cluster
    assert config["online_store"]["redis"]["host"] == f"{resource_prefix}redis.redis.cache.windows.net"


@pytest.mark.parametrize(
    "spark_cluster,cluster_name,databricks_url",
    [
        ("local", None, None),
        ("databricks", None, "https://test_url"),
        ("azure_synapse", "some_spark_pool", None),
    ]
)
def test__generate_config__spark_cluster(
    spark_cluster: str,
    cluster_name: str,
    databricks_url: str,
):
    """Test if spark cluster specific configs are generated without errors.
    TODO - For now, this test doesn't check if the config values are correct.
    """

    if "DATABRICKS_WORKSPACE_TOKEN_VALUE" not in os.environ:
        os.environ["DATABRICKS_WORKSPACE_TOKEN_VALUE"] = "test_token"

    generate_config(
        resource_prefix="test_prefix",
        project_name="test_project",
        spark_cluster=spark_cluster,
        cluster_name=cluster_name,
        databricks_url=databricks_url,
        use_env_vars=False,
    )


@pytest.mark.parametrize(
    "spark_cluster,cluster_name,databricks_url",
    [
        ("databricks", "some_cluster_id", None),
        ("azure_synapse", None, "https://test_url"),
    ]
)
def test__generate_config__exceptions(
    spark_cluster: str,
    cluster_name: str,
    databricks_url: str,
):
    """Test if exceptions are raised when databricks url and token are not provided."""

    if "DATABRICKS_WORKSPACE_TOKEN_VALUE" not in os.environ:
        os.environ["DATABRICKS_WORKSPACE_TOKEN_VALUE"] = "test_token"

    with pytest.raises(ValueError):
        generate_config(
            resource_prefix="test_prefix",
            project_name="test_project",
            spark_cluster=spark_cluster,
            cluster_name=cluster_name,
            databricks_url=databricks_url,
            use_env_vars=False,
        )

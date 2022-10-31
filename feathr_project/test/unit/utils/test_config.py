from pathlib import Path
from tempfile import NamedTemporaryFile

import pytest

from feathr.utils.config import FEATHR_CONFIG_TEMPLATE, generate_config


@pytest.fixture(scope="session")
def feathr_config_str() -> str:
    return FEATHR_CONFIG_TEMPLATE.format(
        resource_prefix="test_prefix",
        project_name="test_project",
        spark_cluster="local",
    )


@pytest.mark.parametrize(
    "output_filepath", [None, "config.yml"],
)
def test__generate_config(
    output_filepath: str,
    feathr_config_str: str,
    tmp_path: Path,
):
    # Use tmp_path so that the test files get cleaned up after the tests
    if output_filepath:
        output_filepath = str(tmp_path / output_filepath)

    config_filepath = generate_config(
        resource_prefix="test_prefix",
        project_name="test_project",
        spark_cluster="local",
        output_filepath=output_filepath,
    )

    # Assert if the config file was generated in the specified output path.
    if output_filepath:
        assert output_filepath == config_filepath

    # Assert the generated config string is correct.
    with open(config_filepath, "r") as f:
        assert feathr_config_str == f.read()

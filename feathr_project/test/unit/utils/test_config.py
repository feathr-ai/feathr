from pathlib import Path
from tempfile import NamedTemporaryFile

import pytest

from feathr.utils.config import FEATHR_CONFIG_TEMPLATE, generate_config


@pytest.mark.parametrize(
    "output_filepath", [None, NamedTemporaryFile().name],
)
def test__generate_config(output_filepath: str):

    config = FEATHR_CONFIG_TEMPLATE.format(
        resource_prefix="test_prefix",
        project_name="test_project",
        spark_cluster="local",
    )

    config_filepath = generate_config(
        resource_prefix="test_prefix",
        project_name="test_project",
        spark_cluster="local",
        output_filepath=output_filepath,
    )

    if output_filepath:
        assert output_filepath == config_filepath

    with open(config_filepath, "r") as f:
        assert config == f.read()

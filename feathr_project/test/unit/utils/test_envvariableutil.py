import os
from tempfile import NamedTemporaryFile

import pytest
from pytest_mock import MockerFixture

import feathr.utils._envvariableutil
from feathr.utils._envvariableutil import _EnvVaraibleUtil


TEST_CONFIG_KEY = "test__config__key"
TEST_CONFIG_ENV_VAL = "test_env_val"
TEST_CONFIG_FILE_VAL = "test_file_val"
TEST_CONFIG_FILE_CONTENT = f"""
test:
    config:
        key: '{TEST_CONFIG_FILE_VAL}'
"""


@pytest.mark.parametrize(
    "use_env_vars, env_value, expected_value",
    [
        (True, TEST_CONFIG_ENV_VAL, TEST_CONFIG_ENV_VAL),
        (True, None, TEST_CONFIG_FILE_VAL),
        (False, TEST_CONFIG_ENV_VAL, TEST_CONFIG_FILE_VAL),
    ]
)
def test__envvariableutil__get_environment_variable_with_default(
    mocker: MockerFixture,
    use_env_vars: bool,
    env_value: str,
    expected_value: str,
):
    """Test `get_environment_variable_with_default` method if it returns the correct value
    along with `use_env_vars` argument.
    """
    if env_value:
        mocker.patch.object(feathr.utils._envvariableutil.os, "environ", {TEST_CONFIG_KEY: env_value})

    f = NamedTemporaryFile(delete=True)
    f.write(TEST_CONFIG_FILE_CONTENT.encode())
    f.seek(0)
    env_var_util = _EnvVaraibleUtil(config_path=f.name, use_env_vars=use_env_vars)
    assert env_var_util.get_environment_variable_with_default(*TEST_CONFIG_KEY.split("__")) == expected_value


@pytest.mark.parametrize(
    "use_env_vars, env_value, expected_value",
    [
        (True, TEST_CONFIG_ENV_VAL, TEST_CONFIG_ENV_VAL),
        (True, None, None),
        (False, TEST_CONFIG_ENV_VAL, TEST_CONFIG_ENV_VAL),
    ]
)
def test__envvariableutil__get_environment_variable(
    mocker: MockerFixture,
    use_env_vars: bool,
    env_value: str,
    expected_value: str,
):
    """Test `get_environment_variable` method if it returns the environment variable regardless of `use_env_vars` argument.

    Args:
        mocker (MockerFixture): _description_
        use_env_vars (bool): _description_
        env_value (str): _description_
        expected_value (str): _description_
    """
    if env_value:
        mocker.patch.object(feathr.utils._envvariableutil.os, "environ", {TEST_CONFIG_KEY: env_value})

    f = NamedTemporaryFile(delete=True)
    f.write(TEST_CONFIG_FILE_CONTENT.encode())
    f.seek(0)
    env_var_util = _EnvVaraibleUtil(config_path=f.name, use_env_vars=use_env_vars)
    assert env_var_util.get_environment_variable(TEST_CONFIG_KEY) == expected_value

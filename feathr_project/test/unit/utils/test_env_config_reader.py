from tempfile import NamedTemporaryFile

import pytest
from pytest_mock import MockerFixture

import feathr.utils._env_config_reader
from feathr.utils._env_config_reader import EnvConfigReader


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
def test__envvariableutil__get(
    mocker: MockerFixture,
    use_env_vars: bool,
    env_value: str,
    expected_value: str,
):
    """Test `get` method if it returns the correct value
    along with `use_env_vars` argument.
    """
    if env_value:
        mocker.patch.object(feathr.utils._env_config_reader.os, "environ", {TEST_CONFIG_KEY: env_value})

    f = NamedTemporaryFile(delete=True)
    f.write(TEST_CONFIG_FILE_CONTENT.encode())
    f.seek(0)
    env_config = EnvConfigReader(config_path=f.name, use_env_vars=use_env_vars)
    assert env_config.get(TEST_CONFIG_KEY) == expected_value


@pytest.mark.parametrize(
    "use_env_vars, env_value, expected_value",
    [
        (True, TEST_CONFIG_ENV_VAL, TEST_CONFIG_ENV_VAL),
        (True, None, None),
        (False, TEST_CONFIG_ENV_VAL, TEST_CONFIG_ENV_VAL),
    ]
)
def test__envvariableutil__get_from_env_or_akv(
    mocker: MockerFixture,
    use_env_vars: bool,
    env_value: str,
    expected_value: str,
):
    """Test `get_from_env_or_akv` method if it returns the environment variable regardless of `use_env_vars` argument.

    Args:
        mocker (MockerFixture): _description_
        use_env_vars (bool): _description_
        env_value (str): _description_
        expected_value (str): _description_
    """
    if env_value:
        mocker.patch.object(feathr.utils._env_config_reader.os, "environ", {TEST_CONFIG_KEY: env_value})

    f = NamedTemporaryFile(delete=True)
    f.write(TEST_CONFIG_FILE_CONTENT.encode())
    f.seek(0)
    env_config = EnvConfigReader(config_path=f.name, use_env_vars=use_env_vars)
    assert env_config.get_from_env_or_akv(TEST_CONFIG_KEY) == expected_value

import os
import yaml
from loguru import logger
from feathr.akv_client import AzureKeyVaultClient


class _EnvVaraibleUtil(object):
    def __init__(self, config_path):
        self.config_path = config_path
        self.akv_client = None

    def get_environment_variable_with_default(self, *args):
        """Gets the environment variable for the variable key.
        Args:
            *args: list of keys in feathr_config.yaml file
        Return:
            A environment variable for the variable key. If it's not set in the environment, then a default is retrieved
            from the feathr_config.yaml file with the same config key.
            """

        # if envs exist, just return the existing env variable without reading the file
        env_keyword = "__".join(args)
        upper_env_keyword = env_keyword.upper()
        # make it work for lower case and upper case.
        env_variable = os.environ.get(
            env_keyword, os.environ.get(upper_env_keyword))
        if env_variable:
            return env_variable

        # if the config path doesn't exist, just return
        try:
            assert os.path.exists(os.path.abspath(self.config_path))
        except:
            logger.info("{} is not set and configuration file {} cannot be found. One of those should be set." , env_keyword, self.config_path)

        with open(os.path.abspath(self.config_path), 'r') as stream:
            try:
                yaml_config = yaml.safe_load(stream)
                # concat all layers
                # check in environment variable
                yaml_layer = yaml_config

                # resolve one layer after another
                for arg in args:
                    yaml_layer = yaml_layer[arg]
                return yaml_layer
            except KeyError as exc:
                logger.info(exc)
                return ""
            except yaml.YAMLError as exc:
                logger.info(exc)

    @staticmethod
    def get_environment_variable(variable_key):
        """Gets the environment variable for the variable key.

        Args:
            variable_key: environment variable key that is used to retrieve the environment variable
        Return:
            A environment variable for the variable key.
        Raises:
            ValueError: If the environment variable is not set for this key, an exception is thrown.
            """
        password = os.environ.get(variable_key)
        if not password:
            logger.info(variable_key +
                        ' is not set in the environment variables, fetching the value from Key Vault')
            akv_name = os.environ.get("KEY_VAULT_NAME")
            if akv_name:
                akv_client = AzureKeyVaultClient(akv_name)
                password = akv_client.get_akv_secret(variable_key)
        return password


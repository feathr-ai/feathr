import os
import yaml
from loguru import logger
from feathr.secrets.akv_client import AzureKeyVaultClient
from azure.core.exceptions import ResourceNotFoundError

class _EnvVaraibleUtil(object):
    def __init__(self, config_path):
        self.config_path = config_path
        # Set to none first to avoid invalid reference
        self.akv_name = None
        self.akv_name = self.get_environment_variable_with_default( 'secrets', 'azure_key_vault', 'name')
        self.akv_client = AzureKeyVaultClient(self.akv_name) if self.akv_name else None

    def get_environment_variable_with_default(self, *args):
        """Gets the environment variable for the variable key.
        Args:
            *args: list of keys in feathr_config.yaml file
        Return:
            A environment variable for the variable key. It will retrieve the value of the environment variables in the following order:
            If the key is set in the environment variable, Feathr will use the value of that environment variable
            If it's not set in the environment, then a default is retrieved from the feathr_config.yaml file with the same config key.
            If it's not available in the feathr_config.yaml file, Feathr will try to retrieve the value from key vault
            If not found, an empty string will be returned with a warning error message.
            """

        # if envs exist, just return the existing env variable without reading the file
        env_keyword = "__".join(args)
        upper_env_keyword = env_keyword.upper()
        # make it work for lower case and upper case.
        env_variable = os.environ.get(
            env_keyword, os.environ.get(upper_env_keyword))

        # If the key is set in the environment variable, Feathr will use the value of that environment variable
        if env_variable:
            return env_variable

        # If it's not set in the environment, then a default is retrieved from the feathr_config.yaml file with the same config key.
        if os.path.exists(os.path.abspath(self.config_path)):
            with open(os.path.abspath(self.config_path), 'r') as stream:
                try:
                    yaml_config = yaml.safe_load(stream)
                    # concat all layers and  check in environment variable
                    yaml_layer = yaml_config

                    # resolve one layer after another
                    for arg in args:
                        yaml_layer = yaml_layer[arg]
                    return yaml_layer
                except KeyError as exc:
                    logger.info("{} not found in the config file.", env_keyword)
                except yaml.YAMLError as exc:
                    logger.warning(exc)
        
        # If it's not available in the feathr_config.yaml file, Feathr will try to retrieve the value from key vault
        if self.akv_name:
            try:
                return  self.akv_client.get_feathr_akv_secret(env_keyword) 
            except ResourceNotFoundError:
                # print out warning message if cannot find the env variable in all the resources
                logger.warning('Environment variable {} not found in environment variable, default YAML config file, or key vault service.', env_keyword)
                return None

    def get_environment_variable(self, variable_key):
        """Gets the environment variable for the variable key.
        

        Args:
            variable_key: environment variable key that is used to retrieve the environment variable
        Return:
            A environment variable for the variable key. It will retrieve the value of the environment variables in the following order:
            If the key is set in the environment variable, Feathr will use the value of that environment variable
            If it's not available in the environment variable file, Feathr will try to retrieve the value from key vault
            If not found, an empty string will be returned with a warning error message.
            """
        env_var_value = os.environ.get(variable_key)

        if env_var_value:
            return env_var_value

        # If it's not available in the environment variable file, Feathr will try to retrieve the value from key vault
        logger.info(variable_key + ' is not set in the environment variables.')
        
        if self.akv_name:
            try:
                return self.akv_client.get_feathr_akv_secret(variable_key)
            except ResourceNotFoundError:
                # print out warning message if cannot find the env variable in all the resources
                logger.warning('Environment variable {} not found in environment variable or key vault service.', variable_key)
                return None
    
import os
import yaml
from loguru import logger


class _EnvVaraibleUtil(object):
    """
    Getter for environment variables or configs from YAML file(currently feathr_config.yaml).
    """
    @staticmethod
    def get_from_config(config_key):
        """Gets config values from the feature_store.yaml file.

        Args:
            config_key: config key that is used to retrieve the config value from the yaml file
        Return:
            A config value. For example, a string, a numeric or a boolean.
            """
        with open(os.path.abspath('feathr_config.yaml'), 'r') as stream:
            try:
                return yaml.safe_load(stream)['resource']['azure'][config_key]
            except yaml.YAMLError as exc:
                logger.error(exc)

    @staticmethod
    def get_environment_variable_with_default(first_layer: str, second_layer: str = None , third_layer: str = None):
        """Gets the environment variable for the variable key.

        Args:
            resource_group: resource group in the config yaml file, for example, azure or aws.
            variable_key: environment variable key that is used to retrieve the environment variable
        Return:
            A environment variable for the variable key. If it's not set in the environment, then a default is retrieved
            from the feathr_config.yaml file with the same config key.
            """
        # read default from the file
        with open(os.path.abspath('feathr_config.yaml'), 'r') as stream:
            try:
                if third_layer:
                    default = yaml.safe_load(stream)[first_layer][second_layer][third_layer]
                    env_variable = os.environ.get(third_layer.lower(), default)
                    return env_variable
                elif second_layer:
                    default = yaml.safe_load(stream)[first_layer][second_layer]
                    env_variable = os.environ.get(second_layer.lower(), default)
                    return env_variable
                elif first_layer:
                    default = yaml.safe_load(stream)[first_layer]
                    env_variable = os.environ.get(first_layer.lower(), default)
                    return env_variable
            except yaml.YAMLError as exc:
                print(exc)

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
            print(variable_key + ' is not set in the environment variables.')
        return password


if  __name__ == "__main__":

    res1 = _EnvVaraibleUtil.get_environment_variable_with_default('project_config', "project_name")
    res2 = _EnvVaraibleUtil.get_environment_variable_with_default('spark_launcher', "azure_synapse_config", "synapse_dev_url")
    print(res1, res2)
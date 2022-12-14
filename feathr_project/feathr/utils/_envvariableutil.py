import os
import yaml
from loguru import logger
from feathr.secrets.akv_client import AzureKeyVaultClient
from azure.core.exceptions import ResourceNotFoundError

class _EnvVaraibleUtil(object):
    """A utility class to read config variables.
    If use_env_vars set to False, `get_environment_variable_with_default` will not use os environment variables.
    Note, `get_environment_variable` still uses os environment variables.
    """

    def __init__(self, config_path: str, use_env_vars: bool = True):
        """Initialize the utility class.

        Args:
            config_path: Config file path.
            use_env_vars (optional): Whether to use os environment variables instead of config file. Defaults to True.
        """
        self.config_path = config_path
        self.use_env_vars = use_env_vars

        self.akv_name = (
            self._get_variable_from_env("secrets__azure_key_vault__name") or
            self._get_variable_from_file("secrets", "azure_key_vault", "name")
        )
        self.akv_client = AzureKeyVaultClient(self.akv_name) if self.akv_name else None

    def get_environment_variable_with_default(self, *args) -> str:
        """Gets the Feathr config variable for the given variable keys.

        Args:
            *args: list of keys in `config_path` yaml file.
                For example, to get `SPARK_CONFIG__DATABRICKS__WORKSPACE_INSTANCE_URL`,
                you may call `get_environment_variable_with_default("SPARK_CONFIG", "DATABRICKS", "WORKSPACE_INSTANCE_URL")`

        Returns:
            Feathr client's config variable. It will retrieve the value in the following order:
                - From the environment variable if `use_env_vars == True` and the key is set in the os environment variables.
                - From the config yaml file.
                - From the Azure Key Vault.
            If the key is not found in any of the above, it will return None.
        """
        variable_key = "__".join(args)

        env_var = (
            (self._get_variable_from_env(variable_key) if self.use_env_vars else None) or
            self._get_variable_from_file(*args) or
            (self._get_variable_from_akv(variable_key) if self.akv_name else None)
        )

        if env_var is None:
            logger.warning(f"Environment variable {variable_key} doesn't exist in environment variable, YAML config file, and key vault service.")

        return env_var

    def get_environment_variable(self, variable_key: str) -> str:
        """Gets the Feathr config variable for the given variable keys.

        Args:
            variable_key: environment variable key that is used to retrieve the environment variable

        Returns:
            Feathr client's config variable. It will retrieve the value in the following order:
                - From the environment variable if the key is set in the os environment variables.
                - From the Azure Key Vault.
            If the key is not found in any of the above, it will return None.
        """
        env_var = (
            self._get_variable_from_env(variable_key) or
            (self._get_variable_from_akv(variable_key) if self.akv_name else None)
        )

        if env_var is None:
            logger.warning(f"Environment variable {variable_key} doesn't exist in environment variable, YAML config file, and key vault service.")

        return env_var

    def _get_variable_from_env(self, variable_key: str) -> str:
        # make it work for lower case and upper case.
        env_variable = os.environ.get(variable_key, os.environ.get(variable_key.upper()))

        # If the key is set in the environment variable, Feathr will use the value of that environment variable
        # If it's not available in the environment variable file, Feathr will try to retrieve the value from key vault
        if env_variable:
            return env_variable
        else:
            logger.info(f"{variable_key} is not set in the environment variables.")

        return None

    def _get_variable_from_akv(self, variable_key: str) -> str:
        try:
            return self.akv_client.get_feathr_akv_secret(variable_key)
        except ResourceNotFoundError:
            logger.warning(f"Resource {self.akv_name} not found")

        return None

    def _get_variable_from_file(self, *args) -> str:
        if os.path.exists(os.path.abspath(self.config_path)):
            with open(os.path.abspath(self.config_path), "r") as stream:
                try:
                    yaml_config = yaml.safe_load(stream)
                    # concat all layers and  check in environment variable
                    yaml_layer = yaml_config

                    # resolve one layer after another
                    for arg in args:
                        yaml_layer = yaml_layer[arg]
                    return yaml_layer
                except KeyError as exc:
                    logger.info(f"{': '.join(args)} not found in the config file.")
                except yaml.YAMLError as exc:
                    logger.warning(exc)

        return None

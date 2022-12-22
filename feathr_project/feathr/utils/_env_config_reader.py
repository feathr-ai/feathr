import os
from pathlib import Path
import yaml

from loguru import logger

from azure.core.exceptions import ResourceNotFoundError
from feathr.secrets.akv_client import AzureKeyVaultClient

class EnvConfigReader(object):
    """A utility class to read Feathr environment variables either from os environment variables,
    the config yaml file or Azure Key Vault.
    If a key is set in the environment variable, ConfigReader will return the value of that environment variable
    unless use_env_vars set to False.
    """
    akv_name: str = None      # Azure Key Vault name to use for retrieving config values.
    yaml_config: dict = None  # YAML config file content.

    def __init__(self, config_path: str, use_env_vars: bool = True):
        """Initialize the utility class.

        Args:
            config_path: Config file path.
            use_env_vars (optional): Whether to use os environment variables instead of config file. Defaults to True.
        """
        if config_path is not None:
            config_path = Path(config_path)
            if config_path.is_file():
                try:
                    self.yaml_config = yaml.safe_load(config_path.read_text())
                except yaml.YAMLError as e:
                    logger.warning(e)

        self.use_env_vars = use_env_vars

        self.akv_name = self.get("secrets__azure_key_vault__name")
        self.akv_client = AzureKeyVaultClient(self.akv_name) if self.akv_name else None

    def get(self, key: str, default: str = None) -> str:
        """Gets the Feathr config variable for the given key.
        It will retrieve the value in the following order:
            - From the environment variable if `use_env_vars == True` and the key is set in the os environment variables.
            - From the config yaml file if the key exists.
            - From the Azure Key Vault.
        If the key is not found in any of the above, it will return `default`.

        Args:
            key: Config variable name. For example, `SPARK_CONFIG__DATABRICKS__WORKSPACE_INSTANCE_URL`
            default (optional): Default value to return if the key is not found. Defaults to None.

        Returns:
            Feathr client's config value.
        """
        conf_var = (
            (self._get_variable_from_env(key) if self.use_env_vars else None) or
            (self._get_variable_from_file(key) if self.yaml_config else None) or
            (self._get_variable_from_akv(key) if self.akv_name else None) or
            default
        )

        return conf_var

    def get_from_env_or_akv(self, key: str) -> str:
        """Gets the Feathr config variable for the given key. This function ignores `use_env_vars` attribute and force to
        look up environment variables or Azure Key Vault.
        It will retrieve the value in the following order:
            - From the environment variable if the key is set in the os environment variables.
            - From the Azure Key Vault.
        If the key is not found in any of the above, it will return None.

        Args:
            key: Config variable name. For example, `ADLS_ACCOUNT`

        Returns:
            Feathr client's config value.
        """
        conf_var = (
            self._get_variable_from_env(key) or
            (self._get_variable_from_akv(key) if self.akv_name else None)
        )

        return conf_var

    def _get_variable_from_env(self, key: str) -> str:
        # make it work for lower case and upper case.
        conf_var = os.environ.get(key.lower(), os.environ.get(key.upper()))

        if conf_var is None:
            logger.info(f"Config {key} is not set in the environment variables.")

        return conf_var

    def _get_variable_from_akv(self, key: str) -> str:
        try:
            # Azure Key Vault object name is case in-sensitive.
            # https://learn.microsoft.com/en-us/azure/key-vault/general/about-keys-secrets-certificates#vault-name-and-object-name
            return self.akv_client.get_feathr_akv_secret(key)
        except ResourceNotFoundError:
            logger.warning(f"Resource {self.akv_name} not found")

        return None

    def _get_variable_from_file(self, key: str) -> str:
        args = key.split("__")
        try:
            conf_var = self.yaml_config
            for arg in args:
                if conf_var is None:
                    break
                # make it work for lower case and upper case.
                conf_var = conf_var.get(arg.lower(), conf_var.get(arg.upper()))

            if conf_var is None:
                logger.info(f"Config {key} is not found in the config file.")

            return conf_var
        except Exception as e:
            logger.warning(e)

        return None

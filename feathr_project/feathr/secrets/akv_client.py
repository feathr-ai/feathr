from azure.keyvault.secrets import SecretClient
from loguru import logger
from azure.core.exceptions import ResourceNotFoundError
from feathr.secrets.abc import FeathrSecretsManagementClient


class AzureKeyVaultClient(FeathrSecretsManagementClient):
    def __init__(self, secret_namespace: str, secret_client: SecretClient = None):
        if isinstance(secret_client, SecretClient):
            self.secret_client = secret_client
        else:
            raise RuntimeError("You need to pass an azure.keyvault.secrets.SecretClient instance.")

    def get_feathr_secret(self, secret_name: str) -> str:
        """Get Feathr Secrets from Azure Key Vault. Note that this function will replace '_' in `secret_name` with '-' since Azure Key Vault doesn't support it

        Returns:
            str: returned secret from secret management service
        """
        try:
            # replace '_' with '-' since Azure Key Vault doesn't support it
            variable_replaced = secret_name.replace('_', '-')  # .upper()
            logger.info('Fetching the secret {} from Key Vault {}.',
                        variable_replaced, self.secret_client.vault_url)
            secret = self.secret_client.get_secret(variable_replaced)
            logger.info('Secret {} fetched from Key Vault {}.',
                        variable_replaced, self.secret_client.vault_url)
            return secret.value
        except ResourceNotFoundError:
            logger.error(
                f"Secret {secret_name} cannot be found in Key Vault {self.secret_client.vault_url}.")
            raise

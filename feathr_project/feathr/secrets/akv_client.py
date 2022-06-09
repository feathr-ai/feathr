from azure.keyvault.secrets import SecretClient
from azure.identity import DefaultAzureCredential
from loguru import logger
from azure.core.exceptions import ResourceNotFoundError

class AzureKeyVaultClient:
    def __init__(self, akv_name: str):
        self.akv_name = akv_name
        self.secret_client = None

    def get_akv_secret(self, secret_name: str):
        if self.secret_client is None:
            self.secret_client = SecretClient(
                vault_url = f"https://{self.akv_name}.vault.azure.net",
                credential=DefaultAzureCredential()
            )
        try:
            # replace '_' with '-' since Azure Key Vault doesn't support it
            variable_replaced = secret_name.replace('_','-')
            logger.info('Fetching the value {} from Key Vault {}.', variable_replaced, self.akv_name)
            secret = self.secret_client.get_secret(secret_name)
            return secret.value
        except ResourceNotFoundError as e:
            logger.error(f"Secret: {secret_name} cannot be found in Key Vault {self.akv_name}.")
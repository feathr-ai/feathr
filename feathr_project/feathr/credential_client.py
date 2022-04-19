from azure.keyvault.secrets import SecretClient
from azure.identity import DefaultAzureCredential

class credentialClient:
    def __init__(self, akv_name: str):
        self.__akv_name = akv_name
        self.__secret_client = None

    def get_secret_client(self) -> SecretClient:
        if self.__secret_client is None:
            self.__secret_client = SecretClient(
                vault_uri = f"https://{self.__akv_name}.vault.azure.net",
                credential=DefaultAzureCredential()
            )
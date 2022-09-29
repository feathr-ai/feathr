from abc import ABC, abstractmethod

from typing import Any, Dict, List, Optional, Tuple


class FeathrSecretsManagementClient(ABC):
    """This is the abstract class for all the secrets management service, which are used to store the credentials that Feathr might use.
    """

    @abstractmethod
    def __init__(self, secret_namespace: str, secret_client) -> None:
        """Initialize the FeathrSecretsManagementClient class.

        Args:
            secret_namespace (str): a namespace that Feathr needs to get secrets from. 
            For Azure Key Vault, it is something like the key vault name. 
            For AWS secrets manager, it is something like a secret name.

            secret_client: A client that will be used to retrieve Feathr secrets.
        """
        pass

    @abstractmethod
    def get_feathr_secret(self, secret_name: str) -> str:
        """Get Feathr Secrets from a certain secret management service, such as Azure Key Vault or AWS Secrets Manager. 

        Returns:
            str: returned secret from secret management service
        """
        pass

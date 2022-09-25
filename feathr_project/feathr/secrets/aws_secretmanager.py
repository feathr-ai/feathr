from loguru import logger
import json
from feathr.secrets.abc import SecretManagementClient

from botocore.exceptions import ClientError


class AWSSecretManagerClient(SecretManagementClient):
    def __init__(self, secret_name: str, secret_client):
        self.secret_name = secret_name
        if secret_client.meta.service_model.service_name != "secretsmanager":
            raise RuntimeError("secret_client need to be a secretsmanager")
        else:
            self.secret_client = secret_client

    def get_feathr_secret(self, secret_string: str):
        """Get Feathr Secrets from Azure Key Vault. Note that this function will replace '_' in `secret_string` with '-' since Azure Key Vault doesn't support it
        """
        if self.secret_client is None:
            raise RuntimeError(
                "You need to initialize a boto3 secretmanager class and pass it to Feathr")
        try:
            get_secret_value_response = self.secret_client.get_secret_value(
                SecretId=secret_string
            )
            if 'SecretString' in get_secret_value_response:
                secret = json.loads(get_secret_value_response['SecretString'])
                return secret[secret_string]
            else:
                raise RuntimeError("Only string format is supported")
        except KeyError as e:
            logger.error(
                f"Secret {secret_string} cannot be found in secretsmanager {self.secret_name}.")
            raise e
        except ClientError as e:
            raise e

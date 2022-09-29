from loguru import logger
import json
from feathr.secrets.abc import FeathrSecretsManagementClient
from aws_secretsmanager_caching.secret_cache import SecretCache


class AWSSecretManagerClient(FeathrSecretsManagementClient):
    def __init__(self, secret_namespace: str = None, secret_client: SecretCache = None):
        self.secret_id = secret_namespace
        self.secret_client = secret_client
        # make sure secret_client is a SecretCache type
        if secret_client is not None and not isinstance(secret_client, SecretCache):
            raise RuntimeError(
                "You need to pass a aws_secretsmanager_caching.secret_cache.SecretCache instance. Please refer to https://docs.aws.amazon.com/secretsmanager/latest/userguide/retrieving-secrets_cache-python.html for more details.")

    def get_feathr_secret(self, secret_name: str):
        """Get Feathr Secrets from AWS Secrets manager. It's also recommended that the client passes a cache objects to reduce cost.
        See more details here: https://docs.aws.amazon.com/secretsmanager/latest/userguide/retrieving-secrets_cache-python.html
        """
        if self.secret_client is None:
            raise RuntimeError(
                "You need to pass a aws_secretsmanager_caching.secret_cache.SecretCache instance when initializing FeathrClient.")

        try:
            get_secret_value_response = self.secret_client.get_secret_string(
                self.secret_id)
            # result is in str format, so we need to load it as a dict
            secret = json.loads(get_secret_value_response)
            return secret[secret_name]
        except KeyError as e:
            logger.error(
                f"Secret {secret_name} cannot be found in secretsmanager {self.secret_id}.")
            raise e

from loguru import logger
import json
from feathr.secrets.abc import FeathrSecretsManagementClient
import botocore
from botocore.exceptions import ClientError
from aws_secretsmanager_caching.secret_cache import SecretCache


class AWSSecretManagerClient(FeathrSecretsManagementClient):
    def __init__(self, secret_namespace: str = None, secret_client: SecretCache = None):
        self.secret_id = secret_namespace
        self.secret_namespace = secret_namespace

        # make sure secret_client is a SecretCache type
        if not isinstance(secret_client, SecretCache):
            raise RuntimeError(
                "You need to pass a aws_secretsmanager_caching.secret_cache.SecretCache instance. Please refer to https://docs.aws.amazon.com/secretsmanager/latest/userguide/retrieving-secrets_cache-python.html for more details.")
        else:
            self.secret_client = secret_client

    def get_feathr_secret(self, secret_string: str):
        """Get Feathr Secrets from AWS Secrets manager. It's also recommended that the client passes a cache objects to reduce cost.
        See more details here: https://docs.aws.amazon.com/secretsmanager/latest/userguide/retrieving-secrets_cache-python.html
        """
        try:
            get_secret_value_response = self.secret_client.get_secret_string(self.secret_namespace)
            # result is in str format, so we need to load it as a dict
            secret = json.loads(get_secret_value_response)
            return secret[secret_string]
        except KeyError as e:
            logger.error(
                f"Secret {secret_string} cannot be found in secretsmanager {self.secret_id}.")
            raise e

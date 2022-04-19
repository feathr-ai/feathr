from feathr._envvariableutil import _EnvVaraibleUtil
import redis
from feathr.credential_client import credentialClient
from azure.core.exceptions import ResourceNotFoundError
from loguru import logger

AKV_NAME = "AKV_NAME"
REDIS_SECRET_NAME = "FEATHR-ONLINE-STORE-CONN"

client = credentialClient(akv_name=_EnvVaraibleUtil.get_environment_variable(AKV_NAME)).get_client()

def get_redis_client():
        secret = get_akv_secret(REDIS_SECRET_NAME)
        password = secret.split(',')[1].split("password=",1)[1]
        host = secret.split(',')[0].split(":")[0]
        port = secret.split(',')[0].split(":")[1]
        ssl_enabled = secret.split(',')[2].split("ssl=",1)[1]

        redis_clint = redis.Redis(
            host=host,
            port=port,
            password=password,
            ssl=ssl_enabled)
        logger.info('Redis connection is successful and completed.')
        return redis_clint

def get_akv_secret(secretName: str):
    try:
        secret = credentialClient(akv_name=_EnvVaraibleUtil.get_environment_variable(AKV_NAME)).get_client().get_secret(secretName)
        logger.debug(f"Secret: {secretName} is retrieved from Key Vault {client.akv_name}.")
        return secret.value
    except ResourceNotFoundError as e:
        logger.error(f"Secret: {secretName} cannot be found in Key Vault {client.akv_name}.")


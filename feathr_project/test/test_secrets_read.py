import os
from pathlib import Path
from azure.identity import DefaultAzureCredential
from feathr.client import FeathrClient
from azure.keyvault.secrets import SecretClient
from test_fixture import secret_test_setup
from feathr.constants import OUTPUT_FORMAT
import botocore
import botocore.session
from aws_secretsmanager_caching import SecretCache, SecretCacheConfig


def test_feathr_get_secrets_from_azure_key_vault():
    """
    Test if the program can read azure key vault secrets as expected
    """
    # TODO: need to test get_environment_variable() as well
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"

    secret_client = SecretClient(
        # hard code the CI key vault endpoint
        vault_url="https://feathrazuretest3-kv.vault.azure.net",
        credential=DefaultAzureCredential(
            exclude_cli_credential=False, exclude_interactive_browser_credential=False)
    )
    client: FeathrClient = secret_test_setup(os.path.join(
        test_workspace_dir, "feathr_config_secret_test_azure_key_vault.yaml"), secret_manager_client=secret_client)

    # `redis_host` should be read from secret management service since it's not available in the environment variable, and not in the config file, we expect we get it from azure key_vault
    assert client.redis_host is not None


# test parquet file read/write without an extension name
def test_feathr_get_secrets_from_aws_secret_manager():
    """
    Test if the program can read AWS secret manager as expected
    """
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"

    client = botocore.session.get_session().create_client(
        service_name='secretsmanager',
        region_name="us-east-1"
    )
    cache_config = SecretCacheConfig()
    secret_cache = SecretCache(config=cache_config, client=client)

    client: FeathrClient = secret_test_setup(os.path.join(
        test_workspace_dir, "feathr_config_secret_test_aws_secret_manager.yaml"), secret_manager_client=secret_cache)

    # `redis_host` should be read from secret management service since it's not available in the environment variable, and not in the config file, we expect we get it from azure key_vault
    assert client.redis_host is not None

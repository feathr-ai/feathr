import os
from starlette.config import Config

env_file = os.path.join("registry", "access_control", ".env")
config = Config(os.path.abspath(env_file))

def _get_config(key:str, default:str = "", config:Config = config):
    return os.environ.get(key) or config.get(key, default=default)

# API Settings
RBAC_API_BASE: str = _get_config("RBAC_API_BASE", default="/api/v1")

# Authentication
RBAC_API_CLIENT_ID: str = _get_config("RBAC_API_CLIENT_ID", default="db8dc4b0-202e-450c-b38d-7396ad9631a5")
RBAC_AAD_TENANT_ID: str = _get_config("RBAC_AAD_TENANT_ID", default="common")
RBAC_AAD_INSTANCE: str = _get_config("RBAC_AAD_INSTANCE", default="https://login.microsoftonline.com")
RBAC_API_AUDIENCE: str = _get_config("RBAC_API_AUDIENCE", default="db8dc4b0-202e-450c-b38d-7396ad9631a5")

# SQL Database
RBAC_CONNECTION_STR: str = _get_config("RBAC_CONNECTION_STR", default= "")

# Downstream API Endpoint
RBAC_REGISTRY_URL: str = _get_config("RBAC_REGISTRY_URL", default= "https://feathr-sql-registry.azurewebsites.net/api/v1")

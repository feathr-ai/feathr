import os
from starlette.config import Config

env_file = os.path.join("registry", "access_control", ".env")
config = Config(os.path.abspath(env_file))

# API Settings
API_BASE: str = config("API_BASE", default = "/api/v1")

# Authentication
API_CLIENT_ID: str = config(
    "API_CLIENT_ID", default="db8dc4b0-202e-450c-b38d-7396ad9631a5")
AAD_TENANT_ID: str = config(
    "AAD_TENANT_ID", default="common")
AAD_INSTANCE: str = config(
    "AAD_INSTANCE", default="https://login.microsoftonline.com")
API_AUDIENCE: str = config(
    "API_AUDIENCE", default="db8dc4b0-202e-450c-b38d-7396ad9631a5")

# SQL Database
CONNECTION_STR: str = config("CONNECTION_STR", default= "")

# Downstream API Endpoint
REGISTRY_URL: str = config("REGISTRY_URL", default= "https://feathr-sql-registry.azurewebsites.net/api/v1")

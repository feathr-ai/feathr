from starlette.config import Config


config = Config(".env")

API_PREFIX: str = "/api"
VERSION: str = "0.1.0"
PROJECT_NAME: str = "FastAPI with AAD Authentication"
DEBUG: bool = config("DEBUG", cast=bool, default=False)

# Authentication
API_CLIENT_ID: str = config("API_CLIENT_ID", default="e1475341-ff84-4b6e-a187-349e35551cff")
API_CLIENT_SECRET: str = config("API_CLIENT_SECRET", default="cMZ8Q~d.gI6A-j-EMFbbmz.xy6jXOxUKH4dLYdhH")
SWAGGER_UI_CLIENT_ID: str = config("SWAGGER_UI_CLIENT_ID", default=" e1335db3-a7eb-42b0-976c-82c9b5bdf0fb")
AAD_TENANT_ID: str = config("AAD_TENANT_ID", default="72f988bf-86f1-41af-91ab-2d7cd011db47")

AAD_INSTANCE: str = config("AAD_INSTANCE", default="https://login.microsoftonline.com")
API_AUDIENCE: str = config("API_AUDIENCE", default="db8dc4b0-202e-450c-b38d-7396ad9631a5")
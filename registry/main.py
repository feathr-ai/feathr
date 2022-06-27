import os
import uvicorn
from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware
from api import router as api_router

rp = "/api/v1"

# Need to be deleted
os.environ["API_BASE"] = "api/v1"
os.environ["CONNECTION_STR"] = "Server=tcp:feathrtestsql4.database.windows.net,1433;Initial Catalog=testsql;Persist Security Info=False;User ID=feathr@feathrtestsql4;Password=Password01!;MultipleActiveResultSets=False;Encrypt=True;TrustServerCertificate=False;Connection Timeout=30"
os.environ["AZURE_CLIENT_ID"] = "db8dc4b0-202e-450c-b38d-7396ad9631a5"
os.environ["RBAC_ENABLED"] = "True"

def get_application() -> FastAPI:
    application = FastAPI(
        title="access control",
        debug=True,
        version="0.1.0",
        swagger_ui_oauth2_redirect_url='/oauth2-redirect',
        swagger_ui_init_oauth={
            "usePkceWithAuthorizationCodeGrant": True,
            "clientId": "e1475341-ff84-4b6e-a187-349e35551cff",
            "scopes": [f'api://e1475341-ff84-4b6e-a187-349e35551cff/access_as_user']
        }
    )
    # Enables CORS
    application.add_middleware(CORSMiddleware,
                   allow_origins=["*"],
                   allow_credentials=True,
                   allow_methods=["*"],
                   allow_headers=["*"],
                   )

    application.include_router(prefix = rp, router = api_router)
    return application


app = get_application()


if __name__ == "__main__":
    uvicorn.run("main:app", host="localhost", port=8000, reload=True)
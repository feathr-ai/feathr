from pyexpat import features
from telnetlib import STATUS
from fastapi import APIRouter, FastAPI, Request, Response
from app.core.configs import *
from opencensus.ext.azure.log_exporter import AzureLogHandler
from fastapi.responses import PlainTextResponse
from app.core.error_code import ErrorCode
from app.core.feathr_api_exception import FeathrApiException
from app.routes import featurestore,projects
from pyapacheatlas.core import (AtlasException)

app = FastAPI()

if app_insights_connection_string:
    azure_handler = AzureLogHandler(connection_string=app_insights_connection_string)
    logger.addHandler(azure_handler)
else:
    logger.warning("APPLICATIONINSIGHTS_CONNECTION_STRING is not set - will NOT log to AppInsights!!")

logger.setLevel(log_level)
logger.info("starting %s", __file__)

api_router = APIRouter()

api_router.include_router(featurestore.router)
api_router.include_router(projects.router)

app.include_router(api_router,prefix = API_Version)


async def catch_exceptions_middleware(request: Request, call_next):
    try:
        return await call_next(request)
    except Exception as exc:
        '''
        Add logic here to assign http status code for different internal exception.
        '''
        if isinstance(exc,FeathrApiException):
            if exc.code == ErrorCode.invalid_code:
                http_response_code =403
            return PlainTextResponse("Feathr Api Error Code {}, Error Message:{}".format(exc.code,str(exc.message)), status_code= http_response_code)
        elif isinstance(exc,AtlasException):
            logger.error("Error retrieving feature: %s", exc.args[0])
            return PlainTextResponse(str(exc.message), status_code=500)
        else:
            logger.error("Error: %s", exc.args[0])
            return PlainTextResponse("Error: " + exc.args[0], status_code=500)
app.middleware('http')(catch_exceptions_middleware)

import string
from fastapi import HTTPException
from pyapacheatlas.core import (AtlasException)
from functools import wraps
from app.core.configs import *
from app.core.error_code import ErrorCode
from app.core.feathr_api_exception import FeathrApiException

'''
This file will keep common error handling by our own logic.
Make sure all exceptions raised here is registered as FeathrApiException.
'''

def verifyCode(code:string):
    if code != appServiceKey:
        raise FeathrApiException(code=ErrorCode.invalid_code, message="Getting 404 from backend,you are not allowed to access this resource,please examine your appServiceKey.")  
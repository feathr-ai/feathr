from app.core.error_code import ErrorCode


class FeathrApiException(Exception):
    def __init__(self,message:str,code:ErrorCode):
        self.code = code
        self.message = message


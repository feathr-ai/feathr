from feathr.api.app.core.error_code import ErrorCode

class FeathrApiException(Exception):
    def __init__(self,message:str,code:ErrorCode):
        self.code = code
        self.message = message


class FeatureNameValidationError(ValueError):
    """An exception for feature name validation.
       Feature names must consist of letters, number, or underscores,
       and cannot begin with a number.
       Periods are also disallowed, as some compute engines, such as Spark,
       will consider them as operators in feature name.
    """



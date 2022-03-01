import os


class _EnvSetterForTest(object):
    """
    Environment variables setter used for testing. 
    This file is added in .gitignore so all the crednetials will be stored locally.
    """
    @staticmethod
    def set_env_feathr_client():
        # os.environ['REDIS_PASSWORD'] = ''
        # os.environ['AZURE_CLIENT_ID'] = ''
        # os.environ['AZURE_TENANT_ID'] = ''
        # os.environ['AZURE_CLIENT_SECRET'] = ''
        # os.environ['ADLS_ACCOUNT'] = ''
        # os.environ['ADLS_KEY'] = ''
        # os.environ['BLOB_ACCOUNT'] = ''
        # os.environ['BLOB_KEY'] = ''
        pass
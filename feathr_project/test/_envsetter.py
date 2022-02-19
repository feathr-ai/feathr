import os


class _EnvSetterForTest(object):
    """
    Environment variables setter used for testing. It's safe to store it here since it's our testing cluster.
    """
    @staticmethod
    def set_env_feathr_client():
        os.environ['REDIS_PASSWORD'] = 'Li7Nn63iNB0x731VTnnz2Vr29WYJHx7JlAzCaH9lbHw='
        os.environ['AZURE_CLIENT_ID'] = "b40e49c0-75c7-4959-ad25-896118cd79e8"
        os.environ['AZURE_TENANT_ID'] = '72f988bf-86f1-41af-91ab-2d7cd011db47'
        os.environ['AZURE_CLIENT_SECRET'] = 'kAB5ps6yvo_f08n-4Av~.IDwHFL_xl_63I'

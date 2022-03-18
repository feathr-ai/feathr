from feathrcli.cli import init
from click.testing import CliRunner
import os
from feathr.client import FeathrClient


def test_configuration_loading():
    """
    Test CLI init() is working properly.
    """
    runner = CliRunner()
    with runner.isolated_filesystem():

        result = runner.invoke(init, [])

        assert result.exit_code == 0
        assert os.path.isdir("./feathr_user_workspace")

        client = FeathrClient(config_path="./feathr_user_workspace/feathr_config.yaml")
        
        # test the loading is correct even if we are not in that folder
        assert client._FEATHR_JOB_JAR_PATH is not None

        TEST_LOCATION = '/test_location'
        
        # since we test synapse and databricks runtime using the same set of configs, we need to make sure we set both variable so that pytest can pass for both runners
        os.environ['SPARK_CONFIG__AZURE_SYNAPSE__FEATHR_RUNTIME_LOCATION'] = TEST_LOCATION
        os.environ['SPARK_CONFIG__DATABRICKS__FEATHR_RUNTIME_LOCATION'] = TEST_LOCATION
        
        # this should not be error out as we will just give users prompt, though the config is not really here
        client = FeathrClient(config_path="./feathr_user_workspace/feathr_config.yaml")
        assert client._FEATHR_JOB_JAR_PATH == TEST_LOCATION
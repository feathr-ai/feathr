from feathrcli.cli import init
from click.testing import CliRunner
import os
import glob
from feathr.client import FeathrClient
from feathr.transformation import ExpressionTransformation
from feathr.feature_derivations import DerivedFeature
from feathr.dtype import BOOLEAN, FLOAT, FLOAT_VECTOR, ValueType
from feathr.typed_key import TypedKey
import pytest


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
        assert client._FEATHR_JOB_JAR_PATH == "https://azurefeathrstorage.blob.core.windows.net/public/feathr-assembly-0.1.0-SNAPSHOT.jar"
        
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
        assert client._FEATHR_JOB_JAR_PATH is not None
        
        # this should not be error out as we will just give users prompt, though the config is not really here
        client = FeathrClient(config_path="./feathr_config.yaml")
        assert client is not None
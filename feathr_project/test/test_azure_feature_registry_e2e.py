from feathrcli.cli import init
from click.testing import CliRunner
from feathr.client import FeathrClient
import os
import glob
import pandavro as pdx
import pandas as pd
import tempfile


def test_feathr_feature_register():
    """
    Test FeathrClient() can register features correctly.
    """
    runner = CliRunner()
    with runner.isolated_filesystem():
        runner.invoke(init, [])
        os.chdir('feathr_user_workspace')
        client = FeathrClient()
        client.register_features()


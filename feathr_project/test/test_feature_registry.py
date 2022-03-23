from feathrcli.cli import init
from click.testing import CliRunner
from feathr.client import FeathrClient
import os
import glob
from test_fixture import basic_test_setup


def test_feathr_register_features_e2e():
    runner = CliRunner()
    with runner.isolated_filesystem():
        runner.invoke(init, [])
        client = basic_test_setup(
            "./feathr_user_workspace/feathr_config.yaml")
        client.register_features()
        # in CI test, the project name is set by the CI pipeline so we don't know it here. Just get all the features to make sure it works
        all_features = client.list_registered_features()
        assert 'f_is_long_trip_distance' in all_features


def test_feathr_get_features_from_registry():
    """
    Test FeathrClient() sync features and get all the conf files from registry
    """
    runner = CliRunner()
    with runner.isolated_filesystem():
        result = runner.invoke(init, [])

        assert result.exit_code == 0
        assert os.path.isdir("./feathr_user_workspace")
        os.chdir('feathr_user_workspace')

        # Look for conf files, we shouldn't have any
        total_conf_files = glob.glob('*/*.conf', recursive=True)
        assert len(total_conf_files) == 0
        
        client = FeathrClient()
        # Sync workspace from registry, will get all conf files back
        client.get_features_from_registry("frame_getting_started")

        total_conf_files = glob.glob('*/*.conf', recursive=True)
        # we should have at least 3 conf files
        assert len(total_conf_files) == 3

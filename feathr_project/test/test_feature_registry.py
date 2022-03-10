from feathrcli.cli import init
from click.testing import CliRunner
from feathr.client import FeathrClient
import os
import glob


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

        total_conf_files = glob.glob('*/*.conf', recursive=True)
        # we should have at least the feature generation conf file
        assert len(total_conf_files) == 1

        # delete all the conf files
        for file in total_conf_files:
            os.remove(file)
        
        # Look for conf files, we shouldn't have any now
        total_conf_files = glob.glob('*/*.conf', recursive=True)
        # we should have at no conf files since we deleted them all
        assert len(total_conf_files) == 0
        
        client = FeathrClient()
        # Sync workspace from registry, will get all conf files back
        client.get_features_from_registry("frame_getting_started")

        total_conf_files = glob.glob('*/*.conf', recursive=True)
        # we should have at least 3 conf files again
        assert len(total_conf_files) == 3

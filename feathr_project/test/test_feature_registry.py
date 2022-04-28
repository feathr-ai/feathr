import glob
import os
import time
from pathlib import Path

import pytest
from click.testing import CliRunner
from feathr.client import FeathrClient
from feathrcli.cli import init

from test_fixture import basic_test_setup, registry_test_setup


def test_feathr_register_features_e2e():

    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    client = registry_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    client.register_features()
    # Allow purview to process a bit
    time.sleep(5)
    # in CI test, the project name is set by the CI pipeline so we read it here
    project_name = os.environ["PROJECT_CONFIG__PROJECT_NAME"]
    all_features = client.list_registered_features(project_name=project_name)
    assert 'f_is_long_trip_distance' in all_features # test regular ones
    assert 'f_trip_time_rounded' in all_features # make sure derived features are there
    assert 'f_location_avg_fare' in all_features # make sure aggregated features are there
    assert 'f_trip_time_rounded_plus' in all_features # make sure derived features are there 
    assert 'f_trip_time_distance' in all_features # make sure derived features are there  

@pytest.mark.skip(reason="Add back get_features is not supported in feature registry for now and needs further discussion")
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

import glob
import os
import time
from datetime import datetime, timedelta
from pathlib import Path

from test_fixture import registry_test_setup_append, registry_test_setup_partially

import pytest
from click.testing import CliRunner
from feathr import (FeatureAnchor, FeatureQuery, ObservationSettings, TypedKey,
                    ValueType)
from feathr._feature_registry import _FeatureRegistry
from feathr.client import FeathrClient
from feathr.feature_derivations import DerivedFeature
from feathrcli.cli import init
from numpy import equal

from test_fixture import basic_test_setup, registry_test_setup


def test_feathr_register_features_e2e():
    """
    This test will register features, get all the registered features, then query a set of already registered features.
    """

    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    client: FeathrClient = registry_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    
    # set output folder based on different runtime
    now = datetime.now()
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".parquet"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".parquet"])

    
    client.register_features()
    # Allow purview to process a bit
    time.sleep(5)
    # in CI test, the project name is set by the CI pipeline so we read it here
    all_features = client.list_registered_features(project_name=client.project_name)
    all_feature_names = [x['name'] for x in all_features]
    
    assert 'f_is_long_trip_distance' in all_feature_names # test regular ones
    assert 'f_trip_time_rounded' in all_feature_names # make sure derived features are there
    assert 'f_location_avg_fare' in all_feature_names # make sure aggregated features are there
    assert 'f_trip_time_rounded_plus' in all_feature_names # make sure derived features are there 
    assert 'f_trip_time_distance' in all_feature_names # make sure derived features are there  

    # Sync workspace from registry, will get all conf files back
    client.get_features_from_registry(client.project_name)

    feature_query = FeatureQuery(
        feature_list=["f_location_avg_fare", "f_trip_time_rounded", "f_is_long_trip_distance"], 
        key=TypedKey(key_column="DOLocationID",key_column_type=ValueType.INT32))
    settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04_with_index.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")
    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path)
    client.wait_job_to_finish(timeout_sec=900)

def test_feathr_register_features_partially():
    """
    This test will register full set of features into one project, then register another project in two partial registrations.
    The length of the return value of get_features_from_registry should be identical.
    """
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    client: FeathrClient = registry_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    client.register_features()
    time.sleep(30)
    full_registration = client.get_features_from_registry(client.project_name)

    client: FeathrClient = registry_test_setup_partially(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    new_project_name = client.project_name
    client.register_features()
    time.sleep(30)


    client: FeathrClient = registry_test_setup_append(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    client.project_name = new_project_name
    client.register_features()
    time.sleep(30)

    appended_registration = client.get_features_from_registry(client.project_name)

    # after a full registration, another registration should not affect the registered anchor features.
    assert len(full_registration.items())==len(appended_registration.items())
    
def test_get_feature_from_registry():
    registry = _FeatureRegistry("mock_project","mock_purview","mock_delimeter")
    derived_feature_with_multiple_inputs = {
            "guid": "derived_feature_with_multiple_input_anchors",
            "typeName": "feathr_derived_feature_v1",
            "attributes": {
                "input_derived_features": [],
                "input_anchor_features": [
                    {
                        "guid": "input_anchorA",
                        "typeName": "feathr_anchor_feature_v1",
                    },
                    {
                        "guid": "input_anchorB",
                        "typeName": "feathr_anchor_feature_v1",
                    }
                ]
            },
        }
    hierarchical_derived_feature = {
            "guid": "hierarchical_derived_feature",
            "typeName": "feathr_derived_feature_v1",
            "attributes": {
                "input_derived_features": [
                    {
                        "guid": "derived_feature_with_multiple_input_anchors",
                        "typeName": "feathr_derived_feature_v1",
                    }
                ],
                "input_anchor_features": [
                    {
                        "guid": "input_anchorC",
                        "typeName": "feathr_anchor_feature_v1",
                    }
                ],
            }
        }
    anchors = [
        {
            "guid": "input_anchorA",
            "typeName": "feathr_anchor_feature_v1",
        },
        {
            "guid": "input_anchorC",
            "typeName": "feathr_anchor_feature_v1",
        },
        {
            "guid": "input_anchorB",
            "typeName": "feathr_anchor_feature_v1",
        }]

    def entity_array_to_dict(arr):
        return {x['guid']:x for x in arr}

    inputs = registry.search_input_anchor_features(['derived_feature_with_multiple_input_anchors'],entity_array_to_dict(anchors+[derived_feature_with_multiple_inputs]))
    assert len(inputs)==2
    assert "input_anchorA" in inputs and "input_anchorB" in inputs

    inputs = registry.search_input_anchor_features(['hierarchical_derived_feature'],entity_array_to_dict(anchors+[derived_feature_with_multiple_inputs,hierarchical_derived_feature]))
    assert len(inputs)==3
    assert "input_anchorA" in inputs and "input_anchorB" in inputs and "input_anchorC" in inputs
    
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


    
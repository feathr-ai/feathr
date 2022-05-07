from datetime import datetime
import glob
import os
import time
from pathlib import Path
from feathr.anchor import FeatureAnchor
from feathr.dtype import INT32
from feathr.feature import Feature
from feathr.feature_derivations import DerivedFeature
from feathr.query_feature_list import FeatureQuery
from feathr.settings import ObservationSettings
from numpy import equal

import pytest
from click.testing import CliRunner
from feathr.client import FeathrClient
from feathrcli.cli import init
from feathr._feature_registry import _FeatureRegistry
from test_fixture import basic_test_setup, registry_test_setup

def test_registered_features_single_project_e2e():
    """Get registered features from registry and use them to build training datasets,
    as well as create derived feature on top of it."""
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    client = registry_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    # client.register_features()
    # Allow purview to process a bit
    # time.sleep(5)
    # in CI test, the project name is set by the CI pipeline so we read it here
    project_name = os.environ["PROJECT_CONFIG__PROJECT_NAME"]

    # Get features from registry, from multiple projects
    client = FeathrClient(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    (foo_features, foo_feature_keys) = client.get_registered_features(project_name)

    """
    "nyc_taxi.location_id" is the full name of the typed key
    It is defined and registered previously using the following:
    location_id = TypedKey(key_column="DOLocationID",
                            key_column_type=ValueType.INT32,
                            description="location id in NYC",
                            full_name="nyc_taxi.location_id")"""
    location_id = foo_feature_keys["nyc_taxi.location_id"]
    feature_query = FeatureQuery(
        feature_list=[
            foo_features['f_is_long_trip_distance'],   # feature imported from project foo
            "f_is_long_trip_distance"                  # feature defined in the current project and has not been registered to registry
        ],
        key=location_id)
    settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")

    now = datetime.now()
    # set output folder based on different runtime
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".avro"])

    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path)

    # Derive the registered feature
    f_trip_time_rounded_in_hour = DerivedFeature(name="f_trip_time_rounded_in_hour",
                                                 feature_type=INT32,
                                                 input_features=[foo_features["f_trip_time_rounded"]],
                                                 transform="f_trip_time_duration / 60")

    client.build_features(anchor_list=[], derived_feature_list=[f_trip_time_rounded_in_hour])
    client.register_features()

@pytest.mark.skip(reason="Create test for test driven development")
def test_registered_features_e2e():
    """Get registered features from registry and use them to build training datasets,
    as well as create derived feature on top of it."""
    test_workspace_dir = Path(
        __file__).parent.resolve() / "test_user_workspace"
    client = registry_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    client.register_features()
    # Allow purview to process a bit
    # time.sleep(5)
    # in CI test, the project name is set by the CI pipeline so we read it here
    project_foo = os.environ["PROJECT_CONFIG__PROJECT_NAME"]
    all_features = client.get_registered_features(project_name=project_foo)
    assert 'f_is_long_trip_distance' in all_features # test regular ones
    assert 'f_trip_time_rounded' in all_features # make sure derived features are there
    assert 'f_location_avg_fare' in all_features # make sure aggregated features are there
    assert 'f_trip_time_rounded_plus' in all_features # make sure derived features are there 
    assert 'f_trip_time_distance' in all_features # make sure derived features are there  
    # project_foo and project_bar should have the same features
    project_bar = os.environ["PROJECT_CONFIG__PROJECT_NAME"]+"_2"

    # Get features from registry, from multiple projects
    client = FeathrClient()
    (foo_features, foo_feature_keys) = client.get_registered_features(project_foo)
    assert len(foo_features)==5
    assert foo_features['f_is_long_trip_distance'] != None
    (bar_features, bar_feature_keys) = client.get_registered_features(project_bar)
    assert len(bar_features)==5
    assert bar_features['f_is_long_trip_distance'] != None

    """
    "nyc_taxi.location_id" is the full name of the typed key
    It is defined and registered previously using the following:
    location_id = TypedKey(key_column="DOLocationID",
                            key_column_type=ValueType.INT32,
                            description="location id in NYC",
                            full_name="nyc_taxi.location_id")"""
    location_id = foo_feature_keys["nyc_taxi.location_id"]
    feature_query = FeatureQuery(
        feature_list=[
            foo_features['f_is_long_trip_distance'],   # feature imported from project foo
            bar_features['f_is_long_trip_distance'],   # feature imported from project bar
            "f_is_long_trip_distance"                  # feature defined in the current project and has not been registered to registry
        ],
         key=location_id)
    settings = ObservationSettings(
        observation_path="wasbs://public@azurefeathrstorage.blob.core.windows.net/sample_data/green_tripdata_2020-04.csv",
        event_timestamp_column="lpep_dropoff_datetime",
        timestamp_format="yyyy-MM-dd HH:mm:ss")

    now = datetime.now()
    # set output folder based on different runtime
    if client.spark_runtime == 'databricks':
        output_path = ''.join(['dbfs:/feathrazure_cijob','_', str(now.minute), '_', str(now.second), ".avro"])
    else:
        output_path = ''.join(['abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/demo_data/output','_', str(now.minute), '_', str(now.second), ".avro"])

    client.get_offline_features(observation_settings=settings,
                                feature_query=feature_query,
                                output_path=output_path)

    # Derive the registered feature
    f_trip_time_rounded_in_hour = DerivedFeature(name="f_trip_time_rounded_in_hour",
                                         feature_type=INT32,
                                         input_features=[foo_features["f_trip_time_rounded"]],
                                         transform="f_trip_time_duration / 60")
 
    client.build_features(anchor_list=[], derived_feature_list=[f_trip_time_rounded_in_hour])
    client.register_features()
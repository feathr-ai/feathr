import os
from pathlib import Path
import pytest
from feathr import (BackfillTime, MaterializationSettings)
from feathr import RedisSink
from test_fixture import kafka_test_setup

@pytest.mark.skipif(os.environ.get('SPARK_CONFIG__SPARK_CLUSTER') != "azure_synapse",
                    reason="skip for databricks, as it cannot stop streaming job automatically for now.")
def test_feathr_kafa_streaming_features():
    """
    Test FeathrClient() materialize_features can ingest streaming feature correctly
    """
    test_workspace_dir = Path(__file__).parent.resolve() / "test_user_workspace"

    client = kafka_test_setup(os.path.join(test_workspace_dir, "feathr_config.yaml"))
    redisSink = RedisSink(table_name="kafkaSampleDemoFeature", streaming=True, streamingTimeoutMs=10000)
    settings = MaterializationSettings(name="kafkaSampleDemo",
                                   sinks=[redisSink],
                                   feature_names=['f_modified_streaming_count']
                                   )
    client.materialize_features(settings)
    client.wait_job_to_finish(timeout_sec=600)

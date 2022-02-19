import logging
import os
from typing import List
import redis
from loguru import logger
from pyhocon import ConfigFactory

from feathr._envvariableutil import _EnvVaraibleUtil
from feathr._feature_registry import _FeatureRegistry
from feathr._submission import _FeathrSynapseJobLauncher


class FeatureJoinJobParams:
    """Parameters related to feature join job.

    Attributes:
        join_config_path: Path to the join config.
        observation_path: Absolute path in Cloud to the observation data path.
        feature_config: Path to the features config.
        job_output_path: Absolute path in Cloud that you want your output data to be in.
    """

    def __init__(self, join_config_path, observation_path, feature_config, job_output_path):
        self.join_config_path = join_config_path
        self.observation_path = observation_path
        self.feature_config = feature_config
        self.job_output_path = job_output_path


class FeatureGenerationJobParams:
    """Parameters related to feature generation job.

    Attributes:
        generation_config_path: Path to the feature generation config.
        feature_config: Path to the features config.
    """

    def __init__(self, generation_config_path, feature_config):
        self.generation_config_path = generation_config_path
        self.feature_config = feature_config


REDIS_PASSWORD = "REDIS_PASSWORD"
REDIS_HOST = "REDIS_HOST"
REDIS_PORT = "REDIS_PORT"
REDIS_SSL_ENABLED = "REDIS_SSL_ENABLED"


class FeathrClient(object):
    """Feathr client.

    The client is used to create training dataset, materialize features, register features, and fetch features from
    the online storage.

    For offline storage and compute engine, Azure ADLS, AWS S3 and Azure Synapse are supported.

    For online storage, currently only Redis is supported.
    The users of this client is responsible for set up all the necessary information needed to start a Redis client via
    environment variable or a Spark cluster. Host address, port and password are needed to start the Redis client.

    Raises:
        RuntimeError: Fail to create the client since necessary environment variables are not set for Redis
        client creation.
    """

    def __init__(self):

        self.logger = logging.getLogger(__name__)
        # Redis key separator
        self._KEY_SEPARATOR = ':'

        self._check_required_environment_variables_exist()

        # Feahtr is a spark-based application so the feathr jar compiled from source code will be used in the Spark job
        # submission. The feathr jar hosted in Azure saves the time users needed to upload the jar from their local.
        self._FEATHR_JOB_JAR_PATH = _EnvVaraibleUtil.get_from_config('FEATHR_RUNTIME_LOCATION')
        # configure the remote environment
        self.feathr_synapse_laucher = _FeathrSynapseJobLauncher(
            synapse_dev_url=_EnvVaraibleUtil.get_from_config(
                'SYNAPSE_DEV_URL'),
            pool_name=_EnvVaraibleUtil.get_from_config(
                'SYNAPSE_POOL_NAME'),
            datalake_dir=_EnvVaraibleUtil.get_from_config(
                'SYNAPSE_WORKSPACE_DIR'),
            executor_size=_EnvVaraibleUtil.get_from_config('SYNAPSE_EXECUTOR_SIZE'),
            executors=_EnvVaraibleUtil.get_from_config('SYNAPSE_EXECUTOR_NUM'))

        self._construct_redis_client()

        self.registry = _FeatureRegistry()

    def _check_required_environment_variables_exist(self):
        """Checks if the required environment variables(form feathr_config.yaml) is set.

        Some required information has to be set via environment variables so the client can work.
        """
        all_required_vars = _EnvVaraibleUtil.get_from_config("REQUIRED_ENVIRONMENT_VARIABLES")
        for required_field in all_required_vars:
            if required_field not in os.environ:
                raise RuntimeError(f'{required_field} is not set in environment variable. All required environment '
                                   f'variables are: {all_required_vars}.')

    def register_features(self):
        """Registers features based on the current workspace
        """
        self.registry.register_features(os.path.abspath('./'))

    def list_registered_features(self, project_name: str = None) -> List[str]:
        """List all the already registered features. If project_name is not provided or is None, it will return all
        the registered features; otherwise it will only return features under this project
        """
        return self.registry.list_registered_features(project_name)

    def get_registry_client(self):
        """
        Returns registry client in case users want to perform more advanced operations
        """
        return self.registry.get_registry_client()

    def online_get_features(self, feature_table, key, feature_names):
        """Fetches feature value for a certain key from a online feature table.

        Args:
            feature_table: the name of the feature table.
            key: the key of the entity
            feature_names: list of feature names to fetch

        Return:
            A list of feature values for this entity. It's ordered by the requested feature names.
            For example, feature_names = ['f_is_medium_trip_distance', 'f_day_of_week', 'f_day_of_month', 'f_hour_of_day']
            then, the returned feature values is: [b'true', b'4.0', b'31.0', b'23.0'].
            If the feature_table or key doesn't exist, then a list of Nones are returned. For example,
            [None, None, None, None].
            If a feature doesn't exist, then a None is returned for that feature. For example:
            [None, b'4.0', b'31.0', b'23.0'].
            """
        redis_key = self._construct_redis_key(feature_table, key)
        res = self.redis_clint.hmget(redis_key, *feature_names)
        return res

    def online_batch_get_features(self, feature_table, keys, feature_names):
        """Fetches feature value for a list of keys from a online feature table. This is the batch version of the get API.

        Args:
            feature_table: the name of the feature table.
            keys: list of keys for the entities
            feature_names: list of feature names to fetch

        Return:
            A list of feature values for the requested entities. It's ordered by the requested feature names. For
            example, keys = [12, 24], feature_names = ['f_is_medium_trip_distance', 'f_day_of_week', 'f_day_of_month',
            'f_hour_of_day'] then, the returned feature values is: {'12': [b'false', b'5.0', b'1.0', b'0.0'],
            '24': [b'true', b'4.0', b'31.0', b'23.0']}. If the feature_table or key doesn't exist, then a list of Nones
            are returned. For example, {'12': [None, None, None, None], '24': [None, None, None, None]} If a feature
            doesn't exist, then a None is returned for that feature. For example: {'12': [None, b'4.0', b'31.0',
            b'23.0'], '24': [b'true', b'4.0', b'31.0', b'23.0']}.
        """
        with self.redis_clint.pipeline() as redis_pipeline:
            for key in keys:
                redis_key = self._construct_redis_key(feature_table, key)
                redis_pipeline.hmget(redis_key, *feature_names)
            pipeline_result = redis_pipeline.execute()

        return dict(zip(keys, pipeline_result))

    def _clean_test_data(self, feature_table):
        """
        WARNING: THIS IS ONLY USED FOR TESTING
        Clears a namespace in redis cache.
        This may be very time consuming.

        Args:
          feature_table: str, feature_table i.e your prefix before the separator in the Redis database.
        """
        cursor = '0'
        ns_keys = feature_table + '*'
        while cursor != 0:
            # 5000 count at a scan seems reasonable faster for our testing data
            cursor, keys = self.redis_clint.scan(
                cursor=cursor, match=ns_keys, count=5000)
            if keys:
                self.redis_clint.delete(*keys)

    def _construct_redis_key(self, feature_table, key):
        return feature_table + self._KEY_SEPARATOR + key

    def _construct_redis_client(self):
        """Constructs the Redis client. The host, port, credential and other parameters can be set via environment
        parameters.
        """
        password = _EnvVaraibleUtil.get_environment_variable(REDIS_PASSWORD)
        host = _EnvVaraibleUtil.get_environment_variable_with_default('azure', REDIS_HOST)
        port = _EnvVaraibleUtil.get_environment_variable_with_default('azure', REDIS_PORT)
        ssl_enabled = _EnvVaraibleUtil.get_environment_variable_with_default('azure', REDIS_SSL_ENABLED)

        redis_clint = redis.Redis(
            host=host,
            port=port,
            password=password,
            ssl=ssl_enabled)
        self.logger.info('Redis connection is successful and completed.')
        self.redis_clint = redis_clint

    def join_offline_features(self, feature_join_conf_path='feature_join_conf/feature_join.conf'):
        """Joins the features to your offline observation dataset based on the join config.

        Args:
          feature_join_conf_path: Relative path to your feature join config file.
        """
        if not feature_join_conf_path.startswith('feature_join_conf'):
            raise RuntimeError('Feature join config should be in feature_join_conf folder.')

        feathr_feature = ConfigFactory.parse_file(feature_join_conf_path)

        feature_join_job_params = FeatureJoinJobParams(join_config_path=os.path.abspath(feature_join_conf_path),
                                                       observation_path=feathr_feature['observationPath'],
                                                       feature_config=os.path.abspath(
                                                           'feature_conf/features.conf'),
                                                       job_output_path=feathr_feature['outputPath'],
                                                       )

        # submit the jars
        return self.feathr_synapse_laucher.submit_feathr_job(
            job_name=_EnvVaraibleUtil.get_from_config(
                'PROJECT_NAME') + '_feathr_feature_join_job',
            main_jar_path=self._FEATHR_JOB_JAR_PATH,
            main_class_name='com.linkedin.feathr.offline.job.FeatureJoinJob',
            arguments=[
                '--join-config', self.feathr_synapse_laucher.upload_to_work_dir(
                    feature_join_job_params.join_config_path),
                '--input', feature_join_job_params.observation_path,
                '--output', feature_join_job_params.job_output_path,
                '--feature-config', self.feathr_synapse_laucher.upload_to_work_dir(
                    feature_join_job_params.feature_config),
                '--num-parts', _EnvVaraibleUtil.get_from_config('RESULT_OUTPUT_PARTS')
            ],
            reference_files_path=[],
        )

    def get_job_result_uri(self, feature_join_conf_path='feature_join_conf/feature_join.conf', local_folder='/tmp',
                           block=True, timeout_sec=300):
        """Gets the job output URI
        """
        feathr_feature = ConfigFactory.parse_file(feature_join_conf_path)
        if not block:
            return feathr_feature['outputPath']
        # Block the API by pooling the job status and wait for complete
        if self.feathr_synapse_laucher.wait_for_completion(timeout_sec):
            return feathr_feature['outputPath']
        else:
            raise RuntimeError('Spark job failed so output cannot be retrieved.')

    def wait_job_to_finish(self, timeout_sec: int = 300):
        """Waits for the job to finish in a blocking way unless it times out
        """
        if self.feathr_synapse_laucher.wait_for_completion(timeout_sec):
            return
        else:
            raise RuntimeError('Spark job failed.')

    def materialize_features(self, feature_gen_conf_path: str = 'feature_gen_conf/feature_gen.conf'):
        """Materializes feature data based on the feature generation config. The feature
        data will be materialized to the destination specified in the feature generation config.

        Args
          feature_gen_conf_path: Relative path to the feature generation config you want to materialize.
        """
        if not feature_gen_conf_path.startswith('feature_gen_conf'):
            raise RuntimeError(
                'Feature generation config should be in feature_gen_conf folder.')

        # Read all features conf
        generation_config = FeatureGenerationJobParams(
            generation_config_path=os.path.abspath(feature_gen_conf_path),
            feature_config=os.path.abspath("feature_conf/features.conf"))

        # submit the jars
        logger.info('See materialization job here: https://ms.web.azuresynapse.net/en-us/monitoring/sparkapplication')

        return self.feathr_synapse_laucher.submit_feathr_job(
            job_name=_EnvVaraibleUtil.get_from_config(
                'PROJECT_NAME') + '_feathr_feature_materialization_job',
            main_jar_path=self._FEATHR_JOB_JAR_PATH,
            main_class_name='com.linkedin.feathr.offline.job.FeatureGenJob',
            arguments=[
                '--generation-config', self.feathr_synapse_laucher.upload_to_work_dir(
                    generation_config.generation_config_path),
                # Local Config, comma seperated file names
                '--feature-config', self.feathr_synapse_laucher.upload_to_work_dir(
                    generation_config.feature_config),
                '--redis-config', self._getRedisConfigStr(),
                '--s3-config', self._get_s3_config_str()
            ],
            reference_files_path=[],
        )

    def _getRedisConfigStr(self):
        """Construct the Redis config string. The host, port, credential and other parameters can be set via environment
        variables."""
        password = _EnvVaraibleUtil.get_environment_variable(REDIS_PASSWORD)
        host = _EnvVaraibleUtil.get_environment_variable_with_default('azure', REDIS_HOST)
        port = _EnvVaraibleUtil.get_environment_variable_with_default('azure', REDIS_PORT)
        ssl_enabled = _EnvVaraibleUtil.get_environment_variable_with_default('azure', REDIS_SSL_ENABLED)
        config_str = """
        REDIS_PASSWORD: "{REDIS_PASSWORD}"
        REDIS_HOST: "{REDIS_HOST}"
        REDIS_PORT: {REDIS_PORT}
        REDIS_SSL_ENABLED: {REDIS_SSL_ENABLED}
        """.format(REDIS_PASSWORD=password, REDIS_HOST=host, REDIS_PORT=port, REDIS_SSL_ENABLED=ssl_enabled)
        return config_str

    def _get_s3_config_str(self):
        """Construct the S3 config string. The endpoint, access key, secret key, and other parameters can be set via
        environment variables."""
        endpoint = _EnvVaraibleUtil.get_environment_variable_with_default('aws', 'S3_ENDPOINT')
        # if s3 endpoint is set in the feathr_config, then we need other environment variables
        # keys can't be only accessed through environment
        access_key = _EnvVaraibleUtil.get_environment_variable('S3_ACCESS_KEY')
        secret_key = _EnvVaraibleUtil.get_environment_variable('S3_SECRET_KEY')
        # HOCCON format will be parsed by the Feathr job
        config_str = """
            S3_ENDPOINT: {S3_ENDPOINT}
            S3_ACCESS_KEY: "{S3_ACCESS_KEY}"
            S3_SECRET_KEY: "{S3_SECRET_KEY}"
            """.format(S3_ENDPOINT=endpoint, S3_ACCESS_KEY=access_key, S3_SECRET_KEY=secret_key)
        return config_str

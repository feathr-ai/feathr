import logging
import os
from typing import List
import redis
from loguru import logger
from pyhocon import ConfigFactory

from feathr._envvariableutil import _EnvVaraibleUtil
from feathr._feature_registry import _FeatureRegistry
from feathr._synapse_submission import _FeathrSynapseJobLauncher
from feathr._databricks_submission import _FeathrDatabricksJobLauncher


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


REDIS_PASSWORD = 'REDIS_PASSWORD'


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

        # Load all configs from yaml at initialization
        # DO NOT load any configs from yaml during runtime.
        self.project_name = _EnvVaraibleUtil.get_environment_variable_with_default(
            'project_config', 'project_name')
        self.required_fields = _EnvVaraibleUtil.get_environment_variable_with_default(
            'project_config', 'required_environment_variables')

        self._check_required_environment_variables_exist()

        # Redis configs
        self.redis_host = _EnvVaraibleUtil.get_environment_variable_with_default(
            'online_store', 'redis', 'host')
        self.redis_port = _EnvVaraibleUtil.get_environment_variable_with_default(
            'online_store', 'redis', 'port')
        self.redis_ssl_enabled = _EnvVaraibleUtil.get_environment_variable_with_default(
            'online_store', 'redis', 'ssl_enabled')

        # S3 configs
        self.s3_endpoint = _EnvVaraibleUtil.get_environment_variable_with_default(
            'offline_store', 's3', 's3_endpoint')

        # spark configs
        self.output_num_parts = _EnvVaraibleUtil.get_environment_variable_with_default(
            'spark_config', 'spark_result_output_parts')
        self.spark_runtime = _EnvVaraibleUtil.get_environment_variable_with_default(
            'spark_config', 'spark_cluster')

        if self.spark_runtime not in {'azure_synapse', 'databricks'}:
            raise RuntimeError(
                'Only \'azure_synapse\' and \'databricks\' are currently supported.')
        elif self.spark_runtime.lower() == 'azure_synapse':
            # Feathr is a spark-based application so the feathr jar compiled from source code will be used in the
            # Spark job submission. The feathr jar hosted in cloud saves the time users needed to upload the jar from
            # their local env.
            self._FEATHR_JOB_JAR_PATH = \
                _EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'azure_synapse', 'feathr_runtime_location')

            self.feathr_spark_laucher = _FeathrSynapseJobLauncher(
                synapse_dev_url=_EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'azure_synapse', 'dev_url'),
                pool_name=_EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'azure_synapse', 'pool_name'),
                datalake_dir=_EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'azure_synapse', 'workspace_dir'),
                executor_size=_EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'azure_synapse', 'executor_size'),
                executors=_EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'azure_synapse', 'executor_num')
            )
        elif self.spark_runtime.lower() == 'databricks':
            # Feathr is a spark-based application so the feathr jar compiled from source code will be used in the
            # Spark job submission. The feathr jar hosted in cloud saves the time users needed to upload the jar from
            # their local env.
            self._FEATHR_JOB_JAR_PATH = \
                _EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'databricks', 'feathr_runtime_location')

            self.feathr_spark_laucher = _FeathrDatabricksJobLauncher(
                workspace_instance_url=_EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'databricks', 'workspace_instance_url'),
                token_value=_EnvVaraibleUtil.get_environment_variable(
                    'DATABRICKS_WORKSPACE_TOKEN_VALUE'),
                config_template=_EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'databricks', 'config_template'),
                databricks_work_dir=_EnvVaraibleUtil.get_environment_variable_with_default(
                    'spark_config', 'databricks', 'work_dir')
            )

        self._construct_redis_client()

        self.registry = _FeatureRegistry()

    def _check_required_environment_variables_exist(self):
        """Checks if the required environment variables(form feathr_config.yaml) is set.

        Some required information has to be set via environment variables so the client can work.
        """
        for required_field in self.required_fields:
            if required_field not in os.environ:
                raise RuntimeError(f'{required_field} is not set in environment variable. All required environment '
                                   f'variables are: {self.required_fields}.')

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
        host = self.redis_host
        port = self.redis_port
        ssl_enabled = self.redis_ssl_enabled

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
            raise RuntimeError(
                'Feature join config should be in feature_join_conf folder.')

        feathr_feature = ConfigFactory.parse_file(feature_join_conf_path)

        feature_join_job_params = FeatureJoinJobParams(join_config_path=os.path.abspath(feature_join_conf_path),
                                                       observation_path=feathr_feature['observationPath'],
                                                       feature_config=os.path.abspath(
                                                           'feature_conf/features.conf'),
                                                       job_output_path=feathr_feature['outputPath'],
                                                       )

        # submit the jars
        return self.feathr_spark_laucher.submit_feathr_job(
            job_name=self.project_name + '_feathr_feature_join_job',
            main_jar_path=self._FEATHR_JOB_JAR_PATH,
            main_class_name='com.linkedin.feathr.offline.job.FeatureJoinJob',
            arguments=[
                '--join-config', self.feathr_spark_laucher.upload_or_get_cloud_path(
                    feature_join_job_params.join_config_path),
                '--input', feature_join_job_params.observation_path,
                '--output', feature_join_job_params.job_output_path,
                '--feature-config', self.feathr_spark_laucher.upload_or_get_cloud_path(
                    feature_join_job_params.feature_config),
                '--num-parts', self.output_num_parts,
                '--s3-config', self._get_s3_config_str(),
                '--adls-config', self._get_adls_config_str(),
                '--blob-config', self._get_blob_config_str(),
                '--sql-config', self._get_sql_config_str(),
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
        if self.feathr_spark_laucher.wait_for_completion(timeout_sec):
            return feathr_feature['outputPath']
        else:
            raise RuntimeError(
                'Spark job failed so output cannot be retrieved.')

    def wait_job_to_finish(self, timeout_sec: int = 300):
        """Waits for the job to finish in a blocking way unless it times out
        """
        if self.feathr_spark_laucher.wait_for_completion(timeout_sec):
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

        return self.feathr_spark_laucher.submit_feathr_job(
            job_name=self.project_name + '_feathr_feature_materialization_job',
            main_jar_path=self._FEATHR_JOB_JAR_PATH,
            main_class_name='com.linkedin.feathr.offline.job.FeatureGenJob',
            arguments=[
                '--generation-config', self.feathr_spark_laucher.upload_or_get_cloud_path(
                    generation_config.generation_config_path),
                # Local Config, comma seperated file names
                '--feature-config', self.feathr_spark_laucher.upload_or_get_cloud_path(
                    generation_config.feature_config),
                '--redis-config', self._getRedisConfigStr(),
                '--s3-config', self._get_s3_config_str(),
                '--adls-config', self._get_adls_config_str(),
                '--blob-config', self._get_blob_config_str(),
                '--sql-config', self._get_sql_config_str()
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
        if self.feathr_spark_laucher.wait_for_completion(timeout_sec):
            return feathr_feature['outputPath']
        else:
            raise RuntimeError(
                'Spark job failed so output cannot be retrieved.')

    def wait_job_to_finish(self, timeout_sec: int = 300):
        """Waits for the job to finish in a blocking way unless it times out
        """
        if self.feathr_spark_laucher.wait_for_completion(timeout_sec):
            return
        else:
            raise RuntimeError('Spark job failed.')

    def _getRedisConfigStr(self):
        """Construct the Redis config string. The host, port, credential and other parameters can be set via environment
        variables."""
        password = _EnvVaraibleUtil.get_environment_variable(REDIS_PASSWORD)
        host = self.redis_host
        port = self.redis_port
        ssl_enabled = self.redis_ssl_enabled
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
        endpoint = self.s3_endpoint
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

    def _get_adls_config_str(self):
        """Construct the ADLS config string for abfs(s). The Account, access key and other parameters can be set via
        environment variables."""
        account = _EnvVaraibleUtil.get_environment_variable('ADLS_ACCOUNT')
        # if ADLS Account is set in the feathr_config, then we need other environment variables
        # keys can't be only accessed through environment
        key = _EnvVaraibleUtil.get_environment_variable('ADLS_KEY')
        # HOCCON format will be parsed by the Feathr job
        config_str = """
            ADLS_ACCOUNT: {ADLS_ACCOUNT}
            ADLS_KEY: "{ADLS_KEY}"
            """.format(ADLS_ACCOUNT=account, ADLS_KEY=key)
        return config_str

    def _get_blob_config_str(self):
        """Construct the Blob config string for wasb(s). The Account, access key and other parameters can be set via
        environment variables."""
        account = _EnvVaraibleUtil.get_environment_variable('BLOB_ACCOUNT')
        # if BLOB Account is set in the feathr_config, then we need other environment variables
        # keys can't be only accessed through environment
        key = _EnvVaraibleUtil.get_environment_variable('BLOB_KEY')
        # HOCCON format will be parsed by the Feathr job
        config_str = """
            BLOB_ACCOUNT: {BLOB_ACCOUNT}
            BLOB_KEY: "{BLOB_KEY}"
            """.format(BLOB_ACCOUNT=account, BLOB_KEY=key)
        return config_str

    def _get_sql_config_str(self):
        """Construct the SQL config string for jdbc. The dbtable (query), user, password and other parameters can be set via
        environment variables."""
        table = _EnvVaraibleUtil.get_environment_variable('JDBC_TABLE')
        user = _EnvVaraibleUtil.get_environment_variable('JDBC_USER')
        password = _EnvVaraibleUtil.get_environment_variable('JDBC_PASSWORD')
        driver = _EnvVaraibleUtil.get_environment_variable('JDBC_DRIVER')
        auth_flag = _EnvVaraibleUtil.get_environment_variable('JDBC_AUTH_FLAG')
        token = _EnvVaraibleUtil.get_environment_variable('JDBC_TOKEN')
        # HOCCON format will be parsed by the Feathr job
        config_str = """
            JDBC_TABLE: {JDBC_TABLE}
            JDBC_USER: {JDBC_USER}
            JDBC_PASSWORD: {JDBC_PASSWORD}
            JDBC_DRIVER: {JDBC_DRIVER}
            JDBC_AUTH_FLAG: {JDBC_AUTH_FLAG}
            JDBC_TOKEN: {JDBC_TOKEN}
            """.format(JDBC_TABLE=table, JDBC_USER=user, JDBC_PASSWORD=password, JDBC_DRIVER = driver, JDBC_AUTH_FLAG = auth_flag, JDBC_TOKEN = token)
        return config_str

import os
import re
import urllib.request
from typing import Any, Dict, List, Optional, Tuple
from urllib.parse import urlparse
from loguru import logger
import time
from feathr._abc import SparkJobLauncher


from azure.identity import (ChainedTokenCredential, DefaultAzureCredential,
                            DeviceCodeCredential, EnvironmentCredential,
                            ManagedIdentityCredential)
from azure.storage.filedatalake import DataLakeServiceClient
from azure.synapse.spark import SparkClient
from azure.synapse.spark.models import SparkBatchJob, SparkBatchJobOptions, LivyStates


class _FeathrSynapseJobLauncher(SparkJobLauncher):
    """
    Submits spark jobs to a Synapse spark cluster.
    """
    def __init__(self, synapse_dev_url: str, pool_name: str, datalake_dir: str, executor_size: str, executors: int):
        # use DeviceCodeCredential if EnvironmentCredential is not available
        self.credential = DefaultAzureCredential()
        # use the same credential for authentication to avoid further login.
        self._api = _SynapseJobRunner(
            synapse_dev_url, pool_name, executor_size=executor_size, executors=executors, credential=self.credential)
        self._datalake = _DataLakeFiler(
            datalake_dir, credential=self.credential)

    def upload_to_work_dir(self, local_path_or_http_path: str):
        """
        Supports transferring file from an http path to cloud working storage, or upload directly from a local storage.
        """
        logger.info('Uploading {} to cloud..', local_path_or_http_path)
        res_path = self._datalake.upload_file_to_workdir(local_path_or_http_path)

        logger.info('{} is uploaded to location: {}', local_path_or_http_path, res_path)
        return res_path

    def download_result(self, result_path: str, local_folder: str):
        """
        Supports downloading files from the result folder
        """

        return self._datalake.download_file(result_path, local_folder)

    def submit_feathr_job(self, job_name: str, main_jar_path: str,  main_class_name: str, arguments: List[str],
                          reference_files_path: List[str], job_tags: Dict[str, str] = None,
                          configuration: Dict[str, str] = None):
        """
        Submits the feathr job
        Refer to the Apache Livy doc for more details on the meaning of the parameters:
        https://livy.apache.org/docs/latest/rest-api.html

        reference files: put everything there and the function will automatically categorize them based on the
        extension name to either the "files" argument in the Livy API, or the "jars" argument in the Livy API. The
        path can be local path and this function will automatically upload the function to the corresponding azure
        storage

        Also, note that the Spark application will automatically run on YARN cluster mode. You cannot change it if
        you are running with Azure Synapse.

        Args:
            job_name (str): name of the job
            main_jar_path (str): main file paths, usually your main jar file
            main_class_name (str): name of your main class
            arguments (str): all the arugments you want to pass into the spark job
            job_tags (str): tags of the job, for exmaple you might want to put your user ID, or a tag with a certain information
            configuration (Dict[str, str]): Additional configs for the spark job
        """

        if main_jar_path.startswith('abfs'):
            main_jar_cloud_path = main_jar_path
            logger.info(
                'Cloud path {} is used for running the job: {}', main_jar_path, job_name)
        else:
            logger.info('Uploading jar from {} to cloud for running job: {}',
                        main_jar_path, job_name)
            main_jar_cloud_path = self._datalake.upload_file_to_workdir(main_jar_path)
            logger.info('{} is uploaded to {} for running job: {}',
                         main_jar_path, main_jar_cloud_path, job_name)

        reference_file_paths = []
        for file_path in reference_files_path:
            reference_file_paths.append(
                self._datalake.upload_file_to_workdir(file_path))

        self.current_job_info = self._api.create_spark_batch_job(job_name=job_name,
                                                                 main_file=main_jar_cloud_path,
                                                                 class_name=main_class_name,
                                                                 arguments=arguments,
                                                                 reference_files=reference_files_path,
                                                                 tags=job_tags,
                                                                 configuration=configuration)
        logger.info('See submitted job here: https://web.azuresynapse.net/en-us/monitoring/sparkapplication')
        return self.current_job_info

    def wait_for_completion(self, timeout_seconds: Optional[float]) -> bool:
        """
        Returns true if the job completed successfully
        """
        start_time = time.time()
        while (timeout_seconds is None) or (time.time() - start_time < timeout_seconds):
            status = self.get_status()
            logger.info('Current Spark job status: {}', status)
            if status in {LivyStates.SUCCESS}:
                return True
            elif status in {LivyStates.ERROR, LivyStates.DEAD, LivyStates.KILLED}:
                return False
            else:
                time.sleep(30)
        else:
            raise TimeoutError('Timeout waiting for job to complete')

    def get_status(self) -> str:
        """Get current job status

        Returns:
            str: Status of the current job
        """
        job = self._api.get_spark_batch_job(self.current_job_info.id)
        assert job is not None
        return job.state


class _SynapseJobRunner(object):
    """
    Class to interact with Synapse Spark cluster
    """
    def __init__(self, synapse_dev_url, spark_pool_name, credential=None, executor_size='Small', executors=2):
        if credential is None:
            logger.warning('No valid Azure credential detected. Using DefaultAzureCredential')
            credential = DefaultAzureCredential()

        self.client = SparkClient(
            credential=credential,
            endpoint=synapse_dev_url,
            spark_pool_name=spark_pool_name
        )

        self._executor_size = executor_size
        self._executors = executors
        self.EXECUTOR_SIZE = {'Small': {'Cores': 4, 'Memory': '28g'}, 'Medium': {'Cores': 8, 'Memory': '56g'},
                              'Large': {'Cores': 16, 'Memory': '112g'}}

    def _categorized_files(self, reference_files: List[str]):
        """categorize files to make sure they are in the ready to submissio format

        Args:
            reference_files (List[str]): a list of reference files, can be either jars, pyfiles, etc.

        Returns:
            a tuple with all the files and jars clasified into list
        """
        if reference_files == None:
            return None, None

        files = []
        jars = []
        for file in reference_files:
            file = file.strip()
            if file.endswith('.jar'):
                jars.append(file)
            else:
                files.append(file)
        return files, jars

    def get_spark_batch_job(self, job_id:int):
        """
        Get the job object by searching a certain ID
        """

        return self.client.spark_batch.get_spark_batch_job(job_id, detailed=True)

    def get_spark_batch_jobs(self):
        """
        Get all the jobs in a certain Spark pool
        """

        return self.client.spark_batch.get_spark_batch_jobs(detailed=True)

    def cancel_spark_batch_job(self, job_id:int):
        """
        Cancel a job by searching a certain ID
        """

        return self.client.spark_batch.cancel_spark_batch_job(job_id)

    def create_spark_batch_job(self, job_name, main_file, class_name=None,
                               arguments=None,  reference_files=None, archives=None, configuration=None, tags=None):
        """
        Submit a spark job to a certain cluster
        """

        files, jars = self._categorized_files(reference_files)
        driver_cores = self.EXECUTOR_SIZE[self._executor_size]['Cores']
        driver_memory = self.EXECUTOR_SIZE[self._executor_size]['Memory']
        executor_cores = self.EXECUTOR_SIZE[self._executor_size]['Cores']
        executor_memory = self.EXECUTOR_SIZE[self._executor_size]['Memory']

        # Adding spaces between brackets. This is to workaround this known YARN issue (when running Spark on YARN):
        # https://issues.apache.org/jira/browse/SPARK-17814?focusedCommentId=15567964&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-15567964
        # updated_arguments = []
        # for elem in arguments:
        #     if type(elem) == str:
        #         updated_arguments.append(elem.replace("}", " }"))
        #     else:
        #         updated_arguments.append(elem)

        spark_batch_job_options = SparkBatchJobOptions(
            tags=tags,
            name=job_name,
            file=main_file,
            class_name=class_name,
            arguments=arguments,
            jars=jars,
            files=files,
            archives=archives,
            configuration=configuration,
            driver_memory=driver_memory,
            driver_cores=driver_cores,
            executor_memory=executor_memory,
            executor_cores=executor_cores,
            executor_count=self._executors)

        return self.client.spark_batch.create_spark_batch_job(spark_batch_job_options, detailed=True)


class _DataLakeFiler(object):
    """
    Class to interact with Azure Data Lake Storage.
    """
    def __init__(self, datalake_dir, credential=None):
        # A datalake path would be something like this:
        # "abfss://feathrazuretest3fs@feathrazuretest3storage.dfs.core.windows.net/frame_getting_started" after this
        # split, datalake_path_split should give out something like this; ['abfss:', '', 'feathrazuretest3fs',
        # 'feathrazuretest3storage.dfs.core.windows.net', 'frame_getting_started'] datalake_path_split[0] should be
        # the protocal, datalake_path_split[1] is empty, datalake_path_split[2] is the file system used (aka
        # container name), datalake_path_split[3] should be the full name of this target path,
        # and datalake_path_split[3:] would be all the directory in this particular container split datalake names by
        # "/" or "@"
        datalake_path_split = list(filter(None, re.split('/|@', datalake_dir)))
        assert len(datalake_path_split) >= 3

        if credential is None:
            credential = DefaultAzureCredential()

        account_url = "https://" + datalake_path_split[2]

        self.file_system_client = DataLakeServiceClient(
            credential=credential,
            account_url=account_url
        ).get_file_system_client(datalake_path_split[1])

        if len(datalake_path_split) > 3:
            # directory exists in datalake path
            self.dir_client = self.file_system_client.get_directory_client(
                '/'.join(datalake_path_split[3:]))
            self.dir_client.create_directory()
        else:
            # otherwise use root folder instead
            self.dir_client = self.file_system_client.get_directory_client('/')

        self.datalake_dir = datalake_dir + \
            '/' if datalake_dir[-1] != '/' else datalake_dir

    def upload_file_to_workdir(self, src_file_path: str) -> str:
        """
        Handles file upload to the corresponding datalake storage. If a path starts with "wasb" or "abfs",
        it will skip uploading and return the original path; otherwise it will upload the source file to the working
        dir
        """

        src_parse_result = urlparse(src_file_path)
        file_name = os.path.basename(src_file_path)
        file_client = self.dir_client.create_file(file_name)
        # returned paths for the uploaded file
        returned_path = self.datalake_dir + file_name
        if src_parse_result.scheme.startswith('http'):
            with urllib.request.urlopen(src_file_path) as f:
                data = f.read()
                file_client.upload_data(data, overwrite=True)
                logger.info("{} is downloaded and then uploaded to location: {}", src_file_path, returned_path)
        elif src_parse_result.scheme.startswith('abfs') or src_parse_result.scheme.startswith('wasb'):
            # passed a cloud path
            logger.info("Skipping file {} as it's already in the cloud", src_file_path)
            returned_path = src_file_path
        else:
            # else it should be a local file path
            with open(src_file_path, 'rb') as f:
                data = f.read()
                file_client.upload_data(data, overwrite=True)
                logger.info("{} is uploaded to location: {}", src_file_path, returned_path)
        return returned_path

    def download_file(self, target_adls_directory: str, local_dir_cache: str):
        """
        Download file to a local cache

        Args:
            target_adls_directory (str): target ADLS directory
            local_dir_cache (str): local cache to store local results
        """
        logger.info('Beginning reading of results from {}',
                    target_adls_directory)
        parse_result = urlparse(target_adls_directory)
        directory_client = self.file_system_client.get_directory_client(
            parse_result.path)
        # returns the paths to all the files in the target director in ADLS
        adls_paths = [file_path.name.split("/")[-1] for file_path in self.file_system_client.get_paths(
            path=parse_result.path) if not file_path.is_directory][1:]
        # need to generate list of local paths to write the files to
        local_paths = [os.path.join(local_dir_cache, file_name)
                       for file_name in adls_paths]
        for idx, file_to_write in enumerate(adls_paths):
            try:
                local_file = open(local_paths[idx], 'wb')
                file_client = directory_client.get_file_client(file_to_write)
                download = file_client.download_file()
                downloaded_bytes = download.readall()
                local_file.write(downloaded_bytes)
                local_file.close()
            except Exception as e:
                logger.error(e)
        logger.info('Finish downloading files from {} to {}.',
                    target_adls_directory,local_dir_cache)

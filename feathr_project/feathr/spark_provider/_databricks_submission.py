from ast import Raise
import copy
import json
import os
import time
from collections import namedtuple
from os.path import basename
from pathlib import Path
from typing import Any, Dict, List, Optional, Union
from urllib.parse import urlparse
from urllib.request import urlopen

import requests
from databricks_cli.dbfs.api import DbfsApi
from databricks_cli.runs.api import RunsApi
from databricks_cli.sdk.api_client import ApiClient
from feathr.constants import *
from feathr.spark_provider._abc import SparkJobLauncher
from loguru import logger
from requests.structures import CaseInsensitiveDict


class _FeathrDatabricksJobLauncher(SparkJobLauncher):
    """Class to interact with Databricks Spark cluster
        This is a light-weight databricks job runner, users should use the provided template json string to get more fine controlled environment for databricks cluster.
        For example, user can control whether to use a new cluster to run the job or not, specify the cluster ID, running frequency, node size, workder no., whether to send out failed notification email, etc.
        This runner will only fill in necessary arguments in the JSON template.

        This class will read from the provided configs string, and do the following steps.
        This default template can be overwritten by users, but users need to make sure the template is compatible with the default template. Specifically:
        1. it's a SparkJarTask (rather than other types of jobs, say NotebookTask or others). See https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-submit for more details
        2. Use the Feathr Jar to run the job (hence will add an entry in `libraries` section)
        3. Only supports `new_cluster` type for now
        4. Will override `main_class_name` and `parameters` field in the JSON template `spark_jar_task` field
        5. will override the name of this job

        Args:
            workspace_instance_url (str): the workinstance url. Document to get workspace_instance_url: https://docs.microsoft.com/en-us/azure/databricks/workspace/workspace-details#workspace-url
            token_value (str): see here on how to get tokens: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/authentication
            config_template (str): config template for databricks cluster. See https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-submit for more details.
            databricks_work_dir (_type_, optional): databricks_work_dir must start with dbfs:/. Defaults to 'dbfs:/feathr_jobs'.
        """
    def __init__(
            self,
            workspace_instance_url: str,
            token_value: str,
            config_template: Union[str,Dict],
            databricks_work_dir: str = 'dbfs:/feathr_jobs',
    ):


        # Below we will use Databricks job APIs (as well as many other APIs) to submit jobs or transfer files
        # For Job APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs
        # for DBFS APIs, see: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs
        self.config_template = config_template
        # remove possible trailing '/' due to wrong input format
        self.workspace_instance_url = workspace_instance_url.rstrip('/')
        self.auth_headers = CaseInsensitiveDict()

        # Authenticate the REST APIs. Documentation: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/authentication
        self.auth_headers['Accept'] = 'application/json'
        self.auth_headers['Authorization'] = f'Bearer {token_value}'
        self.databricks_work_dir = databricks_work_dir
        self.api_client = ApiClient(host=self.workspace_instance_url,token=token_value)

    def upload_or_get_cloud_path(self, local_path_or_http_path: str):
        """
        Supports transferring file from an http path to cloud working storage, or upload directly from a local storage.
        """
        src_parse_result = urlparse(local_path_or_http_path)
        file_name = os.path.basename(local_path_or_http_path)
        # returned paths for the uploaded file. Note that we cannot use os.path.join here, since in Windows system it will yield paths like this:
        # dbfs:/feathrazure_cijob_snowflake_9_30_157692\auto_generated_derived_features.conf, where the path sep is mixed, and won't be able to be parsed by databricks.
        # so we force the path to be Linux style here.
        cloud_dest_path = self.databricks_work_dir + "/" + file_name
        if src_parse_result.scheme.startswith('http'):
            with urlopen(local_path_or_http_path) as f:
                # use REST API to avoid local temp file
                data = f.read()
                files = {'file': data}
                # for DBFS APIs, see: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs
                r = requests.post(url=self.workspace_instance_url+'/api/2.0/dbfs/put',
                                  headers=self.auth_headers, files=files,  data={'overwrite': 'true', 'path': cloud_dest_path})
                logger.info('{} is downloaded and then uploaded to location: {}',
                             local_path_or_http_path, cloud_dest_path)
        elif src_parse_result.scheme.startswith('dbfs'):
            # passed a cloud path
            logger.info(
                'Skip uploading file {} as the file starts with dbfs:/', local_path_or_http_path)
            cloud_dest_path = local_path_or_http_path
        elif src_parse_result.scheme.startswith(('wasb','s3','gs')):
            # if the path starts with a location that's not a local path
            logger.error("File {} cannot be downloaded. Please upload the file to dbfs manually.", local_path_or_http_path)
            raise RuntimeError(f"File {local_path_or_http_path} cannot be downloaded. Please upload the file to dbfs manually.")
        else:
            # else it should be a local file path or dir
            if os.path.isdir(local_path_or_http_path):
                logger.info("Uploading folder {}", local_path_or_http_path)
                dest_paths = []
                for item in Path(local_path_or_http_path).glob('**/*.conf'):
                    cloud_dest_path = self._upload_local_file_to_workspace(item.resolve())
                    dest_paths.extend([cloud_dest_path])
                cloud_dest_path = ','.join(dest_paths)
            else:
                cloud_dest_path = self._upload_local_file_to_workspace(local_path_or_http_path)
        return cloud_dest_path

    def _upload_local_file_to_workspace(self, local_path: str) -> str:
        """
        Supports transferring file from a local path to cloud working storage.
        """
        file_name = os.path.basename(local_path)
        # returned paths for the uploaded file. Note that we cannot use os.path.join here, since in Windows system it will yield paths like this:
        # dbfs:/feathrazure_cijob_snowflake_9_30_157692\auto_generated_derived_features.conf, where the path sep is mixed, and won't be able to be parsed by databricks.
        # so we force the path to be Linux style here.
        cloud_dest_path = self.databricks_work_dir + "/" + file_name
        # `local_path_or_http_path` will be either string or PathLib object, so normalize it to string
        local_path = str(local_path)
        try:
            DbfsApi(self.api_client).cp(recursive=True, overwrite=True, src=local_path, dst=cloud_dest_path)
        except RuntimeError as e:
            raise RuntimeError(f"The source path: {local_path}, or the destination path: {cloud_dest_path}, is/are not valid.") from e
        return cloud_dest_path

    def submit_feathr_job(self, job_name: str, main_jar_path: str,  main_class_name: str, arguments: List[str], python_files: List[str], reference_files_path: List[str] = [], job_tags: Dict[str, str] = None, configuration: Dict[str, str] = {}, properties: Dict[str, str] = {}):
        """
        submit the feathr job to databricks
        Refer to the databricks doc for more details on the meaning of the parameters:
        https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-submit
        Args:
            main_file_path (str): main file paths, usually your main jar file
            main_class_name (str): name of your main class
            arguments (str): all the arguments you want to pass into the spark job
            job_tags (str): tags of the job, for example you might want to put your user ID, or a tag with a certain information
            configuration (Dict[str, str]): Additional configs for the spark job
            properties (Dict[str, str]): Additional System Properties for the spark job
        """

        if properties:
            arguments.append("--system-properties=%s" % json.dumps(properties))

        if isinstance(self.config_template, str):
            # if the input is a string, load it directly
            submission_params = json.loads(self.config_template)
        else:
            # otherwise users might have missed the quotes in the config. Treat them as dict
            # Note that we need to use deep copy here, in order to make `self.config_template` immutable
            # Otherwise, since we need to change submission_params later, which will modify `self.config_template` and cause unexpected behaviors
            submission_params = copy.deepcopy(self.config_template) 

        submission_params['run_name'] = job_name
        if 'existing_cluster_id' not in submission_params:
            # if users don't specify existing_cluster_id
            # Solving this issue: Handshake fails trying to connect from Azure Databricks to Azure PostgreSQL with SSL
            # https://docs.microsoft.com/en-us/answers/questions/170730/handshake-fails-trying-to-connect-from-azure-datab.html
            configuration['spark.executor.extraJavaOptions'] = '-Djava.security.properties='
            configuration['spark.driver.extraJavaOptions'] = '-Djava.security.properties='
            submission_params['new_cluster']['spark_conf'] = configuration

            if job_tags:
                custom_tags = submission_params['new_cluster'].get('custom_tags', {})
                for tag, value in job_tags.items():
                    custom_tags[tag] = value

                submission_params['new_cluster']['custom_tags'] = custom_tags

        # the feathr main jar file is anyway needed regardless it's pyspark or scala spark
        if not main_jar_path:
            logger.info(f"Main JAR file is not set, using default package '{FEATHR_MAVEN_ARTIFACT}' from Maven")
            submission_params['libraries'][0]['maven'] = { "coordinates": FEATHR_MAVEN_ARTIFACT }
        else:
            submission_params['libraries'][0]['jar'] = self.upload_or_get_cloud_path(main_jar_path)
        # see here for the submission parameter definition https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--request-structure-6
        if python_files:
            # this is a pyspark job. definition here: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--sparkpythontask
            # the first file is the pyspark driver code. we only need the driver code to execute pyspark
            param_and_file_dict = {"parameters": arguments, "python_file": self.upload_or_get_cloud_path(python_files[0])}
            # indicates this is a pyspark job
            # `setdefault` method will get the value of the "spark_python_task" item, if the "spark_python_task" item does not exist, insert "spark_python_task" with the value "param_and_file_dict":
            submission_params.setdefault('spark_python_task',param_and_file_dict)
        else:
            # this is a scala spark job
            submission_params['spark_jar_task']['parameters'] = arguments
            submission_params['spark_jar_task']['main_class_name'] = main_class_name

        result = RunsApi(self.api_client).submit_run(submission_params)

        try:
            # see if we can parse the returned result
            self.res_job_id = result['run_id']
        except:
            logger.error("Submitting Feathr job to Databricks cluster failed. Message returned from Databricks: {}", result)
            exit(1)

        result = RunsApi(self.api_client).get_run(self.res_job_id)
        self.job_url = result['run_page_url']
        logger.info('Feathr job Submitted Successfully. View more details here: {}', self.job_url)

        # return ID as the submission result
        return self.res_job_id

    def wait_for_completion(self, timeout_seconds: Optional[int] = 600) -> bool:
        """ Returns true if the job completed successfully
        """
        start_time = time.time()
        while (timeout_seconds is None) or (time.time() - start_time < timeout_seconds):
            status = self.get_status()
            logger.debug('Current Spark job status: {}', status)
            # see all the status here:
            # https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runlifecyclestate
            # https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runresultstate
            if status in {'SUCCESS'}:
                return True
            elif status in {'INTERNAL_ERROR', 'FAILED', 'TIMEDOUT', 'CANCELED'}:
                result = RunsApi(self.api_client).get_run_output(self.res_job_id)
                # See here for the returned fields: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--response-structure-8
                # print out logs and stack trace if the job has failed
                logger.error("Feathr job has failed. Please visit this page to view error message: {}", self.job_url)
                if "error" in result:
                    logger.error("Error Code: {}", result["error"])
                if "error_trace" in result:
                    logger.error("{}", result["error_trace"])
                return False
            else:
                time.sleep(30)
        else:
            raise TimeoutError('Timeout waiting for Feathr job to complete')

    def get_status(self) -> str:
        assert self.res_job_id is not None
        result = RunsApi(self.api_client).get_run(self.res_job_id)
        # first try to get result state. it might not be available, and if that's the case, try to get life_cycle_state
        # see result structure: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--response-structure-6
        res_state = result['state'].get('result_state') or result['state']['life_cycle_state']
        assert res_state is not None
        return res_state

    def get_job_result_uri(self) -> str:
        """Get job output uri

        Returns:
            str: `output_path` field in the job tags
        """
        custom_tags = self.get_job_tags()
        # in case users call this API even when there's no tags available
        return None if custom_tags is None else custom_tags[OUTPUT_PATH_TAG]


    def get_job_tags(self) -> Dict[str, str]:
        """Get job tags

        Returns:
            Dict[str, str]: a dict of job tags
        """
        assert self.res_job_id is not None
        # For result structure, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--response-structure-6
        result = RunsApi(self.api_client).get_run(self.res_job_id)

        if 'new_cluster' in result['cluster_spec']:
            custom_tags = result['cluster_spec']['new_cluster']['custom_tags']
            return custom_tags
        else:
            # this is not a new cluster; it's an existing cluster.
            logger.warning("Job tags are not available since you are using an existing Databricks cluster. Consider using 'new_cluster' in databricks configuration.")
            return None


    def download_result(self, result_path: str, local_folder: str):
        """
        Supports downloading files from the result folder. Only support paths starts with `dbfs:/` and only support downloading files in one folder (per Spark's design, everything will be in the result folder in a flat manner)
        """
        if not result_path.startswith('dbfs'):
            raise RuntimeError('Currently only paths starting with dbfs is supported for downloading results from a databricks cluster. The path should start with \"dbfs:\" .')

        DbfsApi(self.api_client).cp(recursive=True, overwrite=True, src=result_path, dst=local_folder)


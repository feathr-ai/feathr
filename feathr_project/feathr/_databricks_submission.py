import json
import os
import re
import time
import traceback
import urllib
import uuid
from importlib.resources import contents, path
from inspect import trace
from typing import Any, Dict, List, Optional, Tuple
from urllib.parse import urlparse
from feathr._abc import SparkJobLauncher

import requests
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
            workspace_instance_url (str): the workinstance url. Document to get workspakce_instance_url: https://docs.microsoft.com/en-us/azure/databricks/workspace/workspace-details#workspace-url
            token_value (str): see here on how to get tokens: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/authentication
            config_template (str): config template for databricks cluster. See https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-submit for more details.
            databricks_work_dir (_type_, optional): databricks_work_dir must start with dbfs:/. Defaults to 'dbfs:/feathr_jobs'.
        """  
    def __init__(
            self,
            workspace_instance_url: str,
            token_value: str,
            config_template: str,
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

    def upload_to_work_dir(self, local_path_or_http_path: str):
        """
        Supports transferring file from an http path to cloud working storage, or upload directly from a local storage.
        """
        src_parse_result = urlparse(local_path_or_http_path)
        file_name = os.path.basename(local_path_or_http_path)
        # returned paths for the uploaded file
        returned_path = os.path.join(self.databricks_work_dir, file_name)
        if src_parse_result.scheme.startswith('http'):
            with urllib.request.urlopen(local_path_or_http_path) as f:
                data = f.read()
                files = {'file': data}
                # for DBFS APIs, see: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs
                r = requests.post(url=self.workspace_instance_url+'/api/2.0/dbfs/put',
                                  headers=self.auth_headers, files=files,  data={'overwrite': 'true', 'path': returned_path})
                logger.debug('{} is downloaded and then uploaded to location: {}',
                             local_path_or_http_path, returned_path)
        elif src_parse_result.scheme.startswith('dbfs'):
            # passed a cloud path
            logger.debug(
                'Skipping file {} as it is already in the cloud', local_path_or_http_path)
            returned_path = local_path_or_http_path
        else:
            # else it should be a local file path
            with open(local_path_or_http_path, 'rb') as f:
                data = f.read()
                files = {'file': data}
                # for DBFS APIs, see: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs
                r = requests.post(url=self.workspace_instance_url+'/api/2.0/dbfs/put',
                                  headers=self.auth_headers, files=files,  data={'overwrite': 'true', 'path': returned_path})
                logger.debug('{} is uploaded to location: {}',
                             local_path_or_http_path, returned_path)
        return returned_path

    def submit_feathr_job(self, job_name: str, main_jar_path: str,  main_class_name: str, arguments: List[str], reference_files_path: List[str] = [], job_tags: Dict[str, str] = None, configuration: Dict[str, str] = None):
        """
        submit the feathr job to databricks
        Refer to the databricks doc for more details on the meaning of the parameters:
        https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-submit
        Args:
            main_file_path (str): main file paths, usually your main jar file
            main_class_name (str): name of your main class
            arguments (str): all the arugments you want to pass into the spark job
            job_tags (str): tags of the job, for exmaple you might want to put your user ID, or a tag with a certain information
            configuration (Dict[str, str]): Additional configs for the spark job
        """

        submission_params = json.loads(self.config_template)
        submission_params['run_name'] = job_name
        submission_params['libraries'][0]['jar'] = main_jar_path
        submission_params['new_cluster']['spark_conf'] = configuration
        submission_params['spark_jar_task']['parameters'] = arguments
        submission_params['spark_jar_task']['main_class_name'] = main_class_name
        self.res_job_id = None

        try:
            # For Job APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs
            r = requests.post(url=self.workspace_instance_url+'/api/2.0/jobs/runs/submit',
                              headers=self.auth_headers, data=json.dumps(submission_params))
            self.res_job_id = r.json()['run_id']
            # For Job APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs
            r = requests.get(url=self.workspace_instance_url+'/api/2.0/jobs/runs/get',
                             headers=self.auth_headers, params={'run_id': str(self.res_job_id)})
            logger.info('Feathr Job Submitted Sucessfully. View more details here: {}', r.json()[
                        'run_page_url'])
        except:
            traceback.print_exc()
        # return ID as the submission result
        return self.res_job_id

    def wait_for_completion(self, timeout_seconds: Optional[int] = 500) -> bool:
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
                return False
            else:
                time.sleep(30)
        else:
            raise TimeoutError('Timeout waiting for job to complete')

    def get_status(self) -> str:
        assert self.res_job_id is not None
        # For Job APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs
        job = r = requests.get(url=self.workspace_instance_url+'/api/2.0/jobs/runs/get',
                               headers=self.auth_headers, params={'run_id': str(self.res_job_id)})
        # first try to get result state. it might not be available, and if that's the case, try to get life_cycle_state
        res_state = r.json()['state'].get('result_state') or r.json()[
            'state']['life_cycle_state']
        assert res_state is not None
        return res_state

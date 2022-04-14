import base64
import json
import os
import time
import traceback
import urllib
from collections import namedtuple
from os.path import basename
from pathlib import Path
from typing import Any, Dict, List, Optional, Tuple, Union
from urllib.parse import urlparse
from urllib.request import urlopen

import requests
from loguru import logger
from requests.structures import CaseInsensitiveDict
from tqdm import tqdm

from feathr._abc import SparkJobLauncher
from feathr.constants import *


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

    def upload_or_get_cloud_path(self, local_path_or_http_path: str):
        """
        Supports transferring file from an http path to cloud working storage, or upload directly from a local storage.
        """
        src_parse_result = urlparse(local_path_or_http_path)
        file_name = os.path.basename(local_path_or_http_path)
        # returned paths for the uploaded file
        returned_path = os.path.join(self.databricks_work_dir, file_name)
        if src_parse_result.scheme.startswith('http'):
            with urlopen(local_path_or_http_path) as f:
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
            # else it should be a local file path or dir
            if os.path.isdir(local_path_or_http_path):
                logger.info("Uploading folder {}", local_path_or_http_path)
                dest_paths = []
                for item in Path(local_path_or_http_path).glob('**/*.conf'):
                    returned_path = self.upload_file(item.resolve())
                    dest_paths.extend([returned_path])
                returned_path = ','.join(dest_paths)
            else:
                returned_path = self.upload_file(local_path_or_http_path)
        return returned_path

    def upload_file(self, local_path_or_http_path: str) -> str:
        """
        Supports transferring file from an http path to cloud working storage, or upload directly from a local storage.
        """
        file_name = os.path.basename(local_path_or_http_path)
        # returned paths for the uploaded file
        returned_path = os.path.join(self.databricks_work_dir, file_name)
        with open(local_path_or_http_path, 'rb') as f:
            data = f.read()
            files = {'file': data}
            # for DBFS APIs, see: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs
            r = requests.post(url=self.workspace_instance_url+'/api/2.0/dbfs/put',
                              headers=self.auth_headers, files=files,  data={'overwrite': 'true', 'path': returned_path})
            logger.debug('{} is uploaded to location: {}',
                         local_path_or_http_path, returned_path)
        return returned_path

    def submit_feathr_job(self, job_name: str, main_jar_path: str,  main_class_name: str, arguments: List[str], python_files: List[str], reference_files_path: List[str] = [], job_tags: Dict[str, str] = None, configuration: Dict[str, str] = None):
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


        if isinstance(self.config_template, str):
            # if the input is a string, load it directly
            submission_params = json.loads(self.config_template)
        else:
            # otherwise users might have missed the quotes in the config.
            submission_params = self.config_template
            logger.warning("Databricks config template loaded in a non-string fashion. Please consider providing the config template in a string fashion.")

        submission_params['run_name'] = job_name
        submission_params['new_cluster']['spark_conf'] = configuration
        submission_params['new_cluster']['custom_tags'] = job_tags
        # the feathr main jar file is anyway needed regardless it's pyspark or scala spark
        submission_params['libraries'][0]['jar'] = self.upload_or_get_cloud_path(main_jar_path)
        # see here for the submission parameter definition https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--request-structure-6
        if python_files:
            # this is a pyspark job. definition here: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--sparkpythontask
            # the first file is the pyspark driver code. we only need the driver code to execute pyspark
            param_and_file_dict = {"parameters": arguments, "python_file": self.upload_or_get_cloud_path(python_files[0])}
            submission_params.setdefault('spark_python_task',param_and_file_dict)
        else:
            # this is a scala spark job
            submission_params['spark_jar_task']['parameters'] = arguments
            submission_params['spark_jar_task']['main_class_name'] = main_class_name


        # For Job runs APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-submit
        result = requests.post(url=self.workspace_instance_url+'/api/2.0/jobs/runs/submit',
                                headers=self.auth_headers, data=json.dumps(submission_params))

        try:
            # see if we can parse the returned result
            self.res_job_id = result.json()['run_id']
        except:
            logger.error("submitting to Databricks cluster failed. Message returned from Databricks: {}", result.json())
            exit(1)

        # For Job runs APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-submit
        result = requests.get(url=self.workspace_instance_url+'/api/2.0/jobs/runs/get',
                                headers=self.auth_headers, params={'run_id': str(self.res_job_id)})
        logger.info('Feathr Job Submitted Sucessfully. View more details here: {}', result.json()[
            'run_page_url'])

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
                return False
            else:
                time.sleep(30)
        else:
            raise TimeoutError('Timeout waiting for job to complete')

    def get_status(self) -> str:
        assert self.res_job_id is not None
        # For Job Runs APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-get
        result = requests.get(url=self.workspace_instance_url+'/api/2.0/jobs/runs/get',
                              headers=self.auth_headers, params={'run_id': str(self.res_job_id)})
        # first try to get result state. it might not be available, and if that's the case, try to get life_cycle_state
        res_state = result.json()['state'].get('result_state') or result.json()[
            'state']['life_cycle_state']
        assert res_state is not None
        return res_state

    def get_job_result_uri(self) -> str:
        """Get job output uri

        Returns:
            str: `output_path` field in the job tags
        """
        assert self.res_job_id is not None
        # For Job Runs APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-get
        result = requests.get(url=self.workspace_instance_url+'/api/2.0/jobs/runs/get',
                              headers=self.auth_headers, params={'run_id': str(self.res_job_id)})
        custom_tags = result.json()['cluster_spec']['new_cluster']['custom_tags']
        # in case users call this API even when there's no tags available
        return None if custom_tags is None else custom_tags[OUTPUT_PATH_TAG]


    def get_job_tags(self) -> Dict[str, str]:
        """Get job tags

        Returns:
            Dict[str, str]: a dict of job tags
        """
        assert self.res_job_id is not None
        # For Job Runs APIs, see https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/2.0/jobs#--runs-get
        result = requests.get(url=self.workspace_instance_url+'/api/2.0/jobs/runs/get',
                               headers=self.auth_headers, params={'run_id': str(self.res_job_id)})
        custom_tags = result.json()['cluster_spec']['new_cluster']['custom_tags']
        return custom_tags

    def download_result(self, result_path: str, local_folder: str):
        """
        Supports downloading files from the result folder. Only support paths starts with `dbfs:/` and only support downloading files in one folder (per Spark's design, everything will be in the result folder in a flat manner)
        """
        if not result_path.startswith('dbfs'):
            raise RuntimeError('Currently only paths starting with dbfs is supported for downloading results from a databricks cluster. The path should start with \"dbfs:\" .')

        try:
            # listing all the files in a folder: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs#--list
            result = requests.get(url=self.workspace_instance_url+'/api/2.0/dbfs/list',
                                headers=self.auth_headers,  params={ 'path': result_path})
            # see here for response structure: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs#--response-structure-2
            dbfs_files = result.json()['files']
            for file_path in tqdm(dbfs_files, desc="Downloading result files: "):
                # each file_path would be a dict of this type: https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs#dbfsfileinfo
                if not file_path['is_dir']:
                    # if it's not a directory
                    self._download_single_file(file_path, local_folder)
                else:
                    # if the result is dir
                    # us this path as the new folder path
                    folder_path = file_path['path']
                    folder_name = basename(folder_path)
                    result = requests.get(url=self.workspace_instance_url+'/api/2.0/dbfs/list',
                                headers=self.auth_headers,  params={ 'path': folder_path})
                    dbfs_files = result.json()['files']
                    for file_path in dbfs_files:
                        os.makedirs(os.path.join(local_folder,folder_name), exist_ok=True)
                        self._download_single_file(file_path, os.path.join(local_folder,folder_name))
                # Logging once all the download is finished.
            logger.info('Finish downloading files from {} to {}.', result_path,local_folder)
        except requests.exceptions.RequestException as e:  # This is the correct syntax
            raise SystemExit(e)

    def _download_single_file(self,file_path,local_folder):
        dbfs_file_path, dbfs_file_size, local_file_path = file_path['path'], file_path['file_size'], os.path.join(local_folder, os.path.basename(file_path['path']))
        with open(local_file_path, 'wb') as file_obj:
            downloaded_size = 0
            # Loop until we've downloaded the whole file
            while downloaded_size < dbfs_file_size:
                chunk = self._read_single_chunk(path=dbfs_file_path, offset=downloaded_size, length=MB_BYTES)
                file_obj.write(base64.b64decode(chunk.data))
                downloaded_size += chunk.bytes_read


    def _read_single_chunk(self, path, offset, length=MB_BYTES):

        params = {"path": path,
                  "offset": offset,
                  "length": length}

        # get a single chunk of file
        # https://docs.microsoft.com/en-us/azure/databricks/dev-tools/api/latest/dbfs#--read
        resp = requests.get(url=self.workspace_instance_url+'/api/2.0/dbfs/read',  headers=self.auth_headers,  params=params)
        FileReadInfo = namedtuple("FileReadInfo", ['bytes_read', 'data'])
        if resp.status_code == 200:
            return FileReadInfo(**resp.json())
        else:
            raise RuntimeError("Files cannot be downloaded.")

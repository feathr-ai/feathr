import time
from datetime import datetime
import json
import os
from pathlib import Path
from typing import Dict, List, Optional

from feathr.spark_provider._abc import SparkJobLauncher
from loguru import logger

from pyspark import *

from subprocess import TimeoutExpired, STDOUT, Popen
from shlex import split
from feathr.constants import FEATHR_MAVEN_ARTIFACT



class _FeathrDLocalSparkJobLauncher(SparkJobLauncher):
    """Class to interact with local Spark
        This class is not intended to be used in Production environments.
        It is intended to be used for testing and development purposes.
        No authentication is required to use this class.
        Args:
            workspace_path (str): Path to the workspace
    """
    def __init__(
        self,
        workspace_path: str,
        debug_folder:str = 'debug',
        clean_up:bool = True,
        retry:int = 3,
        retry_sec:int = 5,
    ):
        """Initialize the Local Spark job launcher
        """
        self.workspace_path = workspace_path,
        self.debug_folder = debug_folder
        self.spark_job_num = 0
        self.clean_up = clean_up
        self.retry = retry
        self.retry_sec = retry_sec

    def upload_or_get_cloud_path(self, local_path_or_http_path: str):
        """For Local Spark Case, no need to upload to cloud workspace."""
        return local_path_or_http_path

    def submit_feathr_job(self, job_name: str, main_jar_path: str = None,  main_class_name: str = None, arguments: List[str] = None,
                          python_files: List[str]= None, configuration: Dict[str, str] = {}, properties: Dict[str, str] = {}, reference_files_path: List[str] = None, job_tags: Dict[str, str] = None):
        """
        Submits the Feathr job to local spark, using subprocess args.

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
            arguments (str): all the arguments you want to pass into the spark job
            configuration (Dict[str, str]): Additional configs for the spark job
            python_files (List[str]): required .zip, .egg, or .py files of spark job
            properties (Dict[str, str]): Additional System Properties for the spark job
            job_tags (str): not used in local spark mode
            reference_files_path (str): not used in local spark mode
        """
        logger.warning(f"Local Spark Mode only support basic params right now and should be used only for testing purpose.")
        self.cmd_file, self.log_path = self._get_debug_file_name(self.debug_folder, prefix = job_name)
        args = self._init_args(job_name)

        if properties:
            arguments.append("--system-properties %s" % json.dumps(properties))

        if configuration:
            cfg = configuration.copy()  # We don't want to mess up input parameters
        else:
            cfg = {}
        
        if not main_jar_path:
            # We don't have the main jar, use Maven
            # Add Maven dependency to the job configuration
            if "spark.jars.packages" in cfg:
                cfg["spark.jars.packages"] = ",".join(
                    [cfg["spark.jars.packages"], FEATHR_MAVEN_ARTIFACT])
            else:
                default_packages = "org.apache.spark:spark-avro_2.12:3.3.0,com.microsoft.sqlserver:mssql-jdbc:10.2.0.jre8,com.microsoft.azure:spark-mssql-connector_2.12:1.2.0,org.apache.logging.log4j:log4j-core:2.17.2,com.typesafe:config:1.3.4,com.fasterxml.jackson.core:jackson-databind:2.12.6.1,org.apache.hadoop:hadoop-mapreduce-client-core:2.7.7,org.apache.hadoop:hadoop-common:2.7.7,org.apache.avro:avro:1.8.2,org.apache.xbean:xbean-asm6-shaded:4.10,org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.3,com.microsoft.azure:azure-eventhubs-spark_2.12:2.3.21,org.apache.kafka:kafka-clients:3.1.0,com.google.guava:guava:31.1-jre,it.unimi.dsi:fastutil:8.1.1,org.mvel:mvel2:2.2.8.Final,com.fasterxml.jackson.module:jackson-module-scala_2.12:2.13.3,com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.6,com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.12.6,com.jasonclawson:jackson-dataformat-hocon:1.1.0,com.redislabs:spark-redis_2.12:3.1.0,org.apache.xbean:xbean-asm6-shaded:4.10,com.google.protobuf:protobuf-java:3.19.4,net.snowflake:snowflake-jdbc:3.13.18,net.snowflake:spark-snowflake_2.12:2.10.0-spark_3.2,org.apache.commons:commons-lang3:3.12.0,org.xerial:sqlite-jdbc:3.36.0.3,com.github.changvvb:jackson-module-caseclass_2.12:1.1.1,com.azure.cosmos.spark:azure-cosmos-spark_3-1_2-12:4.11.1,org.eclipse.jetty:jetty-util:9.3.24.v20180605,commons-io:commons-io:2.6,org.apache.hadoop:hadoop-azure:2.7.4,com.microsoft.azure:azure-storage:8.6.4"
                cfg["spark.jars.packages"] = ",".join([default_packages, FEATHR_MAVEN_ARTIFACT])

            if not python_files:
                # This is a JAR job
                # Azure Synapse/Livy doesn't allow JAR job starts from Maven directly, we must have a jar file uploaded.
                # so we have to use a dummy jar as the main file.
                logger.info(f"Main JAR file is not set, using default package '{FEATHR_MAVEN_ARTIFACT}' from Maven")
                # Use the no-op jar as the main file
                # This is a dummy jar which contains only one `org.example.Noop` class with one empty `main` function which does nothing
                current_dir = Path(__file__).parent.resolve()
                main_jar_path = os.path.join(current_dir, "noop-1.0.jar")
                args.append("--packages %s" % cfg["spark.jars.packages"])
                args.append('--class %s' % main_class_name)
                args.append('%s '%main_jar_path)
            else:
                args.append("--packages %s" % cfg["spark.jars.packages"])
                # This is a PySpark job, no more things to 
                if python_files.__len__() > 1:
                    args.append('--py-files %s' % ",".join(python_files[1:]))
                print(python_files)
                args.append('%s '%python_files[0])
        else:
            args.append('--class %s' % main_class_name)
            args.append('%s '%main_jar_path)

        cmd = ' '.join(args) + ' '.join(arguments)

        log_a = open(f'{self.log_path}_{self.spark_job_num}.txt' , 'a')     
        proc = Popen(split(cmd), shell=False, stdout=log_a, stderr=STDOUT)
        logger.info(f"Detail job stdout and stderr are in {self.log_path}.")

        self.spark_job_num += 1

        with open(self.cmd_file, 'a') as c:
                c.write(' '.join(proc.args))
                c.write('\n')

        self.latest_spark_proc = proc

        logger.info(f"Local Spark job submit with pid: {proc.pid}.")

        return proc

    def wait_for_completion(self, timeout_seconds: Optional[float] = 500) -> bool:
        """
        this function track local spark job commands and process status.
        files will be write into `debug` folder under your workspace.
        """
        logger.info(f"{self.spark_job_num} local spark job(s) in this Launcher, only the latest will be monitored.")
        logger.info(f"Please check auto generated spark command in {self.cmd_file} and detail logs in {self.log_path}.")

        proc = self.latest_spark_proc
        start_time = time.time()
        retry = self.retry

        log_r = open(f'{self.log_path}_{self.spark_job_num-1}.txt' , 'r') 
        while proc.poll() is None and (((timeout_seconds is None) or (time.time() - start_time < timeout_seconds))):
            time.sleep(1)
            try:
                if retry < 1:
                    logger.warning(f"Spark job has hang for {self.retry * self.retry_sec} seconds. latest msg is {last_line}. please check {log_r.name}")
                    if self.clean_up:
                        self._clean_up()
                        proc.wait()
                    break
                last_line = log_r.readlines()[-1]
                retry = self.retry
                if last_line == []:
                    print("_", end="")
                else:
                    print(">", end="")
                    if last_line.__contains__('Feathr Pyspark job completed'):
                        logger.info(f"Pyspark job Completed")
                        proc.terminate()
            except IndexError as e:
                print("x", end="")
                time.sleep(self.retry_sec)
                retry -= 1

        job_duration = time.time() - start_time
        log_r.close() 

        if proc.returncode == None:
            logger.warning(f"Spark job not completed after {timeout_seconds} sec time out setting, please check.")
            if self.clean_up:
                self._clean_up()
                proc.wait()
                return True
        elif proc.returncode == 1:
            logger.warning(f"Spark job is not successful, please check.")
            return False
        else:
            logger.info(f"Spark job {self.latest_spark_proc.pid} finished in: {int(job_duration)} seconds with returncode {proc.returncode}")
            return True

    def _clean_up(self, proc:Popen = None):
        logger.warning(f"Terminate the spark job due to as clean_up is set to True.")
        if not proc:
            self.latest_spark_proc.terminate()
        else:
            proc.terminate()

    def get_status(self) -> str:
        """Get the status of the job, only a placeholder for local spark"""
        return self.latest_spark_proc.returncode

    def _init_args(self, job_name:str):
        args = []
        args.append('spark-submit')
        args.append('--master local[*]')
        args.append('--name %s' % job_name)
        args.append('--conf "spark.driver.extraClassPath=../target/scala-2.12/classes:jars/config-1.3.4.jar:jars/jackson-dataformat-hocon-1.1.0.jar:jars/jackson-module-caseclass_2.12-1.1.1.jar:jars/mvel2-2.2.8.Final.jar:jars/fastutil-8.1.1.jar"' )
        args.append('--conf "spark.hadoop.fs.wasbs.impl=org.apache.hadoop.fs.azure.NativeAzureFileSystem"')
        args.append('--conf "spark.hadoop.fs.wasbs=org.apache.hadoop.fs.azure.NativeAzureFileSystem"')

        return args

    def _get_debug_file_name(self, debug_folder: str = 'debug', prefix:str = None):
        """
        auto generated command will be write into cmd file
        spark job output will be write into log path with job number as suffix
        """
        prefix += datetime.now().strftime("%Y%m%d%H%M%S")
        debug_path = os.path.join(debug_folder, prefix)

        print(debug_path)
        if not os.path.exists(debug_path):
                os.makedirs(debug_path)

        cmd_file = os.path.join(debug_path, f"command.sh")
        log_path = os.path.join(debug_path, f"log")

        return cmd_file, log_path
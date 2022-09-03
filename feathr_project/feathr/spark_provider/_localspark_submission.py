from datetime import datetime
import os
from string import Template
from pathlib import Path
from typing import Optional

from feathr.utils.spark_job_params import FeatureGenerationJobParams, FeatureJoinJobParams
from feathr.spark_provider._abc import SparkJobLauncher
from loguru import logger

from pyspark import *

from subprocess import run, STDOUT
from feathr.constants import GEN_CLASS_NAME, JOIN_CLASS_NAME, FEATHR_MAVEN_ARTIFACT



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
        workspace_path: str
    ):
        """Initialize the Local Spark job launcher
        """
        self.workspace_path = workspace_path

    def upload_or_get_cloud_path(self, local_path_or_http_path: str):
        """For Local Spark Case, no need to upload to cloud workspace."""
        return local_path_or_http_path

    def submit_feathr_job(self, job_name: str, configs: FeatureGenerationJobParams | FeatureJoinJobParams, redis_config:str = None, main_jar_path: str = None, python_files: str = None, num_parts: int = 1, debug: bool = True):
        logger.warning(f"Local Spark Mode only support basic params right now and only for testing purpose.")
        now = datetime.now().strftime("%Y%m%d%H%M%S")
        current_dir = Path(__file__).parent.resolve()

        self._get_packages(main_jar_path, python_files)

        if isinstance(configs, FeatureJoinJobParams):
            template_file = os.path.join(current_dir, 'local_spark_utils', 'join_template.sh')
            with open(template_file) as t:
                template = Template(t.read())
            
            command = template.substitute(
                jobName=job_name,
                packages = self.packages,
                mainJar=self.main_jar_path,
                joinConfig=configs.join_config_path,
                input=configs.observation_path,
                output=configs.job_output_path,
                featureConfig=self._get_feature_config(configs.feature_config),
                numParts=num_parts,
            )
        elif isinstance(configs, FeatureGenerationJobParams):
            logger.warning("Please notice that UDF and extra feature config are not supported in local spark mode yet.")
            template_file = os.path.join(current_dir, 'local_spark_utils', 'gen_template.sh')
            with open(template_file, 'r') as t:
                template = Template(t.read())
            
            command = template.substitute(
                jobName=job_name,
                packages = self.packages,
                mainJar=self.main_jar_path,
                genConfig = configs.generation_config_path,
                featureConfig = self._get_feature_config(configs.feature_config),
                redisConfig = redis_config,
            )
        else:
            raise ValueError("Config type not supported in Local Spark Mode.")
        
        if debug:
            debug_folder = "debug"
            if not os.path.exists(debug_folder):
                os.makedirs(debug_folder)
            
            command_file = os.path.join(debug_folder, f"auto_generated_command_{now}.sh")
            with open(command_file, 'w') as f:
                f.write(command)
        
            logger.info(f"Auto generated spark-submit command is stored in {command_file} for testing purpose.")

            log_file = os.path.join(debug_folder, f"{job_name}_log_{now}.txt")
            with open(log_file, 'w') as log:
                result = run(command, shell=True, stdout=log, stderr=STDOUT)
            logger.info(f"Detail job log can be find in: {log_file}")
        else:
            result = run(command, shell=True)
        
        return result

    def wait_for_completion(self, timeout_seconds: Optional[float]) -> bool:
        return super().wait_for_completion(timeout_seconds)

    def get_status(self) -> str:
        return super().get_status()

    def _get_command_template(self, main_class_name:str):
        """Get the command template for spark-submit"""
        current_dir = Path(__file__).parent.resolve()

        if main_class_name ==  JOIN_CLASS_NAME:
            template_file = os.path.join(current_dir, 'local_spark_utils', 'join_template.sh')
        elif main_class_name == GEN_CLASS_NAME:
            template_file = os.path.join(current_dir, 'local_spark_utils','gen_template.sh')
        else:
            raise ValueError("Main Class not supported in Local Spark Mode.")
        
        with open(template_file) as t:
             return Template(t.read())
    
    def _get_feature_config(self, feature_config:str):
        """Get the feature config path with folder name"""
        feature_conf_folder = "feature_conf"
        return f"{os.path.join(feature_conf_folder, 'auto_generated_request_features.conf')},{os.path.join(feature_conf_folder,'auto_generated_anchored_features.conf')},{os.path.join(feature_conf_folder,'auto_generated_derived_features.conf')}"

    def _get_packages(self, main_jar_path:str = None, python_files:str = None):
        """Get the packages and modify JAR Path for spark-submit"""
        #TODO: make package list configurable
        self.packages = "org.apache.spark:spark-avro_2.12:3.3.0,com.microsoft.sqlserver:mssql-jdbc:10.2.0.jre8,com.microsoft.azure:spark-mssql-connector_2.12:1.2.0,org.apache.logging.log4j:log4j-core:2.17.2,com.typesafe:config:1.3.4,com.fasterxml.jackson.core:jackson-databind:2.12.6.1,org.apache.hadoop:hadoop-mapreduce-client-core:2.7.7,org.apache.hadoop:hadoop-common:2.7.7,org.apache.avro:avro:1.8.2,org.apache.xbean:xbean-asm6-shaded:4.10,org.apache.spark:spark-sql-kafka-0-10_2.12:3.1.3,com.microsoft.azure:azure-eventhubs-spark_2.12:2.3.21,org.apache.kafka:kafka-clients:3.1.0,com.google.guava:guava:31.1-jre,it.unimi.dsi:fastutil:8.1.1,org.mvel:mvel2:2.2.8.Final,com.fasterxml.jackson.module:jackson-module-scala_2.12:2.13.3,com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.12.6,com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.12.6,com.jasonclawson:jackson-dataformat-hocon:1.1.0,com.redislabs:spark-redis_2.12:3.1.0,org.apache.xbean:xbean-asm6-shaded:4.10,com.google.protobuf:protobuf-java:3.19.4,net.snowflake:snowflake-jdbc:3.13.18,net.snowflake:spark-snowflake_2.12:2.10.0-spark_3.2,org.apache.commons:commons-lang3:3.12.0,org.xerial:sqlite-jdbc:3.36.0.3,com.github.changvvb:jackson-module-caseclass_2.12:1.1.1,com.azure.cosmos.spark:azure-cosmos-spark_3-1_2-12:4.11.1,org.eclipse.jetty:jetty-util:9.3.24.v20180605,commons-io:commons-io:2.6,org.apache.hadoop:hadoop-azure:2.7.4,com.microsoft.azure:azure-storage:8.6.4"
        if not main_jar_path or main_jar_path.__contains__('noop'):
            # When main jar is not provided, we assume the user is using the Maven jar
            # Add Maven jar to the packages
            logger.info("No main jar is provided, using Maven jar instead.")
            self.packages = ",".join([self.packages, FEATHR_MAVEN_ARTIFACT])
            if not python_files:
                current_dir = Path(__file__).parent.resolve()
                self.main_jar_path = os.path.join(current_dir, "noop-1.0.jar")
            else:
                self.main_jar_path = python_files
        else:
            self.main_jar_path = main_jar_path
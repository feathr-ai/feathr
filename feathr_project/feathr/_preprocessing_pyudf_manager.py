import inspect
import os
from pathlib import Path
from typing import List, Optional, Union
import pickle
from jinja2 import Template
from feathr.source import HdfsSource

# Some metadata that are only needed by Feathr
FEATHR_PYSPARK_METADATA = 'generated_feathr_pyspark_metadata'
# UDFs that are provided by users and persisted by Feathr into this file.
# It will be uploaded into the Pyspark cluster as a dependency for execution
FEATHR_CLIENT_UDF_FILE_NAME = 'client_udf_repo.py'
# Pyspark driver code that is executed by the Pyspark driver
FEATHR_PYSPARK_DRIVER_FILE_NAME = 'feathr_pyspark_driver.py'
# Feathr provided imports for pyspark UDFs all go here
PROVIDED_IMPORTS = ['from pyspark.sql import SparkSession, DataFrame\n'] + \
                   ['from pyspark.sql.functions import *\n']


class _PreprocessingPyudfManager(object):
    """This class manages Pyspark UDF preprocessing related artifacts, like UDFs from users, the pyspark_client etc.
    """
    def build_anchor_preprocessing_metadata(self, anchor_list, local_workspace_dir):
        # feature names concatenated to UDF map
        # for example, {'f1,f2,f3': my_udf1, 'f4,f5':my_udf2}
        feature_names_to_func_mapping = {}
        # features that have preprocessing defined. This is used to figure out if we need to kick off Pyspark
        # preprocessing for requested features.
        features_with_preprocessing = []
        need_preprocessing = False
        for anchor in anchor_list:
            # only support batch source preprocessing for now.
            if not isinstance(anchor.source, HdfsSource):
                continue
            preprocessing_func = anchor.source.preprocessing
            if preprocessing_func:
                need_preprocessing = True

                self.persist_pyspark_udf_to_file(preprocessing_func, local_workspace_dir)
                feature_names = [feature.name for feature in anchor.features]
                features_with_preprocessing = features_with_preprocessing + feature_names
                feature_names.sort()
                string_feature_list = ','.join(feature_names)
                feature_names_to_func_mapping[string_feature_list] = anchor.source.preprocessing

        if not need_preprocessing:
            return

        self.write_feature_names_to_udf_name_file(feature_names_to_func_mapping, local_workspace_dir)

        # Save necessary preprocessing-related metadata locally in your workspace
        # Typically it's used as a metadata for join/gen job to figure out if there is preprocessing UDF
        # exist for the features requested
        feathr_pyspark_metadata_abs_path = os.path.join(local_workspace_dir, FEATHR_PYSPARK_METADATA)
        file = open(feathr_pyspark_metadata_abs_path, 'wb')
        pickle.dump(features_with_preprocessing, file)
        file.close()

    def persist_pyspark_udf_to_file(self, user_func, local_workspace_dir):
        udf_source_code = inspect.getsourcelines(user_func)[0]
        lines = []
        # Some basic imports will be provided
        lines = lines + PROVIDED_IMPORTS
        lines = lines + udf_source_code
        lines.append('\n')

        client_udf_repo_path = os.path.join(local_workspace_dir, FEATHR_CLIENT_UDF_FILE_NAME)

        # the directory may actually not exist yet, so create the directory first
        file_name_start = client_udf_repo_path.rfind("/")
        if file_name_start > 0:
            dir_name = client_udf_repo_path[:file_name_start]
            Path(dir_name).mkdir(parents=True, exist_ok=True)

        if Path(client_udf_repo_path).is_file():
            with open(client_udf_repo_path, "a") as handle:
                print("".join(lines), file=handle)
        else:
            with open(client_udf_repo_path, "w") as handle:
                print("".join(lines), file=handle)

    def write_feature_names_to_udf_name_file(self, feature_names_to_func_mapping, local_workspace_dir):
        """Persist feature names(sorted) of an anchor to the corresponding preprocessing function name to source path
        under the local_workspace_dir.
        """
        # indent in since python needs correct indentation
        # Don't change the indentation
        tm = Template("""
feature_names_funcs = {
{% for key, value in func_maps.items() %}
    "{{key}}" : {{value.__name__}},
{% endfor %}
}
        """)
        new_file = tm.render(func_maps=feature_names_to_func_mapping)

        full_file_name = os.path.join(local_workspace_dir, FEATHR_CLIENT_UDF_FILE_NAME)
        with open(full_file_name, "a") as text_file:
            print(new_file, file=text_file)

    def prepare_pyspark_udf_files(self, feature_names: List[str], local_workspace_dir):
        py_udf_files = []

        # Load pyspark_metadata which stores what features contains preprocessing UDFs
        feathr_pyspark_metadata_abs_path = os.path.join(local_workspace_dir, FEATHR_PYSPARK_METADATA)
        # if the preprocessing metadata file doesn't exist or is empty, then we just skip
        if not Path(feathr_pyspark_metadata_abs_path).is_file():
            return py_udf_files
        pyspark_metadata_file = open(feathr_pyspark_metadata_abs_path, 'rb')
        features_with_preprocessing = pickle.load(pyspark_metadata_file)
        pyspark_metadata_file.close()
        # if there is not features that needs preprocessing, just return.
        if not features_with_preprocessing:
            return py_udf_files

        # Figure out if we need to preprocessing via UDFs for requested features.
        # Only if the requested features contain preprocessing logic, we will load Pyspark. Otherwise just use Scala
        # spark.
        has_py_udf_preprocessing = False
        for feature_name in feature_names:
            if feature_name in features_with_preprocessing:
                has_py_udf_preprocessing = True

        # Locate and collect all the python files that needs to be uploaded
        if has_py_udf_preprocessing:
            # pyspark_client should be first in the file since the Spark driver will execute the first file
            pyspark_client_abs_path = str(Path(Path(__file__).parent / FEATHR_PYSPARK_DRIVER_FILE_NAME).absolute())
            py_udf_files = [pyspark_client_abs_path] + py_udf_files

            # UDFs are persisted into the temporary client_udf_repo.py
            client_udf_repo_abs_path = os.path.join(local_workspace_dir, FEATHR_CLIENT_UDF_FILE_NAME)
            py_udf_files.append(client_udf_repo_abs_path)
        return py_udf_files

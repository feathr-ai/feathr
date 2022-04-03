import inspect
import os
from pathlib import Path
from typing import List, Optional, Union
from feathr.query_feature_list import FeatureQuery
import pickle
from jinja2 import Template

FEATHR_PYSPARK_METADATA = "generated_feathr_pyspark_metadata"


class _PreprocessingPyudfManager(object):
    def build_anchor_preprocessing_metadata(self, anchor_list, local_workspace_dir):
        func_maps = {}
        feature_names_to_func_mapping = {}
        # Features that have preprocessing UDFs defined in list form
        features_with_preprocessing = []
        needPreprocessing = False
        for anchor in anchor_list:
            if anchor.preprocessing:
                needPreprocessing = True

                self.persist_pyspark_udf_to_file(anchor.preprocessing, local_workspace_dir)
                feature_names = [feature.name for feature in anchor.features]
                features_with_preprocessing = features_with_preprocessing + feature_names
                feature_names.sort()
                string_feature_list = ','.join(feature_names)
                # TODO: how to ensure the func name is unique?
                # what if 2 func names conflict?
                feature_names_to_func_mapping[string_feature_list] = anchor.preprocessing
                func_maps[string_feature_list] = anchor.source.path

        if not needPreprocessing:
            return

        self.write_feature_names_to_source_path_to_file(func_maps, local_workspace_dir)
        self.write_feature_names_to_udf_name_file(feature_names_to_func_mapping, local_workspace_dir)

        # Save necessary preprocessing-related metadata locally in your workspace
        # Typically it's used as a metadata for join/gen job to figure out if there is preprocessing UDF
        # exist for the features requested
        feathr_pyspark_metadata_abs_path = os.path.join(local_workspace_dir, FEATHR_PYSPARK_METADATA)
        file = open(feathr_pyspark_metadata_abs_path, 'wb')
        pickle.dump(features_with_preprocessing, file)
        file.close()

    def persist_pyspark_udf_to_file(self, user_func, local_workspace_dir):
        # Some basic imports will be provided
        udf_source_code = inspect.getsourcelines(user_func)[0]
        lines = []
        # Feathr provided imports for pyspark UDFs all go here
        provided_imports = ["from pyspark.sql import SparkSession, DataFrame\n"] + \
                           ['from pyspark.sql.functions import *\n']
        lines = lines + provided_imports
        lines = lines + udf_source_code
        lines.append('\n')

        client_udf_repo_path = os.path.join(local_workspace_dir, "client_udf_repo.py")

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

    def write_feature_names_to_udf_name_file(self, func_maps, local_workspace_dir):
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
        new_file = tm.render(func_maps=func_maps)

        full_file_name = os.path.join(local_workspace_dir, "client_udf_repo.py")
        with open(full_file_name, "a") as text_file:
            print(new_file, file=text_file)

    def write_feature_names_to_source_path_to_file(self, func_maps, local_workspace_dir):
        """Persist feature names(sorted) of an anchor to the corresponding preprocessing function name to client_udf_repo
        under the local_workspace_dir.
        """
        # indent in since python needs correct indentation
        # Don't change the indentation
        tm = Template("""
preprocessed_funcs = {
{% for key, value in func_maps.items() %}
    "{{key}}" : "{{value}}",
{% endfor %}
}
        """)
        new_file = tm.render(func_maps=func_maps)

        full_file_name = os.path.join(local_workspace_dir, "client_udf_repo.py")
        with open(full_file_name, "a") as text_file:
            print(new_file, file=text_file)

    def prepare_pyspark_udf_files(self, feature_queries: List[FeatureQuery], local_workspace_dir):
        py_udf_files = []

        # Load pyspark_metadata which stores what features contains preprocessing UDFs
        feathr_pyspark_metadata_abs_path = os.path.join(local_workspace_dir, FEATHR_PYSPARK_METADATA)
        print(feathr_pyspark_metadata_abs_path)
        if not Path(feathr_pyspark_metadata_abs_path).is_file():
            print("not file")
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
        for feature_query in feature_queries:
            for feature_name in feature_query.feature_list:
                if feature_name in features_with_preprocessing:
                    has_py_udf_preprocessing = True

        # Locate and collect all the python files that needs to be uploaded
        if has_py_udf_preprocessing:
            # pyspark_client should be first in the file since the Spark driver will execute the first file
            pyspark_client_abs_path = str(Path(Path(__file__).parent / 'pyspark_client.py').absolute())
            py_udf_files = [pyspark_client_abs_path] + py_udf_files

            # UDFs are persisted into the temporary client_udf_repo.py
            client_udf_repo_abs_path = os.path.join(local_workspace_dir, "client_udf_repo.py")
            py_udf_files.append(client_udf_repo_abs_path)
        return py_udf_files

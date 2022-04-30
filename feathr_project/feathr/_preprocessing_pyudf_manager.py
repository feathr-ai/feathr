import inspect
import os
from pathlib import Path
import sys
from typing import List, Optional, Union
import pickle
from jinja2 import Template
from numpy import append
from feathr.source import HdfsSource
from pyspark import cloudpickle

# Some metadata that are only needed by Feathr
FEATHR_PYSPARK_METADATA = 'generated_feathr_pyspark_metadata'
# UDFs that are provided by users and persisted by Feathr into this file.
# It will be uploaded into the Pyspark cluster as a dependency for execution
FEATHR_CLIENT_UDF_FILE_NAME = 'client_udf_repo.py'
# Pyspark driver code that is executed by the Pyspark driver
FEATHR_PYSPARK_DRIVER_FILE_NAME = 'feathr_pyspark_driver.py'
FEATHR_PYSPARK_DRIVER_TEMPLATE_FILE_NAME = 'feathr_pyspark_driver_template.py'
# Feathr provided imports for pyspark UDFs all go here
PROVIDED_IMPORTS = ['\nfrom pyspark.sql import SparkSession, DataFrame\n'] + \
                   ['from pyspark.sql.functions import *\n'] + \
                   ['from pyspark import cloudpickle\n']


class _PreprocessingPyudfManager(object):
    """This class manages Pyspark UDF preprocessing related artifacts, like UDFs from users, the pyspark_client etc.
    """
    @staticmethod
    def build_anchor_preprocessing_metadata(anchor_list, local_workspace_dir):
        """When the client build features, UDFs and features that need preprocessing will be stored as metadata. Those
        metadata will later be used when uploading the Pyspark jobs.
        """
        # feature names concatenated to UDF callable object map, it is like:
        # {
        #   'f1,f2': cloudpickle.loads('...pickledcode...'),
        #   'f3': cloudpickle.loads('...pickledcode...'),
        # }
        feature_names_to_func_mapping = {}
        # features that have preprocessing defined. This is used to figure out if we need to kick off Pyspark
        # preprocessing for requested features.
        features_with_preprocessing = []
        dep_modules = set()
        for anchor in anchor_list:
            # only support batch source preprocessing for now.
            if not isinstance(anchor.source, HdfsSource):
                continue
            preprocessing_func = anchor.source.preprocessing
            if preprocessing_func:
                # Record module needed by all preprocessing_func
                for feature in anchor.features:
                    # Record module name except __main__
                    if preprocessing_func.__module__ != "__main__":
                        dep_modules.add(preprocessing_func.__module__)
                feature_names = [feature.name for feature in anchor.features]
                features_with_preprocessing = features_with_preprocessing + feature_names
                feature_names.sort()
                string_feature_list = ','.join(feature_names)
                feature_names_to_func_mapping[string_feature_list] = "cloudpickle.loads(%s)" % cloudpickle.dumps(preprocessing_func, protocol=pickle.DEFAULT_PROTOCOL)

        if not features_with_preprocessing:
            return

        _PreprocessingPyudfManager.write_feature_names_to_udf_name_file(feature_names_to_func_mapping, dep_modules, local_workspace_dir)

        # Save necessary preprocessing-related metadata locally in your workspace
        # Typically it's used as a metadata for join/gen job to figure out if there is preprocessing UDF
        # exist for the features requested
        feathr_pyspark_metadata_abs_path = os.path.join(local_workspace_dir, FEATHR_PYSPARK_METADATA)
        with open(feathr_pyspark_metadata_abs_path, 'wb') as file:
            pickle.dump(features_with_preprocessing, file)

    @staticmethod
    def write_feature_names_to_udf_name_file(feature_names_to_func_mapping, dep_modules, local_workspace_dir):
        """Persist feature names(sorted) of an anchor to the corresponding preprocessing function name to source path
        under the local_workspace_dir.
        """
        file_contents = []
        for m in dep_modules:
            """
            The `feature_names_funcs` map contents a `cloudpickle.loads` statement that deserialize the
            UDF function, due to the implementation limitation, cloudpickle shipped with PySpark cannot
            actually serialize the function body if the function is in a module other than `__main__`,
            which causes PyTest failed to run the UDF E2E test as it starts the test code in a local module
            instead of `__main__`.
            The solution is to bundle addition module with the main file.
            First we encode module file in base64 and paste it in following format
            ```
            decode_file("module_name.py", r'''BASE64_ENCODED_CONTENT''')
            ```
            Then we can use `import module_name` to import this module, then `cloudpickle.loads` can work.
            When the PySpark program is running, it first uses `decode_file` function in the template
            to extract module file and drop it into the same directory as the main code, then we can run
            `import UDF_module` and uses functions inside.
            """
            if m != "__main__":
                try:
                    filename = sys.modules[m].__file__
                    content = encode_file(filename)
                    file_contents.append( (os.path.basename(filename), content) )
                except:
                    pass
        
        # indent in since python needs correct indentation
        # Don't change the indentation
        tm = Template(r"""
{% for f in file_contents %}
decode_file('{{f[0]}}', r'''{{f[1]}}''')
{% endfor %}
{% for module in dep_modules %}
import {{module}}
{% endfor %}
feature_names_funcs = {
{% for key, value in func_maps.items() %}
    "{{key}}" : {{value}},
{% endfor %}
}
        """)
        new_file = tm.render(file_contents=file_contents, dep_modules=dep_modules, func_maps=feature_names_to_func_mapping)

        os.makedirs(local_workspace_dir, mode=0o777, exist_ok=True)
        full_file_name = os.path.join(local_workspace_dir, FEATHR_CLIENT_UDF_FILE_NAME)
        with open(full_file_name, "w+") as text_file:
            print("".join(PROVIDED_IMPORTS), file=text_file)
            print(new_file, file=text_file)

    @staticmethod
    def prepare_pyspark_udf_files(feature_names: List[str], local_workspace_dir):
        """Prepare the Pyspark driver code that will be executed by the Pyspark cluster.
        The Pyspark driver code file has two parts:
            1. The driver code itself
            2. The UDFs.
            3. The features that need preprocessing in a map(from feature names to UDFs)
        """
        py_udf_files = []

        # Load pyspark_metadata which stores what features contains preprocessing UDFs
        feathr_pyspark_metadata_abs_path = os.path.join(local_workspace_dir, FEATHR_PYSPARK_METADATA)
        # if the preprocessing metadata file doesn't exist or is empty, then we just skip
        if not Path(feathr_pyspark_metadata_abs_path).is_file():
            return py_udf_files
        with open(feathr_pyspark_metadata_abs_path, 'rb') as pyspark_metadata_file:
            features_with_preprocessing = pickle.load(pyspark_metadata_file)
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
                break

        if has_py_udf_preprocessing:
            pyspark_driver_path = os.path.join(local_workspace_dir, FEATHR_PYSPARK_DRIVER_FILE_NAME)
            pyspark_driver_template_abs_path = str(Path(Path(__file__).parent / FEATHR_PYSPARK_DRIVER_TEMPLATE_FILE_NAME).absolute())
            client_udf_repo_path = os.path.join(local_workspace_dir, FEATHR_CLIENT_UDF_FILE_NAME)
            # write pyspark_driver_template_abs_path and then client_udf_repo_path
            filenames = [pyspark_driver_template_abs_path, client_udf_repo_path]
            with open(pyspark_driver_path, 'w') as outfile:
                for fname in filenames:
                    with open(fname) as infile:
                        for line in infile:
                            outfile.write(line)
            lines = [
                '\n',
                'print("pyspark_client.py: Preprocessing via UDFs and submit Spark job.")\n',
                'submit_spark_job(feature_names_funcs)\n',
                'print("pyspark_client.py: Feathr Pyspark job completed.")\n',
                '\n',
            ]
            with open(pyspark_driver_path, "a") as handle:
                print("".join(lines), file=handle)

            py_udf_files = [pyspark_driver_path]
        return py_udf_files

import base64
def encode_file(filename: str) -> str:
    """
    Encode file content into a multiline base64 encoded string.
    This function is a replacement of the previous `inspect.getsource()`,
    it bundles the whole module file instead of a single function to resolve
    the importing issue.
    Using base64 instead of the original source code to archive
    the source file can avoid complicated escaping process, we can
    directly quote base64 string in the source code by surrounding
    it with `r'''/'''`.
    """
    f = open(filename, "rb")
    content = f.read()
    encoded = base64.b64encode(content).decode('ascii')
    n = 80
    chunks = [encoded[i:i+n] for i in range(0, len(encoded), n)] 
    return '\n'.join(chunks)
    
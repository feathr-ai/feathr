from setuptools import setup, find_packages
from pathlib import Path

# Use the README.md from /docs
root_path = Path(__file__).resolve().parent.parent
long_description = (root_path / "docs/README.md").read_text()

setup(
    name='feathr',
    version='0.8.0',
    long_description=long_description,
    long_description_content_type="text/markdown",
    author_email="feathr-technical-discuss@lists.lfaidata.foundation",
    description="An Enterprise-Grade, High Performance Feature Store",
    url="https://github.com/feathr-ai/feathr",
    project_urls={
        "Bug Tracker": "https://github.com/feathr-ai/feathr/issues",
    },
    packages=find_packages(),
    include_package_data=True,
    # consider
    install_requires=[
        'click<=8.1.3',
        "py4j<=0.10.9.7",
        "loguru<=0.6.0",
        "pandas<=1.5.0",
        "redis<=4.4.0",
        "requests<=2.28.1",
        "tqdm<=4.64.1",
        "pyapacheatlas<=0.14.0",
        "pyhocon<=0.3.59",
        "pandavro<=1.7.1",
        "pyyaml<=6.0",
        "Jinja2<=3.1.2",
        "pyarrow<=9.0.0",
        "pyspark>=3.1.2",
        "python-snappy<=0.6.1",
        # fixing https://github.com/feathr-ai/feathr/issues/687
        "deltalake<=0.5.8",
        "graphlib_backport<=1.0.3",
        "protobuf==3.*",
        "confluent-kafka<=1.9.2",
        "databricks-cli<=0.17.3",
        "avro<=1.11.1",
        "azure-storage-file-datalake<=12.5.0",
        "azure-synapse-spark<=0.7.0",
        # fixing Azure Machine Learning authentication issue per https://stackoverflow.com/a/72262694/3193073
        "azure-identity>=1.8.0",
        "azure-keyvault-secrets<=4.6.0",
        # In 1.23.0, azure-core is using ParamSpec which might cause issues in some of the databricks runtime.
        # see this for more details:
        # https://github.com/Azure/azure-sdk-for-python/pull/22891
        # using a version lower than that to workaround this issue.
        "azure-core<=1.22.1",
        "typing_extensions>=4.2.0"
    ],
    tests_require=[
        'pytest',
    ],
    entry_points={
        'console_scripts': ['feathr=feathrcli.cli:cli']
    },
    classifiers=[
        "Programming Language :: Python :: 3",
        "License :: OSI Approved :: Apache Software License",
        "Operating System :: OS Independent",
    ],
    python_requires=">=3.7"
)

from setuptools import setup, find_packages

setup(
    name='feathr',
    version='0.1.0',
    packages=find_packages(),
    include_package_data=True,
    install_requires=[
        'Click',
        "azure-storage-file-datalake",
        "azure-synapse-spark",
        "azure-identity",
        "py4j",
        "loguru",
        "pandas",
        "redis",
        "requests",
        "scikit-learn",
        "pyapacheatlas",
        "pyhocon",
        "pandavro",
        "python-snappy",
        "pyyaml",
    ],
    entry_points={
        'console_scripts': ['feathr=feathrcli.cli:cli']
    }
)
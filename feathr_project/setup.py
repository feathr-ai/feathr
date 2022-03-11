from setuptools import setup, find_packages
from pathlib import Path

# Use the README.md in root project
root_path = Path(__file__).resolve().parent.parent
long_description = (root_path / "README.md").read_text()

setup(
    name='feathr',
    version='0.1.1',
    long_description=long_description,
    long_description_content_type="text/markdown",
    author_email="frame_dev@linkedin.com",
    description="An Enterprise-Grade, High Performance Feature Store",
    url="https://github.com/linkedin/feathr",
    project_urls={
        "Bug Tracker": "https://github.com/linkedin/feathr/issues",
    },
    packages=find_packages(),
    include_package_data=True,
    install_requires=[
        'Click',
        "azure-storage-file-datalake>=12.5.0",
        "azure-synapse-spark>=0.7.0",
        "azure-identity",
        "py4j",
        "loguru",
        "pandas",
        "redis",
        "requests",
        "pyapacheatlas",
        "pyhocon",
        "pandavro",
        "python-snappy",
        "pyyaml",
        "Jinja2"
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
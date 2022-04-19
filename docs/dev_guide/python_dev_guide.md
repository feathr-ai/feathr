---
layout: default
title: Feathr Python Project Developer Guide
parent: Feathr Developer Guides
---

# Feathr Python Project Developer Guide

## Installation

- Navigate to feathr_project folder
- Install the project by `python3 -m pip install -e .` This will install a feathr CLI for you. Type `feathr` in the terminal to see the instructions.

## CLI Usage

- Run `feathr` in your terminal to see the instructions.
- Run `feathr init` to create a new workspace
- Navigate to the new workspace
- Feathr requires a local engine packaged in the jar to test features locally. you can download the jar by `feathr start`.
- Run `feathr test`, then type in a feature, like feature_a
- After features are fully tested, you can create your training dataset by `feathr join`. You can also materialize your features to online storage by `feathr deploy`.
- You can register your features to the metadata registry by `feathr register`

## Python Coding Style Guide

We use [Google Python Style Guide](https://google.github.io/styleguide/pyguide.html).

## Integration Test

Run `pytest` in this folder to kick off the integration test. The integration test will test the creation of feature dataset, the materialization to online storage, and retrieve from online storage, as well as the CLI. It usually takes 5 ~ 10 minutes. It needs certain keys for cloud resources.


## Using Virtual Environment

It's recommended to use virtual environment for Python project development.

## Using Python VENV

- Install virtualenv: `python3 -m pip install --user virtualenv`
- Make sure you are not using any other virtualenv(either Python or Conda) with: `deactivate` or `conda deactivate`
- Create virtualenv in `my_env` folder: `python3 -m venv my_env`. Use a unique name(here `my_env`), so it doesn't confuse with other virtual environments.
- Activate `my_env `virtualenv: `source my_env/bin/activate`.
- After activated, you should see your terminal started with `(my_env)`
- To confirm your virtual environment is working, you can type `which python` and it should show python path is in `my_env` folder
- Then follow [Installation](#Installation) and [Usage](#CLI-Usage).
- To deactivate virtualenv: deactivate
  Ref: [Installing packages using pip and virtual environments](https://packaging.python.org/en/latest/guides/installing-using-pip-and-virtual-environments/)

## Using Conda VENV

- To create an environment: `conda create --name myenv`
- To create an environment with a specific version of Python: `conda create -n myenv python=3.6`
- To activate `yourenvname`: `conda activate yourenvname`
- Then follow [Installation](#Installation) and [Usage](#CLI-Usage).
- To deactivate: `conda deactivate`
  Ref: [Managing environments](https://docs.conda.io/projects/conda/en/latest/user-guide/tasks/manage-environments.html)

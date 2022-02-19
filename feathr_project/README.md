[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) [![Feathr Runtime](https://github.com/linkedin/feathr/actions/workflows/scala.yml/badge.svg)](https://github.com/linkedin/feathr/actions/workflows/scala.yml)


Feathr Python Project Developer Guide
=============================

# Installation
- Navigate to feathr_project folder
- Install the project by `python3 -m pip install -e .` This will install a feathr CLI for you. Type `feathr` in the terminal to see the instructions.

# Usage of `feathr` CLI
- Run `feathr` in your terminal to see the instructions.
- Run `feathr init` to create a new workspace
- Navigate to the new workspace
- Feathr requires a local engine packaged in the jar to test features locally. you can download the jar by `feathr start`.
- Run `feathr test`, then type in a feature, like feature_a
- After features are fully tested, you can create your training dataset by `feathr join`. You can also materialize your features to online storage by `feathr deploy`.
- You can register your features to the metadata registry by `feathr register`


# Python Coding Style Guide
We use [Google Python Style Guide](https://google.github.io/styleguide/pyguide.html).

# Integration Test
Run pytest in this folder to kick off the integration test. The integration test will test the creation of feature dataset, the materialization to online storage, and retrieve from online storage, as well as the CLI. It usually takes 5 ~ 10 minutes.
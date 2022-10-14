"""Platform utilities.
Refs: https://github.com/microsoft/recommenders/blob/main/recommenders/utils/notebook_utils.py
"""
from pathlib import Path


def is_jupyter() -> bool:
    """Check if the module is running on Jupyter notebook/console.

    Returns:
        bool: True if the module is running on Jupyter notebook or Jupyter console, False otherwise.
    """
    try:
        shell_name = get_ipython().__class__.__name__
        if shell_name == "ZMQInteractiveShell":
            return True
        else:
            return False
    except NameError:
        return False


def is_databricks() -> bool:
    """Check if the module is running on Databricks.

    Returns:
        bool: True if the module is running on Databricks notebook, False otherwise.
    """
    try:
        if str(Path(".").resolve()) == "/databricks/driver":
            return True
        else:
            return False
    except NameError:
        return False


# TODO maybe add is_synapse()

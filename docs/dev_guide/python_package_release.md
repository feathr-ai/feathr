---
layout: default
title: Python Project Build and Release Guide
parent: Feathr Developer Guides
---


# Python Project Build and Release Guide

## Include non-code data files

- Set `include_package_data=True` in `setup.py` file
- Update `MANIFEST.in` to include the data files you wanted.
  - For example, `recursive-include feathrcli/data *` will include everything under `feathrcli/data/` folder.

# Distribute Feathr Python Library via PYPI

- Install build tool: `python3 -m pip install --upgrade build twine`
- Do NOT use `python3 -m build` since it will result in unknown issues.
- Wheel is preferred since package installation with wheel is faster.
- Empty the folder in dist.
- Cd into feathr_project `cd feathr_project`.
- Generate wheel: `python3 -m build --wheel`. A .whl file will be generated.
- Generate sdist: `python3 -m build --sdist`. A tar.gz file will be generated.
- Check the `dist` folder and you should see only two newly generated files, one for wheel(`.wheel`) and one for dist(`.gz`).
- Upload to Pypi `python3 -m twine upload dist/*`. This will upload both wheel and dist to Pypi. Username and password
  of your pypi.org account is needed to finish the uploading.
- If upload completed, you can see your new version in https://pypi.org/manage/project/feathr/.
- You can also delete the uploaded package in https://pypi.org/manage/project/feathr/releases/.

# FAQ

- File already exists. Error: 1. You may be using old dist files. Clear your dist folder. 2. You can't upload same version to Pypi. Change your patch version if you want to re-upload.
  Ref [Packaging Python Projects](https://packaging.python.org/en/latest/tutorials/packaging-projects/)

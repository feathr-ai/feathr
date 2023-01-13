---
layout: default
title: Quick Start Guide with Local Sandbox
---

# Feathr Quick Start Guide with Local Sandbox

We provide a local sandbox so users can use Feathr easily. The goal of the Feathr Sandbox is to:

- make it easier for users to get started, 
- make it easy to validate feature definitions and new ideas
- make it easier for Feathr devs to setup environment and develop new things
- Interactive experience, usually try to run a job takes less than 1 min.

As an end user, you can become productive in less than 5 mins and try out Feathr.

The Sandbox is ideal for:

- Feathr devs to test new features since this docker should everything they need
- Feathr users who want to get started quickly

## Getting Started

To get started, simply run the command:

```bash
docker run -it --rm -p 8888:8888  -p 8000:8000 -p 80:80 --env CONNECTION_STR="Server=" --env API_BASE="api/v1" -e GRANT_SUDO=yes feathrfeaturestore/feathr-sandbox
```

It should pop up a Jupyter link like below. Click to start the Jupyter Notebook and you should be able to see the Feathr sample notebook to run. Click the triangle button on the Jupyter notebook so the whole notebook will run locally.

```bash
http://127.0.0.1:8888/lab?token=b4b05e6d7f419038ddc1176589cc0b9e79b02c3cbc86defe
```
![Feathr Notebook](./images/feathr-sandbox.png)


After running the Notebooks, all the features will be registered in the UI, and you can visit the Feathr UI at:

```bash
http://localhost:80
```


After executing those scripts, you should be able to see a project called `local_spark` in the Feathr UI. You can also view lineage there.
![Feathr UI](./images/feathr-sandbox-ui.png)

![Feathr UI](./images/feathr-sandbox-lineage.png)

## Components

The Feathr sandbox comes with:
- Built-in Jupyter Notebook
- A local spark environment for dev/test purpose
- Feathr samples that can run locally
- A local Feathr registry backed by SQLite
- Feathr UI
- Feathr Registry API
- Local Redis server


## Build Docker Container

If you want to build the Feathr sandbox, run the below command in the Feathr root directory:

```bash
docker build -f FeathrSandbox.Dockerfile -t feathrfeaturestore/feathr-sandbox .
```

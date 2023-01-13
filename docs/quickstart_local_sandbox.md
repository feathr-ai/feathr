---
layout: default
title: Quick Start Guide with Local Sandbox
---

# Feathr Quick Start Guide with Local Sandbox

We provide a local sandbox so users can use Feathr easily (TODO: expand on the motivation)

The Sandbox is ideal for:

- Feathr devs to test new features
- Feathr users who want to get started quickly


To get started, simply run the command:

```bash
docker run -it --rm -p 8888:8888  -p 8000:8000 -p 80:80 --env CONNECTION_STR="Server=" --env API_BASE="api/v1" -e GRANT_SUDO=yes feathrfeaturestore/feathr-sandbox
```

It should pop up a Jupyter link like this. Click to start the Jupyter Notebook and you should be able to see the Feathr sample notebook as well as some sample Python scripts

`http://127.0.0.1:8888/lab?token=b4b05e6d7f419038ddc1176589cc0b9e79b02c3cbc86defe`

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
- Feathr samples that can run locally
- A local Feathr registry backed by SQLite
- Feathr UI
- Feathr Registry API
- Local Redis server


## Build Docker Container

In the Feathr root directory, run command like below:

```bash
docker build -f FeathrSandbox.Dockerfile -t feathrfeaturestore/feathr-sandbox .
```


## Known issues

Materialization job doesn't work and we are working on a fix. It used to work in 0.9.0 so might be a regression issue
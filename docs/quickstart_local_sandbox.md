---
layout: default
title: Quick Start Guide with Local Sandbox
---

# Feathr Quick Start Guide with Local Sandbox

We provide a local sandbox so users can use Feathr easily. The goal of the Feathr Sandbox is to:

- Make it easier for users to get started,
- Make it easy to validate feature definitions and new ideas
- Make it easier for Feathr developers to setup environment and develop new things
- Interactive experience, usually try to run a job takes less than 1 min.

As an end user, you can become productive in less than 5 minutes and try out Feathr.

The Sandbox is ideal for:

- Feathr users who want to get started quickly
- Feathr developers to test new features since this docker should everything they need. It comes with the python package as editable model so developers can iterate easily.

## Getting Started

### Prerequisites

Make sure Docker is installed on your machine. You can find official installation guide [here]. (https://docs.docker.com/get-docker/#supported-platforms). To make sure Docker is successfully installed on your machine, run a test container as follows:

```bash
docker run hello-world
```

You should see the `Hello from Docker!` message.

### Step 1: Start Sandbox

To get started using Feathr, simply run the command below. Note that the image is around 5GB so it might take a while to pull it from DockerHub.

```bash
# 80: Feathr UI, 8888: Jupyter, 7080: Interpret
docker run -it --rm -p 8888:8888 -p 8081:80 -p 7080:7080 -e GRANT_SUDO=yes feathrfeaturestore/feathr-sandbox:releases-v1.0.0
```


If you see errors like below, simply change the `-p 8081:80` part to `-p <new port>:80` so the Feathr UI will be redirected to the new port.

`docker: Error response from daemon: driver failed programming external connectivity on endpoint hardcore_bose (ae107e924cddce6b942f96f2654369345b027ac82e5e44929a9f132e2af71746): Bind for 0.0.0.0:8081 failed: port is already allocated.`

### Step 2: Run Quickstart Sample Notebooks

Open jupyter notebook with below link and click the triangle button on the Jupyter notebook and the whole notebook will run locally.
```bash
http://localhost:8888/lab/workspaces/auto-w/tree/local_quickstart_notebook.ipynb
```

![Feathr Notebook](./images/feathr-sandbox.png)

### Step 3: Browse Features in Feathr UI
After running the Notebooks, all the features will be registered in the UI, and you can visit the Feathr UI at:

```bash
http://localhost:8081
```

You should be able to see a project called `product_recommendation` created in the Feathr UI. You can also view lineage in the Feathr UI and explore all the details.
![Feathr UI](./images/feathr-sandbox-ui.png)

![Feathr UI](./images/feathr-sandbox-lineage.png)

## Components

The Feathr sandbox comes with:
- Built-in Jupyter Notebook
- Pre-installed data science packages such as `interpret` so that data science development becomes easy
- Pre-installed Feathr package
- Local spark environment for dev/test purpose
- Feathr quickstart notebook sample that can run locally
- Feathr UI
- Local Feathr Registry API server backed by SQLite
- Local Redis server

## Configuring Feathr Registry backend in Feathr Sandbox

By default, Feathr Sandbox uses a SQLite backend in Feathr registry, and the content will be deleted if you restart the container. If you want to use a persistent registry, say a remote MySQL database or SQL Server, you can configure an environment variable when starting the Feathr Sandbox container like below, by specifying `FEATHR_SANDBOX_REGISTRY_URL` environment variable:

```bash
docker run -it --rm -p 8888:8888 -p 8081:80 -p 7080:7080 --env FEATHR_SANDBOX_REGISTRY_URL="mysql://scott:tiger@localhost/foo" -e GRANT_SUDO=yes feathrfeaturestore/feathr-sandbox:releases-v1.0.0
```

Take a look at the [Database URLs](https://docs.sqlalchemy.org/en/20/core/engines.html#database-urls) section for more details on how this URL is formatted. Basically it will be like this:

```
dialect+driver://username:password@host:port/database
```

## For Feathr Developers

Feathr Sandbox is ideal for local development purpose as well, as it provides a simplified environment which you can use. There are a few use cases where the Feathr developers can benefit from the Feathr Sandbox:

### Python Client Development

For folks who are changing the code on the Feathr Python client side (i.e. files under the [`feathr_project` folder](../feathr_project/)), Feathr Sandbox has this python client pre-installed with `pip install -e` option, which means you can do interactive development in the python package. For example you want to validate changes, instead of setting up the environment, you can simply go to the folder and edit those files. In those cases, the Feathr client installation in the python environment will reload automatically. 

Pleas also note that if you are using Jupyter Notebooks to validate the Python package development, you need to restart your Jupyter Notebook in order to force Jupyter Notebook to reload the python package.

![Feathr Dev Experience](./images/feathr-sandbox-dev-experience.png)

In the future, an VSCode Server might be installed so that you can do interactive development in the docker container.

### Feathr UI and Feathr Registry API Server development

When packaging the Feathr Sandbox docker image, Feathr automatically builds the latest UI and Registry server code, and put them into the right folder of the Feathr Sandbox images.

### Build Feathr Sandbox Docker Image

If you want to build the Feathr sandbox, run the below command in the Feathr root directory:

```bash
docker build -f FeathrSandbox.Dockerfile -t feathrfeaturestore/feathr-sandbox .
```

---
layout: default
title: How to build and push feathr registry docker image
parent: Developer Guides
---

# How to build and push feathr registry docker image

This doc shows how to build feathr registry docker image locally and publish to DockerHub.

## Prerequisites

Follow the [instructions](https://docs.docker.com/get-docker) to setup docker locally.

## Build docker image locally

Open terminal and go to root of this repository, run following command:

```bash
docker build -f FeathrRegistry.Dockerfile -t feathrfeaturestore/feathr-registry .
```

## Test docker image locally

Run **docker images** command, you will see newly created image listed in output.

```bash
docker images
```

Run **docker run** command to test docker image locally.

### Test SQL-based registry

You need to setup the connection string `CONNECTION_STR` for the docker container, so that it knows which SQL-based registry is connected to. The connection string will be something like this:

```bash
"Server=tcp:testregistry.database.windows.net,1433;Initial Catalog=testsql;Persist Security Info=False;User ID=feathr@feathrtestsql;Password=StrongPassword;MultipleActiveResultSets=False;Encrypt=True;TrustServerCertificate=False;Connection Timeout=30;"
```

Then you can test the docker locally by running this command:

```bash
docker run --env CONNECTION_STR=<REPLACE_ME> --env API_BASE=api/v1 -it --rm -p 3000:80 feathrfeaturestore/sql-registry
```

### Test Purview registry

You need to setup a few environment variables, include:

- `PURVIEW_NAME` indicates the Purview service name
- `AZURE_CLIENT_ID`, `AZURE_TENANT_ID`, `AZURE_CLIENT_SECRET` indicates the service principal account to talk with Purview service.

```bash
docker run --env PURVIEW_NAME=<REPLACE_ME> --env AZURE_CLIENT_ID=<REPLACE_ME> --env AZURE_TENANT_ID=<REPLACE_ME> --env AZURE_CLIENT_SECRET=<REPLACE_ME> --env API_BASE=api/v1  -it --rm -p 3000:80 feathrfeaturestore/feathr-registry
```

### Test SQL registry + RBAC

```bash
docker run --env REACT_APP_ENABLE_RBAC=true --env REACT_APP_AZURE_CLIENT_ID=<REPLACE_ME> --env REACT_APP_AZURE_TENANT_ID=<REPLACE_ME> --env CONNECTION_STR=<REPLACE_ME> --env API_BASE=api/v1 -it --rm -p 3000:80 feathrfeaturestore/feathr-registry
```

After docker image launched, open web browser and navigate to <https://localhost:3000>，verify both the Feathr UI and the registry backend (SQL/Purview) can work correctly.

## Upload to DockerHub (For Feathr Release Manager)

The Feathr repository already have automatic CD pipelines to publish the docker image to DockerHub on release branches. Please checkout [docker publish workflow](https://github.com/feathr-ai/feathr/blob/main/.github/workflows/docker-publish.yml) for details

In case if the Feathr release manager wants to do it manually, login with feathrfeaturestore account and then run **docker push** command to publish docker image to DockerHub. Contact Feathr Team (@jainr, @blrchen) for credentials.

```bash
docker login
docker push feathrfeaturestore/feathr-registry
```

## Published Feathr Registry Image

The published feathr feature registry is located in [DockerHub here](https://hub.docker.com/r/feathrfeaturestore/feathr-registry).

## Include the detailed track back info in registry api HTTP error response

Set environment REGISTRY_DEBUGGING to any non empty string will enable the detailed track back info in registry api http response. This variable is helpful for python client debugging and should only be used for debugging purposes.

---
layout: default
title: How to build and push feathr registry docker image
parent: How-to Guides
---

# How to build and push feathr registry docker image

This doc shows how to build feathr registry docker image locally and publish to registry.

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

Run **docker run** command to test docker image locally:

### Test SQL registry
```bash
docker run --env CONNECTION_STR=<REPLACE_ME> --env API_BASE=api/v1 -it --rm -p 3000:80 feathrfeaturestore/sql-registry
```

### Test Purview registry
```bash
docker run --env PURVIEW_NAME=<REPLACE_ME> --env AZURE_CLIENT_ID=<REPLACE_ME> --env AZURE_TENANT_ID=<REPLACE_ME> --env AZURE_CLIENT_SECRET=<REPLACE_ME> --env API_BASE=api/v1  -it --rm -p 3000:80 feathrfeaturestore/feathr-registry
```

### Test SQL registry + RBAC
```bash
docker run --env ENABLE_RBAC=true --env REACT_APP_AZURE_CLIENT_ID=<REPLACE_ME> --env REACT_APP_AZURE_TENANT_ID=<REPLACE_ME> --env CONNECTION_STR=<REPLACE_ME> --env API_BASE=api/v1 -it --rm -p 3000:80 feathrfeaturestore/feathr-registry
```

After docker image launched, open web browser and navigate to <https://localhost:3000>，verify both UI and backend api can work correctly.

## Upload to DockerHub Registry

Login with feathrfeaturestore account and then run **docker push** command to publish docker image to DockerHub.

```bash
docker login
docker push feathrfeaturestore/sql-registry
```



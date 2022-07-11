---
layout: default
title: How to build and push feathr registry docker image
parent: How-to Guides
---

# How to build and push feathr registry docker image

This doc shows how to build feathr registry docker image locally and publish to registry

## Prerequisites

Follow the [instructions](https://docs.docker.com/get-docker) to setup docker locally

## Build docker image locally

Open terminal and go to root of this repository, run following command

```bash
docker build -t feathrfeaturestore/sql-registry .
```

## Test docker image locally

Run **docker images** command, you will see newly created image listed in output

```bash
docker images
```

Run **docker run** command to test docker image locally

```bash
docker run --env CONNECTION_STR=__REPLACE_ME_WITH_SQL_CONNECTION_STRING__ --env API_BASE=api/v1 -it --rm -p 3000:80 feathrfeaturestore/sql-registry
```

Open web browser and navigate to <https://localhost:3000>，verify you can see feathr ui and able to login successfully.

## Upload to DockerHub Registry

Login with feathrfeaturestore account and then run **docker push** command to publish docker image to DockerHub

```bash
docker login
docker push feathrfeaturestore/sql-registry
```



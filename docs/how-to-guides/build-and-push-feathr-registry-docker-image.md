---
layout: default
title: How to build and push feathr registry docker image
parent: Feathr How-to Guides
---

# How to build and push feathr registry docker image

This doc shows how to build feathr registry docker image locally and publish to registry

## Prerequisites

Follow the [instructions](https://docs.docker.com/get-docker) to setup docker locally

## Build docker image locally

Open terminal and go to root of this repository, run following command

```bash
cd deploy
docker build -t blrchen/feathr-sql-registry .
```

Note: this tutorial uses **blrchen/feathr-sql-registry** as an example. You should replace it with the image name you want to use. <user_name>/<image_name> is not a mandatory format for specifying the name of the docker image.

## Test docker image locally

Run **docker images** command, see newly created image should be listed in output

```bash
docker images
```

Run **docker run** command to test docker image locally

```bash
docker run --env CONNECTION_STR=<__REPLACE_ME_WITH_SQL_CONNECTION_STRING__> --env API_BASE=api/v1 -it --rm -p 3000:80 blrchen/feathr-sql-registry
```

Open web browser and navigate to <https://localhost:3000>，verify you can see feathr ui and able to login successfully.

## Upload to Registry

Run **docker push** command to publish docker image to DockerHub

```bash
docker push blrchen/feathr-sql-registry
```



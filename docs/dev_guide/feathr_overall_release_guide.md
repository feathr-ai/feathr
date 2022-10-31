---
layout: default
title: Developer Guide for Feathr Overall Release Guide
parent: Developer Guides
---

# Feathr Overall Release Guide

This document describes all the release process for the development team.

## Prerequisites

- Make sure the CI tests are passing so there are no surprises on the release day.
- Make sure all the active PRs related to the release are merged.


## When to Release

- For each major and minor version release, please follow these steps.
- For patch versions, there should be no releases.

## Writing Release Note

Write a release note following past examples [here](https://github.com/feathr-ai/feathr/releases).
Read through the [commit log](https://github.com/feathr-ai/feathr/commits/main) to identify the commits after last release to include in the release note. Here are the major things to include

- highlights of the release
- improvements and changes of this release
- new contributors of this release

## Code Changes
Before the release is made, the version needs to be updated in following places
- [build.sbt](https://github.com/feathr-ai/feathr/blob/main/build.sbt#L3) - For Maven release version
- [version.py](https://github.com/feathr-ai/feathr/blob/main/feathr_project/feathr/version.py#L1) - For Feathr version
- [conf.py](https://github.com/feathr-ai/feathr/blob/main/feathr_project/docs/conf.py#L27) - For documentation version
- [feathr_config.yaml](https://github.com/feathr-ai/feathr/blob/main/feathr_project/test/test_user_workspace/feathr_config.yaml#L84) - To set the spark runtime location for Azure Synapse and Azure Databricks used by test suite. Please update all .yaml files under this path. 
- [azure_resource_provision.json](https://github.com/feathr-ai/feathr/blob/main/docs/how-to-guides/azure_resource_provision.json#L114) - To set the deployment template to pull the latest release image.
- [constants.py](https://github.com/feathr-ai/feathr/blob/main/feathr_project/feathr/constants.py#L31) - To set the default maven artifact version (Only needed when maven version is **NOT** the same as python sdk version)
- [package.json](https://github.com/feathr-ai/feathr/blob/main/ui/package.json#L3) - For Feathr UI version

## Triggering automated release pipelines
Our goal is to automate the release process as much as possible. So far, we have automated the following steps
1. Automated [workflow](https://github.com/feathr-ai/feathr/blob/main/.github/workflows/docker-publish.yml) to build and publish for our UI and API container to [dockerhub](https://hub.docker.com/r/feathrfeaturestore/feathr-registry/tags).
    **Triggers** - Nightly, branch with name pattern "releases/*"

1. Automated [workflow](https://github.com/feathr-ai/feathr/blob/main/.github/workflows/publish-to-pypi.yml) for publishing Python package to [PyPi](https://pypi.org/project/feathr/).

    **Triggers** -  branch with name pattern "releases/*"

1. Automated Maven workflow - Coming soon.

**PLEASE NOTE: To trigger the above workflows as part of release, create a new branch with pattern releases/v0.x.0**. See past release branches [here](https://github.com/feathr-ai/feathr/branches/all?query=releases).


## Release Maven

See [Developer Guide for publishing to maven](publish_to_maven.md)

## Upload Feathr Jar

Run the command to generate the Java jar. After the jar is generated, please upload to [Azure storage](https://ms.portal.azure.com/#view/Microsoft_Azure_Storage/ContainerMenuBlade/~/overview/storageAccountId/%2Fsubscriptions%2Fa6c2a7cc-d67e-4a1a-b765-983f08c0423a%2FresourceGroups%2Fazurefeathrintegration%2Fproviders%2FMicrosoft.Storage%2FstorageAccounts%2Fazurefeathrstorage/path/public/etag/%220x8D9E6F64D62D599%22/defaultEncryptionScope/%24account-encryption-key/denyEncryptionScopeOverride//defaultId//publicAccessVal/Container) for faster access.

## Release PyPi
The automated workflow should take care of this, you can check under [actions](https://github.com/feathr-ai/feathr/actions/workflows/publish-to-pypi.yml) to see the triggered run and results. For manual steps, see [Python Package Release Note](https://feathr-ai.github.io/feathr/dev_guide/python_package_release.html)

## Updating docker image for API and Registry
The automated workflow should take care of this as well, you can check under [actions](https://github.com/feathr-ai/feathr/actions/workflows/docker-publish.yml) to see the triggered run and results. For manual steps, see [Feathr Registry docker image](https://feathr-ai.github.io/feathr/dev_guide/build-and-push-feathr-registry-docker-image.html)

## Testing
Run one of the sample [notebook](https://github.com/feathr-ai/feathr/blob/main/docs/samples/azure_synapse/product_recommendation_demo.ipynb) as it uses the latest package from Maven and PyPi.

## Announcement

Please announce the release in our #general Slack channel.

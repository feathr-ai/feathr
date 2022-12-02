---
layout: default
title: Developer Guide for Feathr Overall Release Guide
parent: Developer Guides
---

# Feathr Overall Release Guide

This document describes all the release process for the development team.

## Prerequisites

- Make sure the CI tests are passing prior to bug bash.
- Make sure all the active PRs related to the release are merged.

## When to Release

The release process is triggered by the release manager. The release manager will decide when to release with following steps:

1. Ensure Prerequisites are met.
2. Creation of Release Candidate(rc) on GitHub.
3. Bug Bash.
4. Creation of Release on GitHub.
5. Post Release announcement.

## Release Versioning

- Major and minor version: X.Y.Z
- Release Candidate: X.Y.Z-rcN

## Writing Release Note

Write a release note following past examples [here](https://github.com/feathr-ai/feathr/releases).
Read through the [commit log](https://github.com/feathr-ai/feathr/commits/main) to identify the commits after last release to include in the release note. Here are the major things to include

- Highlights of the release
- Improvements and changes of this release
- New contributors of this release

## Code Changes

Before the release candidate or release is made, the version needs to be updated in following places

- [build.gradle](https://github.com/feathr-ai/feathr/blob/main/gradle.properties#L3) - For Maven release version
- [version.py](https://github.com/feathr-ai/feathr/blob/main/feathr_project/feathr/version.py#L1) - For Feathr version
- [conf.py](https://github.com/feathr-ai/feathr/blob/main/feathr_project/docs/conf.py#L27) - For documentation version
- [feathr_config.yaml](https://github.com/feathr-ai/feathr/blob/main/feathr_project/test/test_user_workspace/feathr_config.yaml#L84) - To set the spark runtime location for Azure Synapse and Azure Databricks used by test suite. Please update all .yaml files under this path.
- [package.json](https://github.com/feathr-ai/feathr/blob/main/ui/package.json#L3) - For Feathr UI version

Following file should only be updated for release, which means should be skipped for release candidate.

- [azure_resource_provision.json](https://github.com/feathr-ai/feathr/blob/main/docs/how-to-guides/azure_resource_provision.json#L114) - To set the deployment template to pull the latest release image.

## Release Branches

Each major and minor release should have a release branch. The release branch should be named as `releases/vX.Y.Z` or `releases/vX.Y.Z-rcN` where `X.Y.Z` is the release version. The release branch should be created from the `main` branch. See past release branches [here](https://github.com/feathr-ai/feathr/branches/all?query=releases).

## Release Tags

Once the release branch is created, a release tag should be created from the release branch. The release tag should be named as `vX.Y.Z` or `vX.Y.Z-rcN` where `X.Y.Z` is the release version. See past release tags [here](https://github.com/feathr-ai/feathr/tags).

## Triggering automated release pipelines

Once the release branch and release tag are created, the release pipelines will be triggered automatically. The release pipelines will build the release artifacts and publish them to Maven and PyPI.

1. Automated [workflow](https://github.com/feathr-ai/feathr/blob/main/.github/workflows/docker-publish.yml) to build and publish for Feathr Registry docker images to [DockerHub](https://hub.docker.com/r/feathrfeaturestore/feathr-registry/tags).

    **Triggers** - Nightly or branch with name pattern "releases/*"

2. Automated [workflow](https://github.com/feathr-ai/feathr/blob/main/.github/workflows/publish-to-pypi.yml) for publishing Python package to [PyPi](https://pypi.org/project/feathr/).

    **Triggers** - branch with name pattern "releases/*"

3. Automated [workflow](https://github.com/feathr-ai/feathr/blob/main/.github/workflows/publish-to-maven.yml) for publishing the jar to [maven/sonatype repository](https://oss.sonatype.org/).

## Upload Feathr Jar

Run the command to generate the Java jar. After the jar is generated, please upload to [Azure storage](https://ms.portal.azure.com/#view/Microsoft_Azure_Storage/ContainerMenuBlade/~/overview/storageAccountId/%2Fsubscriptions%2Fa6c2a7cc-d67e-4a1a-b765-983f08c0423a%2FresourceGroups%2Fazurefeathrintegration%2Fproviders%2FMicrosoft.Storage%2FstorageAccounts%2Fazurefeathrstorage/path/public/etag/%220x8D9E6F64D62D599%22/defaultEncryptionScope/%24account-encryption-key/denyEncryptionScopeOverride//defaultId//publicAccessVal/Container) for faster access.

## Release PyPi

The automated workflow should take care of this, you can check under [actions](https://github.com/feathr-ai/feathr/actions/workflows/publish-to-pypi.yml) to see the triggered run and results. For manual steps, see [Python Package Release Guide](https://feathr-ai.github.io/feathr/dev_guide/python_package_release.html)

## Updating docker image for API and Registry

The automated workflow should take care of this as well, you can check under [actions](https://github.com/feathr-ai/feathr/actions/workflows/docker-publish.yml) to see the triggered run and results. For manual steps, see [Feathr Registry docker image](https://feathr-ai.github.io/feathr/dev_guide/build-and-push-feathr-registry-docker-image.html)

## Release Maven

The automated workflow should take of this too, you can check under [actions](https://github.com/feathr-ai/feathr/blob/main/.github/workflows/publish-to-maven.yml) to see the triggered run and results. For manual steps, see [Feathr Developer Guide for publishing to maven](https://feathr-ai.github.io/feathr/dev_guide/publish_to_maven.html)

## Testing

Run one of the sample [notebook](https://github.com/feathr-ai/feathr/blob/main/docs/samples/azure_synapse/product_recommendation_demo.ipynb) as it uses the latest package from Maven and PyPi.

## Announcement

Please announce the release in our #general Slack channel.

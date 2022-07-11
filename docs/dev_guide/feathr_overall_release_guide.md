---
layout: default
title: Developer Guide for Feathr Overall Release Guide
parent: Developer Guides
---

# When to Release
- For each major and minor version release, please follow these steps. 
- For patch versions, there should be no releases.

# Writing Release Note
Write a release note following past examples [here](https://github.com/linkedin/feathr/releases).
Read through the [commit log](https://github.com/linkedin/feathr/commits/main) to identify the commits after last release to include in the release note. Here are the major things to include
- highlights of the release
- improvements and changes of this release
- new contributors of this release


# Release Maven
See [Developer Guide for publishing to maven](publish_to_maven.md)

## Upload Feathr Jar
Run the command to generate the Java jar. After the jar is generated, please upload to [Azure storage](https://ms.portal.azure.com/#view/Microsoft_Azure_Storage/ContainerMenuBlade/~/overview/storageAccountId/%2Fsubscriptions%2Fa6c2a7cc-d67e-4a1a-b765-983f08c0423a%2FresourceGroups%2Fazurefeathrintegration%2Fproviders%2FMicrosoft.Storage%2FstorageAccounts%2Fazurefeathrstorage/path/public/etag/%220x8D9E6F64D62D599%22/defaultEncryptionScope/%24account-encryption-key/denyEncryptionScopeOverride//defaultId//publicAccessVal/Container) for faster access.

# Release PyPi
See [Python Package Release Note](python_package_release.md)

# Announcement
Please announce the release in our #general Slack channel.

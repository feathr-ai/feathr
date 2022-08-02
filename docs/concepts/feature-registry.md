---
layout: default
title: Feature Registry
parent: Feathr Concepts
---

# Feature Registry and Feathr UI

Feature registry is an important component of a feature store. This documentation will cover the supported backend of a feature registry and the usages.

## Introduction

Feathr UI and Feathr Registry are two optional components to use Feathr, but

## Deployment

Please follow the `Provision Azure Resources using ARM Template` part in the [Azure Resource Provisioning document](../how-to-guides/azure-deployment-arm.md#provision-azure-resources-using-arm-template) to provision corresponding the Azure resources. After completing those steps, you should have a set of resources that can be used for Feature Registry.

In case you want to do it in a more customized way, you can use [this Dockerfile](https://github.com/linkedin/feathr/blob/main/FeathrRegistry.Dockerfile) to deploy the REST API and UI. This docker image is more for illustration purpose, and you can customize it further (like building the REST API and UI in separate docker images).

If you use [Azure Resource Provisioning document](../how-to-guides/azure-deployment-arm.md#provision-azure-resources-using-arm-template) to provision the resources, you should be able to access the UI in this website:

```bash
https://{prefix}webapp.azurewebsites.net
```

And the corresponding REST API will be:

```bash
https://{prefix}webapp.azurewebsites.net/api/v1
```

## Deployment Options

Feathr supports two types of backends for Feature Registry - Azure Purview (Apache Atlas compatible service) and ANSI SQL. Depending on your IT setup, you might choose either of those in the above deployment steps.

Note that if you choose to enable Role-based Access Control (RBAC), you also need to use a SQL service (such as Azure SQL) to store all the RBAC related information.

## Architecture

![Architecture Diagram](../images/architecture.png)

The architecture is as above. More specifically, there are three components for Feathr feature registry:

- Feathr UI, a react based application
- Feathr REST API, which provides abstraction for different registry providers, as well as role-based access control (RBAC)
- Different feature registry backends. Currently only Azure Purview and SQL based registry are supported, but more registry providers from the community are welcome.

Both the Feathr UI and the Feathr Python Client interact with the Feathr REST API service. The REST API service then detect if the user has the right access, and route the corresponding request to the registry providers.

## Accessing Registry in Feathr Python Client

In the Feathr python client, if you want to access the registry, you should set the `FEATURE_REGISTRY__API_ENDPOINT` environment variable. The full [document is here](../how-to-guides/feathr-configuration-and-env.md#a-list-of-environment-variables-that-feathr-uses).

Alternatively, you can set the feature registry and the API endpoint in the YAML file:

```yaml
feature_registry:
  # The API endpoint of the registry service
  api_endpoint: "https://feathr-sql-registry.azurewebsites.net/api/v1"
```

### Register Features

### Reuse Features from Existing Registry

## Accessing Feathr UI

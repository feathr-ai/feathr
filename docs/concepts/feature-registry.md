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

## Deployment Options

Feathr supports two types of backends for Feature Registry - Azure Purview (Apache Atlas compatible service) and ANSI SQL. Depending on your IT setup, you might choose either of those in the above deployment steps.

Note that if you choose to enable Role-based Access Control (RBAC) for the 

## Architecture

The architecture is as below. More specifically, 
![Architecture Diagram](./images/architecture.png)

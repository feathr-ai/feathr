// Copyright (c) Microsoft Corporation.
// Licensed under the MIT license.

targetScope = 'subscription'

@description('Resource prefix for all the resource provisioned. This should be an alphanumeric string.')
@maxLength(15)
@minLength(3)
param resourcePrefix string = 'feathr${take(newGuid(),5)}'

@description('Specifies the principal ID assigned to the role. You can find it by logging into \'https://shell.azure.com/bash\' and run \'az ad signed-in-user show --query objectId -o tsv\' ')
param principalId string

@description('Specifies whether to allow client IPs to connect to Synapse')
@allowed([
  'true'
  'false'
])
param allowAllConnections string = 'true'

@description('Specifies the location of your Azure datacenter')
param location string = deployment().location

var redisCacheName = '${resourcePrefix}redis'
var keyvaultName = '${resourcePrefix}kv'
var sparkPoolName = 'spark31'
var workspaceName = toLower('${resourcePrefix}syws')
var dlsName = toLower('${resourcePrefix}dls')
var dlsFsName = toLower('${resourcePrefix}fs')
var purviewName = '${resourcePrefix}purview'


// Create Resource Group
resource FeathrResourceGroup 'Microsoft.Resources/resourceGroups@2020-10-01' = {
  name: 'FeathrRg-${resourcePrefix}'
  location:location
}

// Create Azure KeyVault for Redis ConnectionString and 
// Resource Prefix
module FeathrKeyVault 'deployKV.bicep' = {
  name : 'FeathrKv-${resourcePrefix}'
  scope: FeathrResourceGroup

  params: {
    kvName: keyvaultName
    location: location
    resourcePrefix: resourcePrefix
    tenantId: tenant().tenantId
  }
}

// Create Redis for Online Feature store
module FeathrRedis 'deployRedis.bicep' = {
  name: 'FeathrRedis-${resourcePrefix}'
  scope: FeathrResourceGroup
  params: {
    kvName: keyvaultName
    location: location
    redisName: redisCacheName
  }
}

// Create DataLake for Feathr and Feathr Offline Feature store
module FeathrStorage 'deployDataLakeStorage.bicep' = {
  name: 'FeathrStorage-${resourcePrefix}'
  scope: FeathrResourceGroup
  params: {
    stroageAccountName: dlsName
    containerName: dlsFsName
    location: location
  }
}

// Create Furview for Feathr
module FeathrPurview 'deployPurview.bicep' = {
  name: 'FeathrPurview-${resourcePrefix}'
  scope: FeathrResourceGroup
  params: {
    name: purviewName
    location: location
  }
}

// Create Azure Synapse for Feathr
module FeathrSynapse 'deploySynapse.bicep' = {
  name: 'FeathrSynapse-${resourcePrefix}'
  scope: FeathrResourceGroup
  params:{
    name: workspaceName
    location: location
    sparkpoolName: sparkPoolName
    perviewResourceId: FeathrPurview.outputs.id
    adlsFileSystem: FeathrStorage.outputs.filesystem
    adlsStorageAccountURL: FeathrStorage.outputs.accountURL
    allowAllConnections: bool(allowAllConnections)
  }
}

// Assign roles to Azure Keyvault and Stoage Account(Data Lake)
module FeathrAddRoles 'deployRoleAssignments.bicep' = {
  name: 'FeathrAddRoles-${resourcePrefix}'
  scope: FeathrResourceGroup
  params: {
    userObjectID: principalId
  }
}

output resource_prefix string = resourcePrefix

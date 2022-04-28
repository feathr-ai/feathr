// Copyright (c) Microsoft Corporation.
// Licensed under the MIT license.

param stroageAccountName string
param containerName string 
param location string

resource adls 'Microsoft.Storage/storageAccounts@2019-06-01' = {
  name: stroageAccountName
  location: location
  sku:{
    name:'Standard_LRS'
  }
  kind:'StorageV2'
  properties:{
    isHnsEnabled : true
    networkAcls:{
      bypass: 'AzureServices'
      defaultAction:'Allow'
      virtualNetworkRules:[]
      ipRules:[]
    }
    supportsHttpsTrafficOnly:true
    encryption:{
      services:{
        file:{
          enabled:true
        }
        blob: {
          enabled:true
        }
      }
      keySource:'Microsoft.Storage'
    }
    accessTier: 'Hot'
  }
}

resource blobSvc 'Microsoft.Storage/storageAccounts/blobServices@2019-06-01' = {
  name: 'default'
  parent: adls
}

resource container 'Microsoft.Storage/storageAccounts/blobServices/containers@2019-06-01' = {
  name : containerName
  properties: {
    publicAccess: 'None'
  }
  parent: blobSvc
}

output accountURL string = 'https://${stroageAccountName}.dfs.${environment().suffixes.storage}'
output filesystem string = containerName


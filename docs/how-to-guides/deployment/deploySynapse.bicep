// Copyright (c) Microsoft Corporation.
// Licensed under the MIT license.

param adlsStorageAccountURL string
param adlsFileSystem string
param location string
param name string
param perviewResourceId string
param allowAllConnections bool
param sparkpoolName string 

resource synapsefeathrws 'Microsoft.Synapse/workspaces@2021-06-01' = {
  name: name
  tags: {}
  location: location

  identity: {
    type:'SystemAssigned'
  }
  
  properties: {
      defaultDataLakeStorage: {
        accountUrl: adlsStorageAccountURL
        filesystem: adlsFileSystem
      }
      virtualNetworkProfile:{
        computeSubnetId: ''
      }

      managedVirtualNetwork: 'default'
      purviewConfiguration: {
        purviewResourceId: perviewResourceId
      }
  }
}

resource synapseFirewallRulesAllowAll 'Microsoft.Synapse/workspaces/firewallRules@2021-06-01' = if (allowAllConnections) {
  name: 'allowAll'

  parent: synapsefeathrws
  properties:{
    startIpAddress: '0.0.0.0'
    endIpAddress: '255.255.255.255'
  }
}

resource synapseFirewallRulesOnlyAzure 'Microsoft.Synapse/workspaces/firewallRules@2021-06-01' = if (allowAllConnections) {
  name: 'AllowAllWindowsAzureIps'

  parent: synapsefeathrws
  properties:{
    startIpAddress: '0.0.0.0'
    endIpAddress: '0.0.0.0'
  }
}

resource synapseManagedVirtualNetwork  'Microsoft.Synapse/workspaces/managedIdentitySqlControlSettings@2021-06-01' = {
  name: 'default'
  parent: synapsefeathrws
  properties: {
    grantSqlControlToManagedIdentity: {
      desiredState: 'Enabled'
    }
  }
}

resource synapsePool 'Microsoft.Synapse/workspaces/bigDataPools@2021-06-01' = {
  name : sparkpoolName
  location: location
  parent: synapsefeathrws

  properties:{
    sparkVersion: '3.1'
    nodeCount: 3
    nodeSize: 'Medium'
    nodeSizeFamily:'MemoryOptimized'
    autoScale:{
      enabled: true
      minNodeCount: 1
      maxNodeCount: 3
    }
    autoPause:{
      enabled: true
      delayInMinutes: 30
    }
    dynamicExecutorAllocation:{
      enabled: true
    }
  }
}

output synapseWorkspaceName string = synapsefeathrws.name

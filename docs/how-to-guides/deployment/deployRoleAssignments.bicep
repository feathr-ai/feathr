// Copyright (c) Microsoft Corporation.
// Licensed under the MIT license.

param userObjectID string
var roleDefinitionIdForBlobContributor = 'ba92f5b4-2d11-453d-a403-e96b0029c9fe'
var roleDefinitionIdForKeyVaultSecretsUser = '4633458b-17de-408a-b874-0445c86b69e6'

var storageRoleID_user = guid('storageroleuser-${resourceGroup().name}')
var kvRoleID_user = guid('kvroleuser-${resourceGroup().name}')

resource assignStorageBlobDataContributor_user 'Microsoft.Authorization/roleAssignments@2020-04-01-preview' = {
  name: storageRoleID_user
  dependsOn:[
     resourceGroup()
  ]
  properties:{
    principalId: userObjectID
    roleDefinitionId: resourceId('Microsoft.Authorization/roleDefinitions',roleDefinitionIdForBlobContributor)
    principalType:'User'
  }
 }

 resource assignKeyVaultSecrets_user 'Microsoft.Authorization/roleAssignments@2020-04-01-preview' = {
  name: kvRoleID_user
  dependsOn:[
     resourceGroup()
  ]
  properties:{
    principalId: userObjectID
    roleDefinitionId: resourceId('Microsoft.Authorization/roleDefinitions',roleDefinitionIdForKeyVaultSecretsUser)
    principalType:'User'
  }
 }

param kvName string
param location string
param tenantId string
param resourcePrefix string

resource deployKV 'Microsoft.KeyVault/vaults@2021-11-01-preview' = {
  name: kvName
  location: location
  properties: {
    sku: {
      family: 'A'
      name: 'standard'
    }
    enableSoftDelete: true
    enableRbacAuthorization: true
    tenantId: tenantId
  }
}

resource kvSecret 'Microsoft.KeyVault/vaults/secrets@2021-11-01-preview' = {
  name: 'FEATHR-PREFIX'
  parent: deployKV
  properties: {
    value: resourcePrefix
  }
}



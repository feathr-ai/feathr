param location string
param name string

resource deployPurview 'Microsoft.Purview/accounts@2021-07-01' = {
  name: name
  location: location

  identity: {
    type : 'SystemAssigned'
  }

  properties: {
    cloudConnectors: {}
    publicNetworkAccess: 'Enabled'
  }
}

output id string = deployPurview.id

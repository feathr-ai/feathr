param redisName string
param location string
param kvName string

resource deployKV 'Microsoft.KeyVault/vaults@2021-11-01-preview' existing = {
  name: kvName
}

resource DeployRedis 'Microsoft.Cache/redis@2021-06-01' = {
  name: redisName
  location: location
  tags: {
    displayName : 'Feathr Online Store'
  }
  properties: {
    redisVersion: '6'
    sku: {
      capacity: 0
      family: 'C'
      name: 'Basic'
    }
  }
}

resource DeployRedisConnectionStringToKv 'Microsoft.KeyVault/vaults/secrets@2021-11-01-preview' = {
  name: 'FEATHR-ONLINE-STORE-CONN'
  parent: deployKV
  properties: {
    value: '${redisName}.redis.cache.windows.net:6380,password=${DeployRedis.listKeys().primaryKey},ssl=True'
  }
}

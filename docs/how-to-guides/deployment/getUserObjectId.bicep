param location string
param utcValue string = utcNow()

resource runBashShellInlineWithOutput 'Microsoft.Resources/deploymentScripts@2020-10-01' = {
  name: 'runBashShellInlineWithOutput'
  location: location
  kind: 'AzureCLI'
  properties: {
    forceUpdateTag: utcValue
    azCliVersion:  '2.34.0'
    scriptContent: 'result=$(az ad signed-in-user show --query objectId -o tsv);echo $result'
    timeout: 'PT30M'
    cleanupPreference: 'OnSuccess'
    retentionInterval: 'P1D'
  }
}

output result string = string(runBashShellInlineWithOutput.properties.outputs)

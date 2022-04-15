Param(
    [Parameter(
        Mandatory=$True,
        HelpMessage='Which Azure region do you willing to deploy?'
    )]
    [ValidateScript({$_ -in (az account list-locations --query '[*].name' -o tsv)})]
    [string] $AzureRegion
)

$principalId = (az ad signed-in-user show --query objectId -o tsv)
$paramString = '{\"principalId\" : { \"value\" : \"' + $principalId + '\" }}'
az deployment sub create --location $AzureRegion -u https://raw.githubusercontent.com/Dongbumlee/feathr/main/docs/how-to-guides/deploy.json -p $paramString --debug



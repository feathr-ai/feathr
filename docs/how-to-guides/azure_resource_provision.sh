#!/bin/bash          

# Fill in details in this section
subscription_id="{your_subscription_id}"
resource_prefix="feathrazuretest"
location="eastus2"
synapse_sql_admin_name="cliuser1"
synapse_sql_admin_password="{your_admin_password}"
synapse_sparkpool_name="spark31"

# You don't have to modify the names below
service_principal_name="$resource_prefix"sp
resoruce_group_name="$resource_prefix"rg
storage_account_name="$resource_prefix"sto
storage_file_system_name="$resource_prefix"fs
synapse_workspace_name="$resource_prefix"spark
redis_cluster_name="$resource_prefix"redis
purview_account_name="$resource_prefix"purview
synapse_firewall_rule_name="$resource_prefix"firewall

# detect whether az cli is installed or not
if ! [ -x "$(command -v az)" ]; then
  echo 'Error: Azure CLI is not installed. Please follow guidance on https://aka.ms/azure-cli to install az command line' >&2
  exit 1
fi

az upgrade --all true --yes
# login if required
az account get-access-token
if [[ $? == 0 ]]; then
  echo "Logged in, using current subscriptions "
else
  echo "Logging in via az login..."
  az login --use-device-code
fi



echo "Setting subscription to $subscription_id, Creating $service_principal_name service principal"
az account set -s $subscription_id

echo "The output includes credentials that you must protect. Be sure that you do not include these credentials in your code or check the credentials into your source control. As an alternative, consider using managed identities if available to avoid the need to use credentials."
# az ad sp create-for-rbac --name $service_principal_name --role Contributor
sp_password=$(az ad sp create-for-rbac --name $service_principal_name --role Contributor --query "[password]"  --output tsv)
sp_objectid=$(az ad sp list --display-name $service_principal_name --query "[].{objectId:objectId}" --output tsv)
sp_appid=$(az ad sp list --display-name $service_principal_name --query "[].{appId:appId}" --output tsv)
sp_tenantid=$(az ad sp list --display-name $service_principal_name --query "[].{appOwnerTenantId:appOwnerTenantId}" --output tsv)

# show output so end users can directly copy those configurations
echo "Those configurations below should be saved and put into the Feathr configuration file:"
echo "IMPORTATNT: Please write AZURE_CLIENT_SECRET down as you won't see it again"
echo "AZURE_CLIENT_ID: $sp_appid"
echo "AZURE_TENANT_ID: $sp_tenantid"
echo "AZURE_CLIENT_SECRET: $sp_password"
az group create -l $location -n $resoruce_group_name

# Create Storage Account
az storage account create --name $storage_account_name  --resource-group $resoruce_group_name --location $location --enable-hierarchical-namespace

az storage fs create -n $storage_file_system_name --account-name $storage_account_name 

az role assignment create --role "Storage Blob Data Contributor" --assignee "$sp_objectid" --scope "/subscriptions/$subscription_id/resourceGroups/$resoruce_group_name/providers/Microsoft.Storage/storageAccounts/$storage_account_name"

# Create Synapse Cluster
az synapse workspace create --name $synapse_workspace_name --resource-group $resoruce_group_name  --storage-account $storage_account_name --file-system $storage_file_system_name --sql-admin-login-user $synapse_sql_admin_name --sql-admin-login-password $synapse_sql_admin_password --location $location

az synapse spark pool create --name $synapse_sparkpool_name --workspace-name $synapse_workspace_name  --resource-group $resoruce_group_name --spark-version 3.1 --node-count 3 --node-size Medium --enable-auto-pause true --delay 30

# depending on your preference, you can set a narrow range of IPs (like below) or a broad range of IPs to allow client access to Synapse clusters
external_ip=$(curl -s http://whatismyip.akamai.com/)
echo "External IP is: ${external_ip}. Adding it to firewall rules" 
az synapse workspace firewall-rule create --name $synapse_firewall_rule_name --workspace-name $synapse_workspace_name --resource-group $resoruce_group_name --start-ip-address "$external_ip" --end-ip-address "$external_ip"

echo "Waiting IP Address ${external_ip} to be added in the firewall rule." 
az synapse workspace firewall-rule wait --rule-name $synapse_firewall_rule_name --created --workspace-name $synapse_workspace_name --resource-group $resoruce_group_name


ERROR=$(az synapse role assignment create --workspace-name $synapse_workspace_name --role "Synapse Contributor" --assignee $service_principal_name 2>&1 >/dev/null)

# adding this logic because sometimes the firewall rule will recognize a different IP address
if [ -z "$ERROR" ]
then
      # meaning the previous command is successful
      echo "Role added successfully"
else  
      # reading IP address from error
      # error will be something like: 
      # WARNING: Command group 'synapse' is in preview and under development. Reference and support levels: https://aka.ms/CLI_refstatus\nERROR: (ClientIpAddressNotAuthorized) Client Ip address : 167.220.102.79\nCode: ClientIpAddressNotAuthorized\nMessage: Client Ip address : 167.220.xx.xx
      read external_ip < <(echo $ERROR | grep -o '[0-9]\+[.][0-9]\+[.][0-9]\+[.][0-9]\+')
      echo "External IP is: ${external_ip}. Adding it to firewall rules" 
      az synapse workspace firewall-rule create --name $synapse_firewall_rule_name --workspace-name $synapse_workspace_name --resource-group $resoruce_group_name --start-ip-address "$external_ip" --end-ip-address "$external_ip"

      echo "Waiting IP Address ${external_ip} to be added in the firewall rule." 
      az synapse workspace firewall-rule wait --rule-name $synapse_firewall_rule_name --created --workspace-name $synapse_workspace_name --resource-group $resoruce_group_name
      
fi

echo "Verify if the assignment is successful or not:"
az synapse role assignment list --workspace-name $synapse_workspace_name  --assignee $service_principal_name

# Create a Redis cluster for online storage
echo "Creating Redis Cluster..."
az redis create --location $location --name $redis_cluster_name --resource-group $resoruce_group_name  --sku Basic --vm-size c0 --redis-version 6

echo "Record this Redis Key which you will use later:"
redis_password=$(az redis list-keys --name $redis_cluster_name --resource-group $resoruce_group_name  --query "[primaryKey]" --out tsv)
echo "REDIS_PASSWORD: $redis_password"

echo "creating purview account"
az extension add --name purview
az purview account create --location $location --account-name $purview_account_name --resource-group $resoruce_group_name 

# echo "Add purview permission:"
# az purview account add-root-collection-admin --name $purview_account_name --object-id "$sp_objectid" --resource-group $resoruce_group_name 

# this is completely optional. It will download some demo NYC data and upload it to the default storage account, to make the setup experience smoother
echo "preparing data"
curl https://s3.amazonaws.com/nyc-tlc/trip+data/green_tripdata_2020-04.csv --output /tmp/green_tripdata_2020-04.csv
az storage fs file upload --account-name $storage_account_name --file-system $storage_file_system_name --path demo_data/green_tripdata_2020-04.csv --source /tmp/green_tripdata_2020-04.csv --auth-mode login

# show output again

echo "Those configurations below should be saved and put into the Feathr configuration file:"
echo "IMPORTATNT: Please write AZURE_CLIENT_SECRET down as you won't see it again"
echo "AZURE_CLIENT_ID: $sp_appid"
echo "AZURE_TENANT_ID: $sp_tenantid"
echo "AZURE_CLIENT_SECRET: $sp_password"
echo "SYNAPSE_DEV_URL: https://$synapse_workspace_name.dev.azuresynapse.net"
echo "SYNAPSE_POOL_NAME: $synapse_sparkpool_name"
echo "SYNAPSE_WORKSPACE_DIR: abfss://$storage_file_system_name@$storage_account_name.dfs.core.windows.net/"
echo "REDIS_PASSWORD: $redis_password"
echo "REDIS_HOST: $redis_cluster_name.redis.cache.windows.net"
echo "FEATHR_RUNTIME_LOCATION: https://azurefeathrstorage.blob.core.windows.net/public/feathr_20220204.jar"
echo "AZURE_PURVIEW_NAME: $purview_account_name"
echo "Demo Data Location: abfss://$storage_file_system_name@$storage_account_name.dfs.core.windows.net/demo_data/green_tripdata_2020-04.csv"

echo "outputPath: abfss://$storage_file_system_name@$storage_account_name.dfs.core.windows.net/demo_data/output.avro"

# Delete resources if you want
# az group delete -n $resoruce_group_name --yes
# az ad sp delete --id $sp_objectid
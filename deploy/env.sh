#!/bin/bash

envfile=/usr/share/nginx/html/env-config.js

echo "window.environment = {" > $envfile

if [[ -z "${AZURE_CLIENT_ID}" ]]; then
    echo "Environment variable AZURE_CLIENT_ID is not defined, skipping"
else
    echo "  \"azureClientId\": \"${AZURE_CLIENT_ID}\"," >> $envfile
fi

if [[ -z "${AZURE_TENANT_ID}" ]]; then
    echo "Environment variable AZURE_TENANT_ID is not defined, skipping"
else
    echo "  \"azureTenantId\": \"${AZURE_TENANT_ID}\"," >> $envfile
fi

echo "}" >> $envfile

echo "Successfully generated ${envfile} with following content"
cat $envfile
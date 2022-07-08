#!/bin/bash

# Generate static env.config.js for UI app
envfile=/usr/share/nginx/html/env-config.js
echo "window.environment = {" > $envfile

if [[ -z "${REACT_APP_AZURE_CLIENT_ID}" ]]; then
    echo "Environment variable REACT_APP_AZURE_CLIENT_ID is not defined, skipping"
else
    echo "  \"azureClientId\": \"${REACT_APP_AZURE_CLIENT_ID}\"," >> $envfile
fi

if [[ -z "${REACT_APP_AZURE_TENANT_ID}" ]]; then
    echo "Environment variable REACT_APP_AZURE_TENANT_ID is not defined, skipping"
else
    echo "  \"azureTenantId\": \"${REACT_APP_AZURE_TENANT_ID}\"," >> $envfile
fi

echo "}" >> $envfile

echo "Successfully generated ${envfile} with following content"
cat $envfile

# Start nginx
nginx

# Start API app
API_PORT="8000"

if [ "x$PURVIEW_NAME" == "x" ]; then
    echo "PurView flag is not configured, run SQL registry"
    cd sql-registry
    uvicorn main:app --host 0.0.0.0 --port $API_PORT 
else
    echo "PurView flag is configured, run PurView registry"
    cd purview-registry
    uvicorn main:app --host 0.0.0.0 --port $API_PORT 
fi
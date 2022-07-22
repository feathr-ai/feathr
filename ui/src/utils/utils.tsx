import { Configuration, PublicClientApplication } from "@azure/msal-browser";

export const getMsalConfig = () => {
  // Use runtime environment variables if found in env-config.js, otherwise fallback to
  // env settings defined in build time.
  // Note: env-config.js is generated on the flying during contain app starts.
  const clientId =
    window.environment?.azureClientId ?? process.env.REACT_APP_AZURE_CLIENT_ID;
  const tenantId =
    window.environment?.azureTenantId ?? process.env.REACT_APP_AZURE_TENANT_ID;
  const authority = `https://login.microsoftonline.com/${tenantId}`;
  const msalConfig: Configuration = {
    auth: {
      clientId: clientId,
      authority: authority,
      redirectUri: window.location.origin,
    },
  };
  console.log("clientId = ", clientId);
  console.log("authority = ", authority);

  return new PublicClientApplication(msalConfig);
};

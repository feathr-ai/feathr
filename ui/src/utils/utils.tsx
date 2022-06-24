import { Configuration, PublicClientApplication } from "@azure/msal-browser";

export const getMsalConfig = () => {
  // Use runtime environment variables if found in env-config.js, otherwise fallback to
  // env settings defined in build time.
  // Note: env-config.js is generated on the flying during contain app starts.
  const clientId = window.environment?.azureClientId ?? process.env.REACT_APP_AAD_APP_CLIENT_ID;
  const authority = window.environment?.azureTenantId
    ? `https://login.microsoftonline.com/${ window.environment.azureTenantId }`
    : process.env.REACT_APP_AAD_APP_AUTHORITY;

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
}

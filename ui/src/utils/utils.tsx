import { Configuration, PublicClientApplication } from "@azure/msal-browser";

export const getMsalConfig = () => {
    const msalConfig: Configuration = {
        auth: {
          clientId: process.env.REACT_APP_AAD_APP_CLIENT_ID,
          authority: process.env.REACT_APP_AAD_APP_AUTHORITY,
          redirectUri: window.location.origin,
        },
      };   
    return new PublicClientApplication(msalConfig);
}
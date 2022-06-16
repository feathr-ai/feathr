/// <reference types="react-scripts" />
declare global {
  namespace NodeJS {
    interface ProcessEnv {
      NODE_ENV: "development" | "production";
      PORT?: string;
      PWD: string;
      REACT_APP_AAD_APP_CLIENT_ID: string;
      REACT_APP_AAD_APP_AUTHORITY: string;
      REACT_APP_API_ENDPOINT: string;
    }
  }

  interface Window {
    environment: EnvironmentConfig;
  }

  interface EnvironmentConfig {
    azureClientId: string;
    azureTenantId: string;
  }
}

export {};

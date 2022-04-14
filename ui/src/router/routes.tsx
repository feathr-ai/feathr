import React, { Suspense } from 'react';
import { BrowserRouter, Route, Switch, withRouter } from 'react-router-dom';
import { Layout } from 'antd';
import Header from '../components/header/header';
import SideMenu from '../components/sidemenu/siteMenu';
import { QueryClient, QueryClientProvider } from "react-query";
import Features from "../pages/feature/features";
import NewFeature from "../pages/feature/newFeature";
import EditFeature from "../pages/feature/editFeature";
import { Configuration, InteractionType, PublicClientApplication } from "@azure/msal-browser";
import { MsalAuthenticationTemplate, MsalProvider } from "@azure/msal-react";
import DataSources from "../pages/dataSource/dataSources";

type Props = {};
const { Footer } = Layout;
const queryClient = new QueryClient();
const AAD_APP_CLIENT_ID = "c8d4653e-ddaf-4154-a342-01e38ce5a4a0";
const AAD_APP_AUTHORITY = "https://login.microsoftonline.com/72f988bf-86f1-41af-91ab-2d7cd011db47";
const msalConfig: Configuration = {
  auth: {
    clientId: AAD_APP_CLIENT_ID,
    authority: AAD_APP_AUTHORITY,
    redirectUri: window.location.origin
  }
};
const pca = new PublicClientApplication(msalConfig);
const Routes: React.FC<Props> = () => {
  return (
    <MsalProvider instance={ pca }>
      <MsalAuthenticationTemplate interactionType={ InteractionType.Redirect }>
        <QueryClientProvider client={ queryClient }>
          <BrowserRouter>
            <Layout style={ { minHeight: "100vh" } }>
              <SideMenu />
              <Layout>
                <Header />
                <Switch>
                  <Suspense fallback={ <div /> }>
                    <Route exact={ true } path="/dataSources" component={ withRouter(DataSources) } />
                    <Route exact={ true } path="/features" component={ withRouter(Features) } />
                    <Route exact={ true } path="/new-feature" component={ withRouter(NewFeature) } />
                    <Route exact={ true } path="/feature/:id" component={ withRouter(EditFeature) } />
                    {/* {publicRoutes} */ }
                    {/* <Route component={NotFound} /> */ }
                  </Suspense>
                </Switch>
                <Footer style={ { textAlign: 'center' } }>© Microsoft { new Date().getFullYear() }</Footer>
              </Layout>
            </Layout>
          </BrowserRouter>
        </QueryClientProvider>
      </MsalAuthenticationTemplate>
    </MsalProvider>
  );
}

export default Routes;

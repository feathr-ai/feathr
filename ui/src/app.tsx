import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Layout } from "antd";
import { QueryClient, QueryClientProvider } from "react-query";
import { InteractionType } from "@azure/msal-browser";
import { MsalAuthenticationTemplate, MsalProvider } from "@azure/msal-react";
import Header from "./components/header/header";
import SideMenu from "./components/sidemenu/siteMenu";
import Features from "./pages/feature/features";
import NewFeature from "./pages/feature/newFeature";
import FeatureDetails from "./pages/feature/featureDetails";
import DataSources from "./pages/dataSource/dataSources";
import DataSourceDetails from "./pages/dataSource/dataSourceDetails";
import Jobs from "./pages/jobs/jobs";
import Monitoring from "./pages/monitoring/monitoring";
import LineageGraph from "./pages/feature/lineageGraph";
import Management from "./pages/management/management";
import ResponseErrors from "./pages/responseErrors/responseErrors";
import RoleManagement from "./pages/management/roleManagement";
import Home from "./pages/home/home";
import Projects from "./pages/project/projects";
import { getMsalConfig } from "./utils/utils";

const queryClient = new QueryClient();

const msalClient = getMsalConfig();

const App = () => {
  return (
    <MsalProvider instance={msalClient}>
      <MsalAuthenticationTemplate interactionType={InteractionType.Redirect}>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <Layout style={{ minHeight: "100vh", position: "relative" }}>
              <SideMenu />
              <Layout style={{ position: "relative" }}>
                <Header />
                <Layout.Content>
                  <Routes>
                    <Route index element={<Home />} />
                    <Route path="/home" element={<Home />} />
                    <Route path="/projects" element={<Projects />} />
                    <Route path="/dataSources" element={<DataSources />} />
                    <Route path="/features" element={<Features />} />
                    <Route path="/new-feature" element={<NewFeature />} />
                    <Route
                      path="/projects/:project/features/:featureId"
                      element={<FeatureDetails />}
                    />
                    <Route
                      path="/projects/:project/dataSources/:dataSourceId"
                      element={<DataSourceDetails />}
                    />
                    <Route
                      path="/projects/:project/lineage"
                      element={<LineageGraph />}
                    />
                    <Route path="/jobs" element={<Jobs />} />
                    <Route path="/monitoring" element={<Monitoring />} />
                    <Route path="/management" element={<Management />} />
                    <Route
                      path="/role-management"
                      element={<RoleManagement />}
                    />
                    <Route
                      path="/responseErrors/:status/:detail"
                      element={<ResponseErrors />}
                    />
                  </Routes>
                </Layout.Content>
              </Layout>
            </Layout>
          </BrowserRouter>
        </QueryClientProvider>
      </MsalAuthenticationTemplate>
    </MsalProvider>
  );
};

export default App;

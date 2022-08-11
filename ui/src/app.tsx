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
import Jobs from "./pages/jobs/jobs";
import Monitoring from "./pages/monitoring/monitoring";
import LineageGraph from "./pages/feature/lineageGraph";
import Management from "./pages/management/management";
import ResponseErrors from "./pages/responseErrors/responseErrors";
import RoleManagement from "./pages/management/roleManagement";
import { getMsalConfig } from "./utils/utils";

const queryClient = new QueryClient();

const msalClient = getMsalConfig();
const App: React.FC = () => {
  return (
    <MsalProvider instance={msalClient}>
      <MsalAuthenticationTemplate interactionType={InteractionType.Redirect}>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <Layout style={{ minHeight: "100vh" }}>
              <SideMenu />
              <Layout>
                <Header />
                <Routes>
                  <Route path="/dataSources" element={<DataSources />} />
                  <Route path="/features" element={<Features />} />
                  <Route path="/features/:project" element={<Features />} />
                  <Route
                    path="/features/:project/:keyword"
                    element={<Features />}
                  />
                  #TODO: better Expression to combine multiple path to same
                  component?
                  <Route
                    path="/projects/:project/features/:featureId"
                    element={<FeatureDetails />}
                  />
                  <Route path="/new-feature" element={<NewFeature />} />
                  <Route
                    path="/projects/:project/lineage"
                    element={<LineageGraph />}
                  />
                  <Route path="/jobs" element={<Jobs />} />
                  <Route path="/monitoring" element={<Monitoring />} />
                  <Route path="/management" element={<Management />} />
                  <Route path="/role-management" element={<RoleManagement />} />
                  <Route
                    path="/responseErrors/:status/:detail"
                    element={<ResponseErrors />}
                  />
                </Routes>
              </Layout>
            </Layout>
          </BrowserRouter>
        </QueryClientProvider>
      </MsalAuthenticationTemplate>
    </MsalProvider>
  );
};

export default App;

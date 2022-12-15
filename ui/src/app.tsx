import React from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Layout } from "antd";
import { QueryClient, QueryClientProvider } from "react-query";
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

import AzureMsal from "./components/AzureMsal";

const queryClient = new QueryClient();

const enableRBAC = window.environment?.enableRBAC;
const authEnable: boolean =
  (enableRBAC ? enableRBAC : process.env.REACT_APP_ENABLE_RBAC) === "true";

const App = () => {
  return (
    <AzureMsal enable={authEnable}>
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
                  <Route path="/role-management" element={<RoleManagement />} />
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
    </AzureMsal>
  );
};

export default App;

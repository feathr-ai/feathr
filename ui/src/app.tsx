import React from 'react'

import { InteractionType } from '@azure/msal-browser'
import { MsalAuthenticationTemplate, MsalProvider } from '@azure/msal-react'
import { Layout } from 'antd'
import { QueryClient, QueryClientProvider } from 'react-query'
import { BrowserRouter, Route, Routes } from 'react-router-dom'

import DataSourceDetails from '@/pages/DataSourceDetails'
import DataSources from '@/pages/DataSources'
import FeatureDetails from '@/pages/FeatureDetails'
import Features from '@/pages/Features'
import Home from '@/pages/Home'
import Jobs from '@/pages/Jobs'
import Management from '@/pages/Management'
import Monitoring from '@/pages/Monitoring'
import NewFeature from '@/pages/NewFeature'
import ProjectLineage from '@/pages/ProjectLineage'
import Projects from '@/pages/Projects'
import ResponseErrors from '@/pages/ResponseErrors'
import RoleManagement from '@/pages/RoleManagement'

import Header from './components/HeaderBar/header'
import SideMenu from './components/SiderMenu/siteMenu'
import { getMsalConfig } from './utils/utils'

const queryClient = new QueryClient()

const msalClient = getMsalConfig()

const App = () => {
  return (
    <MsalProvider instance={msalClient}>
      <MsalAuthenticationTemplate interactionType={InteractionType.Redirect}>
        <QueryClientProvider client={queryClient}>
          <BrowserRouter>
            <Layout style={{ minHeight: '100vh', position: 'relative' }}>
              <SideMenu />
              <Layout style={{ position: 'relative' }}>
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
                    <Route path="/projects/:project/lineage" element={<ProjectLineage />} />
                    <Route path="/jobs" element={<Jobs />} />
                    <Route path="/monitoring" element={<Monitoring />} />
                    <Route path="/management" element={<Management />} />
                    <Route path="/role-management" element={<RoleManagement />} />
                    <Route path="/responseErrors/:status/:detail" element={<ResponseErrors />} />
                  </Routes>
                </Layout.Content>
              </Layout>
            </Layout>
          </BrowserRouter>
        </QueryClientProvider>
      </MsalAuthenticationTemplate>
    </MsalProvider>
  )
}

export default App

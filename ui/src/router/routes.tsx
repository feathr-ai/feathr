import React, { Suspense } from 'react';
import { BrowserRouter, Route, Switch, withRouter } from 'react-router-dom';
import { Layout } from 'antd';
import Header from '../components/header/header';
import SideMenu from '../components/sidemenu/siteMenu';
import Home from '../pages/home/home';
import NewFeature from '../pages/feature/newFeature';
import { QueryClient, QueryClientProvider } from "react-query";
import ExistingFeature from '../pages/feature/existingFeature';

type Props = {};
const { Footer } = Layout;
const queryClient = new QueryClient();

const Routes: React.FC<Props> = () => {
  return (
    <QueryClientProvider client={ queryClient }>
      <BrowserRouter>
        <Layout style={ { minHeight: "100vh" } }>
          <SideMenu />
          <Layout>
            <Header />
            <Switch>
              <Suspense fallback={ <div /> }>
                <Route exact={ true } path="/" component={ withRouter(Home) } />
                <Route exact={ true } path="/new-feature" component={ withRouter(NewFeature) } />
                <Route exact={ true } path="/feature/:id" component={ withRouter(ExistingFeature) } />
                {/* {publicRoutes} */ }
                {/* <Route component={NotFound} /> */ }
              </Suspense>
            </Switch>
            <Footer style={ { textAlign: 'center' } }>© Microsoft { new Date().getFullYear() }</Footer>
          </Layout>
        </Layout>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default Routes;

import React from 'react';
import { Layout, Menu, Typography } from 'antd';
import { CopyOutlined, DatabaseOutlined, DashboardFilled, EyeOutlined, RocketOutlined } from '@ant-design/icons';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';

const { Sider } = Layout;
const { Title, Text } = Typography;

const SideMenu = withRouter(({ history }) => {
  return (
    <Sider theme="dark">
      <Title level={2} style={ { fontSize: '42px', color: 'white', margin: '10px', paddingLeft: '15px'} }>Feathr</Title>
      <Menu
        theme="dark"
        mode="inline"
        defaultSelectedKeys={ ['/'] }
        defaultOpenKeys={ ['/'] }
        selectedKeys={ [history.location.pathname] }
      >
        {/* <Menu.Item key="/dashboard">
        </Menu.Item> */}
        <Menu.Item key="/dashboard" icon={ <DashboardFilled style={{fontSize: '20px', color: '#177ddc'}}/> }>
          <Link to="/dashboard">Homepage</Link>
        </Menu.Item>
        <Menu.Item key="/dataSources" icon={ <DatabaseOutlined style={{fontSize: '20px', color: '#13a8a8'}}/> }>
          <Link to="/dataSources">Data Sources</Link>
        </Menu.Item>
        <Menu.Item key="/features" icon={ <CopyOutlined style={{fontSize: '20px', color: '#d89614'}}/> }>
          <Link to="/features">Features</Link>
        </Menu.Item>
        <Menu.Item key="/jobs" icon={ <RocketOutlined style={{fontSize: '20px', color: '#642ab5'}}/> }>
          <Link to="/jobs">Jobs</Link>
        </Menu.Item>
        <Menu.Item key="/monitoring" icon={ <EyeOutlined style={{fontSize: '20px', color: '#e84749'}}/> }>
          <Link to="/monitoring">Monitoring</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
});

export default SideMenu;

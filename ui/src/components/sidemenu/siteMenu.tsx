import React from 'react';
import { Layout, Menu } from 'antd';
import { CopyOutlined, DatabaseOutlined, EyeOutlined, RocketOutlined } from '@ant-design/icons';
import { Link } from 'react-router-dom';

const { Sider } = Layout;
const SideMenu = () => {
  return (
    <Sider>
      <div style={ { fontSize: 'medium', color: 'white', margin: '10px', paddingLeft: '15px' } }>
        Feathr Web UI
      </div>
      <Menu
        theme="dark"
        mode="inline"
        defaultSelectedKeys={ ['/'] }
        defaultOpenKeys={ ['/'] }
      >
        <Menu.Item key="/dataSources" icon={ <DatabaseOutlined /> }>
          <Link to="/dataSources">Data Sources</Link>
        </Menu.Item>
        <Menu.Item key="/features" icon={ <CopyOutlined /> }>
          <Link to="/features">Features</Link>
        </Menu.Item>
        <Menu.Item key="/jobs" icon={ <RocketOutlined /> }>
          <Link to="/jobs">Jobs</Link>
        </Menu.Item>
        <Menu.Item key="/monitoring" icon={ <EyeOutlined /> }>
          <Link to="/monitoring">Monitoring</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
};

export default SideMenu;

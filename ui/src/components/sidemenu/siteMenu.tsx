import React from 'react';
import { Layout, Menu } from 'antd';
import { AlertOutlined } from '@ant-design/icons';
import { withRouter } from 'react-router';
import { Link } from 'react-router-dom';

const { Sider } = Layout;

const SideMenu = withRouter(({ history }) => {
  return (
    <Sider>
      <div style={ { fontSize: 'medium', color: 'white', margin: '10px', paddingLeft: '15px' } }>
        Feature Store Demo
      </div>

      <Menu
        theme="dark"
        mode="inline"
        defaultSelectedKeys={ ['/'] }
        defaultOpenKeys={ ['/'] }
        selectedKeys={ [history.location.pathname] }
      >
        <Menu.Item key="/features" icon={ <AlertOutlined /> }>
          <Link to="/">Features</Link>
        </Menu.Item>
        <Menu.Item key="/data-sources" icon={ <AlertOutlined /> }>
          <Link to="/">Data Sources</Link>
        </Menu.Item>
        <Menu.Item key="/feature-jobs" icon={ <AlertOutlined /> }>
          <Link to="/">Feature Jobs</Link>
        </Menu.Item>
      </Menu>
    </Sider>
  );
});

export default SideMenu;

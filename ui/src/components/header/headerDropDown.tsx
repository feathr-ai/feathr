import { Dropdown, Menu, Avatar } from "antd";
import React from "react";
import { LogoutOutlined, UserOutlined } from '@ant-design/icons';
import { useMsal } from "@azure/msal-react";

// @ts-ignore
const menuHeaderDropdown = (instance) => {
  const menuItems = [
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      value: 'Logout',
      callback: () => {
        // @ts-ignore
        instance.logoutRedirect().catch(e => {
          console.error(e);
        });
      }
    }
  ];
  // @ts-ignore
  const onClick = ({ key }) => {
    const item = menuItems.find(i => i.key === key);
    if (item && item.callback)
      item.callback();
  };
  return (
    <Menu onClick={ onClick } selectedKeys={ [] }>
      { menuItems.map(item => {
        const { key, icon, value } = item;
        return (
          <Menu.Item key={ key }>
            { icon }
            <span>{ value }</span>
          </Menu.Item>);
      }) }
    </Menu>
  );
}

// @ts-ignore
const HeaderDropdown = props => {
  const { instance } = useMsal();
  const { name: username } = props;
  if (!username)
    return null;
  return (
    <Dropdown
      overlay={ menuHeaderDropdown(instance) }
      { ...props }
    >
      <div className='header-container'>
        <Avatar size="small" style={ { marginRight: 4 } } icon={ <UserOutlined /> } />
        <span>{ username }</span>
      </div>
    </Dropdown>
  )
};

export default HeaderDropdown;

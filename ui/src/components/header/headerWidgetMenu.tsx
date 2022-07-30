import React from "react";
import { LogoutOutlined } from "@ant-design/icons";
import { Menu } from "antd";
import { IPublicClientApplication } from "@azure/msal-browser";

type Props = { instance: IPublicClientApplication };
const HeaderWidgetMenu: React.FC<Props> = ({ instance }) => {
  const menuItems = [
    {
      key: "logout",
      icon: <LogoutOutlined />,
      value: "Logout",
      callback: () => {
        instance.logoutRedirect().catch((e) => {
          console.error(e);
        });
      },
    },
  ];
  // @ts-ignore
  const onClick = ({ key }) => {
    const item = menuItems.find((i) => i.key === key);
    if (item && item.callback) item.callback();
  };
  return (
    <Menu onClick={onClick} selectedKeys={[]}>
      {menuItems.map((item) => {
        const { key, icon, value } = item;
        return (
          <Menu.Item key={key}>
            {icon}
            <span>{value}</span>
          </Menu.Item>
        );
      })}
    </Menu>
  );
};

export default HeaderWidgetMenu;

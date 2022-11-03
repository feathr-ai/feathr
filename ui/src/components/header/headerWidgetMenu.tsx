import React from "react";
import { LogoutOutlined } from "@ant-design/icons";
import { Menu, MenuProps } from "antd";
import { IPublicClientApplication } from "@azure/msal-browser";

type Props = { instance: IPublicClientApplication };
const HeaderWidgetMenu = ({ instance }: Props) => {
  const menuItems = [
    {
      key: "logout",
      icon: <LogoutOutlined />,
      label: "Logout",
    },
  ];

  const logout = () => {
    instance.logoutRedirect().catch((e) => {
      console.error(e);
    });
  };

  const onClick: MenuProps["onClick"] = ({ key }) => {
    switch (key) {
      case "logout":
        logout();
        break;
      default:
        break;
    }
  };

  return <Menu onClick={onClick} items={menuItems} />;
};

export default HeaderWidgetMenu;

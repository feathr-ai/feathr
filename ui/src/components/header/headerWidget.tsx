import React from "react";
import { Dropdown, Avatar } from "antd";
import { UserOutlined } from "@ant-design/icons";
import { useMsal } from "@azure/msal-react";
import HeaderWidgetMenu from "./headerWidgetMenu";

type Props = { username?: string };
const HeaderWidget: React.FC<Props> = ({ username }) => {
  const { instance } = useMsal();
  if (!username) {
    return null;
  }
  return (
    <Dropdown overlay={<HeaderWidgetMenu instance={instance} />}>
      <div className="dropdown-trigger">
        <Avatar
          size="small"
          style={{ marginRight: 4 }}
          icon={<UserOutlined />}
        />
        <span>{username}</span>
      </div>
    </Dropdown>
  );
};

export default HeaderWidget;

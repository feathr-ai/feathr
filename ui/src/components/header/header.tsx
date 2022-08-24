import React from "react";
import { Layout } from "antd";
import { useAccount, useMsal } from "@azure/msal-react";
import "./header.css";
import HeaderWidget from "./headerWidget";

const Header = () => {
  const { accounts } = useMsal();
  const account = useAccount(accounts[0] || {});
  return (
    <Layout.Header
      className="layout-header"
      style={{ backgroundColor: "#fff", height: "auto" }}
    >
      <span></span>
      <span className="layout-header-right">
        <HeaderWidget username={account?.username} />
      </span>
    </Layout.Header>
  );
};

export default Header;

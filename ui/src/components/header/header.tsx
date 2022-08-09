import React from "react";
import { Layout } from "antd";
import { useAccount, useMsal } from "@azure/msal-react";
import "./header.css";
import HeaderWidget from "./headerWidget";

const Header: React.FC = () => {
  const { accounts } = useMsal();
  const account = useAccount(accounts[0] || {});
  return (
    <Layout.Header
      className="layout-header"
      style={{ backgroundColor: "#fff", height: "auto" }}
    >
      <span>
        In Feathr Feature Store, you can manage and share features.
        <a
          target="_blank"
          rel="noreferrer"
          href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"
        >
          {" "}
          Learn More
        </a>
      </span>
      <span className="layout-header-right">
        <HeaderWidget username={account?.username} />
      </span>
    </Layout.Header>
  );
};

export default Header;

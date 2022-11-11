import React from "react";
import { Layout } from "antd";
import { useAccount, useMsal } from "@azure/msal-react";
import HeaderWidget from "./headerWidget";

import styles from "./index.module.less";

const Header = () => {
  const { accounts } = useMsal();
  const account = useAccount(accounts[0] || {});
  return (
    <>
      <Layout.Header className={styles.header}>
        <span></span>
        <span className={styles.right}>
          <HeaderWidget username={account?.username} />
        </span>
      </Layout.Header>
      <div className={styles.vacancy} />
    </>
  );
};

export default Header;

import React from 'react';
import { Button, Input, Layout } from 'antd';
import { useAccount, useMsal } from "@azure/msal-react";
import './header.css';
import HeaderWidget from "./headerWidget";

type Props = {};
const Header: React.FC<Props> = () => {
  const { accounts } = useMsal();
  const account = useAccount(accounts[0] || {});
  return (
    <Layout.Header className="layout-header" style={ { boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", backgroundColor: "white", height : "80px" } }>
    {/* <Layout.Header className="layout-header" style={ { boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)", backgroundColor: "#3c9ae8", height : "auto" } }></Layout.Header> */}
      <Input placeholder="Search features" style={ { width: "10%", marginLeft: "5px" } } />
      <Button type="primary" style={ { marginLeft: "-1715px" } }>Search</Button>
      <span className='layout-header-right'>
        <HeaderWidget username={ account?.username } />
      </span>
    </Layout.Header>
  );
};

export default Header;

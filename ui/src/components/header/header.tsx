import React from 'react';
import { Layout } from 'antd';
import { useAccount, useMsal } from "@azure/msal-react";
import './header.css';
import HeaderDropdown from "./headerDropDown";

type Props = {};

const Header: React.FC<Props> = () => {
  const { accounts } = useMsal();
  const account = useAccount(accounts[0] || {});
  console.log(account);
  return (
    <Layout.Header className="container" style={{  backgroundColor: '#fff' }}>
      <span>In Feathr Feature Store, you can manage and share features.
        <a target="_blank" rel="noreferrer"
           href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"> Learn More</a>
      </span>
      <span className='right-side'><HeaderDropdown name={ account?.username } /></span>
    </Layout.Header>
  );
};

export default Header;

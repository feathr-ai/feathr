import React, { useEffect, useState } from 'react';
import { Button, PageHeader } from 'antd';
import { useAccount, useMsal } from "@azure/msal-react";
import './header.less';

type Props = {};

const Header: React.FC<Props> = () => {
  const { accounts, instance } = useMsal();
  const account = useAccount(accounts[0] || {});
  const [name, setName] = useState("");

  useEffect(() => {
    if (account && account.name) {
      setName(account.name.split(" ")[0]);
    }
  }, [account]);

  const onClickLogout = () => {
    instance.logoutRedirect();
  }

  return (
    <PageHeader
      title={ `Hello ${ name }, welcome to Azure Feature Store` }
      style={ { backgroundColor: "white", paddingLeft: "50px", paddingRight: "50px" } }
      extra={ [<Button key="3" onClick={ onClickLogout }>Logout</Button>] } />
  );
};


export default Header;

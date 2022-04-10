import React, { useEffect, useState } from 'react';
import { Button, PageHeader } from 'antd';
import './header.less';

type Props = {};

const Header: React.FC<Props> = () => {
  const [name, setName] = useState("");

  useEffect(() => {
    setName("Demo User");
  }, []);

  const onClickLogout = () => {
  }

  return (
    <PageHeader
      title={ `Hello ${ name }, welcome to Azure Feature Store` }
      style={ { backgroundColor: "white", paddingLeft: "50px", paddingRight: "50px" } }
      extra={ [<Button key="3" onClick={ onClickLogout }>Logout</Button>] } />
  );
};


export default Header;

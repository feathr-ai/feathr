import React, { useEffect, useState } from 'react';
import { Button, PageHeader, Row, Typography } from 'antd';
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
    instance.logoutRedirect().catch(e => {
      console.error(e);
    });
  }

  const { Paragraph } = Typography;
  return (
    <PageHeader
      title={ `Hello ${ name }, welcome to Feathr Feature Store` }
      style={ { backgroundColor: "white", paddingLeft: "50px", paddingRight: "50px" } }
      extra={ [<Button key="3" onClick={ onClickLogout }>Logout</Button>] }>
      <Row>
        <div style={ { flex: 1 } }>
          <>
            <Paragraph>
              In this web ui, you can see Data Sources, Features and Jobs registered in Feathr. If you are new to
              Feathr, please visit
              <a target="_blank" href="https://linkedin.github.io/feathr/concepts/feathr-concepts-for-beginners.html"
                 rel="noreferrer"> Feathr Concepts for Beginners
              </a> to get start
            </Paragraph>
            <div>

            </div>
          </>
        </div>
      </Row>
    </PageHeader>
  );
};


export default Header;

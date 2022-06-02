import React from 'react';
import { Card } from 'antd';
import UserRoles from '../../components/userRoles';

type Props = {};

const Management: React.FC<Props> = () => {
  return (
    <>
      <div className="home" style={{ margin: "2%" }}>
        <Card style={{ minWidth: '1000px' }}>
          <UserRoles />
        </Card>
      </div>
    </>
  );
};

export default Management;
import React from "react";
import { Card, Typography } from "antd";
import UserRoles from "../../components/userRoles";

const { Title } = Typography;

const Management = () => {
  return (
    <div className="page">
      <Card>
        <Title level={3}>Role Management</Title>
        <UserRoles />
      </Card>
    </div>
  );
};

export default Management;

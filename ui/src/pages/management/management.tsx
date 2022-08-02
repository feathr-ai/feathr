import React from "react";
import { Card, Typography } from "antd";
import UserRoles from "../../components/userRoles";

const { Title } = Typography;

const Management: React.FC = () => {
  return (
    <div className="page">
      <Card style={{ minWidth: "1000px" }}>
        <Title level={3}>Management</Title>
        <UserRoles />
      </Card>
    </div>
  );
};

export default Management;

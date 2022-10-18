import React, { useState } from "react";
import { Card, Typography, Space, Alert } from "antd";
import RoleForm from "./components/RoleForm";

const { Title } = Typography;

const RoleManagement = () => {
  const [showAlert, setShowAlert] = useState<boolean>(false);

  const handleRole = (isAdmin: boolean) => {
    setShowAlert(!isAdmin);
  };

  return (
    <div className="page">
      <Card>
        <Space direction="vertical" style={{ width: "100%" }}>
          {showAlert && (
            <Alert
              type="warning"
              message="You are not admin of any project. Only Project Admins
      can retrieve management details and grant or delete user roles."
            />
          )}
          <Title level={3}>Role Management</Title>
        </Space>
        <RoleForm getRole={handleRole} />
      </Card>
    </div>
  );
};

export default RoleManagement;

import React from "react";
import { Card, Typography } from "antd";
import RoleManagementForm from "../../components/roleManagementForm";

const { Title } = Typography;

const RoleManagement: React.FC = () => {
  return (
    <div className="page">
      <Card>
        <Title level={3}>Role Management</Title>
        <RoleManagementForm isNew={true} editMode={true} />
      </Card>
    </div>
  );
};

export default RoleManagement;

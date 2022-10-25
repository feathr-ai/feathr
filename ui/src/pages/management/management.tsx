import React, { useRef } from "react";
import { Card, Typography, Alert, Space } from "antd";
import UserRolesTable, {
  SearchModel,
  UserRolesTableInstance,
} from "./components/UserRolesTable";
import SearchBar from "./components/SearchBar";

const { Title } = Typography;

const Management = () => {
  const tableRef = useRef<UserRolesTableInstance>(null);

  const handleSearch = (values: SearchModel) => {
    tableRef.current?.onSearch?.(values);
  };

  return (
    <div className="page">
      <Card>
        <Space direction="vertical" style={{ width: "100%" }}>
          <Alert
            type="info"
            message="This page is protected by Feathr Access Control. Only Project Admins
      can retrieve management details and grant or delete user roles."
          />
          <Title level={3}>Role Management </Title>
          <SearchBar onSearch={handleSearch} />
        </Space>
        <UserRolesTable ref={tableRef} />
      </Card>
    </div>
  );
};

export default Management;

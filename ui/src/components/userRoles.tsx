import React, { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Button,
  Menu,
  message,
  PageHeader,
  Popconfirm,
  Row,
  Space,
  Table,
  Tag,
} from "antd";
import { UserRole } from "../models/model";
import { deleteUserRole, listUserRole } from "../api";

const UserRoles: React.FC = () => {
  const navigate = useNavigate();

  const onDelete = async (row: UserRole) => {
    console.log(
      `The [${row.roleName}] Role of [${row.userName}] user role delete request is sent.`
    );
    const res = await deleteUserRole(row);
    if (res.status === 200) {
      message.success(`Role ${row.roleName} of user ${row.userName} deleted`);
    } else {
      message.error("Failed to delete userrole.");
    }
    setLoading(false);
    fetchData();
  };

  const columns = [
    {
      title: <div>Scope (Project/Global)</div>,
      dataIndex: "scope",
      key: "scope",
      align: "center" as "center",
    },
    {
      title: <div style={{ userSelect: "none" }}>Role</div>,
      dataIndex: "roleName",
      key: "roleName",
      align: "center" as "center",
    },
    {
      title: <div>User</div>,
      dataIndex: "userName",
      key: "userName",
      align: "center" as "center",
    },
    {
      title: <div>Permissions</div>,
      key: "access",
      dataIndex: "access",
      render: (tags: any[]) => (
        <>
          {tags.map((tag) => {
            let color = tag.length > 5 ? "red" : "green";
            if (tag === "write") color = "blue";
            return (
              <Tag color={color} key={tag}>
                {tag.toUpperCase()}
              </Tag>
            );
          })}
        </>
      ),
    },
    {
      title: <div>Create By</div>,
      dataIndex: "createBy",
      key: "createBy",
      align: "center" as "center",
    },
    {
      title: <div>Create Reason</div>,
      dataIndex: "createReason",
      key: "createReason",
      align: "center" as "center",
    },
    {
      title: <div>Create Time</div>,
      dataIndex: "createTime",
      key: "createTime",
      align: "center" as "center",
    },
    {
      title: "Action",
      key: "action",
      render: (userName: string, row: UserRole) => (
        <Space size="middle">
          <Menu>
            <Menu.Item key="delete">
              <Popconfirm
                placement="left"
                title="Are you sure to delete?"
                onConfirm={() => {
                  onDelete(row);
                }}
              >
                Delete
              </Popconfirm>
            </Menu.Item>
          </Menu>
        </Space>
      ),
    },
  ];
  const [page, setPage] = useState(1);
  const [, setLoading] = useState(false);
  const [tableData, setTableData] = useState<UserRole[]>();

  const fetchData = useCallback(async () => {
    setLoading(true);
    const result = await listUserRole();
    console.log(result);
    setPage(page);
    setTableData(result);
    setLoading(false);
  }, [page]);

  const onClickRoleAssign = () => {
    navigate("/role-management");
    return;
  };

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return (
    <div>
      <PageHeader
        title={`Role Managements`}
        style={{
          backgroundColor: "white",
          paddingLeft: "50px",
          paddingRight: "50px",
        }}
      >
        <Row>
          <div style={{ flex: 1 }}>
            <>
              <p>
                This page is protected by Feathr Access Control. Only Global
                Admin can retrieve management details and grant or delete user
                roles.
              </p>
            </>
          </div>
        </Row>
      </PageHeader>
      <Space style={{ marginBottom: 16 }}>
        <Button
          type="primary"
          onClick={onClickRoleAssign}
          style={{ position: "absolute", right: "12px", top: "56px" }}
        >
          + Create Role Assignment
        </Button>
      </Space>
      <Table dataSource={tableData} columns={columns} />;
    </div>
  );
};

export default UserRoles;

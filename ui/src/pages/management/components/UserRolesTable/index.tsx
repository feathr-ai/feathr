import React, {
  forwardRef,
  useCallback,
  useEffect,
  useImperativeHandle,
  useRef,
  useState,
} from "react";
import { Table, Tag, Button, message, Popconfirm } from "antd";
import { DeleteOutlined } from "@ant-design/icons";
import { ColumnsType } from "antd/lib/table";
import dayjs from "dayjs";
import { UserRole } from "../../../../models/model";
import { listUserRole, deleteUserRole } from "../../../../api";

export interface UserRolesTableProps {}

export interface UserRolesTableInstance {
  onSearch?: (values: any) => void;
}

export interface SearchModel {
  scope?: string;
  roleName?: string;
}

const UserRolesTable = (props: UserRolesTableProps, ref: any) => {
  const [loading, setLoading] = useState<boolean>(false);

  const [tableData, setTableData] = useState<UserRole[]>([]);

  const searchRef = useRef<SearchModel>();

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      let result = await listUserRole();
      if (searchRef.current) {
        const { scope, roleName } = searchRef.current;
        result = result.filter((item) => {
          let value = true;
          if (scope) {
            value = item.scope.includes(scope.toLocaleLowerCase());
          }
          if (value && roleName) {
            value = item.roleName === roleName;
          }
          return value;
        });
      }
      result.sort((a: UserRole, b: UserRole) => {
        return dayjs(b.createTime).diff(
          dayjs(a.createTime),
          "milliseconds",
          true
        );
      });

      setTableData(result);
    } catch {
      //
    } finally {
      setLoading(false);
    }
  }, []);

  const onDelete = async (row: UserRole) => {
    try {
      await deleteUserRole(row);
      message.success(`Role ${row.roleName} of user ${row.userName} deleted`);
      fetchData();
    } catch {
      message.error("Failed to delete userrole.");
    }
  };

  const columns: ColumnsType<UserRole> = [
    {
      title: "Scope (Project / Global)",
      dataIndex: "scope",
      ellipsis: true,
    },
    {
      title: "Role",
      dataIndex: "roleName",
      width: 120,
    },
    {
      title: "User",
      dataIndex: "userName",
      ellipsis: true,
    },
    {
      title: "Permissions",
      dataIndex: "access",
      width: 240,
      render: (col: string[]) => {
        return col.map((tag) => {
          let color = tag.length > 5 ? "red" : "green";
          if (tag === "write") color = "blue";
          return (
            <Tag color={color} key={tag}>
              {tag.toUpperCase()}
            </Tag>
          );
        });
      },
    },
    {
      title: "Reason",
      dataIndex: "createReason",
      ellipsis: true,
    },
    {
      title: "Create By",
      dataIndex: "createBy",
      width: 200,
      ellipsis: true,
    },
    {
      title: "Create Time",
      dataIndex: "createTime",
      width: 200,
      sorter: {
        compare: (a: UserRole, b: UserRole) => {
          return dayjs(b.createTime).diff(
            dayjs(a.createTime),
            "milliseconds",
            true
          );
        },
      },
      render: (col: string) => {
        return dayjs(col).format("YYYY-MM-DD HH:mm:ss");
      },
    },
    {
      title: "Action",
      fixed: "right",
      width: 130,
      render: (col: string, record: UserRole) => {
        return (
          <Popconfirm
            placement="left"
            title="Are you sure to delete?"
            onConfirm={() => {
              onDelete(record);
            }}
          >
            <Button danger icon={<DeleteOutlined />}>
              Delete
            </Button>
          </Popconfirm>
        );
      },
    },
  ];

  useImperativeHandle<any, UserRolesTableInstance>(ref, () => {
    return {
      onSearch: (values: SearchModel) => {
        searchRef.current = values;
        fetchData();
      },
    };
  });

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  return (
    <Table
      rowKey="id"
      loading={loading}
      columns={columns}
      dataSource={tableData}
      scroll={{ x: 1200 }}
    />
  );
};

const UserRolesTableComponent = forwardRef<
  UserRolesTableInstance,
  UserRolesTableProps
>(UserRolesTable);

UserRolesTableComponent.displayName = "UserRolesTableComponent";

export default UserRolesTableComponent;

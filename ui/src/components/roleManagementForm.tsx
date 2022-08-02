import React, { CSSProperties, useEffect, useState } from "react";
import { BackTop, Button, Form, Input, Select, Space } from "antd";
import { Navigate } from "react-router-dom";
import { addUserRole } from "../api";
import { UpCircleOutlined } from "@ant-design/icons";
import { Role, UserRole } from "../models/model";

type RoleManagementFormProps = {
  isNew: boolean;
  editMode: boolean;
  userRole?: UserRole;
};

const Admin = "admin";
const Producer = "producer";
const Consumer = "consumer";

const RoleManagementForm: React.FC<RoleManagementFormProps> = ({
  editMode,
  userRole,
}) => {
  const [fireRedirect] = useState<boolean>(false);
  const [createLoading, setCreateLoading] = useState<boolean>(false);

  const [form] = Form.useForm();
  const { Option } = Select;

  useEffect(() => {
    if (userRole !== undefined) {
      form.setFieldsValue(userRole);
    }
  }, [userRole, form]);

  const onClickSave = async () => {
    setCreateLoading(true);
    const roleForm: Role = form.getFieldsValue();
    await addUserRole(roleForm);
    setCreateLoading(false);
  };

  const styling: CSSProperties = { width: "92%" };
  return (
    <>
      <Form
        form={form}
        style={styling}
        labelCol={{ span: 4 }}
        wrapperCol={{ span: 24 }}
        layout="horizontal"
        initialValues={{ remember: true }}
      >
        <Space direction="vertical" size="large" style={styling}>
          <Form.Item name="scope" label="Scope" rules={[{ required: true }]}>
            <Input disabled={!editMode} />
          </Form.Item>
          <Form.Item
            name="userName"
            label="User Name"
            rules={[{ required: true }]}
          >
            <Input disabled={!editMode} />
          </Form.Item>
          <Form.Item
            name="roleName"
            label="Role Name"
            rules={[{ required: true }]}
          >
            <Select placeholder="Select a role to assign:" allowClear>
              <Option value={Admin}>{Admin}</Option>
              <Option value={Producer}>{Producer}</Option>
              <Option value={Consumer}>{Consumer}</Option>
            </Select>
          </Form.Item>
          <Form.Item name="reason" label="Reason" rules={[{ required: true }]}>
            <Input disabled={!editMode} />
          </Form.Item>
        </Space>
        <Form.Item wrapperCol={{ offset: 11 }}>
          <Button
            type="primary"
            htmlType="button"
            title="submit and go back to list"
            style={{ float: "inline-start" }}
            onClick={onClickSave}
            loading={createLoading}
            disabled={!editMode}
          >
            Submit
          </Button>
        </Form.Item>
        <BackTop style={{ marginBottom: "5%", marginRight: "20px" }}>
          <UpCircleOutlined style={{ fontSize: "400%", color: "#3F51B5" }} />
        </BackTop>
      </Form>
      {fireRedirect && <Navigate to={"/management"}></Navigate>}
    </>
  );
};

export default RoleManagementForm;

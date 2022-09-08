import React, { CSSProperties, useState } from "react";
import { UpCircleOutlined } from "@ant-design/icons";
import { BackTop, Button, Form, Input, Select, Space, Typography } from "antd";
import { ValueType } from "../../models/model";

type Props = {
  onFeatureKeyChange: any;
};
const FeatureKeyForm = ({ onFeatureKeyChange }: Props) => {
  const [featureKey, setFeatureKey] = useState({});
  const [form] = Form.useForm();
  const valueOptions = ValueType.map((p) => ({ value: p, label: p }));
  const onClickNext = () => {
    const values = form.getFieldsValue();
    onFeatureKeyChange(values);
  };
  const styling: CSSProperties = {
    width: "85%",
    paddingTop: "2%",
  };

  return (
    <div>
      <Form
        form={form}
        style={styling}
        labelCol={{ span: 5 }}
        wrapperCol={{ span: 15 }}
        layout="horizontal"
        initialValues={{ remember: true }}
      >
        <Space direction="vertical" size="large" style={styling}>
          {/* Feature keys; TODO: support more than one feature keys */}
          <Typography.Title level={4}>Feature Keys </Typography.Title>
          <Form.Item
            name="keyColumn"
            label="Key Column"
            style={{ marginLeft: "5%" }}
            rules={[{ required: true }]}
          >
            <Input name="keyColumn" />
          </Form.Item>
          <Form.Item
            name="keyColumnType"
            label="Key Column Type"
            style={{ marginLeft: "5%" }}
            rules={[{ required: true }]}
          >
            <Select options={valueOptions}></Select>
          </Form.Item>
          <Form.Item
            name="description"
            label="Description"
            style={{ marginLeft: "5%" }}
          >
            <Input name="description" />
          </Form.Item>
          <Form.Item
            name="keyFullName"
            label="Key Full Name"
            style={{ marginLeft: "5%" }}
          >
            <Input name="keyFullName" />
          </Form.Item>
          <Form.Item
            name="keyColumnAlias"
            label="Key Column Alias"
            style={{ marginLeft: "5%" }}
          >
            <Input name="keyColumnAlias" />
          </Form.Item>
          <Form.Item wrapperCol={{ offset: 11 }}>
            <Button
              type="primary"
              htmlType="button"
              title="submit and go back to list"
              style={{ float: "inline-start" }}
              onClick={onClickNext}
            >
              Save + Next: Feature Type {">"}
            </Button>
          </Form.Item>
          <BackTop style={{ marginBottom: "5%", marginRight: "20px" }}>
            <UpCircleOutlined style={{ fontSize: "400%", color: "#3F51B5" }} />
          </BackTop>
        </Space>
      </Form>
    </div>
  );
};

export default FeatureKeyForm;

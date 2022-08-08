import React, { CSSProperties, useEffect, useState } from "react";
import { UpCircleOutlined } from "@ant-design/icons";
import { BackTop, Button, Form, Input, message, Space } from "antd";
import { Navigate } from "react-router-dom";
import { createFeature, updateFeature } from "../api";
import { FeatureAttributes, Feature } from "../models/model";

type FeatureFormProps = {
  isNew: boolean;
  editMode: boolean;
  feature?: FeatureAttributes;
};

const FeatureForm: React.FC<FeatureFormProps> = ({
  isNew,
  editMode,
  feature,
}) => {
  const [fireRedirect, setRedirect] = useState<boolean>(false);
  const [createLoading, setCreateLoading] = useState<boolean>(false);

  const [form] = Form.useForm();

  useEffect(() => {
    if (feature !== undefined) {
      form.setFieldsValue(feature);
    }
  }, [feature, form]);

  const onClickSave = async () => {
    setCreateLoading(true);
    const featureToSave: Feature = form.getFieldsValue();
    if (isNew) {
      const resp = await createFeature(featureToSave);
      if (resp.status === 201) {
        message.success("New feature created");
        setRedirect(true);
      } else {
        message.error(`${resp.data}`, 8);
      }
    } else if (feature?.qualifiedName !== undefined) {
      const resp = await updateFeature(featureToSave, feature.qualifiedName);
      if (resp.status === 200) {
        message.success("Feature is updated successfully");
        setRedirect(true);
      } else {
        message.error(`${resp.data}`, 8);
      }
    }
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
          <Form.Item name="name" label="Name">
            <Input disabled={!editMode} />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input disabled={!editMode} />
          </Form.Item>
          <Form.Item name="type" label="Type">
            <Input disabled={!editMode} />
          </Form.Item>
          <Form.Item name="dataSource" label="Data Source">
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
      {fireRedirect && <Navigate to={"/features"}></Navigate>}
    </>
  );
};

export default FeatureForm;

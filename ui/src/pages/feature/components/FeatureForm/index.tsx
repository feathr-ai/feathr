import React, { forwardRef, useEffect, useState } from "react";
import { Button, Form, Input, message } from "antd";
import { useNavigate } from "react-router-dom";
import { createFeature, updateFeature } from "@/api";
import { FeatureAttributes, Feature } from "@/models/model";

export interface FeatureFormProps {
  isNew: boolean;
  editMode: boolean;
  feature?: FeatureAttributes;
}

const FeatureForm = (props: FeatureFormProps, ref: any) => {
  const navigate = useNavigate();

  const { isNew, editMode, feature } = props;

  const [createLoading, setCreateLoading] = useState<boolean>(false);

  const [form] = Form.useForm();

  const handleFinish = async (values: Feature) => {
    setCreateLoading(true);
    try {
      if (isNew) {
        await createFeature(values);
        message.success("New feature created");
      } else if (feature?.qualifiedName) {
        values.guid = feature.qualifiedName;
        await updateFeature(values);
        message.success("Feature is updated successfully");
      }
      navigate("/features");
    } catch (err: any) {
      message.error(err.detail || err.message, 8);
    } finally {
      setCreateLoading(false);
    }
  };

  useEffect(() => {
    if (feature) {
      form.setFieldsValue(feature);
    }
  }, [feature, form]);

  return (
    <>
      <Form
        style={{ margin: "0 auto", maxWidth: 600 }}
        layout="vertical"
        form={form}
        onFinish={handleFinish}
        disabled={!editMode}
      >
        <Form.Item name="name" label="Name">
          <Input />
        </Form.Item>
        <Form.Item name="description" label="Description">
          <Input />
        </Form.Item>
        <Form.Item name="type" label="Type">
          <Input />
        </Form.Item>
        <Form.Item name="dataSource" label="Data Source">
          <Input />
        </Form.Item>
        <Form.Item>
          <Button
            type="primary"
            htmlType="submit"
            title="submit and go back to list"
            loading={createLoading}
          >
            Submit
          </Button>
        </Form.Item>
      </Form>
    </>
  );
};

const FeatureFormComponent = forwardRef<unknown, FeatureFormProps>(FeatureForm);

FeatureFormComponent.displayName = "FeatureFormComponent";

export default FeatureFormComponent;

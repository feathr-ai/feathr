import React, { CSSProperties, useState } from "react";
import { UpCircleOutlined } from "@ant-design/icons";
import { BackTop, Button, Form, Select, Space, Typography } from "antd";
import { ValueType, TensorCategory, VectorType } from "../../models/model";

type Props = {
  onFeatureTypeChange: any;
  featureTypeProp: any;
};
const FeatureTypeForm = ({ onFeatureTypeChange, featureTypeProp }: Props) => {
  const [form] = Form.useForm();
  const valueOptions = ValueType.map((p) => ({ value: p, label: p }));
  const tensorOptions = TensorCategory.map((p) => ({ value: p, label: p }));
  const typeOptions = VectorType.map((p) => ({ value: p, label: p }));
  const onClickNext = () => {
    const values = form.getFieldsValue();
    onFeatureTypeChange(values);
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
          <Typography.Title level={4}>Feature Type </Typography.Title>
          <Form.Item
            name="type"
            label="Type"
            style={{ marginLeft: "5%" }}
            rules={[{ required: true }]}
            initialValue={featureTypeProp?.type}
          >
            <Select options={typeOptions}></Select>
          </Form.Item>
          <Form.Item
            name="tensorCategory"
            label="Tensor Category"
            style={{ marginLeft: "5%" }}
            rules={[{ required: true }]}
            initialValue={featureTypeProp?.tensorCategory}
          >
            <Select options={tensorOptions}></Select>
          </Form.Item>
          <Form.Item
            name="dimensionType"
            label="Dimension Type"
            style={{ marginLeft: "5%" }}
            rules={[{ required: true }]}
            initialValue={featureTypeProp?.dimensionType}
          >
            <Select mode="multiple" options={valueOptions}></Select>
          </Form.Item>
          <Form.Item
            name="valType"
            label="Value Type"
            style={{ marginLeft: "5%" }}
            rules={[{ required: true }]}
            initialValue={featureTypeProp?.valType}
          >
            <Select options={valueOptions}></Select>
          </Form.Item>
          <Form.Item wrapperCol={{ offset: 11 }}>
            <Button
              type="primary"
              htmlType="button"
              title="submit and go to the next menu"
              style={{ float: "inline-start" }}
              onClick={onClickNext}
            >
              Save + Next: Transformation {">"}
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

export default FeatureTypeForm;

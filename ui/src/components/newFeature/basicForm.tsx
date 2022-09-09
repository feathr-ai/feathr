import React, { CSSProperties } from "react";
import { UpCircleOutlined } from "@ant-design/icons";
import { BackTop, Button, Form, Input, Space, Typography } from "antd";

type Props = {
  onBasicChange: any;
  basicProp: any;
};

const BasicForm = ({ onBasicChange, basicProp }: Props) => {
  const [form] = Form.useForm();

  const styling: CSSProperties = {
    width: "85%",
    paddingTop: "2%",
  };

  const onClickNext = () => {
    const values = form.getFieldsValue();
    onBasicChange(values);
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
          <Form.Item
            name="name"
            label="Name"
            initialValue={basicProp?.name}
            style={{ marginBottom: "1%" }}
            rules={[{ required: true }]}
          >
            <Input name="name" />
          </Form.Item>
          {/*TODO: create qualifed name automatically after inputing name */}
          <Form.Item
            name="qualifiedName"
            label="Qualified name"
            initialValue={basicProp?.qualifiedName}
            style={{ marginBottom: "1%" }}
            rules={[{ required: true }]}
          >
            <Input name="qualifiedName" />
          </Form.Item>

          {/*TODO: support more than one tags */}
          <Typography.Title level={4}> Tags </Typography.Title>
          <Form.Item style={{ marginLeft: "5%" }}>
            <Form.Item
              name="tagName"
              label="name"
              style={{ display: "inline-block", width: "40%" }}
              initialValue={basicProp?.tagName}
            >
              <Input name="tagName" />
            </Form.Item>
            <Form.Item
              name="tagValue"
              label="value"
              style={{
                display: "inline-block",
                width: "50%",
                paddingLeft: "10px",
                paddingRight: "0px",
              }}
              initialValue={basicProp?.tagValue}
            >
              <Input name="tagValue" />
            </Form.Item>
          </Form.Item>

          <Form.Item wrapperCol={{ offset: 11 }}>
            <Button
              type="primary"
              htmlType="button"
              title="submit and go to the next menu"
              style={{ float: "inline-start" }}
              onClick={onClickNext}
            >
              Save + Next: Feature Keys {">"}
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

export default BasicForm;

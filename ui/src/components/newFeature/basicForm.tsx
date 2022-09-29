import React, { CSSProperties } from "react";
import {
  UpCircleOutlined,
  MinusCircleOutlined,
  PlusOutlined,
} from "@ant-design/icons";
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
        <Space direction="vertical" style={styling}>
          <Form.Item
            name="name"
            label="Name"
            initialValue={basicProp?.name}
            style={{ marginBottom: "1%" }}
            rules={[{ required: true }]}
          >
            <Input name="name" />
          </Form.Item>
          <Typography.Title level={4}>Feature Tags </Typography.Title>
          <Form.List name="tags" initialValue={basicProp.tags}>
            {(fields, { add, remove }) => (
              <>
                {fields.map((field, index) => (
                  <Space
                    key={field.key}
                    align="baseline"
                    direction="horizontal"
                  >
                    <Form.Item
                      {...field}
                      label="Name"
                      name={[field.name, "name"]}
                      rules={[{ required: true, message: "Missing tag name" }]}
                    >
                      <Input name="tagName" style={{ marginLeft: "10%" }} />
                    </Form.Item>
                    <Form.Item
                      {...field}
                      label="Value"
                      name={[field.name, "value"]}
                      rules={[{ required: true, message: "Missing tag value" }]}
                    >
                      <Input name="tagValue" style={{ marginLeft: "10%" }} />
                    </Form.Item>

                    <MinusCircleOutlined onClick={() => remove(field.name)} />
                  </Space>
                ))}

                <Form.Item>
                  <Button
                    type="dashed"
                    onClick={() => add()}
                    block
                    icon={<PlusOutlined />}
                    style={{ marginLeft: "5%" }}
                  >
                    Add tags
                  </Button>
                </Form.Item>
              </>
            )}
          </Form.List>

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

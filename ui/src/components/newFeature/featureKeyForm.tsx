import React, { CSSProperties } from "react";
import { UpCircleOutlined } from "@ant-design/icons";
import { BackTop, Button, Form, Input, Select, Space, Typography } from "antd";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { ValueType } from "../../models/model";

type Props = {
  onFeatureKeyChange: any;
  featureKeyProp: any;
};
const FeatureKeyForm = ({ onFeatureKeyChange, featureKeyProp }: Props) => {
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
          <Typography.Title level={4}>Feature Keys </Typography.Title>
          <Form.List name="keys" initialValue={featureKeyProp}>
            {(fields, { add, remove }) => (
              <>
                {fields.map((field) => (
                  <div key={field.name}>
                    <Form.Item
                      {...field}
                      label="Key Column"
                      name={[field.name, "keyColumn"]}
                      rules={[
                        { required: true, message: "Missing key column" },
                      ]}
                    >
                      <Input name="keyColumn" style={{ marginLeft: "1%" }} />
                    </Form.Item>
                    <Form.Item
                      {...field}
                      label="Key Column Type"
                      name={[field.name, "keyColumnType"]}
                      rules={[
                        {
                          required: true,
                          message: "Missing key column type",
                        },
                      ]}
                    >
                      <Select options={valueOptions}></Select>
                    </Form.Item>
                    <Form.Item
                      {...field}
                      label="Key Full Name"
                      name={[field.name, "keyFullName"]}
                    >
                      <Input name="keyFullName" />
                    </Form.Item>
                    <Form.Item
                      {...field}
                      label="Key Column Alias"
                      name={[field.name, "keyColumnAlias"]}
                    >
                      <Input name="keyColumnAlias" />
                    </Form.Item>
                    <Form.Item
                      {...field}
                      label="Description"
                      name={[field.name, "description"]}
                    >
                      <Input name="description" />
                    </Form.Item>

                    <MinusCircleOutlined
                      onClick={() => {
                        remove(field.name);
                      }}
                    />
                  </div>
                ))}

                <Form.Item>
                  <Button
                    type="dashed"
                    onClick={() => add()}
                    block
                    icon={<PlusOutlined />}
                    style={{ marginLeft: "5%" }}
                  >
                    Add keys
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

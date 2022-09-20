import React, { CSSProperties, useState } from "react";
import { UpCircleOutlined } from "@ant-design/icons";
import { BackTop, Button, Form, Input, Radio, Space, Typography } from "antd";

type Props = {
  onTransformationChange: any;
  transformationProp: any;
};
const TransformationForm = ({
  onTransformationChange,
  transformationProp,
}: Props) => {
  const [transformationType, setTransformationType] = useState("");
  const [form] = Form.useForm();

  const styling: CSSProperties = {
    width: "85%",
    paddingTop: "2%",
  };

  const onRadioChange = (e: any) => {
    setTransformationType(e.target.value);
  };

  const onClickNext = () => {
    const values = form.getFieldsValue();
    onTransformationChange(values);
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
            label={"Select Transformation Type"}
            style={{ marginBottom: "1%" }}
            rules={[
              {
                required: true,
              },
            ]}
          >
            <Radio.Group
              onChange={onRadioChange}
              style={{ paddingLeft: "2%" }}
              defaultValue={transformationType}
            >
              <Radio
                value={"expression"}
                style={{ width: "100%", paddingLeft: "5%", marginBottom: "1%" }}
              >
                Expression Transformation
              </Radio>
              <Radio
                value={"window"}
                style={{ width: "100%", paddingLeft: "5%", marginBottom: "1%" }}
              >
                Window Transformation
              </Radio>
              <Radio
                value={"udf"}
                style={{ width: "100%", paddingLeft: "5%", marginBottom: "1%" }}
              >
                UDF Transformation
              </Radio>
            </Radio.Group>
          </Form.Item>
          {transformationType === "expression" && (
            <div>
              <Form.Item
                name="transformExpr"
                label="Expression Transformation"
                style={{ marginBottom: "1%", marginLeft: "1%" }}
                rules={[
                  {
                    required: true,
                  },
                ]}
              >
                <Input
                  name="transformExpr"
                  defaultValue={transformationProp?.transformExpr}
                />
              </Form.Item>
            </div>
          )}
          {transformationType === "window" && (
            <div>
              <Typography.Title level={4}>
                Window Transformation
              </Typography.Title>
              <Form.Item
                name="defExpr"
                label="Definition Expression"
                style={{ marginLeft: "5%" }}
                rules={[
                  {
                    required: true,
                  },
                ]}
              >
                <Input
                  name="defExpr"
                  defaultValue={transformationProp?.defExpr}
                />
              </Form.Item>
              <Form.Item
                name="aggFunc"
                label="Aggregation Function"
                style={{ marginLeft: "5%" }}
              >
                <Input
                  name="aggFunc"
                  defaultValue={transformationProp?.aggFunc}
                />
              </Form.Item>

              <Form.Item
                name="window"
                label="Window"
                style={{ marginLeft: "5%" }}
              >
                <Input
                  name="window"
                  defaultValue={transformationProp?.window}
                />
              </Form.Item>
              <Form.Item
                name="groupBy"
                label="Group By"
                style={{ marginLeft: "5%" }}
              >
                <Input
                  name="groupBy"
                  defaultValue={transformationProp?.groupBy}
                />
              </Form.Item>
              <Form.Item
                name="filter"
                label="Filter"
                style={{ marginLeft: "5%" }}
              >
                <Input
                  name="filter"
                  defaultValue={transformationProp?.filter}
                />
              </Form.Item>
              <Form.Item
                name="limit"
                label="Limit"
                style={{ marginLeft: "5%" }}
              >
                <Input name="limit" defaultValue={transformationProp?.limit} />
              </Form.Item>
            </div>
          )}

          {transformationType === "udf" && (
            <div>
              <Form.Item
                label="UDF Transformation"
                name="name"
                style={{ marginBottom: "4%", marginLeft: "1%" }}
                rules={[{ required: true }]}
              >
                <Input
                  name="udfTransformation"
                  defaultValue={transformationProp?.name}
                />
              </Form.Item>
            </div>
          )}
          <Form.Item wrapperCol={{ offset: 11 }}>
            <Button
              type="primary"
              htmlType="button"
              title="submit and go back to list"
              style={{ float: "inline-start" }}
              onClick={onClickNext}
            >
              Save + Next: Review {">"}
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

export default TransformationForm;

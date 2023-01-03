import React, { forwardRef, Fragment } from "react";
import { Button, Divider, Form, Input, Radio, Select, Space } from "antd";
import ProjectsSelect from "@/components/ProjectsSelect";
import { MinusCircleOutlined, PlusOutlined } from "@ant-design/icons";
import { useBasicForm } from "./useForm";

export interface FeatureFormProps {}

const { Item } = Form;

const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 8 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 16 },
  },
};

const FeatureForm = (props: FeatureFormProps, ref: any) => {
  const [form] = Form.useForm();

  const {
    createLoading,
    loading,
    featureType,
    selectTransformationType,
    anchorOptions,
    anchorFeatureOptions,
    derivedFeatureOptions,
    valueOptions,
    tensorOptions,
    typeOptions,
    onFinish,
  } = useBasicForm(form);

  return (
    <>
      <Form
        {...formItemLayout}
        style={{ margin: "0 auto", maxWidth: 700 }}
        labelWrap
        form={form}
        onFinish={onFinish}
      >
        <Item name="name" label="Name" rules={[{ required: true }]}>
          <Input maxLength={200} />
        </Item>
        <Item
          label="Select Project"
          name="project"
          rules={[
            {
              required: true,
              message: "Please select a project to start.",
            },
          ]}
        >
          <ProjectsSelect width={"100%"} />
        </Item>
        <Item label="Select Feature Type" name="featureType">
          <Radio.Group>
            <Radio value={1}>Anchor Feature</Radio>
            <Radio value={2}>Derived Feature</Radio>
          </Radio.Group>
        </Item>
        {featureType === 1 ? (
          <>
            <Item
              label="Select Anchor"
              name="anchor"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <Select options={anchorOptions} loading={loading} />
            </Item>
          </>
        ) : (
          <>
            <Item label="Select Input Anchor Features" name="anchorFeatures">
              <Select
                mode="multiple"
                showArrow
                options={anchorFeatureOptions}
                loading={loading}
              />
            </Item>
            <Item label="Select Input Derived Features" name="derivedFeatures">
              <Select
                mode="multiple"
                showArrow
                options={derivedFeatureOptions}
                loading={loading}
              />
            </Item>
          </>
        )}
        <Divider orientation="left">Feature Tags</Divider>

        <Form.List name="tags">
          {(fields, { add, remove }) => (
            <>
              {fields.map(({ key, name }, index) => (
                <Fragment key={key}>
                  <Form.Item key={key} label="Name">
                    <div
                      style={{
                        display: "flex",
                        alignItems: "baseline",
                        gap: 8,
                      }}
                    >
                      <Form.Item
                        style={{ marginBottom: 0, flex: 1 }}
                        name={[name, "name"]}
                      >
                        <Input maxLength={50} />
                      </Form.Item>
                      <Form.Item
                        label="Value"
                        style={{ marginBottom: 0, flex: 1 }}
                        name={[name, "value"]}
                      >
                        <Input maxLength={50} />
                      </Form.Item>
                      <MinusCircleOutlined onClick={() => remove(name)} />
                    </div>
                  </Form.Item>
                </Fragment>
              ))}

              <Form.Item label=" " colon={false}>
                <Button
                  type="dashed"
                  onClick={() => add()}
                  block
                  icon={<PlusOutlined />}
                >
                  Add tags
                </Button>
              </Form.Item>
            </>
          )}
        </Form.List>
        <Divider orientation="left">Feature Keys</Divider>
        <Form.List name="keys">
          {(fields, { add, remove }) => (
            <>
              {fields.map(({ key, name }, index) => (
                <Fragment key={key}>
                  <Item
                    label="Key Column"
                    name={[name, "keyColumn"]}
                    rules={[{ required: true, message: "Missing key column" }]}
                  >
                    <div
                      style={{
                        display: "flex",
                        alignItems: "baseline",
                        gap: 8,
                      }}
                    >
                      <Form.Item noStyle style={{ flex: 1 }}>
                        <Input maxLength={50} />
                      </Form.Item>
                      <MinusCircleOutlined onClick={() => remove(name)} />
                    </div>
                  </Item>
                  <Item
                    label="Key Column Type"
                    name={[name, "keyColumnType"]}
                    rules={[
                      {
                        required: true,
                        message: "Missing key column type",
                      },
                    ]}
                  >
                    <Select options={valueOptions} />
                  </Item>
                  <Form.Item label="Key Full Name" name={[name, "fullName"]}>
                    <Input />
                  </Form.Item>
                  <Form.Item
                    label="Key Column Alias"
                    name={[name, "keyColumnAlias"]}
                  >
                    <Input />
                  </Form.Item>
                  <Form.Item label="Description" name={[name, "description"]}>
                    <Input />
                  </Form.Item>
                  <Divider dashed></Divider>
                </Fragment>
              ))}
              <Form.Item label=" " colon={false}>
                <Button
                  type="dashed"
                  onClick={() => add()}
                  block
                  icon={<PlusOutlined />}
                >
                  Add keys
                </Button>
              </Form.Item>
            </>
          )}
        </Form.List>
        <Divider orientation="left">Feature Type</Divider>
        <Item name="type" label="Type" rules={[{ required: true }]}>
          <Select options={typeOptions}></Select>
        </Item>
        <Item
          name="tensorCategory"
          label="Tensor Category"
          rules={[{ required: true }]}
        >
          <Select options={tensorOptions}></Select>
        </Item>
        <Item name="dimensionType" label="Dimension Type">
          <Select mode="multiple" options={valueOptions} />
        </Item>
        <Item name="valType" label="Value Type" rules={[{ required: true }]}>
          <Select options={valueOptions} />
        </Item>
        <Divider orientation="left">Transformation</Divider>
        <Item
          label="Select Transformation Type"
          name="selectTransformationType"
        >
          <Radio.Group>
            <Space direction="vertical">
              <Radio value={1}>Expression Transformation</Radio>
              <Radio value={2}>Window Transformation</Radio>
              <Radio value={3}>UDF Transformation</Radio>
            </Space>
          </Radio.Group>
        </Item>
        {selectTransformationType === 1 && (
          <Item
            name="transformExpr"
            label="Expression Transformation"
            rules={[
              {
                required: true,
              },
            ]}
          >
            <Input />
          </Item>
        )}
        {selectTransformationType === 2 && (
          <>
            <Item
              name="defExpr"
              label="Definition Expression"
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <Input />
            </Item>
            <Item name="aggFunc" label="Aggregation Function">
              <Input />
            </Item>
            <Item name="window" label="Window">
              <Input />
            </Item>
            <Item name="groupBy" label="Group By">
              <Input />
            </Item>
            <Item name="filter" label="Filter">
              <Input />
            </Item>
            <Item name="limit" label="Limit">
              <Input />
            </Item>
          </>
        )}
        {selectTransformationType === 3 && (
          <Item
            name="udfExpr"
            label="UDF Transformation"
            rules={[{ required: true }]}
          >
            <Input />
          </Item>
        )}

        <Item label=" " colon={false}>
          <Button
            type="primary"
            htmlType="submit"
            title="submit and go back to list"
            loading={createLoading}
          >
            Submit
          </Button>
        </Item>
      </Form>
    </>
  );
};

const FeatureFormComponent = forwardRef<unknown, FeatureFormProps>(FeatureForm);

FeatureFormComponent.displayName = "FeatureFormComponent";

export default FeatureFormComponent;

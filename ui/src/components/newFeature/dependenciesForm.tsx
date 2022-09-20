import React, { CSSProperties, useEffect, useState, useCallback } from "react";
import { UpCircleOutlined } from "@ant-design/icons";
import { BackTop, Button, Form, Select, Space, Radio } from "antd";

import { fetchProjects, fetchProjectLineages } from "../../api";

type Props = {
  onDependenciesChange: any;
  dependenciesProp: any;
  projectIdProp: string;
};

const DependenciesForm = ({
  onDependenciesChange,
  dependenciesProp,
  projectIdProp,
}: Props) => {
  const [form] = Form.useForm();
  const [projects, setProjects] = useState<any>();
  const [project, setProject] = useState<string>(
    dependenciesProp?.project ?? ""
  );
  const [projectId, setProjectId] = useState<string>(projectIdProp);
  const [featureType, setFeatureType] = useState<string>();
  const [anchorOptions, setAnchorOptions] = useState<any>();
  const [anchorFeatureOptions, setAnchorFeatureOptions] = useState<any>();
  const [derivedFeatureOptions, setDerivedFeatureOptions] = useState<any>();
  const [loading, setLoading] = useState<boolean>(false);

  const styling: CSSProperties = {
    width: "85%",
    paddingTop: "2%",
  };

  const loadProjects = useCallback(async () => {
    const currProjects = await fetchProjects();
    const projectOptions = currProjects.map((p) => ({ value: p, label: p }));
    setProjects(projectOptions);
  }, []);

  useEffect(() => {
    loadProjects();
  }, [loadProjects]);

  const fetchData = useCallback(async (project) => {
    const result = await fetchProjectLineages(project);
    const entities = result?.guidEntityMap;
    if (!entities) return;
    const currAnchorOptions = [];
    const currAnchorFeatureOptions = [];
    const currDerivedFeatureOptions = [];

    for (const key in entities) {
      const value = entities[key];
      const name = value.attributes?.name;
      const typeName = value.typeName;
      if (typeName === "feathr_anchor_feature_v1") {
        currAnchorFeatureOptions.push({ value: key, label: name });
      } else if (typeName === "feathr_anchor_v1") {
        currAnchorOptions.push({ value: key, label: name });
      } else if (typeName === "feathr_derived_feature_v1") {
        currDerivedFeatureOptions.push({ value: key, label: name });
      } else if (typeName === "feathr_workspace_v1") {
        setProjectId(key);
      }
    }
    setAnchorOptions(currAnchorOptions);
    setAnchorFeatureOptions(currAnchorFeatureOptions);
    setDerivedFeatureOptions(currDerivedFeatureOptions);
  }, []);

  const onProjectChange = (value: string) => {
    setProject(value);
  };

  useEffect(() => {
    const fetchOptions = async (project: string) => {
      if (project) {
        setLoading(true);
        await fetchData(project);
        setLoading(false);
      }
    };
    fetchOptions(project);
  }, [project, fetchData]);

  const onRadioChange = async (value: any) => {
    setFeatureType(value.target.value);
  };

  const onClickNext = () => {
    const values = form.getFieldsValue();
    onDependenciesChange(values, projectId);
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
            name="project"
            label="Select Project:"
            style={{ marginBottom: "1%" }}
            rules={[
              {
                required: true,
                message: "Please select a project to start.",
              },
            ]}
          >
            <Select
              options={projects}
              defaultValue={project}
              notFoundContent={<div>No projects found from server</div>}
              showSearch={true}
              onChange={onProjectChange}
              style={{ paddingLeft: "2%" }}
            ></Select>
          </Form.Item>
          <Form.Item
            name="featureType"
            label="Select Feature Type: "
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
              defaultValue={dependenciesProp?.featureType}
            >
              <Radio value={"anchor"}>Anchor Feature</Radio>
              <Radio value={"derived"}>Derived Feature</Radio>
            </Radio.Group>
          </Form.Item>
          {featureType === "anchor" && (
            <Form.Item
              name="anchor"
              label="Select Anchor: "
              style={{ marginBottom: "1%" }}
              rules={[
                {
                  required: true,
                },
              ]}
            >
              <Select
                showArrow
                style={{ paddingLeft: "2%" }}
                options={anchorOptions}
                disabled={loading}
                defaultValue={dependenciesProp?.anchor}
              />
            </Form.Item>
          )}
          {featureType === "derived" && (
            <div>
              <Form.Item
                name="inputAnchorFeatures"
                label="Select Input Anchor Features:"
                style={{ marginBottom: "1%" }}
              >
                <Select
                  mode="multiple"
                  showArrow
                  style={{ paddingLeft: "2%" }}
                  options={anchorFeatureOptions}
                  disabled={loading}
                  defaultValue={dependenciesProp?.inputAnchorFeatures}
                />
              </Form.Item>
              <Form.Item
                name="inputDerivedFeatures"
                label="Select Input Derived Features:"
                style={{ marginBottom: "1%" }}
              >
                <Select
                  mode="multiple"
                  showArrow
                  style={{ paddingLeft: "2%" }}
                  options={derivedFeatureOptions}
                  disabled={loading}
                  defaultValue={dependenciesProp?.inputDerivedFeatures}
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
              Save + Next: Basic {">"}
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

export default DependenciesForm;

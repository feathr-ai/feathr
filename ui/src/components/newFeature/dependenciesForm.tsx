import React, { CSSProperties, useEffect, useState, useCallback } from "react";
import { UpCircleOutlined } from "@ant-design/icons";
import { BackTop, Button, Form, Select, Space, Radio } from "antd";

import { fetchProjects, fetchProjectLineages } from "../../api";

type Props = {
  onDependenciesChange: any;
};

const DependenciesForm = ({ onDependenciesChange }: Props) => {
  const [dependencies, setDependencies] = useState({});
  const [form] = Form.useForm();
  const [projects, setProjects] = useState<any>([]);
  const [project, setProject] = useState<string>("");
  const [projectId, setProjectId] = useState<string>("");
  const [featureType, setFeatureType] = useState<string>("");
  const [anchorOptions, setAnchorOptions] = useState<any>([]);
  const [anchor, setAnchor] = useState<string>("");
  const [anchorId, setAnchorId] = useState<string>("");
  const [anchorFeatureOptions, setAnchorFeatureOptions] = useState<any>([]);
  const [anchorFeatures, setAnchorFeatures] = useState<string[]>([]);
  const [derivedFeatureOptions, setDerivedFeatureOptions] = useState<any>([]);
  const [derivedFeatures, setDerivedFeatures] = useState<string[]>([]);

  const styling: CSSProperties = {
    width: "85%",
    paddingTop: "2%",
  };

  const loadProjects = useCallback(async () => {
    const projects = await fetchProjects();
    const projectOptions = projects.map((p) => ({ value: p, label: p }));
    setProjects(projectOptions);
  }, []);

  useEffect(() => {
    loadProjects();
  }, [loadProjects]);

  const fetchData = useCallback(
    async (project) => {
      console.log("fetching features");
      const result = await fetchProjectLineages(project);
      const entities = result.guidEntityMap;
      const currAnchorOptions = anchorOptions;
      const currAnchorFeatureOptions = anchorFeatureOptions;
      const currDerivedFeatureOptions = derivedFeatureOptions;

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
    },
    [anchorOptions, anchorFeatureOptions, derivedFeatureOptions]
  );

  const onProjectChange = async (value: string) => {
    setProject(value);
    await fetchData(value);
  };

  const onRadioChange = (value: any) => {
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
              value={project}
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
            <Radio.Group onChange={onRadioChange} style={{ paddingLeft: "2%" }}>
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
                //tagRender={tagRender}
                //defaultValue={['gold', 'cyan']}
                style={{ paddingLeft: "2%" }}
                options={anchorOptions}
              />
            </Form.Item>
          )}
          {featureType === "derived" && (
            <div>
              <Form.Item
                name="inputAnchorFeatures"
                label="Select Input Anchor Features:"
                style={{ marginBottom: "1%" }}
                rules={[
                  {
                    required: true,
                  },
                ]}
              >
                <Select
                  mode="multiple"
                  showArrow
                  //tagRender={tagRender}
                  //defaultValue={['gold', 'cyan']}
                  style={{ paddingLeft: "2%" }}
                  options={anchorFeatureOptions}
                />
              </Form.Item>
              <Form.Item
                name="inputDerivedFeatures"
                label="Select Input Derived Features:"
                style={{ marginBottom: "1%" }}
                rules={[
                  {
                    required: true,
                  },
                ]}
              >
                <Select
                  mode="multiple"
                  showArrow
                  //tagRender={tagRender}
                  //defaultValue={['gold', 'cyan']}
                  style={{ paddingLeft: "2%" }}
                  options={derivedFeatureOptions}
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

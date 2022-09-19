import React, { useState } from "react";
import { Card, Typography, Menu, Layout, Button, Alert } from "antd";
import BasicForm from "../../components/newFeature/basicForm";
import TransformationForm from "../../components/newFeature/transformationForm";
import DependenciesForm from "../../components/newFeature/dependenciesForm";
import FeatureKeyForm from "../../components/newFeature/featureKeyForm";
import FeatureTypeForm from "../../components/newFeature/featureTypeForm";
import {
  AnchorFeature,
  DerivedFeature,
  FeatureKey,
  FeatureType,
} from "../../models/model";
import { createAnchorFeature, createDerivedFeature } from "../../api";

const { Title } = Typography;

const NewFeature = () => {
  const [menu, setMenu] = useState<string>("dependencies");
  const [anchorFeature, setAnchorFeature] = useState<AnchorFeature>();
  const [derivedFeature, setDerivedFeature] = useState<DerivedFeature>();
  const [type, setType] = useState<string>();
  const [featureType, setFeatureType] = useState<FeatureType>();
  const [dependencies, setDependencies] = useState<any>();
  const [basic, setBasic] = useState<any>();
  const [transformation, setTransformation] = useState<any>();
  const [featureKeys, setFeatureKeys] = useState<FeatureKey[]>([]);
  const [projectId, setProjectid] = useState<string>("");
  const [newFeature, setNewFeature] = useState<any>();
  const [alerts, setAlerts] = useState<string>("");
  const [result, setResult] = useState<any>();

  const onMenuChange = (e: any) => {
    setMenu(e.key);
  };

  const onDependenciesChange = (e: any, projectId: string) => {
    setDependencies(e);
    setProjectid(projectId);
    setType(e.featureType);
    setMenu("basic");
  };

  const onBasicChange = (e: any) => {
    setBasic(e);
    setMenu("featureKey");
  };

  const onFeatureKeyChange = (e: any) => {
    setFeatureKeys(e.keys);
    setMenu("featureType");
  };

  const onFeatureTypeChange = (e: any) => {
    setFeatureType(e);
    setMenu("transformation");
  };

  const onTransformationChange = async (e: any) => {
    setTransformation(e);
    onReview();
    setMenu("review + create");
  };

  const validateOnReview = () => {
    var alerts = "";
    if (!basic) {
      alerts += "basic; \n";
    } else {
      if (!basic.name) alerts += "basic.name;\n";
      if (!basic.qualifiedName) alerts += "basic.qualifiedName;\n";
    }
    if (!dependencies) {
      alerts += "dependencies; \n";
    } else {
      if (!dependencies.project) alerts += "dependencies.project; \n";
      if (!dependencies.featureType) alerts += "dependencies.featureType; \n";
      if (type === "anchor" && !dependencies.anchor)
        alerts += "dependencies.anchor; \n";
    }
    if (!featureType) {
      alerts += "featureType;\n";
    } else {
      if (!featureType.tensorCategory) alerts += "featureType.tensorCategory;";
      if (!featureType.type) alerts += "featureType.type; \n";
      if (!featureType.valType) alerts += "featureType.calType; \n";
    }
    if (!transformation) alerts += "transformation; \n";
    return alerts;
  };

  const onReview = () => {
    const alerts = validateOnReview();
    if (alerts !== "") {
      setAlerts(
        "These items are required.: \n" +
          alerts +
          "Please fill them before submitting"
      );
      setNewFeature(undefined);
      return;
    }

    setAlerts("");
    if (!featureType || !transformation) {
      console.error("invalid feature definition");
      return;
    }

    if (type === "derived") {
      const newFeature = {
        name: basic.name,
        qualifiedName: basic.qualifiedName,
        featureType: featureType,
        transformation: transformation,
        key: featureKeys,
        tags: basic.tags ?? {},
        inputAnchorFeatures: dependencies?.inputAnchorFeatures
          .toString()
          .split(","),
        inputDerivedFeatures: dependencies?.inputDerivedFeatures
          .toString()
          .split(","),
      };
      setNewFeature(newFeature);
    } else if (type === "anchor") {
      const newFeature = {
        name: basic.name,
        qualifiedName: basic.qualifiedName,
        featureType: featureType,
        transformation: transformation,
        key: featureKeys,
        tags: basic.tags ?? {},
      };
      setNewFeature(newFeature);
    }
  };

  const onSubmit = async () => {
    if (type === "derived") {
      const result = await createDerivedFeature(projectId, newFeature);
      setResult(result);
      console.log(result);
    } else {
      const anchorId = dependencies.anchor;
      const result = await createAnchorFeature(projectId, anchorId, newFeature);
      setResult(result);
      console.log(result);
    }
  };

  return (
    <div className="page">
      <Title level={3}>Create Feature</Title>
      <Layout.Header
        className="layout-header"
        style={{ backgroundColor: "#fff", height: "auto" }}
      >
        <Menu
          mode={"horizontal"}
          theme={"light"}
          defaultSelectedKeys={[menu]}
          selectedKeys={[menu]}
        >
          <Menu.Item
            key="dependencies"
            style={{ width: "20%" }}
            onClick={onMenuChange}
          >
            dependencies
          </Menu.Item>
          <Menu.Item
            key="basic"
            style={{ width: "15%" }}
            onClick={onMenuChange}
          >
            basic
          </Menu.Item>
          <Menu.Item
            key="featureKey"
            style={{ width: "20%" }}
            onClick={onMenuChange}
          >
            feature keys
          </Menu.Item>
          <Menu.Item
            key="featureType"
            style={{ width: "20%" }}
            onClick={onMenuChange}
          >
            feature type
          </Menu.Item>
          <Menu.Item
            key="transformation"
            style={{ width: "20%" }}
            onClick={onMenuChange}
          >
            transformation
          </Menu.Item>
        </Menu>
        <Button
          type="primary"
          htmlType="button"
          title="submit and go back to list"
          style={{ float: "inline-start" }}
          onClick={() => {
            onReview();
            setMenu("review + create");
          }}
        >
          Review + create
        </Button>
      </Layout.Header>
      <span></span>
      {menu === "basic" && (
        <BasicForm onBasicChange={onBasicChange} basicProp={basic ?? {}} />
      )}
      {menu === "featureKey" && (
        <FeatureKeyForm
          onFeatureKeyChange={onFeatureKeyChange}
          featureKeyProp={featureKeys ?? {}}
        />
      )}
      {menu === "featureType" && (
        <FeatureTypeForm
          onFeatureTypeChange={onFeatureTypeChange}
          featureTypeProp={featureType ?? {}}
        />
      )}
      {menu === "dependencies" && (
        <DependenciesForm
          onDependenciesChange={onDependenciesChange}
          dependenciesProp={dependencies ?? {}}
          projectIdProp={projectId ?? " "}
        />
      )}
      {menu === "transformation" && (
        <TransformationForm onTransformationChange={onTransformationChange} />
      )}
      {/* TODO: apply styles for the summary */}
      {menu === "review + create" && (
        <div>
          {alerts !== "" && (
            <Alert
              showIcon
              message={"Verification Failed."}
              description={alerts}
              type="error"
            />
          )}
          {newFeature && (
            <div>
              <Alert
                message={"Verification Succeed."}
                description={JSON.stringify(newFeature)}
                type="success"
              />

              <Button
                type="primary"
                htmlType="button"
                title="submit to create this feature"
                style={{ float: "inline-start", marginTop: "5%" }}
                onClick={() => {
                  onSubmit();
                }}
              >
                Submit
              </Button>
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default NewFeature;

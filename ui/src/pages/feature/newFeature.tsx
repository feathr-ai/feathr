import React, { useState } from "react";
import { Typography, Menu, Layout, Button, Alert } from "antd";
import BasicForm from "../../components/newFeature/basicForm";
import TransformationForm from "../../components/newFeature/transformationForm";
import DependenciesForm from "../../components/newFeature/dependenciesForm";
import FeatureKeyForm from "../../components/newFeature/featureKeyForm";
import FeatureTypeForm from "../../components/newFeature/featureTypeForm";
import { FeatureKey, FeatureType } from "../../models/model";
import { createAnchorFeature, createDerivedFeature } from "../../api";

const { Title } = Typography;

const NewFeature = () => {
  const [menu, setMenu] = useState<string>("dependencies");
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

  const onTransformationChange = (e: any) => {
    setTransformation(e);
    setMenu("review + create");
    onReview(e);
  };

  const validateOnReview = (transformationParam: any) => {
    var alerts = "";
    if (!basic) {
      alerts += "basic; \n";
    } else {
      if (!basic.name) alerts += "basic.name;\n";
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
    if (!transformationParam) alerts += "transformation; \n";
    else if (
      !transformationParam.transformExpr &&
      !transformationParam.defExpr &&
      !transformationParam.aggFunc
    ) {
      alerts +=
        "one of [transformationParam.transformExpr, transformationParam.defExpr, transformationParam.aggFunc]; \n";
    }
    return alerts;
  };

  const onReview = (transformationParam: any) => {
    setResult(undefined);
    setAlerts("");
    setNewFeature(undefined);
    console.log("transformation: ", transformationParam);
    const alerts = validateOnReview(transformationParam);
    if (alerts !== "") {
      setAlerts(
        "These items are required: \n" +
          alerts +
          "Please fill them before submitting"
      );
      return;
    }

    if (!featureType || !transformationParam) {
      console.error("invalid feature definition");
      return;
    }

    if (type === "derived") {
      const newFeature = {
        name: basic.name,
        featureType: featureType,
        transformation: transformationParam,
        key: featureKeys,
        tags: basic.tags ?? {},
        inputAnchorFeatures: dependencies.inputAnchorFeatures
          ? dependencies.inputAnchorFeatures.toString().split(",")
          : [],
        inputDerivedFeatures: dependencies.inputDerivedFeatures
          ? dependencies.inputDerivedFeatures.toString().split(",")
          : [],
      };
      setNewFeature(newFeature);
    } else if (type === "anchor") {
      const newFeature = {
        name: basic.name,
        featureType: featureType,
        transformation: transformationParam,
        key: featureKeys,
        tags: basic.tags ?? {},
      };
      setNewFeature(newFeature);
    }
  };

  const onSubmit = async () => {
    var currResult = undefined;
    if (type === "derived") {
      currResult = await createDerivedFeature(projectId, newFeature);
      setResult(currResult);
    } else {
      const anchorId = dependencies.anchor;
      currResult = await createAnchorFeature(projectId, anchorId, newFeature);
      setResult(currResult);
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
            onReview(transformation);
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
        <TransformationForm
          onTransformationChange={onTransformationChange}
          transformationProp={transformation ?? {}}
        />
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
            // <ReviewAndCreate onSubmit={onSubmit} newFeatureProp={newFeature} />
            <div>
              <Title level={5}>New Feature Review</Title>
              <Alert
                message={<p>Verification Succeeded.</p>}
                description={<p>{JSON.stringify(newFeature, null, "\t\n")}</p>}
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
          {result &&
            result.status &&
            JSON.stringify(result.status) === "200" && (
              <Alert
                message={<p> Feature Creation Succeeded! </p>}
                type="success"
              />
            )}
          {result &&
            (!result.status || JSON.stringify(result.status) !== "200") && (
              <Alert
                message={<p> Feature Creation Failed! </p>}
                type="error"
                description={<p>{JSON.stringify(result)}</p>}
              />
            )}
        </div>
      )}
    </div>
  );
};

export default NewFeature;

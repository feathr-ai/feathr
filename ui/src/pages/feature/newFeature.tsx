import React, { useState } from "react";
import { Card, Typography, Menu, Layout, Button } from "antd";
import FeatureForm from "../../components/featureForm";
import { Link, BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import MenuItem from "antd/lib/menu/MenuItem";
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
    setFeatureKeys([e]);
    setMenu("featureType");
  };

  const onFeatureTypeChange = (e: any) => {
    setFeatureType(e);
    setMenu("transformation");
  };

  const onTransformationChange = async (e: any) => {
    setTransformation(e);
    //TODO: create a summary page
  };

  const onCreate = async () => {
    //TODO: create a page/message box to show success or failure
    if (!featureType || !featureKeys || !transformation) {
      console.error("invalid feature definition");
      return;
    }

    const featureTypeTemp = featureType;
    if (featureType.dimensionType)
      featureTypeTemp.dimensionType = featureType.dimensionType
        .toString()
        .split(",");

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
      const result = await createDerivedFeature(projectId, newFeature);
    } else if (type === "anchor") {
      const newFeature = {
        name: basic.name,
        qualifiedName: basic.qualifiedName,
        featureType: featureType,
        transformation: transformation,
        key: featureKeys,
        tags: basic.tags ?? {},
      };
      const anchorId = dependencies.anchor;
      const result = await createAnchorFeature(projectId, anchorId, newFeature);
    }
    //TODO: some validation
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
          onClick={onCreate}
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
          featureKeyProp={featureKeys[0] ?? {}}
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
    </div>
  );
};

export default NewFeature;

import React from "react";
import { Space } from "antd";
import { Feature } from "@/models/model";
import CardDescriptions from "@/components/CardDescriptions";
import {
  TransformationMap,
  FeatureKeyMap,
  TypeMap,
} from "@/utils/attributesMapping";

export interface FeatureNodeDetialProps {
  feature: Feature;
}

const FeatureNodeDetial = (props: FeatureNodeDetialProps) => {
  const { feature } = props;

  const { attributes } = feature;
  const { transformation, key, type } = attributes;
  const FeatureKey = key?.[0];

  return (
    <Space
      className="display-flex"
      direction="vertical"
      size="middle"
      align="start"
    >
      <CardDescriptions
        title="Transformation"
        mapping={TransformationMap}
        descriptions={transformation}
      />
      <CardDescriptions
        title="Entity Key"
        mapping={FeatureKeyMap}
        descriptions={FeatureKey}
      />
      <CardDescriptions title="Type" mapping={TypeMap} descriptions={type} />
    </Space>
  );
};

export default FeatureNodeDetial;

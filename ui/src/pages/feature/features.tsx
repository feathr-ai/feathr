import React from "react";
import { Card, Typography } from "antd";
import FeatureList from "../../components/featureList";

const { Title } = Typography;

const Features = () => {
  return (
    <div className="page">
      <Card>
        <Title level={3}>Features</Title>
        <FeatureList />
      </Card>
    </div>
  );
};

export default Features;

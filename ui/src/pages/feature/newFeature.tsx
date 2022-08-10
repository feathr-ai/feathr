import React from "react";
import { Card, Typography } from "antd";
import FeatureForm from "../../components/featureForm";

const { Title } = Typography;

const NewFeature: React.FC = () => {
  return (
    <div className="page">
      <Card>
        <Title level={3}>Create Feature</Title>
        <FeatureForm isNew={true} editMode={true} />
      </Card>
    </div>
  );
};

export default NewFeature;

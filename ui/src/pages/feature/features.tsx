import React, { useState } from "react";
import { Button, Card, Space, Typography } from "antd";
import { useNavigate, useNavigationType } from "react-router-dom";
import FeatureList from "../../components/featureList";
import { LocalVariables, getLocalVariable } from "../../utils/utils";

const { Title } = Typography;

const Features: React.FC = () => {
  const navigationType = useNavigationType();
  const navigate = useNavigate();
  const onCreateFeatureClick = () => {
    navigate("/new-feature");
  };
  const [preProject] = useState(
    navigationType === "POP" ? getLocalVariable(LocalVariables.PreProject) : ""
  );
  const [preKeyword] = useState(
    navigationType === "POP" ? getLocalVariable(LocalVariables.PreKeyword) : ""
  );

  return (
    <div className="page">
      <Card>
        <Title level={3}>Features</Title>
        <Space style={{ marginBottom: 16 }}>
          <Button
            type="primary"
            onClick={onCreateFeatureClick}
            style={{
              position: "absolute",
              right: "12px",
              top: "56px",
            }}
          >
            + Create Feature
          </Button>
        </Space>
        <FeatureList preProject={preProject} preKeyword={preKeyword} />
      </Card>
    </div>
  );
};

export default Features;

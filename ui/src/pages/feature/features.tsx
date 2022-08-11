import React from "react";
import { Button, Card, Space, Typography } from "antd";
import { useNavigate, useParams } from "react-router-dom";
import FeatureList from "../../components/featureList";

const { Title } = Typography;

const Features: React.FC = () => {
  const { project, keyword } = useParams();
  const navigate = useNavigate();
  const onCreateFeatureClick = () => {
    navigate("/new-feature");
  };

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
        <FeatureList preProject={project ?? ""} preKeyword={keyword ?? ""} />
      </Card>
    </div>
  );
};

export default Features;

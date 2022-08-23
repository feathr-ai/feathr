import { Button, Card, Space, Typography } from "antd";
import { useNavigate, useSearchParams } from "react-router-dom";
import FeatureList from "../../components/featureList";

const { Title } = Typography;

const Features = () => {
  const navigate = useNavigate();
  const onCreateFeatureClick = () => {
    navigate("/new-feature");
  };
  const [searchParams] = useSearchParams();
  const project = (searchParams.get("project") as string) ?? "";
  const keyword = (searchParams.get("keyword") as string) ?? "";

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
        <FeatureList projectProp={project} keywordProp={keyword} />
      </Card>
    </div>
  );
};

export default Features;

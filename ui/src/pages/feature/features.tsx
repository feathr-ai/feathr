import { Button, Card, Space, Typography } from "antd";
import { useNavigate, useSearchParams } from "react-router-dom";
import FeatureList from "../../components/featureList";

const { Title } = Typography;

const Features = () => {
  const [searchParams] = useSearchParams();
  const project = (searchParams.get("project") as string) ?? "";
  const keyword = (searchParams.get("keyword") as string) ?? "";

  return (
    <div className="page">
      <Card>
        <Title level={3}>Features</Title>
        <FeatureList projectProp={project} keywordProp={keyword} />
      </Card>
    </div>
  );
};

export default Features;

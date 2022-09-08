import { Card, Typography } from "antd";
import { useSearchParams } from "react-router-dom";
import DataSourceList from "../../components/dataSourceList";

const { Title } = Typography;

const DataSources = () => {
  const [searchParams] = useSearchParams();
  const project = (searchParams.get("project") as string) ?? "";
  const keyword = (searchParams.get("keyword") as string) ?? "";

  return (
    <div className="page">
      <Card>
        <Title level={3}>Data Sources</Title>
        <DataSourceList projectProp={project} keywordProp={keyword} />
      </Card>
    </div>
  );
};

export default DataSources;

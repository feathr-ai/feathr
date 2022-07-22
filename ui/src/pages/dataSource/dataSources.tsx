import React from "react";
import { Card, Typography } from "antd";
import DataSourceList from "../../components/dataSourceList";

const { Title } = Typography;

const DataSources: React.FC = () => {
  return (
    <div className="page">
      <Card>
        <Title level={3}>Data Sources</Title>
        <DataSourceList />
      </Card>
    </div>
  );
};

export default DataSources;

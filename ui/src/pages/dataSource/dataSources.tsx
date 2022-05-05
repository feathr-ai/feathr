import React from 'react';
import { Card } from 'antd';
import DataSourceList from "../../components/dataSourceList";

type Props = {};

const DataSources: React.FC<Props> = () => {
  return (
    <>
      <div className="home" style={ { margin: "2%" } }>
        <Card style={ { minWidth: '1000px' } }>
          <DataSourceList />
        </Card>
      </div>
    </>
  );
};

export default DataSources;

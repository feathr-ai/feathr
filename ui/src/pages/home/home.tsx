import React from 'react';
import { Button, Card, Space } from 'antd';
import './home.less';
import { useHistory } from 'react-router';
import FeatureTable from "../../components/featureTable";

type Props = {};

const Home: React.FC<Props> = () => {
  const history = useHistory();
  const onCreateFeatureClick = () => {
    history.push('/new-feature');
  };

  return (
    <>
      <div className="home" style={ { margin: "2%" } }>
        <Card style={ { minWidth: '1000px' } }>
          <Space style={ { marginBottom: 16 } }>
            <Button type="primary" onClick={ onCreateFeatureClick }
                    style={ { position: "absolute", right: "12px", top: "56px" } }>
              + Create Feature
            </Button>
          </Space>
          <FeatureTable />
        </Card>
      </div>
    </>
  );
};

export default Home;

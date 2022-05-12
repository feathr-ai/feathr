import React from 'react';
import { Button, Card, Space } from 'antd';
import { useHistory } from 'react-router';
import FeatureList from "../../components/featureList";

type Props = {};

const Features: React.FC<Props> = () => {
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
          <FeatureList />
        </Card>
      </div>
    </>
  );
};

export default Features;

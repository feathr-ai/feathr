import React from 'react';
import { Card } from 'antd';
import FeatureForm from '../../components/featureForm';

type Props = {};

const NewFeature: React.FC<Props> = () => {
  return (
    <div style={ { "margin": "2%" } }>
      <Card title="Create Feature">
        <FeatureForm isDisabled={ false } />
      </Card>
    </div>
  );
};

export default NewFeature;

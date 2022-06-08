import React from 'react';
import { Card } from 'antd';
import RoleManagementForm from '../../components/roleManagementForm';

type Props = {};

const RoleManagement: React.FC<Props> = () => {
  return (
    <div style={ { "margin": "2%" } }>
      <Card title="Role Management">
        <RoleManagementForm isNew={ true } editMode={ true } />
      </Card>
    </div>
  );
};

export default RoleManagement;
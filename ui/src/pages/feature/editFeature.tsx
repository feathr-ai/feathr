import React, { useState } from 'react';
import { Alert, Button, Card, Modal, Space, Spin } from 'antd';
import { useHistory, useParams } from 'react-router';
import { QueryStatus, useQuery } from "react-query";
import { deleteFeature, fetchFeature } from '../../api';
import { ExclamationCircleOutlined, LoadingOutlined } from '@ant-design/icons';
import { AxiosError } from 'axios';
import FeatureForm from '../../components/featureForm';
import { IFeature } from "../../models/feature";

const { confirm } = Modal;

type Props = {};

type IdParams = {
  id: string;
}

const EditFeature: React.FC<Props> = () => {
  const [editMode, setEditMode] = useState<boolean>(false);

  const { id } = useParams<IdParams>();
  const history = useHistory();

  const { status, error, data } = useQuery<IFeature, AxiosError>(['feature', id], () => fetchFeature(id));
  const antIcon = <LoadingOutlined style={ { fontSize: 24 } } spin />;

  const onClickDeleteFeature = () => {
    showConfirm();
  };

  const onClickEditFeature = () => {
    setEditMode(true);
  };

  const onClickCancelEdit = () => {
    setEditMode(false);
  }

  const showConfirm = () => {
    confirm({
      title: 'Are you sure you want to delete this feature?',
      icon: <ExclamationCircleOutlined />,
      async onOk() {
        await deleteFeature(id);
        history.push('/features');
      },
      onCancel() {
        console.log('Cancel clicked');
      },
    });
  }

  const renderCommandButtons = () => {
    return (
      <div>
        <Space>
          <Button type="primary" onClick={ onClickEditFeature }>
            Edit Feature
          </Button>
          <Button danger onClick={ onClickDeleteFeature }>
            Delete Feature
          </Button>
        </Space>
      </div>
    )
  }

  const renderCancelEdit = () => {
    return (
      <Space>
        <Button type="primary" onClick={ onClickCancelEdit }>
          Cancel Editing
        </Button>
      </Space>
    )
  }

  const renderFeature = (feature: IFeature): JSX.Element => {
    return (
      <div>
        { editMode && renderCancelEdit() }
        { !editMode && renderCommandButtons() }
        <FeatureForm isNew={ false } editMode={ editMode } feature={ feature } />
      </div>
    )
  }

  const render = (status: QueryStatus): JSX.Element => {
    switch (status) {
      case "error":
        return (
          <Card>
            <Alert
              message="Error"
              description={ error?.message }
              type="error"
              showIcon
            />
          </Card>
        );
      case "idle":
        return (
          <Card>
            <Spin indicator={ antIcon } />
          </Card>
        );
      case "loading":
        return (
          <Card>
            <Spin indicator={ antIcon } />
          </Card>
        );
      case "success":
        if (data === undefined) {
          return (
            <Card>
              <Alert
                message="Error"
                description="Data does not exist for this alert..."
                type="error"
                showIcon
              />
            </Card>
          );
        } else {
          return (
            <Card title={ data.name }>
              { renderFeature(data) }
            </Card>
          );
        }
    }
  }

  return (
    <>
      <div style={ { margin: "2%" } }>
        { render(status) }
      </div>
    </>
  );
};

export default EditFeature;

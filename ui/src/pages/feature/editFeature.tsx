import React, { useState } from 'react';
import { Alert, Button, Card, Modal, Space, Spin } from 'antd';
import { useHistory, useParams } from 'react-router';
import { QueryStatus, useQuery } from "react-query";
import { deleteFeature, fetchFeature } from '../../api';
import { ExclamationCircleOutlined, LoadingOutlined } from '@ant-design/icons';
import { AxiosError } from 'axios';
import FeatureForm from '../../components/featureForm';
import { FeatureAttributes } from "../../models/model";

const { confirm } = Modal;

type Props = {};

type QualifiedNameParams = {
  qualifiedName: string;
}

const EditFeature: React.FC<Props> = () => {
  const [editMode, setEditMode] = useState<boolean>(false);

  const { qualifiedName } = useParams<QualifiedNameParams>();
  const history = useHistory();

  const {
    status,
    error,
    data
  } = useQuery<FeatureAttributes, AxiosError>(['feature', qualifiedName], () => fetchFeature(qualifiedName));
  const antIcon = <LoadingOutlined style={ { fontSize: 24 } } spin />;

  const openNewWin = (url: string) => {
    window.open(url);
  }

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
        await deleteFeature(qualifiedName);
        history.push('/features');
      },
      onCancel() {
        console.log('Cancel clicked');
      },
    });
  }

  const renderCommandButtons = () => {
    const lineageUrl = "https://ms.web.purview.azure.com/resource/feathrazuretest3-purview1/main/catalog/entity?guid=06fec203-3096-4c25-aa20-65a20c8fac98&section=lineage&feature.tenant=72f988bf-86f1-41af-91ab-2d7cd011db47";
    return (
      <div>
        <Space>
          <Button type="primary" onClick={ () => openNewWin(lineageUrl) }>
            View Lineage
          </Button>
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

  const renderFeature = (feature: FeatureAttributes): JSX.Element => {
    return (
      <div>
        { editMode && renderCancelEdit() }
        { !editMode && renderCommandButtons() }
        <FeatureForm isNew={ false } editMode={ editMode } feature={ feature } />
      </div>
    )
  }

  const render = (status: QueryStatus): JSX.Element => {
    console.log(data);
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
                description="Data does not exist..."
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

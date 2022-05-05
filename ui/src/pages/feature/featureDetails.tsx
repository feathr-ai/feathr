import React, { useCallback } from 'react';
import { Alert, Button, Card, Col, Modal, Row, Space, Spin } from 'antd';
import { ExclamationCircleOutlined, LoadingOutlined } from '@ant-design/icons';
import { useHistory, useParams } from 'react-router';
import { QueryStatus, useQuery } from "react-query";
import { deleteFeature, fetchFeature } from '../../api';
import { AxiosError } from 'axios';
import { FeatureAttributes, InputAnchorFeatures } from "../../models/model";

const { confirm } = Modal;

type Props = {};

type QualifiedNameParams = {
  qualifiedName: string;
}

const FeatureDetails: React.FC<Props> = () => {
  const { qualifiedName } = useParams<QualifiedNameParams>();
  const loadingIcon = <LoadingOutlined style={ { fontSize: 24 } } spin />;
  const history = useHistory();
  const navigateTo = useCallback((location) => history.push(location), [history]);
  const {
    status,
    error,
    data
  } = useQuery<FeatureAttributes, AxiosError>(['feature', qualifiedName], () => fetchFeature(qualifiedName));

  const openLineageWindow = () => {
    navigateTo(`/features/${ qualifiedName }/lineage`);
    const lineageUrl = `/features/${ qualifiedName }/lineage`;
    window.open(lineageUrl);
  }

  const onClickDeleteFeature = () => {
    showConfirm();
  };

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
    return (
      <div>
        <Space>
          <Button type="primary" onClick={ () => openLineageWindow() }>
            View Lineage
          </Button>
          <Button danger onClick={ onClickDeleteFeature }>
            Delete Feature
          </Button>
        </Space>
      </div>
    )
  }
  const renderInputFeatureList = (features: InputAnchorFeatures[]) => {
    return (
      <ul>
        { features.map((_) => (
          <Button type="link" onClick={ () => {
            navigateTo(`/features/${ _.uniqueAttributes.qualifiedName }`)
          } }>{ _.uniqueAttributes.qualifiedName }</Button>
        )) }
      </ul>
    );
  }

  const renderFeature = (feature: FeatureAttributes): JSX.Element => {
    return (
      <div className="site-card-wrapper">
        <Row gutter={ 16 }>
          { feature?.type &&
              <Col span={ 8 }>
                  <Card title="Type" bordered={ false }>
                    { feature.type }
                  </Card>
              </Col>
          }
          { feature?.definition &&
              <Col span={ 8 }>
                  <Card title="Definition" bordered={ false }>
                    { feature.definition }
                  </Card>
              </Col>
          }
          { feature?.def &&
              <Col span={ 8 }>
                  <Card title="SQL Expression" bordered={ false }>
                    { feature["def.sqlExpr"] }
                  </Card>
              </Col>
          }
          { feature?.input_anchor_features && feature?.input_anchor_features.length > 0 &&
              <Col span={ 16 }>
                  <Card title="Input Anchor Features" bordered={ false }>
                    { renderInputFeatureList(feature.input_anchor_features) }
                  </Card>
              </Col>
          }
          { feature?.input_derived_features && feature?.input_derived_features.length > 0 &&
              <Col span={ 16 }>
                  <Card title="Input Derived Features" bordered={ false }>
                    { renderInputFeatureList(feature.input_derived_features) }
                  </Card>
              </Col>
          }
          { feature?.transformation &&
              <Col span={ 8 }>
                  <Card title="Transformation" bordered={ false }>
                    { feature.transformation.transform_expr }
                  </Card>
              </Col>
          }
          { feature?.window &&
              <Col span={ 8 }>
                  <Card title="Window" bordered={ false }>
                    { feature.window }
                  </Card>
              </Col>
          }
        </Row>
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
            <Spin indicator={ loadingIcon } />
          </Card>
        );
      case "loading":
        return (
          <Card>
            <Spin indicator={ loadingIcon } />
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
              { renderCommandButtons() }
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

export default FeatureDetails;

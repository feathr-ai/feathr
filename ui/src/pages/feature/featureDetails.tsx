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
type Params = {
  project: string;
  qualifiedName: string;
}

const FeatureDetails: React.FC<Props> = () => {
  const { project, qualifiedName } = useParams<Params>();
  const loadingIcon = <LoadingOutlined style={ { fontSize: 24 } } spin />;
  const history = useHistory();
  const navigateTo = useCallback((location) => history.push(location), [history]);
  const {
    status,
    error,
    data
  } = useQuery<FeatureAttributes, AxiosError>(['feature', qualifiedName], () => fetchFeature(project, qualifiedName));

  const openLineageWindow = () => {
    const lineageUrl = `/projects/${ project }/features/${ qualifiedName }/lineage`;
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
            navigateTo(`/projects/${ project }/features/${ _.uniqueAttributes.qualifiedName }`)
          } }>{ _.uniqueAttributes.qualifiedName }</Button>
        )) }
      </ul>
    );
  }

  const renderFeature = (feature: FeatureAttributes): JSX.Element => {
    return (
      <div className="site-card-wrapper">
        <Row>
          { feature?.key && feature.key.length > 0 &&
              <Col span={ 8 }>
                  <Card title="Key" bordered={ false }>
                      <p>full_name: { feature.key[0].full_name }</p>
                      <p>key_column: { feature.key[0].key_column }</p>
                      <p>description: { feature.key[0].description }</p>
                      <p>key_column_alias: { feature.key[0].key_column_alias }</p>
                      <p>key_column_type: { feature.key[0].key_column_type }</p>
                  </Card>
              </Col>
          }
          { feature?.type &&
              <Col span={ 8 }>
                  <Card title="Type" bordered={ false }>
                    { feature.type }
                  </Card>
              </Col>
          }
          { feature?.transformation &&
              <Col span={ 8 }>
                  <Card title="Transformation" bordered={ false }>
                    <p>transform_expr: { feature.transformation.transform_expr }</p>
                    <p>filter: { feature.transformation.filter }</p>
                    <p>agg_func: { feature.transformation.agg_func }</p>
                    <p>limit: { feature.transformation.limit }</p>
                    <p>group_by: { feature.transformation.group_by }</p>
                    <p>window: { feature.transformation.window }</p>
                    <p>def_expr: { feature.transformation.def_expr }</p>
                  </Card>
              </Col>
          }
        </Row>
        <Row>
          { feature?.input_anchor_features && feature?.input_anchor_features.length > 0 &&
              <Col span={ 24 }>
                  <Card title="Input Anchor Features" bordered={ false }>
                    { renderInputFeatureList(feature.input_anchor_features) }
                  </Card>
              </Col>
          }
        </Row>
        <Row>
          { feature?.input_derived_features && feature?.input_derived_features.length > 0 &&
              <Col span={ 24 }>
                  <Card title="Input Derived Features" bordered={ false }>
                    { renderInputFeatureList(feature.input_derived_features) }
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

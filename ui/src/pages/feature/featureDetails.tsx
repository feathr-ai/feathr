import React from 'react';
import { Alert, Button, Card, Col, Modal, Row, Space, Spin } from 'antd';
import { ExclamationCircleOutlined, LoadingOutlined } from '@ant-design/icons';
import { useNavigate, useParams } from "react-router-dom";
import { QueryStatus, useQuery } from "react-query";
import { AxiosError } from 'axios';
import { deleteFeature, fetchFeature } from '../../api';
import { IFeature } from "../../models/model";

const { confirm } = Modal;

type Props = {};
type Params = {
  project: string;
  featureId: string;
}

const FeatureDetails: React.FC<Props> = () => {
  const { project, featureId } = useParams() as Params;
  const navigate = useNavigate();
  const loadingIcon = <LoadingOutlined style={ { fontSize: 24 } } spin />;
  const {
    status,
    error,
    data
  } = useQuery<IFeature, AxiosError>(['featureId', featureId], () => fetchFeature(project, featureId));

  const openLineageWindow = () => {
    const lineageUrl = `/projects/${ project }/lineage`;
    navigate(lineageUrl);
  }

  const onClickDeleteFeature = () => {
    showConfirm();
  };

  const showConfirm = () => {
    confirm({
      title: 'Are you sure you want to delete this feature?',
      icon: <ExclamationCircleOutlined />,
      async onOk() {
        await deleteFeature(featureId);
        navigate('/features');
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

  const renderFeature = (feature: IFeature): JSX.Element => {
    return (
      <div className="site-card-wrapper">
        <Row>
          { feature.attributes.key && feature.attributes.key.length > 0 &&
              <Col span={ 8 }>
                  <Card title="Key" bordered={ false }>
                      <p>full_name: { feature.attributes.key[0].full_name }</p>
                      <p>key_column: { feature.attributes.key[0].key_column }</p>
                      <p>description: { feature.attributes.key[0].description }</p>
                      <p>key_column_alias: { feature.attributes.key[0].key_column_alias }</p>
                      <p>key_column_type: { feature.attributes.key[0].key_column_type }</p>
                  </Card>
              </Col>
          }
          { feature.attributes.type &&
              <Col span={ 8 }>
                  <Card title="Type" bordered={ false }>
                      <p>dimension_type: { feature.attributes.type.dimension_type }</p>
                      <p>tensor_category: { feature.attributes.type.tensor_category }</p>
                      <p>type: { feature.attributes.type.type }</p>
                      <p>val_type: { feature.attributes.type.val_type }</p>
                  </Card>
              </Col>
          }
          { feature.attributes.transformation &&
              <Col span={ 8 }>
                  <Card title="Transformation" bordered={ false }>
                      <p>transform_expr: { feature.attributes.transformation.transform_expr ?? "N/A" }</p>
                      <p>filter: { feature.attributes.transformation.filter ?? "N/A" }</p>
                      <p>agg_func: { feature.attributes.transformation.agg_func ?? "N/A" }</p>
                      <p>limit: { feature.attributes.transformation.limit ?? "N/A" }</p>
                      <p>group_by: { feature.attributes.transformation.group_by ?? "N/A" }</p>
                      <p>window: { feature.attributes.transformation.window ?? "N/A" }</p>
                      <p>def_expr: { feature.attributes.transformation.def_expr ?? "N/A" }</p>
                  </Card>
              </Col>
          }
        </Row>
        <Row>
          { feature.attributes._input_anchor_features && feature.attributes._input_anchor_features.length > 0 &&
              <Col span={ 24 }>
                  <Card title="Input Anchor Features" bordered={ false }>
                    {
                      feature.attributes._input_anchor_features.map((feature) =>
                        <Button type="link" onClick={ () => {
                          navigate(`/projects/${ project }/features/${ feature.id }`)
                        } }>{ feature.attributes.name }</Button>)
                    }
                  </Card>
              </Col>
          }
        </Row>
        <Row>
          { feature.attributes._input_derived_features && feature.attributes._input_derived_features.length > 0 &&
              <Col span={ 24 }>
                  <Card title="Input Derived Features" bordered={ false }>
                    {
                      feature.attributes._input_derived_features.map((feature) =>
                        <Button type="link" onClick={ () => {
                          navigate(`/projects/${ project }/features/${ feature.id }`)
                        } }>{ feature.attributes.name }</Button>)
                    }
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
            <Card title={ data.displayText }>
              { renderCommandButtons() }
              { renderFeature(data) }
            </Card>
          );
        }
    }
  }

  return (
    <div style={ { margin: "2%" } }>
      { render(status) }
    </div>
  );
};

export default FeatureDetails;

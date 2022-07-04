import React from 'react';
import { Alert, Button, Card, Col, Row, Space, Spin, Typography } from 'antd';
import { LoadingOutlined } from '@ant-design/icons';
import { useNavigate, useParams } from "react-router-dom";
import { QueryStatus, useQuery } from "react-query";
import { AxiosError } from 'axios';
import { fetchFeature } from '../../api';
import { Feature } from "../../models/model";

const { Title } = Typography;

function FeatureKey(props: { feature: Feature }) {
  return <>
    { props.feature.attributes.key && props.feature.attributes.key.length > 0 &&
        <Col span={ 24 }>
            <Card className="card">
                <Title level={ 4 }>Key</Title>
                <p>Full name: { props.feature.attributes.key[0].full_name }</p>
                <p>Key column: { props.feature.attributes.key[0].key_column }</p>
                <p>Description: { props.feature.attributes.key[0].description }</p>
                <p>Key column alias: { props.feature.attributes.key[0].key_column_alias }</p>
                <p>key column type: { props.feature.attributes.key[0].key_column_type }</p>
            </Card>
        </Col>
    }
  </>;
}

function FeatureType(props: { feature: Feature }) {
  return <>
    { props.feature.attributes.type &&
        <Col span={ 24 }>
            <Card className="card">
                <Title level={ 4 }>Type</Title>
                <p>Dimension Type: { props.feature.attributes.type.dimensionType }</p>
                <p>Tensor Category: { props.feature.attributes.type.tensorCategory }</p>
                <p>Type: { props.feature.attributes.type.type }</p>
                <p>Value Type: { props.feature.attributes.type.valType }</p>
            </Card>
        </Col>
    }
  </>;
}

function FeatureTransformation(props: { feature: Feature }) {
  return <>
    { props.feature.attributes.transformation &&
        <Col span={ 24 }>
            <Card className="card">
                <Title level={ 4 }>Transformation</Title>
              { props.feature.attributes.transformation.transform_expr &&
                  <p>Expression: { props.feature.attributes.transformation.transform_expr }</p> }
              { props.feature.attributes.transformation.filter &&
                  <p>Filter: { props.feature.attributes.transformation.filter }</p> }
              { props.feature.attributes.transformation.agg_func &&
                  <p>Aggregation: { props.feature.attributes.transformation.agg_func }</p> }
              { props.feature.attributes.transformation.limit &&
                  <p>Limit: { props.feature.attributes.transformation.limit }</p> }
              { props.feature.attributes.transformation.group_by &&
                  <p>Group By: { props.feature.attributes.transformation.group_by }</p> }
              { props.feature.attributes.transformation.window &&
                  <p>Window: { props.feature.attributes.transformation.window }</p> }
              { props.feature.attributes.transformation.def_expr &&
                  <p>Expression: { props.feature.attributes.transformation.def_expr }</p> }
            </Card>
        </Col>
    }
  </>;
}

function InputAnchorFeatures(props: { project: string, feature: Feature }) {
  const navigate = useNavigate();
  return <>
    { props.feature.attributes._input_anchor_features && props.feature.attributes._input_anchor_features.length > 0 &&
        <Col span={ 24 }>
            <Card style={ {
              marginTop: "15px",
              marginRight: "15px",
              minWidth: "1000px",
              boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
              borderRadius: "8px"
            } }>
                <Title level={ 4 }>Input Anchor Features</Title>
              {
                props.feature.attributes._input_anchor_features.map((input_feature) =>
                  <Button type="link" onClick={ () => {
                    navigate(`/projects/${ props.project }/features/${ input_feature.id }`)
                  } }>{ input_feature.attributes.name }</Button>)
              }
            </Card>
        </Col>
    }
  </>;
}

function InputDerivedFeatures(props: { project: string, feature: Feature }) {
  const navigate = useNavigate();
  return <>
    { props.feature.attributes._input_derived_features && props.feature.attributes._input_derived_features.length > 0 &&
        <Col span={ 24 }>
            <Card style={ {
              marginTop: "15px",
              marginRight: "15px",
              minWidth: "1000px",
              boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
              borderRadius: "8px"
            } }>
                <Title level={ 4 }>Input Derived Features</Title>
              {
                props.feature.attributes._input_derived_features.map((input_feature) =>
                  <Button type="link" onClick={ () => {
                    navigate(`/projects/${ props.project }/features/${ input_feature.id }`)
                  } }>{ input_feature.attributes.name }</Button>)
              }
            </Card>
        </Col>
    }
  </>;
}

type Params = {
  project: string;
  featureId: string;
}
const FeatureDetails: React.FC = () => {
  const { project, featureId } = useParams() as Params;
  const navigate = useNavigate();
  const loadingIcon = <LoadingOutlined style={ { fontSize: 24 } } spin />;
  const {
    status,
    error,
    data
  } = useQuery<Feature, AxiosError>(['featureId', featureId], () => fetchFeature(project, featureId));

  const openLineageWindow = () => {
    const lineageUrl = `/projects/${ project }/lineage`;
    navigate(lineageUrl);
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
            <>
              <Card>
                <Title level={ 3 }>{ data.attributes.name }</Title>
                <div>
                  <Space>
                    <Button type="primary" onClick={ () => openLineageWindow() }>
                      View Lineage
                    </Button>
                  </Space>
                </div>
                <div>
                  <Row>
                    <InputAnchorFeatures project={ project } feature={ data } />
                    <InputDerivedFeatures project={ project } feature={ data } />
                    <FeatureTransformation feature={ data } />
                    <FeatureKey feature={ data } />
                    <FeatureType feature={ data } />
                  </Row>
                </div>
              </Card>
            </>
          );
        }
    }
  }

  return (
    <div className="page">
      { render(status) }
    </div>
  );
};

export default FeatureDetails;

import React, { useEffect, useState } from "react";
import { Alert, Button, Card, Col, Row, Space, Spin, Typography } from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "react-router-dom";
import { QueryStatus, useQuery } from "react-query";
import { AxiosError } from "axios";
import { fetchFeature } from "../../api";
import { Feature } from "../../models/model";
import { FeatureLineage } from "../../models/model";
import { fetchFeatureLineages } from "../../api";
import { Elements } from "react-flow-renderer";
import Graph from "../../components/graph/graph";
import { getElements } from "../../components/graph/utils";

const { Title } = Typography;

function FeatureKey(props: { feature: Feature }) {
  const keys = props.feature.attributes.key;
  console.log(props.feature.attributes);
  return (
    <>
      {keys && keys.length > 0 && (
        <Col span={24}>
          <Card className="card">
            <Title level={4}>Key</Title>
            <div className="feature-container">
              <p>Full Name: {keys[0].fullName}</p>
              <p>Key Column: {keys[0].keyColumn}</p>
              <p>Description: {keys[0].description}</p>
              <p>Key Column Alias: {keys[0].keyColumnAlias}</p>
              <p>Key Column Type: {keys[0].keyColumnType}</p>
            </div>
          </Card>
        </Col>
      )}
    </>
  );
}

function FeatureType(props: { feature: Feature }) {
  const type = props.feature.attributes.type;
  return (
    <>
      {type && (
        <Col span={24}>
          <Card className="card">
            <Title level={4}>Type</Title>
            <div className="feature-container">
              <p>Dimension Type: {type.dimensionType}</p>
              <p>Tensor Category: {type.tensorCategory}</p>
              <p>Type: {type.type}</p>
              <p>Value Type: {type.valType}</p>
            </div>
          </Card>
        </Col>
      )}
    </>
  );
}

function FeatureTransformation(props: { feature: Feature }) {
  const transformation = props.feature.attributes.transformation;
  return (
    <>
      {transformation && (
        <Col span={24}>
          <Card className="card">
            <Title level={4}>Transformation</Title>
            <div className="feature-container">
              {transformation.transformExpr && (
                <p>Expression: {transformation.transformExpr}</p>
              )}
              {transformation.filter && <p>Filter: {transformation.filter}</p>}
              {transformation.aggFunc && (
                <p>Aggregation: {transformation.aggFunc}</p>
              )}
              {transformation.limit && <p>Limit: {transformation.limit}</p>}
              {transformation.groupBy && (
                <p>Group By: {transformation.groupBy}</p>
              )}
              {transformation.window && <p>Window: {transformation.window}</p>}
              {transformation.defExpr && (
                <p>Expression: {transformation.defExpr}</p>
              )}
            </div>
          </Card>
        </Col>
      )}
    </>
  );
}

function InputAnchorFeatures(props: { project: string; feature: Feature }) {
  const navigate = useNavigate();
  const inputAnchorFeatures = props.feature.attributes.inputAnchorFeatures;
  return (
    <>
      {inputAnchorFeatures && inputAnchorFeatures.length > 0 && (
        <Col span={24}>
          <Card
            style={{
              marginTop: "15px",
              marginRight: "15px",
              minWidth: "1000px",
              boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
              borderRadius: "8px",
            }}
          >
            <Title level={4}>Input Anchor Features</Title>
            {inputAnchorFeatures.map((input_feature) => (
              <Button
                type="link"
                onClick={() => {
                  navigate(
                    `/projects/${props.project}/features/${input_feature.guid}`
                  );
                }}
              >
                {input_feature.uniqueAttributes.qualifiedName}
              </Button>
            ))}
          </Card>
        </Col>
      )}
    </>
  );
}

function InputDerivedFeatures(props: { project: string; feature: Feature }) {
  const navigate = useNavigate();
  const inputDerivedFeatures = props.feature.attributes.inputDerivedFeatures;
  return (
    <>
      {inputDerivedFeatures && inputDerivedFeatures.length > 0 && (
        <Col span={24}>
          <Card
            style={{
              marginTop: "15px",
              marginRight: "15px",
              minWidth: "1000px",
              boxShadow: "5px 8px 15px 5px rgba(208, 216, 243, 0.6)",
              borderRadius: "8px",
            }}
          >
            <Title level={4}>Input Derived Features</Title>
            {inputDerivedFeatures.map((input_feature) => (
              <Button
                type="link"
                onClick={() => {
                  navigate(
                    `/projects/${props.project}/features/${input_feature.guid}`
                  );
                }}
              >
                {input_feature.uniqueAttributes.qualifiedName}
              </Button>
            ))}
          </Card>
        </Col>
      )}
    </>
  );
}

function FeatureLineageGraph() {
  const { featureId } = useParams() as Params;
  const [lineageData, setLineageData] = useState<FeatureLineage>({
    guidEntityMap: null,
    relations: null,
  });
  const [elements, SetElements] = useState<Elements>([]);
  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    const fetchLineageData = async () => {
      setLoading(true);
      const data = await fetchFeatureLineages(featureId);
      setLineageData(data);
      setLoading(false);
    };

    fetchLineageData();
  }, [featureId]);

  // Generate graph data on client side, invoked after graphData or featureType is changed
  useEffect(() => {
    const generateGraphData = async () => {
      SetElements(getElements(lineageData, "all_nodes")!);
    };

    generateGraphData();
  }, [lineageData]);

  return (
    <>
      {loading ? (
        <Spin indicator={<LoadingOutlined style={{ fontSize: 24 }} spin />} />
      ) : (
        <Col span={24}>
          <Card className="card">
            <Title level={4}>Lineage</Title>
            <Graph data={elements} nodeId={featureId} />
          </Card>
        </Col>
      )}
    </>
  );
}

type Params = {
  project: string;
  featureId: string;
};
const FeatureDetails: React.FC = () => {
  const { project, featureId } = useParams() as Params;
  const navigate = useNavigate();
  const loadingIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;
  const { status, error, data } = useQuery<Feature, AxiosError>(
    ["featureId", featureId],
    () => fetchFeature(project, featureId)
  );

  const openLineageWindow = () => {
    const lineageUrl = `/projects/${project}/lineage`;
    navigate(lineageUrl);
  };

  const render = (status: QueryStatus): JSX.Element => {
    switch (status) {
      case "error":
        return (
          <Card>
            <Alert
              message="Error"
              description={error?.message}
              type="error"
              showIcon
            />
          </Card>
        );
      case "idle":
        return (
          <Card>
            <Spin indicator={loadingIcon} />
          </Card>
        );
      case "loading":
        return (
          <Card>
            <Spin indicator={loadingIcon} />
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
                <Title level={3}>{data.attributes.name}</Title>
                <div>
                  <Space>
                    <Button type="primary" onClick={() => openLineageWindow()}>
                      View Lineage
                    </Button>
                  </Space>
                </div>
                <div>
                  <Row>
                    <InputAnchorFeatures project={project} feature={data} />
                    <InputDerivedFeatures project={project} feature={data} />
                    <FeatureTransformation feature={data} />
                    <FeatureKey feature={data} />
                    <FeatureType feature={data} />
                    <FeatureLineageGraph />
                  </Row>
                </div>
              </Card>
            </>
          );
        }
    }
  };

  return <div className="page">{render(status)}</div>;
};

export default FeatureDetails;

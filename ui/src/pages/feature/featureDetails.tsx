import React, { useEffect, useRef, useState } from "react";
import {
  Alert,
  Button,
  PageHeader,
  Breadcrumb,
  Space,
  Card,
  Spin,
  Descriptions,
} from "antd";
import { LoadingOutlined } from "@ant-design/icons";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useQuery } from "react-query";
import { AxiosError } from "axios";
import { fetchFeature, fetchFeatureLineages } from "@/api";
import { Feature, InputFeature, FeatureLineage } from "@/models/model";
import FlowGraph from "@/components/FlowGraph";

const contentStyle = { marginRight: 16 };

type FeatureKeyProps = { feature: Feature };
const FeatureKey = ({ feature }: FeatureKeyProps) => {
  const { key } = feature.attributes;
  const info = key?.[0];

  return info ? (
    <Card className="card" title="Entity Key">
      <Descriptions contentStyle={contentStyle}>
        <Descriptions.Item label="Full Name">{info.fullName}</Descriptions.Item>
        <Descriptions.Item label="Key Column">
          {info.keyColumn}
        </Descriptions.Item>
        <Descriptions.Item label="Description">
          {info.description}
        </Descriptions.Item>
        <Descriptions.Item label="Key Column Alias">
          {info.keyColumnAlias}
        </Descriptions.Item>
        <Descriptions.Item label="Key Column Type">
          {info.keyColumnType}
        </Descriptions.Item>
      </Descriptions>
    </Card>
  ) : null;
};

type FeatureTypeProps = { feature: Feature };
const FeatureType = ({ feature }: FeatureTypeProps) => {
  const type = feature.attributes.type;
  return type ? (
    <Card className="card" title="Type">
      <Descriptions contentStyle={contentStyle}>
        <Descriptions.Item label="Dimension Type">
          {type.dimensionType}
        </Descriptions.Item>
        <Descriptions.Item label="Tensor Category">
          {type.tensorCategory}
        </Descriptions.Item>
        <Descriptions.Item label="Type">{type.type}</Descriptions.Item>
        <Descriptions.Item label="Value Type">{type.valType}</Descriptions.Item>
      </Descriptions>
    </Card>
  ) : null;
};

type FeatureTransformationProps = { feature: Feature };
const FeatureTransformation = ({ feature }: FeatureTransformationProps) => {
  const { transformation } = feature.attributes;

  return transformation ? (
    <Card className="card" title="Transformation">
      <Descriptions contentStyle={contentStyle}>
        {transformation.transformExpr && (
          <Descriptions.Item label="Expression">
            {transformation.transformExpr}
          </Descriptions.Item>
        )}
        {transformation.filter && (
          <Descriptions.Item label="Filter">
            {transformation.filter}
          </Descriptions.Item>
        )}
        {transformation.aggFunc && (
          <Descriptions.Item label="Aggregation">
            {transformation.aggFunc}
          </Descriptions.Item>
        )}
        {transformation.limit && (
          <Descriptions.Item label="Limit">
            {transformation.limit}
          </Descriptions.Item>
        )}
        {transformation.groupBy && (
          <Descriptions.Item label="Group By">
            {transformation.groupBy}
          </Descriptions.Item>
        )}
        {transformation.window && (
          <Descriptions.Item label="Window">
            {transformation.window}
          </Descriptions.Item>
        )}
        {transformation.defExpr && (
          <Descriptions.Item label="defExpr">
            {transformation.defExpr}
          </Descriptions.Item>
        )}
      </Descriptions>
    </Card>
  ) : null;
};

type InputAnchorFeaturesProps = { project: string; feature: Feature };

const InputAnchorFeatures = (props: InputAnchorFeaturesProps) => {
  const { project, feature } = props;

  const { inputAnchorFeatures } = feature.attributes;

  return inputAnchorFeatures?.length > 0 ? (
    <Card className="card" title="Input Anchor Features">
      <Descriptions contentStyle={contentStyle}>
        {inputAnchorFeatures.map((input_feature) => (
          <Descriptions.Item>
            <Link to={`/projects/${project}/features/${input_feature.guid}`}>
              {input_feature.uniqueAttributes.qualifiedName}
            </Link>
          </Descriptions.Item>
        ))}
      </Descriptions>
    </Card>
  ) : null;
};

type InputDerivedFeaturesProps = { project: string; feature: Feature };

const InputDerivedFeatures = (props: InputDerivedFeaturesProps) => {
  const { project, feature } = props;

  const { inputDerivedFeatures } = feature.attributes;

  return inputDerivedFeatures?.length ? (
    <Card className="card" title="Input Derived Features">
      <Descriptions contentStyle={contentStyle}>
        {inputDerivedFeatures.map((input_feature: InputFeature) => (
          <Descriptions.Item>
            <Link to={`/projects/${project}/features/${input_feature.guid}`}>
              {input_feature.uniqueAttributes.qualifiedName}
            </Link>
          </Descriptions.Item>
        ))}
      </Descriptions>
    </Card>
  ) : null;
};

const FeatureLineageGraph = () => {
  const { project, featureId } = useParams() as Params;
  const [lineageData, setLineageData] = useState<FeatureLineage>({
    guidEntityMap: {},
    relations: [],
  });

  const [loading, setLoading] = useState<boolean>(false);

  const mountedRef = useRef<Boolean>(true);

  useEffect(() => {
    const fetchLineageData = async () => {
      setLoading(true);
      const data = await fetchFeatureLineages(featureId);
      if (mountedRef.current) {
        setLineageData(data);
        setLoading(false);
      }
    };

    fetchLineageData();
  }, [featureId]);

  useEffect(() => {
    mountedRef.current = true;
    return () => {
      mountedRef.current = false;
    };
  }, []);

  return (
    <Card className="card" title="Lineage">
      <div style={{ height: 500 }}>
        <FlowGraph
          loading={loading}
          data={lineageData}
          nodeId={featureId}
          project={project}
        />
      </div>
    </Card>
  );
};

type Params = {
  project: string;
  featureId: string;
};
const FeatureDetails = () => {
  const { project, featureId } = useParams() as Params;
  const navigate = useNavigate();

  const {
    isLoading,
    error,
    data = { attributes: {} } as Feature,
  } = useQuery<Feature, AxiosError>(
    ["featureId", featureId],
    () => fetchFeature(project, featureId),
    {
      retry: false,
      refetchOnWindowFocus: false,
    }
  );

  return (
    <div className="page">
      <PageHeader
        ghost={false}
        title="Data Source Attributes"
        breadcrumb={
          <Breadcrumb>
            <Breadcrumb.Item>
              <Link to={`/features?project=${project}`}>Features</Link>
            </Breadcrumb.Item>
            <Breadcrumb.Item>Feature Details</Breadcrumb.Item>
          </Breadcrumb>
        }
        extra={[
          <Button
            key="1"
            type="primary"
            onClick={() => {
              navigate(`/projects/${project}/lineage`);
            }}
          >
            View Lineage
          </Button>,
        ]}
      >
        <Spin
          spinning={isLoading}
          indicator={<LoadingOutlined style={{ fontSize: 24 }} spin />}
        >
          <Space direction="vertical" style={{ width: "100%" }} size="middle">
            {error && <Alert message={error} type="error" showIcon />}
            <InputAnchorFeatures project={project} feature={data} />
            <InputDerivedFeatures project={project} feature={data} />
            <FeatureTransformation feature={data} />
            <FeatureKey feature={data} />
            <FeatureType feature={data} />
            <FeatureLineageGraph />
          </Space>
        </Spin>
      </PageHeader>
    </div>
  );
};

export default FeatureDetails;

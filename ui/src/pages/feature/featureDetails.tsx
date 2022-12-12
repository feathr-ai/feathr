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
import CardDescriptions from "@/components/CardDescriptions";
import {
  FeatureKeyMap,
  TransformationMap,
  TypeMap,
} from "@/utils/attributesMapping";
import { getJSONMap } from "@/utils/utils";

const contentStyle = { marginRight: 16 };

type InputAnchorFeaturesProps = { project: string; feature: Feature };

const InputAnchorFeatures = (props: InputAnchorFeaturesProps) => {
  const { project, feature } = props;

  const { inputAnchorFeatures } = feature.attributes;

  return inputAnchorFeatures?.length > 0 ? (
    <Card className="card" title="Input Anchor Features">
      <Descriptions contentStyle={contentStyle}>
        {inputAnchorFeatures.map((input_feature) => (
          <Descriptions.Item key={input_feature.guid}>
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
          <Descriptions.Item key={input_feature.guid}>
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

  return !loading ? (
    <Card className="card" title="Lineage">
      <FlowGraph
        height={500}
        loading={loading}
        data={lineageData}
        nodeId={featureId}
        project={project}
      />
    </Card>
  ) : null;
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
  const { attributes } = data;
  const { transformation, key, type, name, tags } = attributes;

  const tagsMap = getJSONMap(tags);

  return (
    <div className="page">
      <PageHeader
        ghost={false}
        title={name}
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
          <Space className="display-flex" direction="vertical" size="middle">
            {error && <Alert message={error} type="error" showIcon />}
            <InputAnchorFeatures project={project} feature={data} />
            <InputDerivedFeatures project={project} feature={data} />
            <CardDescriptions
              title="Transformation"
              mapping={TransformationMap}
              descriptions={transformation}
            />
            {key?.map((item, index) => {
              return (
                <CardDescriptions
                  key={index}
                  title={`Entity Key ${index + 1}`}
                  mapping={FeatureKeyMap}
                  descriptions={item}
                />
              );
            })}

            <CardDescriptions
              title="Type"
              mapping={TypeMap}
              descriptions={type}
            />
            <CardDescriptions
              title="Tags"
              mapping={tagsMap}
              descriptions={tags}
            />
            <FeatureLineageGraph />
          </Space>
        </Spin>
      </PageHeader>
    </div>
  );
};

export default FeatureDetails;

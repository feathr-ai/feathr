import React, { useEffect, useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";
import { fetchFeature } from "../../api";
import { Feature } from "../../models/model";
import { LoadingOutlined } from "@ant-design/icons";
import { Card, Spin, Typography } from "antd";
import { isFeature } from "../../utils/utils";

const { Title } = Typography;

type Params = {
  project: string;
  featureId: string;
};

const GraphNodeDetails: React.FC = () => {
  const [searchParams] = useSearchParams();
  const { project } = useParams() as Params;
  const nodeId = searchParams.get("nodeId") as string;
  const featureType = searchParams.get("featureType") as string;
  const [feature, setFeature] = useState<Feature>();
  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    const fetchFeatureData = async () => {
      setFeature(undefined);
      if (nodeId && isFeature(featureType)) {
        setLoading(true);
        const data = await fetchFeature(project, nodeId);
        setFeature(data);
        setLoading(false);
      }
    };

    fetchFeatureData();
  }, [featureType, project, nodeId]);

  return (
    <>
      {loading ? (
        <Spin indicator={<LoadingOutlined style={{ fontSize: 24 }} spin />} />
      ) : (
        <div style={{ margin: "2%" }}>
          {!feature && (
            <p>Click on feature node to show metadata and metric details</p>
          )}
          {feature?.attributes.transformation && (
            <Card>
              <Title level={4}>Transformation</Title>
              {feature.attributes.transformation.transformExpr && (
                <p>
                  Expression: {feature.attributes.transformation.transformExpr}
                </p>
              )}
              {feature.attributes.transformation.filter && (
                <p>Filter {feature.attributes.transformation.filter}</p>
              )}
              {feature.attributes.transformation.aggFunc && (
                <p>Aggregation: {feature.attributes.transformation.aggFunc}</p>
              )}
              {feature.attributes.transformation.limit && (
                <p>Limit: {feature.attributes.transformation.limit}</p>
              )}
              {feature.attributes.transformation.groupBy && (
                <p>Group By: {feature.attributes.transformation.groupBy}</p>
              )}
              {feature.attributes.transformation.window && (
                <p>Window: {feature.attributes.transformation.window}</p>
              )}
              {feature.attributes.transformation.defExpr && (
                <p>Expression: {feature.attributes.transformation.defExpr}</p>
              )}
            </Card>
          )}
          {feature?.attributes.key && feature.attributes.key.length > 0 && (
            <Card>
              <Title level={4}>Key</Title>
              <p>Full name: {feature.attributes.key[0].fullName}</p>
              <p>Description: {feature.attributes.key[0].description}</p>
              <p>Key column: {feature.attributes.key[0].keyColumn}</p>
              <p>
                Key column alias: {feature.attributes.key[0].keyColumnAlias}
              </p>
              <p>Key column type: {feature.attributes.key[0].keyColumnType}</p>
            </Card>
          )}
          {feature?.attributes.type && (
            <Card>
              <Title level={4}>Type</Title>
              <p>Dimension Type: {feature.attributes.type.dimensionType}</p>
              <p>Tensor Category: {feature.attributes.type.tensorCategory}</p>
              <p>Type: {feature.attributes.type.type}</p>
              <p>Value Type: {feature.attributes.type.valType}</p>
            </Card>
          )}
        </div>
      )}
    </>
  );
};

export default GraphNodeDetails;

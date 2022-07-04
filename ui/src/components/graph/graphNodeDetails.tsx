import React, { useEffect, useState } from 'react';
import { useParams, useSearchParams } from "react-router-dom";
import { fetchFeature } from '../../api';
import { Feature } from "../../models/model";
import { LoadingOutlined } from "@ant-design/icons";
import { Card, Spin, Typography } from "antd";

const { Title } = Typography;

type Params = {
  project: string;
  featureId: string;
}

const GraphNodeDetails: React.FC = () => {
  const [searchParams] = useSearchParams();
  const { project } = useParams() as Params;
  const nodeId = searchParams.get('nodeId') as string;
  const featureType = searchParams.get('featureType') as string;
  const [feature, setFeature] = useState<Feature>();
  const [loading, setLoading] = useState<boolean>(false);

  const isFeature = (featureType: string) => {
    return featureType === 'feathr_anchor_feature_v1' || featureType === 'feathr_derived_feature_v1'
  }

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
  }, [nodeId]);

  return (
    <>
      {
        loading
          ? (<Spin indicator={ <LoadingOutlined style={ { fontSize: 24 } } spin /> } />)
          : (<div style={ { margin: "2%" } }>
            { !feature && <p>Click on node to show metadata and metric details</p> }
            { feature?.attributes.transformation &&
                <Card>
                  <Title level={4}>Transformation</Title>
                  { feature.attributes.transformation.transform_expr &&
                      <p>Expression: { feature.attributes.transformation.transform_expr }</p> }
                  { feature.attributes.transformation.filter &&
                      <p>Filter { feature.attributes.transformation.filter }</p> }
                  { feature.attributes.transformation.agg_func &&
                      <p>Aggregation: { feature.attributes.transformation.agg_func }</p> }
                  { feature.attributes.transformation.limit &&
                      <p>Limit: { feature.attributes.transformation.limit }</p> }
                  { feature.attributes.transformation.group_by &&
                      <p>Group By: { feature.attributes.transformation.group_by }</p> }
                  { feature.attributes.transformation.window &&
                      <p>Window: { feature.attributes.transformation.window }</p> }
                  { feature.attributes.transformation.def_expr &&
                      <p>Expression: { feature.attributes.transformation.def_expr }</p> }
                </Card>
            }
            { feature?.attributes.key && feature.attributes.key.length > 0 &&
                <Card>
                    <Title level={4}>Key</Title>
                    <p>Full name: { feature.attributes.key[0].full_name }</p>
                    <p>Description: { feature.attributes.key[0].description }</p>
                    <p>Key column: { feature.attributes.key[0].key_column }</p>
                    <p>Key column alias: { feature.attributes.key[0].key_column_alias }</p>
                    <p>Key column type: { feature.attributes.key[0].key_column_type }</p>
                </Card>
            }
            { feature?.attributes.type &&
                <Card>
                    <Title level={4}>Type</Title>
                    <p>Dimension Type: { feature.attributes.type.dimensionType }</p>
                    <p>Tensor Category: { feature.attributes.type.tensorCategory }</p>
                    <p>Type: { feature.attributes.type.type }</p>
                    <p>Value Type: { feature.attributes.type.valType }</p>
                </Card>
            }
          </div>)
      }
    </>
  )
}


export default GraphNodeDetails;

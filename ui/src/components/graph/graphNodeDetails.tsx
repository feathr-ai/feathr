import React, { useEffect, useState } from 'react';
import { useParams, useSearchParams } from "react-router-dom";
import { fetchFeature } from '../../api';
import { Feature } from "../../models/model";
import { LoadingOutlined } from "@ant-design/icons";
import { Card, Spin } from "antd";

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
                <Card title="Transformation">
                  { feature.attributes.transformation.transform_expr &&
                      <p>transform_expr: { feature.attributes.transformation.transform_expr }</p> }
                  { feature.attributes.transformation.filter &&
                      <p>filter: { feature.attributes.transformation.filter }</p> }
                  { feature.attributes.transformation.agg_func &&
                      <p>agg_func: { feature.attributes.transformation.agg_func }</p> }
                  { feature.attributes.transformation.limit &&
                      <p>limit: { feature.attributes.transformation.limit }</p> }
                  { feature.attributes.transformation.group_by &&
                      <p>group_by: { feature.attributes.transformation.group_by }</p> }
                  { feature.attributes.transformation.window &&
                      <p>window: { feature.attributes.transformation.window }</p> }
                  { feature.attributes.transformation.def_expr &&
                      <p>def_expr: { feature.attributes.transformation.def_expr }</p> }
                </Card>
            }
            { feature?.attributes.key && feature.attributes.key.length > 0 &&
                <Card title="Key">
                    <p>full_name: { feature.attributes.key[0].full_name }</p>
                    <p>key_column: { feature.attributes.key[0].key_column }</p>
                    <p>description: { feature.attributes.key[0].description }</p>
                    <p>key_column_alias: { feature.attributes.key[0].key_column_alias }</p>
                    <p>key_column_type: { feature.attributes.key[0].key_column_type }</p>
                </Card>
            }
            { feature?.attributes.type &&
                <Card title="Type">
                    <p>dimension_type: { feature.attributes.type.dimension_type }</p>
                    <p>tensor_category: { feature.attributes.type.tensor_category }</p>
                    <p>type: { feature.attributes.type.type }</p>
                    <p>val_type: { feature.attributes.type.val_type }</p>
                </Card>
            }
          </div>)
      }
    </>
  )
}


export default GraphNodeDetails;

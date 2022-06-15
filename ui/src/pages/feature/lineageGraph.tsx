import React, { useEffect, useState } from 'react';
import { Card, Col, Radio, Row, Spin } from 'antd';
import { useParams, useSearchParams } from "react-router-dom";
import { Elements } from 'react-flow-renderer';
import Graph from "../../components/graph/graph";
import { generateEdge, generateNode } from "../../components/graph/utils";
import { fetchProjectLineages } from "../../api";
import { FeatureLineage } from "../../models/model";
import { LoadingOutlined } from "@ant-design/icons";
import GraphNodeDetails from "../../components/graph/graphNodeDetails";

type Params = {
  project: string;
}
const LineageGraph: React.FC = () => {
  const { project } = useParams() as Params;
  const [searchParams] = useSearchParams();
  const nodeId = searchParams.get('nodeId') as string;

  const [lineageData, setLineageData] = useState<FeatureLineage>({ guidEntityMap: null, relations: null });
  const [loading, setLoading] = useState<boolean>(false);
  const [elements, SetElements] = useState<Elements>([]);
  const [featureType, setFeatureType] = useState<string | null>("all_nodes");

  // Fetch lineage data from server side, invoked immediately after component is mounted
  useEffect(() => {
    const fetchLineageData = async () => {
      setLoading(true);
      const data = await fetchProjectLineages(project);
      setLineageData(data);
      setLoading(false);
    };

    fetchLineageData();
  }, [project]);

  // Generate graph data on client side, invoked after graphData or featureType is changed
  useEffect(() => {
    const generateGraphData = async () => {
      if (lineageData.guidEntityMap === null && lineageData.relations === null) {
        return;
      }

      const elements: Elements = [];
      const elementObj: Record<string, string> = {};

      for (let index = 0; index < Object.values(lineageData.guidEntityMap).length; index++) {
        const currentNode: any = Object.values(lineageData.guidEntityMap)[index];

        if (currentNode.typeName === "feathr_workspace_v1") {
          continue; // Open issue: should project node get displayed as well?
        }

        const nodeId = currentNode.guid;

        // If toggled feature type exists, skip other types
        if (featureType && featureType !== "all_nodes" && currentNode.typeName !== featureType) {
          continue;
        }

        const node = generateNode({
          index,
          nodeId,
          currentNode
        });

        elementObj[nodeId] = index?.toString();

        elements.push(node);
      }

      for (let index = 0; index < lineageData.relations.length; index++) {
        const { fromEntityId: from, toEntityId: to, relationshipType } = lineageData.relations[index];
        const edge = generateEdge({ obj: elementObj, from, to });
        if (edge?.source && edge?.target) {
          // Currently, API returns all relationships, filter out Contains, Consumes, etc
          if (relationshipType === "Produces") {
            elements.push(edge);
          }
        }
      }

      SetElements(elements);
    };

    generateGraphData();
  }, [lineageData, featureType])

  const toggleFeatureType = (type: string) => {
    setFeatureType((prevType: string | null) => {
      if (prevType === type) {
        return null;
      }
      return type;
    });
  };

  return (
    <Card title={ "Lineage" }>
      <div>
        <Radio.Group value={ featureType } onChange={ e => toggleFeatureType(e.target.value) }>
          <Radio.Button value="all_nodes">All Features</Radio.Button>
          <Radio.Button value="feathr_source_v1"> Source </Radio.Button>
          <Radio.Button value="feathr_anchor_v1">Anchor</Radio.Button>
          <Radio.Button value="feathr_anchor_feature_v1">Anchor Feature</Radio.Button>
          <Radio.Button value="feathr_derived_feature_v1">Derived Feature</Radio.Button>
        </Radio.Group>
      </div>
      <div>
        {
          loading
            ? (<Spin indicator={ <LoadingOutlined style={ { fontSize: 24 } } spin /> } />)
            : (
              <Row>
                <Col flex="2">
                  <Graph data={ elements } nodeId={ nodeId } />
                </Col>
                <Col flex="1">
                  <GraphNodeDetails></GraphNodeDetails>
                </Col>
              </Row>
            )
        }
      </div>
    </Card>
  );
}

export default LineageGraph;

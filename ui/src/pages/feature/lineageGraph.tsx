import React, { useCallback, useEffect, useState } from 'react';
import { Button, Card, Radio } from 'antd';
import { useParams, useSearchParams } from "react-router-dom";
import { Elements } from 'react-flow-renderer';
import Graph from "../../components/lineage/graph";
import { generateEdge, generateNode } from "../../components/lineage/utils";
import { fetchProjectLineages } from "../../api";

type Params = {
  project: string;
}
const LineageGraph: React.FC = () => {
  const { project } = useParams() as Params;
  const [searchParams] = useSearchParams();
  const nodeId = searchParams.get('nodeId') as string;

  const [elements, SetElements] = useState<Elements>([]);
  const [featureType, setFeatureType] = useState<string | null>(null);

  // Fetch lineage data from server side and convert into graph nodes and edges
  const fetchGraphData = useCallback(async () => {
    const graphData = await fetchProjectLineages(project);
    const elements: Elements = [];
    const elementObj: Record<string, string> = {};

    for (let index = 0; index < Object.values(graphData.guidEntityMap).length; index++) {
      const currentNode: any = Object.values(graphData.guidEntityMap)[index];

      if (currentNode.typeName === "feathr_workspace_v1") {
        continue; // Open issue: should project node get displayed as well?
      }

      const nodeId = currentNode.guid;

      // check if model type exists and this currentNode is of that type
      if (featureType && featureType !== currentNode.typeName) {
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

    for (let index = 0; index < graphData.relations.length; index++) {
      const { fromEntityId: from, toEntityId: to, relationshipType } = graphData.relations[index];
      const edge = generateEdge({ obj: elementObj, from, to });
      if (edge?.source && edge?.target) {
        if (relationshipType === "Produces") { // Drop edges with other relationship like Consumes, BelongsTo, etc)
          elements.push(edge);
        }
      }
    }
    SetElements(elements);

    console.log("setElements fired in fetchGraphData, elements count = ", elements.length);
  }, [featureType])

  useEffect(() => {
    fetchGraphData();
  }, [fetchProjectLineages, featureType])

  const toggleFeatureType = (type: string) => {
    setFeatureType((prevType: string | null) => {
      if (type === "all_nodes") {
        return null;
      }
      if (prevType === type) {
        return null;
      }
      return type;
    });
  };

  const toggleAllFeatures = async () => {
    setFeatureType(null);
  };

  return (
    <Card title={ "Lineage" }>
      <div>
        <Radio.Group onChange={ e => toggleFeatureType(e.target.value) }>
          <Radio.Button value="feathr_source_v1"> Source </Radio.Button>
          <Radio.Button value="feathr_anchor_v1">Anchor</Radio.Button>
          <Radio.Button value="feathr_anchor_feature_v1">Anchor Feature</Radio.Button>
          <Radio.Button value="feathr_derived_feature_v1">Derived Feature</Radio.Button>
          <Radio.Button value="all_nodes">All Features</Radio.Button>
        </Radio.Group>
        <Button onClick={ () => toggleAllFeatures() }></Button>
      </div>
      <div>
        { elements && <Graph data={ elements } nodeId={nodeId} /> }
      </div>
    </Card>
  );
}

export default LineageGraph;

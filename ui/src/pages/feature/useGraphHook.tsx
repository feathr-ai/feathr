import { useEffect } from "react";
import { Elements } from 'react-flow-renderer';
import { generateEdge, generateNode } from "../../components/graph/utils";
import { FeatureLineage } from "../../models/model";

const useGraphHook = (lineageData: FeatureLineage, featureType: string|null, setElements: Function) => {
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
        var { fromEntityId: from, toEntityId: to, relationshipType } = lineageData.relations[index];
        if (relationshipType === "Consumes") [from, to] = [to, from];
        const edge = generateEdge({ obj: elementObj, from, to });
        if (edge?.source && edge?.target) {
          if (relationshipType === "Consumes" || relationshipType === "Produces") {
            elements.push(edge);
          }
        }
      }

      setElements(elements);
    };

    generateGraphData();

  }, [lineageData, featureType]);
}

export default useGraphHook;
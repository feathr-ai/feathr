import dagre from "dagre";
import {
  ArrowHeadType,
  Edge,
  Elements,
  isNode,
  Node,
  Position,
} from "react-flow-renderer";
import { FeatureLineage } from "../../models/model";

const DEFAULT_WIDTH = 172;
const DEFAULT_HEIGHT = 36;

type getLayoutElementsRet = {
  layoutedElements: Elements;
  elementMapping: Record<string, number>;
};

const getElements = (
  lineageData: FeatureLineage,
  featureType: string | null
) => {
  if (lineageData.guidEntityMap === null && lineageData.relations === null) {
    return;
  }

  const elements: Elements = [];
  const elementObj: Record<string, string> = {};

  for (
    let index = 0;
    index < Object.values(lineageData.guidEntityMap).length;
    index++
  ) {
    const currentNode: any = Object.values(lineageData.guidEntityMap)[index];

    if (currentNode.typeName === "feathr_workspace_v1") {
      continue; // Open issue: should project node get displayed as well?
    }

    const nodeId = currentNode.guid;

    // If toggled feature type exists, skip other types
    if (
      featureType &&
      featureType !== "all_nodes" &&
      currentNode.typeName !== featureType
    ) {
      continue;
    }

    const node = generateNode({
      index,
      nodeId,
      currentNode,
    });

    elementObj[nodeId] = index?.toString();

    elements.push(node);
  }

  for (let index = 0; index < lineageData.relations.length; index++) {
    var {
      fromEntityId: from,
      toEntityId: to,
      relationshipType,
    } = lineageData.relations[index];
    if (relationshipType === "Consumes") [from, to] = [to, from];
    const edge = generateEdge({ obj: elementObj, from, to });
    if (edge?.source && edge?.target) {
      if (relationshipType === "Consumes" || relationshipType === "Produces") {
        elements.push(edge);
      }
    }
  }

  return elements;
};

const getLayoutedElements = (
  elements: Elements,
  direction = "LR"
): getLayoutElementsRet => {
  const dagreGraph = new dagre.graphlib.Graph();
  dagreGraph.setDefaultEdgeLabel(() => ({}));

  dagreGraph.setGraph({ rankdir: direction });

  const isHorizontal = direction === "LR";

  for (let index = 0; index < elements.length; index++) {
    const element: Node | Edge = elements[index];
    if (isNode(element)) {
      dagreGraph.setNode(element.id, {
        width: DEFAULT_WIDTH,
        height: DEFAULT_HEIGHT,
      });
    } else {
      dagreGraph.setEdge(element.source, element.target);
    }
  }

  dagre.layout(dagreGraph);

  const newElements = [];
  const elementsObj: Record<string, number> = {};

  for (let index = 0; index < elements.length; index++) {
    const element = elements[index] as Node;

    if (isNode(element)) {
      elementsObj[element.data?.id] = index;

      const nodeWithPosition = dagreGraph.node(element.id);
      element.targetPosition = isHorizontal ? Position.Left : Position.Top;
      element.sourcePosition = isHorizontal ? Position.Right : Position.Bottom;

      element.position = {
        x: nodeWithPosition.x - DEFAULT_WIDTH / 2,
        y: nodeWithPosition.y - DEFAULT_HEIGHT / 2,
      };
    }

    newElements.push(element);
  }

  return {
    layoutedElements: newElements,
    elementMapping: elementsObj,
  };
};

const featureTypeColors: Record<string, string> = {
  feathr_source_v1: "hsl(315, 100%, 50%)",
  feathr_anchor_v1: "hsl(270, 100%, 50%)",
  feathr_anchor_feature_v1: "hsl(225, 100%, 50%)",
  feathr_derived_feature_v1: "hsl(135, 100%, 50%)",
};

const generateNode = ({
  nodeId,
  index,
  currentNode,
}: // eslint-disable-next-line @typescript-eslint/no-explicit-any
any): any => ({
  key: nodeId,
  id: index?.toString(),
  type: "custom-node",
  label: currentNode.displayText,
  shape: "box",
  color: {
    background: featureTypeColors[currentNode.typeName],
  },
  data: {
    id: nodeId,
    title: currentNode.displayText,
    subtitle: currentNode.typeName,
    featureId: currentNode.guid,
    version: currentNode.version,
    borderColor: featureTypeColors[currentNode.typeName],
  },
});

type GenerateEdgeProps = {
  obj: Record<string, string>;
  from: string;
  to: string;
};

const generateEdge = ({ obj, from, to }: GenerateEdgeProps): Edge => {
  const source = obj?.[from];
  const target = obj?.[to];

  const id = `e${source}-${target}`;
  return {
    id,
    source,
    target,
    arrowHeadType: ArrowHeadType.ArrowClosed,
  };
};

export { generateEdge, generateNode, getLayoutedElements, getElements };

export const findNodeInElement = (
  nodeId: string | null,
  elements: Elements
): Node | null => {
  if (nodeId) {
    const node = elements.find(
      (element) => isNode(element) && element.data.id === nodeId
    );
    return node as Node;
  }
  return null;
};

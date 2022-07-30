import React, {
  MouseEvent as ReactMouseEvent,
  useCallback,
  useEffect,
  useState,
} from "react";
import ReactFlow, {
  ConnectionLineType,
  Controls,
  Edge,
  Elements,
  getIncomers,
  getOutgoers,
  isEdge,
  isNode,
  Node,
  OnLoadParams,
  ReactFlowProvider,
} from "react-flow-renderer";
import { useSearchParams } from "react-router-dom";
import LineageNode from "./graphNode";
import { findNodeInElement, getLayoutedElements } from "./utils";
import { isFeature } from "../../utils/utils";

const nodeTypes = {
  "custom-node": LineageNode,
};
type Props = {
  data: Elements;
  nodeId: string;
};
const Graph: React.FC<Props> = ({ data, nodeId }) => {
  const [, setURLSearchParams] = useSearchParams();

  const { layoutedElements, elementMapping } = getLayoutedElements(data);
  const [elements, setElements] = useState<Elements>(layoutedElements);

  useEffect(() => {
    setElements(layoutedElements);
  }, [layoutedElements, data, nodeId]);

  // Reset all node highlight status
  const resetHighlight = useCallback((): void => {
    if (!elements || elements.length === 0) {
      return;
    }

    const values: Elements = [];

    for (let index = 0; index < elements.length; index++) {
      const element = elements[index];

      if (isNode(element)) {
        values.push({
          ...element,
          style: {
            ...element.style,
            opacity: 1,
          },
        });
      }
      if (isEdge(element)) {
        values.push({
          ...element,
          animated: false,
        });
      }
    }

    setElements(values);
  }, [elements]);

  // Highlight path of selected node, including all linked up and down stream nodes
  const highlightPath = useCallback(
    (node: Node, check: boolean): void => {
      const checkElements = check ? layoutedElements : elements;

      const incomerIds = new Set([
        ...getIncomers(node, checkElements).map((i) => i.id),
      ]);
      const outgoerIds = new Set([
        ...getOutgoers(node, checkElements).map((o) => o.id),
      ]);

      const values: Elements = [];
      for (let index = 0; index < checkElements.length; index++) {
        const element = checkElements[index];
        let highlight = false;
        if (isNode(element)) {
          highlight =
            element.id === node.id ||
            incomerIds.has(element.id) ||
            outgoerIds.has(element.id);
        } else {
          highlight = element.source === node.id || element.target === node.id;
          const animated =
            incomerIds.has(element.source) &&
            (incomerIds.has(element.target) || node.id === element.target);

          highlight = highlight || animated;
        }

        if (isNode(element)) {
          values.push({
            ...element,
            style: {
              ...element.style,
              opacity: highlight ? 1 : 0.25,
            },
            data: {
              ...element.data,
              active:
                element.id === node.id && isFeature(element.data?.subtitle),
            },
          });
        }
        if (isEdge(element)) {
          values.push({
            ...element,
            animated: highlight,
          });
        }
      }

      setElements(values);
    },
    [elements, layoutedElements]
  );

  useEffect(() => {
    if (nodeId) {
      const node = findNodeInElement(nodeId, layoutedElements);
      if (node) {
        resetHighlight();
        highlightPath(node, !!nodeId);
      }
    }
  }, [highlightPath, layoutedElements, resetHighlight, nodeId]);

  // Fit the graph to the center of layout view when graph is initialized
  const onLoad = (reactFlowInstance: OnLoadParams<unknown> | null) => {
    reactFlowInstance?.fitView();
  };

  // Fired when panel is clicked, reset all highlighted path, and remove the nodeId query string in url path.
  const onPaneClick = useCallback(() => {
    resetHighlight();
    setURLSearchParams({});
  }, [resetHighlight, setURLSearchParams]);

  const onNodeDragStop = (_: ReactMouseEvent, node: Node) => {
    const nodePosition = elementMapping[node.data?.id];
    const values: Elements = [...elements];
    values[nodePosition] = node;

    setElements(values);
  };

  return (
    <div className="lineage-graph">
      <ReactFlowProvider>
        <ReactFlow
          style={{ height: window.innerHeight - 250, width: "100%" }}
          elements={elements}
          snapToGrid
          snapGrid={[15, 15]}
          zoomOnScroll={false}
          onLoad={onLoad}
          onPaneClick={onPaneClick}
          onElementClick={(_: ReactMouseEvent, element: Node | Edge): void => {
            if (isNode(element)) {
              resetHighlight();
              highlightPath(element, false);
              setURLSearchParams({
                nodeId: element.data.id,
                featureType: element.data.subtitle,
              });
            }
          }}
          onNodeDragStop={onNodeDragStop}
          connectionLineType={ConnectionLineType.SmoothStep}
          nodeTypes={nodeTypes}
        >
          <Controls />
        </ReactFlow>
      </ReactFlowProvider>
    </div>
  );
};

export default Graph;

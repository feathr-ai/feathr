import React, { MouseEvent as ReactMouseEvent, useCallback, useEffect, useRef, useState, } from 'react';
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
  ReactFlowProvider
} from 'react-flow-renderer';
import { useSearchParams } from 'react-router-dom-v5-compat';
import LineageNode from "./lineageNode";
import { findNodeInElement, getLayoutElements } from "./utils";
import { Spin } from "antd";
import { LoadingOutlined } from "@ant-design/icons";

const nodeTypes = {
  'custom-node': LineageNode,
};
type Props = {
  data: Elements;
  nodeId: string;
}
const LineageGraph: React.FC<Props> = ({ data, nodeId }) => {
  const [, setURLSearchParams] = useSearchParams();
  const instanceRef = useRef<OnLoadParams | null>(null);

  const { res, elementMapping } = getLayoutElements(data);
  const [elements, setElements] = useState<Elements>(res);
  const [loading, setLoading] = useState<boolean>(false);


  useEffect(() => {
    setElements(res);
  }, [data, nodeId]);

  const resetHighlight = (): void => {
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
  };

  const highlightPath = (node: Node, check: boolean): void => {
    const checkElements = check ? res : elements;

    const incomerIds = new Set([...getIncomers(node, checkElements).map((i) => i.id)]);
    const outgoerIds = new Set([...getOutgoers(node, checkElements).map((o) => o.id)]);

    const values: Elements = [];
    for (let index = 0; index < checkElements.length; index++) {
      const element = checkElements[index];
      let highlight = false;
      if (isNode(element)) {
        highlight = element.id === node.id
          || incomerIds.has(element.id)
          || outgoerIds.has(element.id);
      } else {
        highlight = element.source === node.id || element.target === node.id;
        const animated = incomerIds.has(element.source)
          && (incomerIds.has(element.target) || node.id === element.target);

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
            active: element.id === node.id,
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
  };

  const fitElements = (): void => {
    setTimeout(() => {
      instanceRef?.current?.fitView();
    }, 1);
    setTimeout(() => {
      setLoading(false);
    }, 1000);
  };

  const onLoad = (reactFlowInstance: OnLoadParams<unknown> | null) => {
    instanceRef.current = reactFlowInstance;
    fitElements();
  };

  useEffect(() => {
    if (nodeId) {
      const node = findNodeInElement(nodeId, res);
      if (node) {
        resetHighlight(); // new changes to the graph
        highlightPath(node, !!nodeId);
      }
    }
  }, [nodeId]);

  useEffect(() => {
    if (instanceRef.current) {
      setLoading(true);

      fitElements();
    }
  }, [instanceRef]);

  const onPaneClick = useCallback(() => {
    resetHighlight();
    setURLSearchParams({});
  }, []);

  const onNodeDragStop = (_: ReactMouseEvent, node: Node) => {
    const nodePosition = elementMapping[node.data?.id];
    const values: Elements = [
      ...elements,
    ];
    values[nodePosition] = node;

    setElements(values);
  };

  return (
    <div className="lineage-graph">
      { loading ?  <Spin indicator={ <LoadingOutlined style={ { fontSize: 24 } } spin /> } /> : (
        <ReactFlowProvider>
          <ReactFlow
            style={ { height: "700px", width: "100%" } }
            elements={ elements }
            onLoad={ onLoad }
            snapToGrid
            snapGrid={ [15, 15] }
            zoomOnScroll={ false }
            onPaneClick={ onPaneClick }
            onElementClick={ (_: ReactMouseEvent, element: Node | Edge): void => {
              if (isNode(element)) {
                resetHighlight();
                highlightPath(element, false);
                setURLSearchParams({ nodeId: element.data.id });
              }
            } }
            onNodeDragStop={ onNodeDragStop }
            connectionLineType={ ConnectionLineType.SmoothStep }
            nodeTypes={ nodeTypes }
          >
            <Controls />
          </ReactFlow>
        </ReactFlowProvider>
      ) }
    </div>
  );
}

export default LineageGraph;

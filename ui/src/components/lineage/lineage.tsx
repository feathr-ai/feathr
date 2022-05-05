import React, { useCallback } from 'react';
import ReactFlow, {
  Node, Edge,
  ReactFlowProvider,
  addEdge,
  Controls,
  Connection,
  CoordinateExtent,
  Position,
  useNodesState,
  useEdgesState,
} from 'react-flow-renderer';
import dagre from 'dagre';

import './layouting.css';

const dagreGraph = new dagre.graphlib.Graph();
dagreGraph.setDefaultEdgeLabel(() => ({}));

const nodeExtent: CoordinateExtent = [
  [0, 0],
  [1000, 1000],
];

type LineageProps = {
  lineageNodes: Node[];
  lineageEdges: Edge[];
};

const Lineage: React.FC<LineageProps> = ({ lineageNodes, lineageEdges }) => {
  const [nodes, setNodes, onNodesChange] = useNodesState(lineageNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(lineageEdges);

  const onConnect = useCallback((connection: Connection) => {
    setEdges((eds) => addEdge(connection, eds));
  }, [setEdges]);

  const onLayout = (direction: string) => {
    const isHorizontal = direction === 'LR';
    dagreGraph.setGraph({ rankdir: direction });

    nodes.forEach((node) => {
      dagreGraph.setNode(node.id, { width: 150, height: 50 });
    });

    edges.forEach((edge) => {
      dagreGraph.setEdge(edge.source, edge.target);
    });

    dagre.layout(dagreGraph);

    const layoutedNodes = nodes.map((node) => {
      const nodeWithPosition = dagreGraph.node(node.id);
      node.targetPosition = isHorizontal ? Position.Left : Position.Top;
      node.sourcePosition = isHorizontal ? Position.Right : Position.Bottom;
      node.position = { x: nodeWithPosition.x + Math.random() / 1000, y: nodeWithPosition.y };

      return node;
    });

    setNodes(layoutedNodes);
  };

  return (
    <div className="layoutflow">
      <ReactFlowProvider>
        <ReactFlow
          nodes={ nodes }
          edges={ edges }
          onConnect={ onConnect }
          nodeExtent={ nodeExtent }
          onInit={ () => onLayout('LR') }
          onNodesChange={ onNodesChange }
          onEdgesChange={ onEdgesChange }
        >
          <Controls />
        </ReactFlow>
        <div className="controls">
          <button onClick={ () => onLayout('TB') } style={ { marginRight: 10 } }>
            vertical layout
          </button>
          <button onClick={ () => onLayout('LR') }>horizontal layout</button>
        </div>
      </ReactFlowProvider>
    </div>
  );
};

export default Lineage;

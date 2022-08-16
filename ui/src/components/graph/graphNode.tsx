import React, { FC, memo } from "react";
import { RightCircleOutlined } from "@ant-design/icons";
import { Handle, NodeProps, Position } from "react-flow-renderer";
import { useNavigate, useParams } from "react-router-dom";

type Params = {
  project: string;
};

const GraphNode: FC<NodeProps> = (props: NodeProps) => {
  const navigate = useNavigate();
  const { project } = useParams<Params>();

  const {
    data: { title, subtitle, featureId, version, borderColor, active },
  } = props;
  const nodeTitle = version ? `${title} (v${version})` : title;
  const nodeSubtitle = subtitle.replace("feathr_", "");
  const nodeColorStyle = {
    border: `2px solid ${borderColor}`,
  };

  const onNodeIconClick = () => {
    navigate(`/projects/${project}/features/${featureId}`);
  };

  return (
    <div
      style={active ? {} : nodeColorStyle}
      className={active ? "lineage-node-active" : ""}
    >
      <div className="lineage-node-box">
        <Handle type="target" position={Position.Left} />
        <div>
          <div className="lineage-node-title">
            {nodeTitle}
            {active && (
              <RightCircleOutlined
                className="lineage-navigate"
                onClick={onNodeIconClick}
              />
            )}
          </div>
          <div className="lineage-node-subtitle">{nodeSubtitle}</div>
        </div>

        <Handle type="source" position={Position.Right} />
      </div>
    </div>
  );
};

export default memo(GraphNode);

import React, { FC, memo } from 'react';
import { RightCircleOutlined } from "@ant-design/icons";
import { Handle, NodeProps, Position } from 'react-flow-renderer';
import { useNavigate, useSearchParams } from "react-router-dom-v5-compat";
import { useParams } from "react-router";


const LineageNode: FC<NodeProps> = (props: NodeProps) => {
  const navigate = useNavigate();
  // @ts-ignore
  const { project, qualifiedName } = useParams();
  const [searchParams] = useSearchParams();
  const nodeId = searchParams.get('nodeId') as string;

  const {
    data: { label, otherName, active, id, borderColor },
  } = props;

  const nodeColorStyle = {
    border: `2px solid ${ borderColor }`,
  };

  const onIconClick = () => {
    navigate(`/projects/${ project }/features/${ qualifiedName }`)
  };

  const isActive = active || (nodeId === id);

  return (
    <div
      style={ isActive ? {} : nodeColorStyle }
      className={ isActive ? 'lineage-node-active' : '' }
    >
      <div className="lineage-node">
        <Handle type="target" position={ Position.Left } />
        <div>
          <div className="lineage-node-title">{ label }
            { isActive && (<RightCircleOutlined className="lineage-navigate" onClick={ onIconClick } />) }
          </div>
          <div className="lineage-node-subtitle">
            { otherName }
          </div>
        </div>

        <Handle type="source" position={ Position.Right } />
      </div>
    </div>
  );
};

export default memo(LineageNode);

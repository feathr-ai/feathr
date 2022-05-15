import React, { useCallback } from 'react';
import { Alert, Card, Spin } from 'antd';
import { useHistory, useParams } from 'react-router';
import { QueryStatus, useQuery } from "react-query";
import { LoadingOutlined } from '@ant-design/icons';
import { AxiosError } from 'axios';
import { Edge, Node, XYPosition } from "react-flow-renderer";
import { fetchProjectLineages } from '../../api';
import { IFeatureLineage } from "../../models/model";
import Lineage from "../../components/lineage/lineage";

type Props = {};
type Params = {
  project: string;
  qualifiedName: string;
}

const FeatureDetails: React.FC<Props> = () => {
  const { project, qualifiedName } = useParams<Params>();
  const loadingIcon = <LoadingOutlined style={ { fontSize: 24 } } spin />;
  const history = useHistory();
  useCallback((location) => history.push(location), [history]);
  const {
    status,
    error,
    data
  } = useQuery<IFeatureLineage, AxiosError>(['feature', qualifiedName], () => fetchProjectLineages(project));

  const render = (status: QueryStatus): JSX.Element => {
    switch (status) {
      case "error":
        return (
          <Card>
            <Alert
              message="Error"
              description={ error?.message }
              type="error"
              showIcon
            />
          </Card>
        );
      case "idle":
        return (
          <Card>
            <Spin indicator={ loadingIcon } />
          </Card>
        );
      case "loading":
        return (
          <Card>
            <Spin indicator={ loadingIcon } />
          </Card>
        );
      case "success":
        if (data === undefined) {
          return (
            <Card>
              <Alert
                message="Error"
                description="Data does not exist..."
                type="error"
                showIcon
              />
            </Card>
          );
        } else {
          const position: XYPosition = { x: 0, y: 0 };
          const nodes = Object.values(data.guidEntityMap).map((entity: any) => {
            return {
              id: entity.guid,
              type: "",
              data: { label: entity.displayText },
              position: position
            } as Node
          });
          const edges = data.relations.map((relation: any) => {
            return {
              id: relation.relationshipId,
              source: relation.fromEntityId,
              target: relation.toEntityId
            } as Edge
          });
          return (
            <Card title={ "Lineage" }>
              <Lineage lineageNodes={ nodes } lineageEdges={ edges }></Lineage>
            </Card>
          );
        }
    }
  }

  return (
    <>
      <div style={ { margin: "2%" } }>
        { render(status) }
      </div>
    </>
  );
};

export default FeatureDetails;

import React, { useEffect, useRef, useState } from "react";
import { PageHeader, Row, Col, Radio, Tabs } from "antd";
import { useParams, useSearchParams } from "react-router-dom";
import FlowGraph from "@/components/FlowGraph";
import { fetchProjectLineages } from "@/api";
import { FeatureLineage } from "@/models/model";
import { FeatureType } from "@/utils/utils";
import NodeDetails from "./components/NodeDetails";

const items = [
  { label: "Metadata", key: "1", children: <NodeDetails /> },
  { label: "Metrics", key: "2", children: <p>Under construction</p> }, // 务必填写 key
  { label: "Jobs", key: "3", children: <p>Under construction</p> },
];

type Params = {
  project: string;
};
const LineageGraph = () => {
  const { project } = useParams() as Params;
  const [searchParams] = useSearchParams();
  const nodeId = searchParams.get("nodeId") as string;

  const [lineageData, setLineageData] = useState<FeatureLineage>({
    guidEntityMap: {},
    relations: [],
  });

  const [loading, setLoading] = useState<boolean>(false);

  const [featureType, setFeatureType] = useState<FeatureType>(
    FeatureType.AllNodes
  );

  const mountedRef = useRef<Boolean>(true);

  // Fetch lineage data from server side, invoked immediately after component is mounted
  useEffect(() => {
    const fetchLineageData = async () => {
      setLoading(true);
      const data = await fetchProjectLineages(project);
      if (mountedRef.current) {
        setLineageData(data);
        setLoading(false);
      }
    };

    fetchLineageData();
  }, [project]);

  const toggleFeatureType = (type: FeatureType) => {
    setFeatureType(type);
  };

  useEffect(() => {
    mountedRef.current = true;
    return () => {
      mountedRef.current = false;
    };
  }, []);

  return (
    <div className="page">
      <PageHeader title={`Lineage ${project}`} ghost={false}>
        <Radio.Group
          value={featureType}
          onChange={(e) => toggleFeatureType(e.target.value)}
        >
          <Radio.Button value={FeatureType.AllNodes}>All Nodes</Radio.Button>
          <Radio.Button value={FeatureType.Source}> Source </Radio.Button>
          <Radio.Button value={FeatureType.AnchorFeature}>
            Anchor Feature
          </Radio.Button>
          <Radio.Button value={FeatureType.DerivedFeature}>
            Derived Feature
          </Radio.Button>
        </Radio.Group>
        <Row>
          <Col flex="2">
            <FlowGraph
              minHeight="calc(100vh - 215px)"
              loading={loading}
              data={lineageData}
              nodeId={nodeId}
              project={project}
              featureType={featureType}
            />
          </Col>
          <Col flex="1">
            <Tabs defaultActiveKey="1" items={items} />
          </Col>
        </Row>
      </PageHeader>
    </div>
  );
};

export default LineageGraph;

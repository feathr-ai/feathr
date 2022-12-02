import React from "react";
import { useParams, useSearchParams } from "react-router-dom";
import { fetchFeature, fetchDataSource } from "@/api";
import { LoadingOutlined } from "@ant-design/icons";
import { useQuery } from "react-query";
import { Spin, Typography } from "antd";
import { FeatureType } from "@/utils/utils";
import FeatureNodeDetail from "./FeatureNodeDetail";
import SourceNodeDetial from "./SourceNodeDetial";

import styles from "./index.module.less";

const { Paragraph } = Typography;

const NodeDetails = () => {
  const [searchParams] = useSearchParams();
  const { project } = useParams();
  const nodeId = searchParams.get("nodeId") as string;
  const featureType = searchParams.get("featureType") as string;

  const isSource = featureType === FeatureType.Source;
  const isFeature =
    featureType === FeatureType.AnchorFeature ||
    featureType === FeatureType.DerivedFeature;

  const { isLoading, data } = useQuery<any | null>(
    ["nodeDetails", project, nodeId],
    async () => {
      if (isSource || isFeature) {
        const api = isSource ? fetchDataSource : fetchFeature;
        return await api(project!, nodeId);
      }
    },
    {
      retry: false,
      refetchOnWindowFocus: false,
    }
  );

  return (
    <Spin
      wrapperClassName={styles.wrap}
      spinning={isLoading}
      indicator={<LoadingOutlined style={{ fontSize: 36 }} spin />}
    >
      <div style={{ height: "calc(100vh - 300px)", overflow: "auto" }}>
        {data ? (
          isSource ? (
            <SourceNodeDetial source={data} />
          ) : (
            <FeatureNodeDetail feature={data} />
          )
        ) : (
          !isLoading && (
            <Paragraph>
              Click on source or feature node to show metadata and metric
              details
            </Paragraph>
          )
        )}
      </div>
    </Spin>
  );
};

export default NodeDetails;

import React from "react";
import { LoadingOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "react-router-dom";
import {
  Button,
  Card,
  Row,
  Space,
  Typography
} from "antd";

const { Title } = Typography;

type Params = {
  project: string;
  dataSourceId: string;
};

const DataSourceDetails = () => {
    const { project, dataSourceId } = useParams() as Params;
    const navigate = useNavigate();
    const loadingIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;
    // const { status, error, data } = useQuery<Feature, AxiosError>(
    //   ["dataSourceId", dataSourceId],
    //   () => fetchFeature(project, featureId)
    // );
    return <div className="page">Hello World</div>;
    // return (
    //     <Card>
    //     <Title level={3}>data.attributes.name</Title>
    //     <div>
    //       <Space>
    //         <Button type="primary">
    //         {/* <Button type="primary" onClick={() => openLineageWindow()}> */}
    //           View Lineage
    //         </Button>
    //       </Space>
    //     </div>
    //     <div>
    //       <Row>
    //         Test
    //         {/* <InputAnchorFeatures project={project} feature={data} />
    //         <InputDerivedFeatures project={project} feature={data} />
    //         <FeatureTransformation feature={data} />
    //         <FeatureKey feature={data} />
    //         <FeatureType feature={data} />
    //         <FeatureLineageGraph /> */}
    //       </Row>
    //     </div>
    //   </Card>
    // )
};

export default DataSourceDetails;

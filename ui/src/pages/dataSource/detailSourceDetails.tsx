import React from "react";
import { Button, Card, Row, Space, Typography } from "antd";

const { Title } = Typography;

const FeatureDetails: React.FC = () => {
    // return <div className="page">Hello World</div>;
    return (
        <Card>
        <Title level={3}>data.attributes.name</Title>
        <div>
          <Space>
            <Button type="primary">
            {/* <Button type="primary" onClick={() => openLineageWindow()}> */}
              View Lineage
            </Button>
          </Space>
        </div>
        <div>
          <Row>
            Test
            {/* <InputAnchorFeatures project={project} feature={data} />
            <InputDerivedFeatures project={project} feature={data} />
            <FeatureTransformation feature={data} />
            <FeatureKey feature={data} />
            <FeatureType feature={data} />
            <FeatureLineageGraph /> */}
          </Row>
        </div>
      </Card>
    )
};

export default FeatureDetails;

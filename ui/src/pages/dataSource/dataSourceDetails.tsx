import React from "react";
import { LoadingOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "react-router-dom";
import {
  Alert,
  Button,
  Card,
  Row,
  Space,
  Spin,
  Typography
} from "antd";
import { QueryStatus, useQuery } from "react-query";
import { AxiosError } from "axios";
import { fetchDataSource } from "../../api";
import { DataSource, DataSourceAttributes } from "../../models/model";

const { Title } = Typography;

type Params = {
  project: string;
  dataSourceId: string;
};

const DataSourceDetails = () => {
    const { project, dataSourceId } = useParams() as Params;
    const navigate = useNavigate();
    const loadingIcon = <LoadingOutlined style={{ fontSize: 24 }} spin />;
    const { status, error, data } = useQuery<DataSource, AxiosError>(
      ["dataSourceId", dataSourceId],
      () => fetchDataSource(project, dataSourceId)
    );

    const render = (status: QueryStatus): JSX.Element => {
      switch (status) {
        case "error":
          return (
            <Card>
              <Alert
                message="Error"
                description={error?.message}
                type="error"
                showIcon
              />
            </Card>
          );
        case "idle":
          return (
            <Card>
              <Spin indicator={loadingIcon} />
            </Card>
          );
        case "loading":
          return (
            <Card>
              <Spin indicator={loadingIcon} />
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
            return (
              <>
                <Button type="link" onClick={() => navigate(-1)}>
                  dataSource list {">"}
                </Button>
                <Card>
                  <Title level={3}>{data.attributes.name}</Title>
                  {/* <div>
                    <Space>
                      <Button type="primary" onClick={() => openLineageWindow()}>
                        View Lineage
                      </Button>
                    </Space>
                  </div> */}
                  <div>
                    <Row>
                      Test Below
                      {/* <InputAnchorFeatures project={project} feature={data} />
                      <InputDerivedFeatures project={project} feature={data} />
                      <FeatureTransformation feature={data} />
                      <FeatureKey feature={data} />
                      <FeatureType feature={data} />
                      <FeatureLineageGraph /> */}
                    </Row>
                  </div>
                </Card>
              </>
            );
          }
      }
    };

    // return <div className="page">{render(status)}</div>;
    return <div className="page">{data}</div>;
};

export default DataSourceDetails;

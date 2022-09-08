import React from "react";
import { LoadingOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "react-router-dom";
import {
  Alert,
  Button,
  Card,
  Col,
  Row,
  Spin,
  Typography
} from "antd";
import { QueryStatus, useQuery } from "react-query";
import { AxiosError } from "axios";
import { fetchDataSource } from "../../api";
import { DataSource, DataSourceAttributes } from "../../models/model";

const { Title } = Typography;

type DataSourceKeyProps = { dataSource: DataSource };
const DataSourceKey = ({ dataSource }: DataSourceKeyProps) => {
  const keys = dataSource.attributes;
  return (
    <>
      {keys && (
        <Col span={24}>
          <Card className="card">
            <Title level={4}>Data Source Attributes</Title>
            <div className="dataSource-container">
              <p>Name: {keys.name}</p>
              <p>Type: {keys.type}</p>
              <p>Path: {keys.path}</p>
              <p>Preprocessing: {keys.preprocessing}</p>
              <p>Event Timestamp Column: {keys.eventTimestampColumn}</p>
              <p>Timestamp Format: {keys.timestampFormat}</p>
              <p>Qualified Name: {keys.qualifiedName}</p>
              <p>Tags: {JSON.stringify(keys.tags)}</p>
            </div>
          </Card>
        </Col>
      )}
    </>
  );
};

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
                  <div>
                    <Row>
                      <DataSourceKey dataSource={data} />
                    </Row>
                  </div>
                </Card>
              </>
            );
          }
      }
    };

    return <div className="page">{render(status)}</div>;
};

export default DataSourceDetails;

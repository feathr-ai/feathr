import React from "react";
import { LoadingOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "react-router-dom";
import { Alert, Space, Breadcrumb, PageHeader, Spin, Button } from "antd";
import { Link } from "react-router-dom";
import { useQuery } from "react-query";
import { AxiosError } from "axios";
import { fetchDataSource } from "@/api";
import { DataSource } from "@/models/model";
import { SourceAttributesMap } from "@/utils/attributesMapping";
import CardDescriptions from "@/components/CardDescriptions";

const DataSourceDetails = () => {
  const navigate = useNavigate();

  const { project = "", dataSourceId = "" } = useParams();

  const {
    isLoading,
    error,
    data = { attributes: {} } as DataSource,
  } = useQuery<DataSource, AxiosError>(
    ["dataSourceId", dataSourceId],
    () => fetchDataSource(project, dataSourceId),
    {
      retry: false,
      refetchOnWindowFocus: false,
    }
  );

  const { attributes } = data;

  return (
    <div className="page">
      <PageHeader
        ghost={false}
        title="Data Source Attributes"
        breadcrumb={
          <Breadcrumb>
            <Breadcrumb.Item>
              <Link to={`/dataSources?project=${project}`}>Data Sources</Link>
            </Breadcrumb.Item>
            <Breadcrumb.Item>Data Source Attributes</Breadcrumb.Item>
          </Breadcrumb>
        }
        extra={[
          <Button
            key="1"
            ghost
            type="primary"
            onClick={() => {
              navigate(`/features?project=${project}`);
            }}
          >
            View Features
          </Button>,
          <Button
            key="2"
            type="primary"
            onClick={() => {
              navigate(`/projects/${project}/lineage`);
            }}
          >
            View Lineage
          </Button>,
        ]}
      >
        <Spin
          spinning={isLoading}
          indicator={<LoadingOutlined style={{ fontSize: 24 }} spin />}
        >
          <Space className="display-flex" direction="vertical" size="middle">
            {error && <Alert message={error} type="error" showIcon />}
            <CardDescriptions
              mapping={SourceAttributesMap}
              descriptions={attributes}
            />
          </Space>
        </Spin>
      </PageHeader>
    </div>
  );
};

export default DataSourceDetails;

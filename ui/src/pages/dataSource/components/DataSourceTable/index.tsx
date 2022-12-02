import React, { forwardRef, useRef } from "react";
import { Button } from "antd";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";
import { DataSource } from "@/models/model";
import { fetchDataSources } from "@/api";
import ResizeTable, { ResizeColumnType } from "@/components/ResizeTable";

export interface DataSourceTableProps {
  project?: string;
}

export interface SearchModel {
  scope?: string;
  roleName?: string;
}

const DataSourceTable = (props: DataSourceTableProps, ref: any) => {
  const navigate = useNavigate();

  const { project } = props;

  const projectRef = useRef(project);

  const getDetialUrl = (guid: string) => {
    return `/projects/${projectRef.current}/dataSources/${guid}`;
  };

  const columns: ResizeColumnType<DataSource>[] = [
    {
      key: "name",
      title: "Name",
      ellipsis: true,
      width: 200,
      render: (record: DataSource) => {
        return (
          <Button
            type="link"
            onClick={() => {
              navigate(getDetialUrl(record.guid));
            }}
          >
            {record.displayText}
          </Button>
        );
      },
    },
    {
      key: "type",
      title: "Type",
      ellipsis: true,
      width: 80,
      render: (record: DataSource) => {
        return record.attributes.type;
      },
    },
    {
      key: "path",
      title: "Path",
      width: 220,
      render: (record: DataSource) => {
        return record.attributes.path;
      },
    },
    {
      key: "preprocessing",
      title: "Preprocessing",
      ellipsis: true,
      width: 190,
      render: (record: DataSource) => {
        return record.attributes.preprocessing;
      },
    },
    {
      key: "eventTimestampColumn",
      title: "Event Timestamp Column",
      ellipsis: true,
      width: 190,
      render: (record: DataSource) => {
        return record.attributes.eventTimestampColumn;
      },
    },
    {
      key: "timestampFormat",
      title: "Timestamp Format",
      ellipsis: true,
      width: 190,
      render: (record: DataSource) => {
        return record.attributes.timestampFormat;
      },
    },
    {
      title: "Action",
      fixed: "right",
      width: 130,
      resize: false,
      render: (record: DataSource) => {
        return (
          <Button
            type="primary"
            ghost
            onClick={() => {
              navigate(getDetialUrl(record.guid));
            }}
          >
            View Details
          </Button>
        );
      },
    },
  ];

  const { isLoading, data: tableData } = useQuery<DataSource[]>(
    ["dataSources", project],
    async () => {
      if (project) {
        projectRef.current = project;
        return await fetchDataSources(project);
      } else {
        return [];
      }
    },
    {
      retry: false,
      refetchOnWindowFocus: false,
    }
  );

  return (
    <ResizeTable
      rowKey="guid"
      loading={isLoading}
      columns={columns}
      dataSource={tableData}
      scroll={{ x: "100%" }}
    />
  );
};

const DataSourceTableComponent = forwardRef<unknown, DataSourceTableProps>(
  DataSourceTable
);

DataSourceTableComponent.displayName = "DataSourceTableComponent";

export default DataSourceTableComponent;

import React, { forwardRef, useRef } from "react";
import { Button } from "antd";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";
import { Feature } from "@/models/model";
import { fetchFeatures } from "@/api";
import ResizeTable, { ResizeColumnType } from "@/components/ResizeTable";

export interface DataSourceTableProps {
  project?: string;
  keyword?: string;
}

export interface SearchModel {
  scope?: string;
  roleName?: string;
}

const DataSourceTable = (props: DataSourceTableProps, ref: any) => {
  const navigate = useNavigate();

  const { project, keyword } = props;

  const projectRef = useRef(project);

  const getDetialUrl = (guid: string) => {
    return `/projects/${projectRef.current}/features/${guid}`;
  };

  const columns: ResizeColumnType<Feature>[] = [
    {
      key: "name",
      title: "Name",
      ellipsis: true,
      width: 200,
      render: (record: Feature) => {
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
      width: 120,
      render: (record: Feature) => {
        return record.typeName.replace(/feathr_|_v1/gi, "");
      },
    },
    {
      key: "transformation",
      title: "Transformation",
      width: 220,
      render: (record: Feature) => {
        const { transformExpr, defExpr } = record.attributes.transformation;
        return transformExpr || defExpr;
      },
    },
    {
      key: "entitykey",
      title: "Entity Key",
      ellipsis: true,
      width: 120,
      render: (record: Feature) => {
        const key = record.attributes.key && record.attributes.key[0];
        if ("NOT_NEEDED" !== key.keyColumn) {
          return `${key.keyColumn} (${key.keyColumnType})`;
        } else {
          return "N/A";
        }
      },
    },
    {
      key: "aggregation",
      title: "Aggregation",
      ellipsis: true,
      width: 150,
      render: (record: Feature) => {
        const { transformation } = record.attributes;
        return (
          <>
            {transformation.aggFunc && `Type: ${transformation.aggFunc}`}
            <br />
            {transformation.aggFunc && `Window: ${transformation.window}`}
          </>
        );
      },
    },
    {
      title: "Action",
      fixed: "right",
      width: 100,
      resize: false,
      render: (record: Feature) => {
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

  const { isLoading, data: tableData } = useQuery<Feature[]>(
    ["dataSources", project, keyword],
    async () => {
      if (project) {
        projectRef.current = project;
        return await fetchFeatures(project, 1, 10, keyword || "");
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

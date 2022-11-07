import React, { forwardRef } from "react";
import { Button, Space } from "antd";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";
import { Project } from "@/models/model";
import { fetchProjects } from "@/api";
import ResizeTable, { ResizeColumnType } from "@/components/ResizeTable";

export interface ProjectTableProps {
  project?: string;
}

export interface SearchModel {
  scope?: string;
  roleName?: string;
}

const ProjectTable = (props: ProjectTableProps, ref: any) => {
  const navigate = useNavigate();

  const { project } = props;

  const columns: ResizeColumnType<Project>[] = [
    {
      key: "name",
      title: "Name",
      dataIndex: "name",
      resize: false,
    },
    {
      key: "action",
      title: "Action",
      width: 130,
      resize: false,
      render: (record: Project) => {
        const { name } = record;
        return (
          <Space size="middle">
            <Button
              type="primary"
              ghost
              onClick={() => {
                navigate(`/features?project=${name}`);
              }}
            >
              View Features
            </Button>
            <Button
              type="primary"
              ghost
              onClick={() => {
                navigate(`/projects/${name}/lineage`);
              }}
            >
              View Lineage
            </Button>
          </Space>
        );
      },
    },
  ];

  const { isLoading, data: tableData } = useQuery<Project[]>(
    ["Projects", project],
    async () => {
      const reuslt = await fetchProjects();

      return reuslt.reduce((list, item: string) => {
        const text = project?.trim().toLocaleLowerCase();
        if (!text || item.includes(text)) {
          list.push({ name: item });
        }
        return list;
      }, [] as Project[]);
    },
    {
      retry: false,
      refetchOnWindowFocus: false,
    }
  );

  return (
    <ResizeTable
      rowKey="name"
      loading={isLoading}
      columns={columns}
      dataSource={tableData}
      scroll={{ x: "100%" }}
    />
  );
};

const ProjectTableComponent = forwardRef<unknown, ProjectTableProps>(
  ProjectTable
);

ProjectTableComponent.displayName = "ProjectTableComponent";

export default ProjectTableComponent;

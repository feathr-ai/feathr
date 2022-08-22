import React, { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Button, Table } from "antd";
import { Project } from "../models/model";
import { fetchProjects } from "../api";

const ProjectList = () => {
  const navigate = useNavigate();
  const columns = [
    {
      title: <div style={{ userSelect: "none" }}>Name</div>,
      key: "name",
      align: "center" as "center",
      width: 120,
      render: (row: Project) => {
        return row.name;
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 400,
          },
        };
      },
    },
    {
      title: <div>Action</div>,
      key: "name",
      width: 120,
      render: (row: Project) => {
        return (
          <Button
            type="link"
            onClick={() => {
              navigate(`/projects/${row.name}/lineage`);
            }}
          >
            View Lineage
          </Button>
        );
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120,
          },
        };
      },
    },
  ];
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [tableData, setTableData] = useState<Project[]>();

  const loadProjects = useCallback(async () => {
    setLoading(true);
    const result = await fetchProjects();
    const projects = result.map((p) => ({ name: p } as Project));
    setPage(page);
    setTableData(projects);
    setLoading(false);
  }, [page]);

  useEffect(() => {
    loadProjects();
  }, [loadProjects]);

  return (
    <div>
      <Table
        dataSource={tableData}
        columns={columns}
        rowKey={"id"}
        loading={loading}
      />
    </div>
  );
};

export default ProjectList;

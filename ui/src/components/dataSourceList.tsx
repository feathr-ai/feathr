import React, { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Form, Select, Table, Button } from "antd";
import { DataSource } from "../models/model";
import { fetchDataSources, fetchProjects } from "../api";

type Props = {
  projectProp: string;
  keywordProp: string;
};

const DataSourceList = ({ projectProp, keywordProp }: Props) => {
  const navigate = useNavigate();
  const columns = [
    {
      title: <div style={{ userSelect: "none" }}>Name</div>,
      key: "name",
      align: "center" as "center",
      width: 120,
      render: (row: DataSource) => {
        // return row.attributes.name;
        return (
          <Button
            type="link"
            onClick={() => {
              navigate(`/projects/${project}/features/${row.guid}`);
            }}
          >
            {row.displayText}
          </Button>
        )
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120,
          },
        };
      },
    },
    {
      title: <div>Type</div>,
      key: "type",
      align: "center" as "center",
      width: 80,
      render: (row: DataSource) => {
        return row.attributes.type;
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120,
          },
        };
      },
    },
    {
      title: <div>Path</div>,
      key: "path",
      align: "center" as "center",
      width: 190,
      render: (row: DataSource) => {
        return row.attributes.path;
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120,
          },
        };
      },
    },
    {
      title: <div>Pre Processing</div>,
      key: "preprocessing",
      align: "center" as "center",
      width: 190,
      render: (row: DataSource) => {
        return row.attributes.preprocessing;
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120,
          },
        };
      },
    },
    {
      title: <div>Event Timestamp Column</div>,
      key: "eventTimestampColumn",
      align: "center" as "center",
      width: 190,
      render: (row: DataSource) => {
        return row.attributes.eventTimestampColumn;
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120,
          },
        };
      },
    },
    {
      title: <div>Timestamp Format</div>,
      key: "timestampFormat",
      align: "center" as "center",
      width: 190,
      render: (row: DataSource) => {
        return row.attributes.timestampFormat;
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
  const [tableData, setTableData] = useState<DataSource[]>();
  const [projects, setProjects] = useState<any>([]);
  const [project, setProject] = useState<string>("");

  const fetchData = useCallback(
    async (project: string) => {
      setLoading(true);
      const result = await fetchDataSources(project);
      setPage(page);
      setTableData(result);
      setLoading(false);
    },
    [page]
  );

  const loadProjects = useCallback(async () => {
    const projects = await fetchProjects();
    const projectOptions = projects.map((p) => ({ value: p, label: p }));
    setProjects(projectOptions);
  }, []);

  useEffect(() => {
    loadProjects();
  }, [loadProjects]);

  const onProjectChange = async (value: string) => {
    setProject(value);
    fetchData(value);
  };

  return (
    <div>
      <Form.Item
        label="Select Project: "
        style={{ minWidth: "35%", float: "left", paddingLeft: "10px" }}
        rules={[
          {
            required: true,
            message: "Please select a project to start.",
          },
        ]}
      >
        <Select
          options={projects}
          defaultValue={project}
          value={project}
          optionFilterProp="label"
          notFoundContent={<div>No projects found from server</div>}
          showSearch={true}
          onChange={onProjectChange}
        ></Select>
      </Form.Item>
      <Table
        dataSource={tableData}
        columns={columns}
        rowKey={"id"}
        loading={loading}
      />
    </div>
  );
};

export default DataSourceList;

import React, { useCallback, useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { DownOutlined } from "@ant-design/icons";
import {
  Button,
  Dropdown,
  Input,
  Menu,
  Select,
  Tooltip,
  Form,
  Table,
} from "antd";
import { Feature } from "../models/model";
import { fetchProjects, fetchFeatures } from "../api";

const FeatureList: React.FC = () => {
  const navigate = useNavigate();
  const columns = [
    {
      title: <div>Name</div>,
      key: "name",
      width: 150,
      render: (name: string, row: Feature) => {
        return (
          <Button
            type="link"
            onClick={() => {
              navigate(`/projects/${project}/features/${row.guid}`);
            }}
          >
            {row.displayText}
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
    {
      title: <div>Type</div>,
      key: "type",
      width: 120,
      render: (name: string, row: Feature) => {
        return (
          <div>{row.typeName.replace("feathr_", "").replace("_v1", "")}</div>
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
    {
      title: <div>Transformation</div>,
      key: "transformation",
      width: 190,
      render: (name: string, row: Feature) => {
        return (
          <div>
            {row.attributes.transformation.transformExpr ??
              row.attributes.transformation.defExpr}
          </div>
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
    {
      title: <div>Aggregation</div>,
      key: "aggregation",
      width: 150,
      render: (name: string, row: Feature) => {
        return (
          <>
            <div>
              {row.attributes.transformation.aggFunc &&
                `Type: ${row.attributes.transformation.aggFunc}`}
            </div>
            <div>
              {row.attributes.transformation.aggFunc &&
                `Window: ${row.attributes.transformation.window}`}
            </div>
            <div>
              {row.attributes.transformation.aggFunc &&
                `Key: ${row.attributes.key[0].keyColumn}`}
            </div>
          </>
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
    {
      title: (
        <div>
          Action{" "}
          <Tooltip
            title={
              <Link style={{ color: "cyan" }} to="/help">
                Learn more
              </Link>
            }
          ></Tooltip>
        </div>
      ),
      key: "action",
      align: "center" as "center",
      width: 120,
      render: (name: string, row: Feature) => (
        <Dropdown
          overlay={() => {
            return (
              <Menu>
                <Menu.Item key="edit">
                  <Button
                    type="link"
                    onClick={() => {
                      navigate(`/projects/${project}/features/${row.guid}`);
                    }}
                  >
                    Edit
                  </Button>
                </Menu.Item>
              </Menu>
            );
          }}
        >
          <Button icon={<DownOutlined />}>action</Button>
        </Dropdown>
      ),
    },
  ];
  const limit = 10;
  const defaultPage = 1;
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [tableData, setTableData] = useState<Feature[]>();
  const [query, setQuery] = useState<string>("");
  const [projects, setProjects] = useState<any>([]);
  const [project, setProject] = useState<string>("");

  const fetchData = useCallback(
    async (project) => {
      setLoading(true);
      const result = await fetchFeatures(project, page, limit, query);
      setPage(page);
      setTableData(result);
      setLoading(false);
    },
    [page, query]
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

  const onKeywordChange = useCallback((value) => {
    setQuery(value);
  }, []);

  const onClickSearch = () => {
    setPage(defaultPage);
    fetchData(project);
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
      <Input
        placeholder="keyword"
        style={{ width: "10%", marginLeft: "5px" }}
        onChange={(e) => onKeywordChange(e.target.value)}
        onPressEnter={onClickSearch}
      />
      <Button
        disabled={!project}
        onClick={onClickSearch}
        type="primary"
        style={{ marginLeft: "5px" }}
      >
        Search
      </Button>
      <Table
        dataSource={tableData}
        columns={columns}
        rowKey={"id"}
        loading={loading}
      />
    </div>
  );
};

export default FeatureList;

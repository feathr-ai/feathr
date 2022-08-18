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
import { setLocalVariables } from "../utils/utils";

type Props = {
  preProject: string;
  preKeyword: string;
};

const FeatureList: React.FC<Props> = ({ preProject, preKeyword }) => {
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
      width: 80,
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
        const transformation = row.attributes.transformation;
        return (
          <div>{transformation.transformExpr ?? transformation.defExpr}</div>
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
      title: <div>Entity Key</div>,
      key: "aggregation",
      width: 80,
      render: (name: string, row: Feature) => {
        const key = row.attributes.key && row.attributes.key[0];
        if ("NOT_NEEDED" !== key.keyColumn) {
          return (
            <div>
              {key.keyColumn && `${key.keyColumn}`}{" "}
              {key.keyColumnType && `(${key.keyColumnType})`}
            </div>
          );
        } else {
          return <div>N/A</div>;
        }
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
        const transformation = row.attributes.transformation;
        return (
          <>
            <div>
              {transformation.aggFunc && `Type: ${transformation.aggFunc}`}
            </div>
            <div>
              {transformation.aggFunc && `Window: ${transformation.window}`}
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
  const [query, setQuery] = useState<string>(preKeyword);
  const [projects, setProjects] = useState<any>([]);
  const [project, setProject] = useState<string>(preProject);

  const fetchData = useCallback(
    async (project) => {
      setLoading(true);
      const result = await fetchFeatures(project, page, limit, query);
      setPage(page);
      setTableData(result);
      setLoading(false);
      setLocalVariables((preProject = project), (preKeyword = query));
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

  useEffect(() => {
    if (preProject !== "") {
      console.log("fetch data onload");
      fetchData(preProject);
    }
  }, []);

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
        value={query}
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

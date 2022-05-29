import React, { useCallback, useEffect, useState } from 'react';
import { Link, useNavigate } from "react-router-dom";
import { DownOutlined, LoadingOutlined } from '@ant-design/icons';
import { Button, Dropdown, Input, Menu, message, Popconfirm, Select, Tooltip, Form, Table } from 'antd';
import { IFeature } from "../models/model";
import { deleteFeature, fetchProjects, fetchFeatures } from "../api";

const FeatureList: React.FC = () => {
  const navigate = useNavigate();
  const columns = [
    {
      title: <div style={ { userSelect: "none" } }>Name</div>,
      dataIndex: 'name',
      key: 'name',
      width: 150,
      render: (name: string, row: IFeature) => {
        return (
          <Button type="link" onClick={ () => {
            navigate(`/projects/${ project }/features/${ row.qualifiedName }`)
          } }>{ name }</Button>
        )
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120,
          }
        }
      }
    },
    {
      title: <div style={ { userSelect: "none" } }>Qualified Name</div>,
      dataIndex: 'qualifiedName',
      key: 'qualifiedName',
      align: 'center' as 'center',
      width: 190,
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    },
    {
      title: (<div style={ { userSelect: "none" } }>Action <Tooltip
        title={ <Link style={ { color: "cyan" } } to="/help">Learn more</Link> }></Tooltip></div>),
      dataIndex: 'action',
      key: 'action',
      align: 'center' as 'center',
      width: 120,
      render: (name: string, row: IFeature) => (
        <Dropdown overlay={ () => {
          return (
            <Menu>
              <Menu.Item key="edit">
                <Button type="link" onClick={ () => {
                  navigate(`/projects/${ project }/features/${ row.qualifiedName }`)
                } }>Edit</Button>
              </Menu.Item>
              <Menu.Item key="delete">
                <Popconfirm
                  placement="left"
                  title="Are you sure to delete?"
                  onConfirm={ () => {
                    onDelete(row.id)
                  } }
                >
                  Delete
                </Popconfirm>
              </Menu.Item>
            </Menu>
          )
        } }>
          <Button icon={ <DownOutlined /> }>
            action
          </Button>
        </Dropdown>
      )
    }
  ];
  const limit = 10;
  const defaultPage = 1;
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [tableData, setTableData] = useState<IFeature[]>();
  const [query, setQuery] = useState<string>("");
  const [projects, setProjects] = useState<any>([]);
  const [project, setProject] = useState<string>("");

  const fetchData = useCallback(async (project) => {
    setLoading(true);
    const result = await fetchFeatures(project, page, limit, query);
    setPage(page);
    setTableData(result);
    setLoading(false);
  }, [page, query])

  const loadProjects = useCallback(async () => {
    const projects = await fetchProjects();
    const projectOptions = projects.map(p => ({ value: p, label: p }));
    setProjects(projectOptions);
  }, [])

  useEffect(() => {
    loadProjects();
  }, [loadProjects])

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

  const onDelete = async (id: string) => {
    setLoading(true);
    const res = await deleteFeature(id);
    if (res.status === 200) {
      message.success(`Feature ${ id } deleted`);
    } else {
      message.error("Failed to delete feature with id {id}");
    }
    setLoading(false);
    fetchData(project);
  };

  return (
    <div>
      <Form.Item label="Select Project: "
                 style={ { minWidth: "35%", float: "left", paddingLeft: "10px" } }
                 rules={ [{ required: true, message: "Please select a project to start." }] }>
        <Select options={ projects } defaultValue={ project } value={ project } optionFilterProp="label"
                notFoundContent={ <LoadingOutlined style={ { fontSize: 24 } } spin /> }
                showSearch={ true } onChange={ onProjectChange }>
        </Select>
      </Form.Item>
      <Input placeholder="keyword" style={ { width: "10%", marginLeft: "5px" } }
             onChange={ (e) => onKeywordChange(e.target.value) } onPressEnter={ fetchData } />
      <Button onClick={ onClickSearch } type="primary" style={ { marginLeft: "5px" } }>Search</Button>
      <Table
        dataSource={ tableData }
        columns={ columns }
        rowKey={ "id" }
        loading={ loading }
      />
    </div>
  );
}

export default FeatureList;

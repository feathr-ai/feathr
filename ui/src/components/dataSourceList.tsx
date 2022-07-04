import React, { useCallback, useEffect, useState } from 'react';
import { Form, Select, Table } from "antd";
import { DataSourceAttributes, DataSource } from "../models/model";
import { fetchDataSources, fetchProjects } from "../api";

const DataSourceList: React.FC = () => {
  const columns = [
    {
      title: <div style={ { userSelect: "none" } }>Name</div>,
      dataIndex: 'attributes',
      key: 'name',
      align: 'center' as 'center',
      width: 120,
      render: (row: DataSourceAttributes) => {
        return row.name;
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    },
    {
      title: <div style={ { userSelect: "none" } }>QualifiedName</div>,
      dataIndex: 'attributes',
      key: 'qualifiedName',
      align: 'center' as 'center',
      width: 120,
      render: (row: DataSourceAttributes) => {
        return row.qualifiedName;
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    },
    {
      title: <div style={ { userSelect: "none" } }>Type</div>,
      dataIndex: 'attributes',
      key: 'attributes',
      align: 'center' as 'center',
      width: 80,
      render: () => {
        return "BatchFile"
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    },
    {
      title: <div style={ { userSelect: "none" } }>Path</div>,
      dataIndex: 'attributes',
      key: 'attributes',
      align: 'center' as 'center',
      width: 190,
      render: (row: DataSourceAttributes) => {
        return row.path;
      },
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    }
  ];
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [tableData, setTableData] = useState<DataSource[]>();
  const [projects, setProjects] = useState<any>([]);
  const [project, setProject] = useState<string>("");

  const fetchData = useCallback(async (project: string) => {
    setLoading(true);
    const result = await fetchDataSources(project);
    setPage(page);
    setTableData(result);
    setLoading(false);
  }, [page])

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

  return (
    <div>
      <Form.Item label="Select Project: "
                 style={ { minWidth: "35%", float: "left", paddingLeft: "10px" } }
                 rules={ [{ required: true, message: "Please select a project to start." }] }>
        <Select options={ projects } defaultValue={ project } value={ project } optionFilterProp="label"
                notFoundContent={ <div>No projects found from server</div> }
                onChange={ onProjectChange }>
        </Select>
      </Form.Item>
      <Table
        dataSource={ tableData }
        columns={ columns }
        rowKey={ "id" }
        loading={ loading }
      />
    </div>
  );
}

export default DataSourceList;

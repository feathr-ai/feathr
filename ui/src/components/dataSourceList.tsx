import React, { useCallback, useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import { Button } from "antd";
import TableResize from './resizableTable/resizableTable';
import { DataSourceAttributes, IDataSource } from "../models/model";
import { fetchDataSources } from "../api";

const DataSourceList: React.FC = () => {
  const history = useHistory();
  const navigateTo = useCallback((location) => history.push(location), [history]);
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
    }, {
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
    },
    {
      title: <div style={ { userSelect: "none" } }>Consumers</div>,
      dataIndex: 'name',
      key: 'name',
      render: () => {
        return (
          <Button type={ "link" } onClick={ () => {
            navigateTo(`/features`)
          } }>9</Button>
        )
      },
      width: 80,
      onCell: () => {
        return {
          style: {
            maxWidth: 80,
          }
        }
      }
    }
  ];
  let [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  let [tableData, setTableData] = useState<IDataSource[]>();
  const fetchData = useCallback(
    async () => {
      setLoading(true);
      const result = await fetchDataSources();
      setPage(page);
      setTableData(result);
      setLoading(false);
    }, [page]
  );

  useEffect(() => {
    fetchData();
  }, [fetchData])

  return (
    <TableResize
      dataSource={ tableData }
      columns={ columns }
      rowKey={ "id" }
      loading={ loading }
    />
  );
}

export default DataSourceList;

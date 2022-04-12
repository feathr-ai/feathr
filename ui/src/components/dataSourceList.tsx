import React, { useCallback, useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import TableResize from './resizableTable/resizableTable';
import { IDataSource } from "../models/feature";
import { fetchDataSources } from "../api";

const DataSourceList: React.FC = () => {
  const history = useHistory();
  const navigateTo = useCallback((location) => history.push(location), [history]);
  const columns = [
    {
      title: <div style={ { userSelect: "none" } }>Name</div>,
      dataIndex: 'name',
      key: 'name',
      align: 'center' as 'center',
      width: 120,
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    }, {
      title: <div style={ { userSelect: "none" } }>Type</div>,
      dataIndex: 'event_timestamp_column',
      key: 'event_timestamp_column',
      align: 'center' as 'center',
      width: 120,
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
      dataIndex: 'path',
      key: 'path',
      align: 'center' as 'center',
      width: 190,
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    }, {
      title: <div style={ { userSelect: "none" } }>event_timestamp_column</div>,
      dataIndex: 'event_timestamp_column',
      key: 'event_timestamp_column',
      align: 'center' as 'center',
      width: 120,
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    },
    {
      title: <div style={ { userSelect: "none" } }>Features #</div>,
      dataIndex: 'name',
      key: 'name',
      render: () => {
        return (
          // eslint-disable-next-line jsx-a11y/anchor-is-valid
          <a onClick={ () => {
            navigateTo(`/features`)
          } }>3</a>
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

  useEffect(() => {
    fetchData();
  }, [])

  const fetchData = async () => {
    setLoading(true);
    const result = await fetchDataSources();
    setPage(page);
    setTableData(tableData = result);
    setLoading(false);
  }

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

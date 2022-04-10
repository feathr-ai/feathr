import React, { useCallback, useEffect, useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { Button, Dropdown, Input, Menu, message, Popconfirm, Select, Tabs, Tag, Tooltip } from 'antd';
import { DownOutlined } from '@ant-design/icons';
import TableResize from './resizableTable';
import { IFeature } from "../models/feature";
import { deleteFeature, fetchFeatures } from '../api';

const { TabPane } = Tabs;

const FeatureList: React.FC = () => {
  const history = useHistory();
  const navigateTo = useCallback((location) => history.push(location), [history]);
  const projectOptions = [
    { label: <Tag color={ "green" }>NYC Taxi</Tag>, value: "nyc" }
  ];
  const columns = [
    {
      title: <div style={ { userSelect: "none" } }>Name</div>,
      dataIndex: 'name',
      key: 'name',
      render: (name: string, row: IFeature) => {
        return (
          // eslint-disable-next-line jsx-a11y/anchor-is-valid
          <a onClick={ () => {
            navigateTo(`/feature/${ row.id }`)
          } }>{ name }</a>
        )
      },
      width: 190,
      onCell: () => {
        return {
          style: {
            maxWidth: 120,
          }
        }
      }
    }, {
      title: <div style={ { userSelect: "none" } }>Description</div>,
      dataIndex: 'description',
      key: 'description',
      align: 'center' as 'center',
      width: 150,
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    },
    {
      title: <div style={ { userSelect: "none" } }>Status</div>,
      dataIndex: 'status',
      key: 'status',
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
      title: <div style={ { userSelect: "none" } }>Feature Type</div>,
      dataIndex: 'featureType',
      key: 'featureType',
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
      title: <div style={ { userSelect: "none" } }>Data Source</div>,
      dataIndex: 'dataSource',
      key: 'dataSource',
      align: 'center' as 'center',
      render: (name: string, row: IFeature) => {
        return (
          // eslint-disable-next-line jsx-a11y/anchor-is-valid
          <a onClick={ () => {
            navigateTo(`/feature/${ row.id }`)
          } }>{ name }</a>
        )
      },
      width: 150,
      onCell: () => {
        return {
          style: {
            maxWidth: 120
          }
        }
      }
    }, {
      title: <div style={ { userSelect: "none" } }>Owners</div>,
      dataIndex: 'owners',
      key: 'owners',
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
                {/* eslint-disable-next-line jsx-a11y/anchor-is-valid */ }
                <a onClick={ () => {
                  navigateTo(`/feature/${ row.id }`)
                } }>Edit</a>
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
  let [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  let [tableData, setTableData] = useState<IFeature[]>();
  let [tabValue, setTab] = useState<string>("my");
  let [query, setQuery] = useState<string>("");

  useEffect(() => {
    console.log('useEffect in FeatureList fired');
    fetchData();
  }, [])

  const fetchData = async () => {
    setLoading(true);
    const result = await fetchFeatures(page, limit, query);
    setPage(page);
    setTableData(tableData = result);
    setLoading(false);
  }

  const onTabChange = useCallback((currentTab) => {
      setTab(tabValue = currentTab);
      fetchData(); // TODO: Load correct data when tab change fires
    },
    [],
  )


  const onKeywordChange = useCallback(
    (value) => {
      console.log('keyword change callback')
      setQuery(query = value);
    }, []
  )

  const onClickSearch = () => {
    setPage(defaultPage);
    fetchData();
  }

  const onDelete = async (id: string) => {
    setLoading(true);
    const res = await deleteFeature(id);
    if (res.status === 200) {
      message.success(`Feature ${ id } deleted`);
    } else {
      message.error("Failed to delete feature with id {id}");
    }
    setLoading(false);
    fetchData();
  }

  return (
    <div>
      <Select placeholder="Select a project" style={ { width: "18%", marginLeft: "5px" } }
              options={ projectOptions } mode="tags" showArrow />
      <Input placeholder="keyword" style={ { width: "10%", marginLeft: "5px" } }
             onChange={ (e) => onKeywordChange(e.target.value) } onPressEnter={ fetchData } />
      <Button onClick={ onClickSearch } type="primary" style={ { marginLeft: "5px" } }>Search</Button>
      <div>
        <Tabs tabBarGutter={ 80 } size="large" activeKey={ tabValue } onChange={ onTabChange }>
          <TabPane tab="My Features" key="my"></TabPane>
          <TabPane tab="All Features" key="all"></TabPane>
        </Tabs>
      </div>
      <TableResize
        dataSource={ tableData }
        columns={ columns }
        rowKey={ "id" }
        loading={ loading }
      />
    </div>
  );
}

export default FeatureList;

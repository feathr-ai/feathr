import { useEffect, useState } from 'react'

import {
  ControlOutlined,
  CopyOutlined,
  DatabaseOutlined,
  EyeOutlined,
  HomeOutlined,
  PartitionOutlined,
  ProjectOutlined,
  RocketOutlined
} from '@ant-design/icons'
import { Layout, Menu, MenuProps, Typography } from 'antd'
import { Link } from 'react-router-dom'

import { observer, useStore } from '@/hooks'

import VersionBar from './VersionBar'

import styles from './index.module.less'

type MenuItems = MenuProps['items']
export interface SiderMenuProps {
  collapsedWidth?: number
  siderWidth?: number
}

const { Title } = Typography
const { Sider } = Layout

const enableRBAC = window.environment?.enableRBAC
const showManagement = enableRBAC ? enableRBAC : process.env.REACT_APP_ENABLE_RBAC

const defaultProps = {
  collapsedWidth: 60,
  siderWidth: 200
}

const defaultMeunItems = () => {
  const items: MenuItems = [
    {
      key: 'home',
      icon: <HomeOutlined style={{ fontSize: '20px', color: '#e28743' }} />,
      label: <Link to="/">Home</Link>
    },
    {
      key: 'projects',
      icon: <ProjectOutlined style={{ fontSize: '20px', color: '#177ddc' }} />,
      label: <Link to={'/projects'}>Projects</Link>
    }
  ]

  if (showManagement === 'true') {
    items.push({
      key: 'management',
      icon: <ControlOutlined style={{ fontSize: '20px', color: '#6495ed' }} />,
      label: <Link to="/management">Management</Link>
    })
  }

  return items
}

const getMeunItems = (project: string) => {
  const items: MenuItems = [
    {
      key: 'home',
      icon: <HomeOutlined style={{ fontSize: '20px', color: '#e28743' }} />,
      label: <Link to={`/${project}`}>Home</Link>
    },
    {
      key: 'projects',
      icon: <ProjectOutlined style={{ fontSize: '20px', color: '#177ddc' }} />,
      label: <Link to={'/projects'}>Projects</Link>
    },
    {
      key: 'lineage',
      icon: <PartitionOutlined style={{ fontSize: '20px', color: '#b9038b' }} />,
      label: <Link to={`${project}/lineage`}>Lineage</Link>
    },
    {
      key: 'datasources',
      icon: <DatabaseOutlined style={{ fontSize: '20px', color: '#13a8a8' }} />,
      label: <Link to={`${project}/dataSources`}>Data Sources</Link>
    },
    {
      key: 'features',
      icon: <CopyOutlined style={{ fontSize: '20px', color: '#d89614' }} />,
      label: <Link to={`${project}/features`}>Features</Link>
    },
    {
      key: 'jobs',
      icon: <RocketOutlined style={{ fontSize: '20px', color: '#642ab5' }} />,
      label: <Link to={`${project}/jobs`}>Jobs</Link>
    },
    {
      key: 'monitoring',
      icon: <EyeOutlined style={{ fontSize: '20px', color: '#e84749' }} />,
      label: <Link to={`${project}/monitoring`}>Monitoring</Link>
    }
  ]

  if (showManagement === 'true') {
    items.push({
      key: 'management',
      icon: <ControlOutlined style={{ fontSize: '20px', color: '#6495ed' }} />,
      label: <Link to="/management">Management</Link>
    })
  }

  return items
}

const SideMenu = (props: SiderMenuProps) => {
  const { globalStore } = useStore()
  const { project, menuKeys } = globalStore

  const [menuItems, setMenuItems] = useState<MenuItems>([])

  const { siderWidth, collapsedWidth } = { ...defaultProps, ...props }

  const [collapsed] = useState<boolean>(false)

  useEffect(() => {
    if (project) {
      setMenuItems(getMeunItems(project))
    } else {
      setMenuItems(defaultMeunItems())
    }
  }, [project])

  return (
    <>
      <div
        style={{
          width: collapsed ? collapsedWidth : siderWidth,
          overflow: 'hidden',
          flex: `0 0 ${collapsed ? collapsedWidth : siderWidth}px`,
          maxWidth: collapsed ? collapsedWidth : siderWidth,
          minWidth: collapsed ? collapsedWidth : siderWidth,
          transition: 'all 0.2s ease 0s'
        }}
      />
      <Sider className={styles.siderMenu} theme="dark" width={siderWidth}>
        <Title
          style={{
            fontSize: '36px',
            color: 'white',
            margin: '10px',
            paddingLeft: '35px'
          }}
          level={1}
        >
          Feathr
        </Title>
        <Menu theme="dark" mode="inline" selectedKeys={menuKeys} items={menuItems} />

        <VersionBar className={styles.versionBar} />
      </Sider>
    </>
  )
}

export default observer(SideMenu)

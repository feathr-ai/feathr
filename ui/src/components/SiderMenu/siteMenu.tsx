import { useEffect, useState } from 'react'

import {
  ControlOutlined,
  CopyOutlined,
  DatabaseOutlined,
  EyeOutlined,
  HomeOutlined,
  ProjectOutlined,
  RocketOutlined
} from '@ant-design/icons'
import { Layout, Menu, Typography } from 'antd'
import { Link, useLocation } from 'react-router-dom'

import VersionBar from './VersionBar'

import styles from './index.module.less'

export interface SiderMenuProps {
  collapsedWidth?: number
  siderWidth?: number
}

const { Title } = Typography
const { Sider } = Layout

const menuItems = [
  {
    key: '',
    icon: <HomeOutlined style={{ fontSize: '20px', color: '#e28743' }} />,
    label: <Link to="/">Home</Link>
  },
  {
    key: 'projects',
    icon: <ProjectOutlined style={{ fontSize: '20px', color: '#177ddc' }} />,
    label: <Link to="/projects">Projects</Link>
  },
  {
    key: 'datasources',
    icon: <DatabaseOutlined style={{ fontSize: '20px', color: '#13a8a8' }} />,
    label: <Link to="/dataSources">Data Sources</Link>
  },
  {
    key: 'features',
    icon: <CopyOutlined style={{ fontSize: '20px', color: '#d89614' }} />,
    label: <Link to="/features">Features</Link>
  },
  {
    key: 'jobs',
    icon: <RocketOutlined style={{ fontSize: '20px', color: '#642ab5' }} />,
    label: <Link to="/jobs">Jobs</Link>
  },
  {
    key: 'monitoring',
    icon: <EyeOutlined style={{ fontSize: '20px', color: '#e84749' }} />,
    label: <Link to="/monitoring">Monitoring</Link>
  }
]

const enableRBAC = window.environment?.enableRBAC
const showManagement = enableRBAC ? enableRBAC : process.env.REACT_APP_ENABLE_RBAC

if (showManagement === 'true') {
  menuItems.push({
    key: 'management',
    icon: <ControlOutlined style={{ fontSize: '20px', color: '#6495ed' }} />,
    label: <Link to="/management">Management</Link>
  })
}

const getMenuKey = (pathname: string) => {
  return pathname.split('/')[1].toLocaleLowerCase()
}

const defaultProps = {
  collapsedWidth: 60,
  siderWidth: 200
}

const SideMenu = (props: SiderMenuProps) => {
  const location = useLocation()

  const { siderWidth, collapsedWidth } = { ...defaultProps, ...props }

  const [collapsed] = useState<boolean>(false)

  const [current, setcurrent] = useState<string>(getMenuKey(location.pathname))

  useEffect(() => {
    setcurrent(getMenuKey(location.pathname))
  }, [location.pathname])

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
        <Menu theme="dark" mode="inline" selectedKeys={[current]} items={menuItems} />

        <VersionBar className={styles.versionBar} />
      </Sider>
    </>
  )
}

export default SideMenu

import React from 'react'

import { UserOutlined } from '@ant-design/icons'
import { useMsal } from '@azure/msal-react'
import { Dropdown, Avatar } from 'antd'

import HeaderWidgetMenu from './headerWidgetMenu'

interface HeaderWidgetProps {
  username?: string
}

const HeaderWidget = (props: HeaderWidgetProps) => {
  const { username } = props
  const { instance } = useMsal()
  if (!username) {
    return null
  }
  return (
    <Dropdown overlay={<HeaderWidgetMenu instance={instance} />}>
      <div className="dropdown-trigger">
        <Avatar size="small" style={{ marginRight: 4 }} icon={<UserOutlined />} />
        <span>{username}</span>
      </div>
    </Dropdown>
  )
}

export default HeaderWidget

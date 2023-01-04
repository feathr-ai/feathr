import React from 'react'

import { LogoutOutlined } from '@ant-design/icons'
import { IPublicClientApplication } from '@azure/msal-browser'
import { Menu, MenuProps } from 'antd'

interface HeaderWidgetMenuProps {
  instance: IPublicClientApplication
}

const HeaderWidgetMenu = (props: HeaderWidgetMenuProps) => {
  const { instance } = props
  const menuItems = [
    {
      key: 'logout',
      icon: <LogoutOutlined />,
      label: 'Logout'
    }
  ]

  const logout = () => {
    instance.logoutRedirect().catch((e) => {
      console.error(e)
    })
  }

  const onClick: MenuProps['onClick'] = ({ key }) => {
    switch (key) {
      case 'logout':
        logout()
        break
      default:
        break
    }
  }

  return <Menu items={menuItems} onClick={onClick} />
}

export default HeaderWidgetMenu

import React, { forwardRef, useEffect, useImperativeHandle, useRef, useState } from 'react'

import { DeleteOutlined, EditOutlined } from '@ant-design/icons'
import { Popconfirm, Button, Space, Table, Form, Input, Typography } from 'antd'

import EditTable from '@/components/EditTable'
import { EditTableInstance, EditColumnType } from '@/components/EditTable/interface'

export interface AddTabsProps {
  onChange?: (tabs: Tab[]) => void
}

export interface AddTabsInstance {
  getTabs: () => Tab[]
}

export interface Tab {
  name: string
  value: string
}

const { Link } = Typography

const AddTabs = (props: AddTabsProps, ref: any) => {
  const [form] = Form.useForm()

  const { onChange } = props

  const [tabs, setTabs] = useState<Tab[]>([])

  const editTableRef = useRef<EditTableInstance>(null)

  const [editingKey, setEditingKey] = useState('')

  const isEditing = (record: Tab) => record.name === editingKey

  const updateTabs = (oldTab: Tab, newTab: Tab) => {
    const newData = [...tabs]
    const index = newData.findIndex((item) => oldTab.name === item.name)
    if (index > -1) {
      const item = newData[index]
      newData.splice(index, 1, {
        ...item,
        ...newTab
      })
      setTabs(newData)
      setEditingKey('')
    } else {
      newData.push(newTab)
      setTabs(newData)
      setEditingKey('')
    }
  }
  const onSave = (record: Tab) => {
    const { form } = editTableRef.current!
    form.validateFields().then((tab: Tab) => {
      updateTabs(record, tab)
    })
  }

  const onCancel = () => {
    setEditingKey('')
  }

  const onEdit = (record: Tab) => {
    const { form } = editTableRef.current!
    form?.setFieldsValue(record)
    setEditingKey(record.name)
  }

  const onDelete = (record: Tab) => {
    setTabs((tabs) => {
      const index = tabs.findIndex((tab) => tab.name === record.name)
      tabs.splice(index, 1)
      return [...tabs]
    })
  }

  const onAdd = () => {
    form.validateFields().then((value: Tab) => {
      updateTabs(value, value)
      form.resetFields()
    })
  }

  const columns: EditColumnType<Tab>[] = [
    {
      title: 'Name',
      dataIndex: 'name',
      editable: true
    },
    {
      title: 'Value',
      dataIndex: 'value',
      editable: true
    },
    {
      title: 'Actions',
      width: 120,
      render: (record: Tab) => {
        const editable = isEditing(record)
        return (
          <Space>
            {editable ? (
              <>
                <Link
                  onClick={() => {
                    onSave(record)
                  }}
                >
                  Save
                </Link>
                <Link onClick={onCancel}>Cancel</Link>
              </>
            ) : (
              <>
                <Link
                  title="edit"
                  onClick={() => {
                    onEdit(record)
                  }}
                >
                  <EditOutlined />
                </Link>
                <Popconfirm
                  title="Sure to delete?"
                  onConfirm={() => {
                    onDelete(record)
                  }}
                >
                  <Link title="delete">
                    <DeleteOutlined />
                  </Link>
                </Popconfirm>
              </>
            )}
          </Space>
        )
      }
    }
  ]

  useImperativeHandle<any, AddTabsInstance>(
    ref,
    () => {
      return {
        getTabs: () => {
          return tabs
        }
      }
    },
    [form]
  )

  useEffect(() => {
    onChange?.(tabs)
  }, [tabs])

  return (
    <EditTable
      ref={editTableRef}
      summary={() => (
        <Form form={form} component={false}>
          <Table.Summary>
            <Table.Summary.Row style={{ verticalAlign: 'baseline' }}>
              <Table.Summary.Cell index={1}>
                <Form.Item
                  rules={[
                    {
                      required: true
                    }
                  ]}
                  style={{ marginBottom: 0 }}
                  name="name"
                >
                  <Input />
                </Form.Item>
              </Table.Summary.Cell>
              <Table.Summary.Cell index={2}>
                <Form.Item
                  rules={[
                    {
                      required: true
                    }
                  ]}
                  style={{ marginBottom: 0 }}
                  name="value"
                >
                  <Input />
                </Form.Item>
              </Table.Summary.Cell>
              <Table.Summary.Cell index={3}>
                <Button onClick={onAdd}>Add</Button>
              </Table.Summary.Cell>
            </Table.Summary.Row>
          </Table.Summary>
        </Form>
      )}
      rowKey="name"
      isEditing={isEditing}
      columns={columns}
      pagination={false}
      dataSource={tabs}
    />
  )
}

export default forwardRef<AddTabsInstance, AddTabsProps>(AddTabs)

import React from 'react'

import { Form } from 'antd'

import ProjectsSelect from '@/components/ProjectsSelect'

export interface SearchBarProps {
  defaultProject?: string
  onSearch: (values: any) => void
}

const { Item } = Form

const SearchBar = (props: SearchBarProps) => {
  const [form] = Form.useForm()

  const { defaultProject, onSearch } = props

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        marginBottom: 16
      }}
    >
      <Form layout="inline" form={form} onFinish={onSearch}>
        <Item label="Select Project" name="project" initialValue={defaultProject}>
          <ProjectsSelect onChange={form.submit} />
        </Item>
      </Form>
    </div>
  )
}

export default SearchBar

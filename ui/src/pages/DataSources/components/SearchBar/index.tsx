import React from 'react'

import { Button, Form } from 'antd'
import { useNavigate } from 'react-router-dom'

import ProjectsSelect from '@/components/ProjectsSelect'

export interface SearchBarProps {
  defaultProject?: string
  onSearch: (values: any) => void
}

const { Item } = Form

const SearchBar = (props: SearchBarProps) => {
  const [form] = Form.useForm()

  const navigate = useNavigate()

  const { defaultProject, onSearch } = props

  const onNavigateNewSource = () => {
    const project = form.getFieldValue('project') || ''
    navigate(`/new-source?project=${project}`)
  }

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
      <Button type="primary" onClick={onNavigateNewSource}>
        + Create Source
      </Button>
    </div>
  )
}

export default SearchBar

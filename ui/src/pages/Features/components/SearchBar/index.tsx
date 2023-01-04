import React, { useRef } from 'react'

import { Form, Input, Button } from 'antd'
import { useNavigate } from 'react-router-dom'

import ProjectsSelect from '@/components/ProjectsSelect'

export interface SearchValue {
  project?: string
  keyword?: string
}

export interface SearchBarProps {
  defaultValues?: SearchValue
  onSearch?: (values: SearchValue) => void
}

const { Item } = Form

const SearchBar = (props: SearchBarProps) => {
  const [form] = Form.useForm()

  const navigate = useNavigate()

  const { defaultValues, onSearch } = props

  const timeRef = useRef<any>(null)

  const onChangeKeyword = () => {
    clearTimeout(timeRef.current)
    timeRef.current = setTimeout(() => {
      form.submit()
    }, 350)
  }

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        marginBottom: 16
      }}
    >
      <Form layout="inline" form={form} initialValues={defaultValues} onFinish={onSearch}>
        <Item label="Select Project" name="project">
          <ProjectsSelect onChange={form.submit} />
        </Item>
        <Item name="keyword">
          <Input placeholder="keyword" onChange={onChangeKeyword} />
        </Item>
      </Form>
      <Button
        type="primary"
        onClick={() => {
          navigate('/new-feature')
        }}
      >
        + Create Feature
      </Button>
    </div>
  )
}

export default SearchBar

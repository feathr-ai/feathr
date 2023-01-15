import React, { forwardRef } from 'react'

import { Button, Col, Divider, Form, Input, Row, Select } from 'antd'

import AddTabs from '@/components/AddTabs'
import ProjectsSelect from '@/components/ProjectsSelect'

import { useForm } from './useForm'

export interface SourceFormProps {
  project?: string
}

const { Item } = Form
const { TextArea } = Input

const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 8 }
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 16 }
  }
}

const SourceForm = (props: SourceFormProps, ref: any) => {
  const { project } = props
  const [form] = Form.useForm()

  const { createLoading, sourceTypeOptions, onTabsChange, onFinish } = useForm(form, project)

  return (
    <>
      <Form
        {...formItemLayout}
        labelWrap
        style={{ margin: '0 auto', maxWidth: 700 }}
        form={form}
        onFinish={onFinish}
      >
        <Item name="name" label="Name" rules={[{ required: true }]}>
          <Input maxLength={200} />
        </Item>
        <Item
          rules={[
            {
              required: true,
              message: 'Please select a project to start.'
            }
          ]}
          label="Select Project"
          name="project"
        >
          <ProjectsSelect width={'100%'} />
        </Item>
        <Item label="Type" name="type" rules={[{ required: true }]}>
          <Select options={sourceTypeOptions} />
        </Item>
        <Item name="path" label="Path" rules={[{ required: true }]}>
          <TextArea autoSize={{ maxRows: 3, minRows: 3 }} />
        </Item>
        <Item name="preprocessing" label="Preprocessing">
          <TextArea autoSize={{ maxRows: 3, minRows: 3 }} />
        </Item>
        <Item name="eventTimestampColumn" label="Event Timestamp Column">
          <Input />
        </Item>
        <Item name="timestampFormat" label="Timestamp Format">
          <Input />
        </Item>
        <Divider orientation="left">Datasource Tags</Divider>
        <Row>
          <Col xs={24} sm={{ span: 22, offset: 2 }}>
            <AddTabs onChange={onTabsChange} />
          </Col>
        </Row>
        <Divider />
        <Item label=" " colon={false}>
          <Button
            type="primary"
            htmlType="submit"
            title="submit and go back to list"
            loading={createLoading}
          >
            Submit
          </Button>
        </Item>
      </Form>
    </>
  )
}

const SourceFormComponent = forwardRef<unknown, SourceFormProps>(SourceForm)

SourceFormComponent.displayName = 'SourceFormComponent'

export default SourceFormComponent

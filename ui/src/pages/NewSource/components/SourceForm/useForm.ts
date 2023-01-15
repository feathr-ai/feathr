import { useEffect, useRef, useState } from 'react'

import { FormInstance, Form, message } from 'antd'
import { useNavigate } from 'react-router-dom'

import { createSource } from '@/api'
import { Tab } from '@/components/AddTabs'
import { SourceType } from '@/models/model'

const sourceTypeOptions = SourceType.map((value: string) => ({
  value: value,
  label: value
}))

export const useForm = (form: FormInstance<any>, projectStr?: string) => {
  const navigate = useNavigate()

  const [createLoading, setCreateLoading] = useState<boolean>(false)

  const tabsRef = useRef<Tab[]>([])

  const project = Form.useWatch('project', form)

  const onFinish = async (values: any) => {
    setCreateLoading(true)
    try {
      const tags = tabsRef.current.reduce((tags: any, item: any) => {
        tags[item.name.trim()] = item.value.trim() || ''
        return tags
      }, {} as any)

      const { data } = await createSource(project, { ...values, project: undefined, tags })

      message.success('New datasource created')
      navigate(`/projects/${project}/dataSources/${data.guid}`)
    } catch (err: any) {
      message.error(err.detail || err.message)
    } finally {
      setCreateLoading(false)
    }
  }

  const onTabsChange = (tabs: Tab[]) => {
    tabsRef.current = tabs
  }

  useEffect(() => {
    form.setFieldsValue({
      project: projectStr?.trim()
    })
  }, [form])

  return {
    createLoading,
    sourceTypeOptions,
    onTabsChange,
    onFinish
  }
}

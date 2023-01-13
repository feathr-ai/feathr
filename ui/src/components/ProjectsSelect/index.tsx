import React from 'react'

import { Select } from 'antd'
import { useQuery } from 'react-query'

import { fetchProjects } from '@/api'

export interface ProjectsSelectProps {
  width?: number | string
  defaultValue?: string
  onChange?: (value: string) => void
}

const ProjectsSelect = (props: ProjectsSelectProps) => {
  const { width = 350, defaultValue, onChange, ...restProps } = props

  const { isLoading, data: options } = useQuery<{ value: string; label: string }[]>(
    ['projectsSelect'],
    async () => {
      try {
        const result = await fetchProjects()
        return result.map((item) => ({
          value: item,
          label: item
        }))
      } catch (e) {
        return Promise.reject(e)
      }
    },
    {
      retry: false,
      refetchOnWindowFocus: false
    }
  )

  return (
    <Select
      showSearch
      style={{ width }}
      defaultValue={defaultValue}
      loading={isLoading}
      placeholder="Project Name"
      options={options}
      notFoundContent={'No projects found from server'}
      onChange={onChange}
      {...restProps}
    />
  )
}

export default ProjectsSelect

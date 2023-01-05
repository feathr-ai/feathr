import { useState } from 'react'

import { PageHeader } from 'antd'
import { useSearchParams } from 'react-router-dom'

import DataSourceTable from './components/DataSourceTable'
import SearchBar from './components/SearchBar'

const DataSources = () => {
  const [searchParams] = useSearchParams()

  const [project, setProject] = useState<string | undefined>(
    searchParams.get('project') || undefined
  )

  const onSearch = ({ project }: { project: string }) => {
    setProject(project)
  }

  return (
    <div className="page">
      <PageHeader ghost={false} title="Data Sources">
        <SearchBar defaultProject={project} onSearch={onSearch} />
        <DataSourceTable project={project} />
      </PageHeader>
    </div>
  )
}

export default DataSources

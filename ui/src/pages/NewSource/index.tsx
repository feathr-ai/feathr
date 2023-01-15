import React from 'react'

import { PageHeader } from 'antd'
import { useSearchParams } from 'react-router-dom'

import ScourceForm from './components/SourceForm'

const NewFeature = () => {
  const [searchParams] = useSearchParams()

  const project = searchParams.get('project') || ''

  return (
    <div className="page">
      <PageHeader title="Create Data Source" ghost={false}>
        <ScourceForm project={project} />
      </PageHeader>
    </div>
  )
}

export default NewFeature

import React from 'react'

import { PageHeader } from 'antd'
import { useSearchParams } from 'react-router-dom'

import FeatureForm from './components/FeatureForm'

const NewFeature = () => {
  const [searchParams] = useSearchParams()

  const project = searchParams.get('project') || ''

  return (
    <div className="page">
      <PageHeader title="Create Feature" ghost={false}>
        <FeatureForm project={project} />
      </PageHeader>
    </div>
  )
}

export default NewFeature

import React from 'react'

import { PageHeader } from 'antd'

import FeatureForm from './components/FeatureForm'

const NewFeature = () => {
  return (
    <div className="page">
      <PageHeader title="Create Feature" ghost={false}>
        <FeatureForm />
      </PageHeader>
    </div>
  )
}

export default NewFeature

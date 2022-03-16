package com.linkedin.feathr.offline.config.datasourceprovider

import com.linkedin.feathr.offline.config.datasourceprovider.provider.DataSourceProvider

trait DataSourceProviderRegistry {
  def getDataSourceProvider(name: String): DataSourceProvider
}

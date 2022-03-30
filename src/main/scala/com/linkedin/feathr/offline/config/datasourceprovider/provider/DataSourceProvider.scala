package com.linkedin.feathr.offline.config.datasourceprovider.provider

import com.linkedin.feathr.common.DateParam
import com.linkedin.feathr.offline.config.datasourceprovider.credential.Credential
import org.apache.spark.sql.{DataFrame, SparkSession}

trait DataSourceProvider {
  def load(ss: SparkSession): DataFrame
}

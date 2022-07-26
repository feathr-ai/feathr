package com.linkedin.feathr.offline.client.plugins

import com.linkedin.feathr.sparkcommon.SimpleAnchorExtractorSpark

trait DfWiseAnchorExtractorUdfAdaptor extends UdfAdaptor {
  def adaptUdf(udf: AnyRef): SimpleAnchorExtractorSpark
}

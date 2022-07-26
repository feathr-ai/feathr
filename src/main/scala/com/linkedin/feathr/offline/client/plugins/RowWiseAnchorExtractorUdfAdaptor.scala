package com.linkedin.feathr.offline.client.plugins

import com.linkedin.feathr.common.AnchorExtractor

trait RowWiseAnchorExtractorUdfAdaptor extends UdfAdaptor {
  def adaptUdf(udf: AnyRef): AnchorExtractor[_]
}

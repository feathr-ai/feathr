package com.linkedin.feathr.offline.util

import com.linkedin.feathr.offline.anchored.feature.FeatureAnchorWithSource

/**
 * Util classes and methods to handle suppressed exceptions.
 */
object SuppressedExceptionHandlerUtils {
  val MISSING_DATA_EXCEPTION = "missing_data_exception"

  // Set of features that may be missing because of missing data.
  var missingFeatures = scala.collection.mutable.Set.empty[String]
}

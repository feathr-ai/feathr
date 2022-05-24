package com.linkedin.frame.config.featureanchor;

/**
 * Represents which environment the feature anchor is defined for.
 */
public enum FeatureAnchorEnvironment {
  /**
   * Online environment, for example, where anchors with Rest.li data source are defined.
   */
  ONLINE,

  /**
   * Offline environment, for example, where anchors with Hdfs data source are defined.
   */
  OFFLINE,

  /**
   * Environment-agnostic that indicates feature anchors can be used across different environments, eg. derived feature defined in common folder.
   */
  CROSS_ENVIRONMENT
}

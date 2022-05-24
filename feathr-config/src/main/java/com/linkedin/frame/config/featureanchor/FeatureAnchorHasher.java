package com.linkedin.frame.config.featureanchor;

import com.linkedin.feathr.featureDataModel.FeatureAnchor;


/**
 * Hasher class used to generate hash code for FeatureAnchor object.
 */
public interface FeatureAnchorHasher {
  public long hash(FeatureAnchor featureAnchor);
}

package com.linkedin.frame.config.featureanchor.builder;

import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;


/**
 * AnchorBuilder builds a specific type of anchor. An anchor encloses source and transformation logic of a feature.
 * Concrete implementations are under the package, eg. {@link HdfsAnchorBuilder}.
 *
 * @see AnchorBuilderFactory for all supported anchor types.
 */
public interface AnchorBuilder {

  /**
   * Build and return the specific anchor type.
   */
  Anchor build();
}

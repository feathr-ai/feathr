package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.linkedin.data.template.UnionTemplate;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfig;

/**
 * This interface serves as the generic builder for all derived feature TransformationFunctions. TransformationFunction
 * data model is defined in a flat way as it is defined as inner class of anchors, e.g., there are
 * {@link com.linkedin.proml.mlFeatureAnchor.anchor.OnlineFeatureSourcesAnchor.TransformationFunction},
 * {@link com.linkedin.proml.mlFeatureAnchor.anchor.OfflineFeatureSourcesAnchor.TransformationFunction}. These
 * Transformation fFunctions all have similar definitions and serve for the same purpose in different anchors, so that
 * we decide to add this interface.
 */
public interface DerivedFeatureTransformationFunctionBuilder<TRANSFORMATION extends UnionTemplate> {
  /**
   * Build specific TransformationFunction.
   */
  TRANSFORMATION build(DerivationConfig derivationConfig);
}

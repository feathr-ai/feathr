package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.linkedin.data.template.UnionTemplate;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;


/**
 * This interface serves as the generic builder for all anchored feature TransformationFunctions. TransformationFunction
 * data model is defined in a flat way as it is defined as inner class of anchors, e.g., there are
 * {@link com.linkedin.proml.mlFeatureAnchor.transformation.TransformationFunctionForOnlineDataSource.TransformationFunction},
 * {@link com.linkedin.proml.mlFeatureAnchor.anchor.HdfsDataSourceAnchor.TransformationFunction}. These Transformation
 * Functions all have similar definitions and serve for the same purpose in different anchors, so that we decide to
 * add this interface.
 */
public interface AnchoredFeatureTransformationFunctionBuilder<TRANSFORMATION extends UnionTemplate> {
  /**
   * Build specific TransformationFunction.
   */
  TRANSFORMATION build(FeatureConfig featureConfig, AnchorConfig anchorConfig);
}

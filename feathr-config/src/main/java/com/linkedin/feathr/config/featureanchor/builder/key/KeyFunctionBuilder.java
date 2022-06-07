package com.linkedin.feathr.config.featureanchor.builder.key;

import com.linkedin.data.template.UnionTemplate;


/**
 * This interface serves as the generic builder for feature KeyFunction. There are two types of KeyFunctions:
 * {@link com.linkedin.proml.mlFeatureAnchor.key.OnlineDataSourceKey.KeyFunction} and
 * {@link com.linkedin.proml.mlFeatureAnchor.key.OfflineDataSourceKey.KeyFunction}. This builder will build
 * corresponding KeyFunction based on given input feature source type.
 */
public interface KeyFunctionBuilder<FUNCTION extends UnionTemplate> {
  /**
   * Build specific KeyFunction.
   */
  FUNCTION build();
}

package com.linkedin.feathr.config;

import com.linkedin.feathr.config.featureanchor.builder.transformation.SlidingWindowAggregationBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.SlidingWindowEmbeddingAggregationBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.SlidingWindowLatestAvailableBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.TransformationFunctionExpressionBuilder;
import com.linkedin.feathr.config.featureversion.FeatureVersionBuilder;
import com.linkedin.feathr.config.featureversion.builder.DefaultValueBuilder;
import com.linkedin.feathr.config.featureversion.builder.FrameFeatureTypeBuilder;
import com.linkedin.feathr.config.featureversion.builder.TensorFeatureFormatBuilderFactory;
import com.linkedin.feathr.config.featureanchor.FeatureAnchorBuilder;
import com.linkedin.feathr.config.featureanchor.builder.AnchorBuilderFactory;
import com.linkedin.feathr.core.configbuilder.ConfigBuilder;
import com.linkedin.feathr.core.utils.DerivedFeatureDependencyResolver;
import com.linkedin.feathr.core.utils.MlFeatureVersionUrnCreator;
import com.linkedin.feathr.core.utils.MvelInputsResolver;


/**
 * Factory of {@link FeatureDefinitionLoader}
 */
public class FeatureDefinitionLoaderFactory {
  private static FeatureDefinitionLoader _instance;

  private FeatureDefinitionLoaderFactory() {
  }

  /**
   * Get an instance of {@link FeatureDefinitionLoader}.
   */
  public static FeatureDefinitionLoader getInstance() {
    if (_instance == null) {
      MlFeatureVersionUrnCreator mlFeatureVersionUrnCreator = MlFeatureVersionUrnCreator.getInstance();
      MvelInputsResolver mvelInputsResolver = MvelInputsResolver.getInstance();
      TransformationFunctionExpressionBuilder transformationFunctionExpressionBuilder =
          new TransformationFunctionExpressionBuilder(SlidingWindowAggregationBuilder.getInstance(),
              SlidingWindowEmbeddingAggregationBuilder.getInstance(), SlidingWindowLatestAvailableBuilder.getInstance());
      FeatureAnchorBuilder featureAnchorBuilder = new FeatureAnchorBuilder(new AnchorBuilderFactory(
          new DerivedFeatureDependencyResolver(mlFeatureVersionUrnCreator, mvelInputsResolver),
          mlFeatureVersionUrnCreator, transformationFunctionExpressionBuilder));
      FeatureVersionBuilder featureVersionBuilder =
          new FeatureVersionBuilder(new TensorFeatureFormatBuilderFactory(), DefaultValueBuilder.getInstance(), FrameFeatureTypeBuilder
              .getInstance());
      AnchoredFeatureDefinitionLoader anchoredFeatureDefinitionLoader =
          new AnchoredFeatureDefinitionLoader(mlFeatureVersionUrnCreator, featureAnchorBuilder, featureVersionBuilder);
      DerivedFeatureDefinitionLoader derivedFeatureDefinitionLoader = new DerivedFeatureDefinitionLoader(
          mlFeatureVersionUrnCreator, featureAnchorBuilder, featureVersionBuilder);
      _instance = new FeatureDefinitionLoader(ConfigBuilder.get(), anchoredFeatureDefinitionLoader, derivedFeatureDefinitionLoader);
    }
    return _instance;
  }
}

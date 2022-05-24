package com.linkedin.frame.config;

import com.linkedin.frame.config.featureanchor.FeatureAnchorBuilder;
import com.linkedin.frame.config.featureanchor.builder.AnchorBuilderFactory;
import com.linkedin.frame.config.featureanchor.builder.transformation.SlidingWindowAggregationBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.SlidingWindowEmbeddingAggregationBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.SlidingWindowLatestAvailableBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.TransformationFunctionExpressionBuilder;
import com.linkedin.frame.config.featureversion.FeatureVersionBuilder;
import com.linkedin.frame.config.featureversion.builder.DefaultValueBuilder;
import com.linkedin.frame.config.featureversion.builder.FrameFeatureTypeBuilder;
import com.linkedin.frame.config.featureversion.builder.TensorFeatureFormatBuilderFactory;
import com.linkedin.frame.core.configbuilder.ConfigBuilder;
import com.linkedin.frame.core.utils.DerivedFeatureDependencyResolver;
import com.linkedin.frame.core.utils.MlFeatureVersionUrnCreator;
import com.linkedin.frame.core.utils.MvelInputsResolver;


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
          new FeatureVersionBuilder(new TensorFeatureFormatBuilderFactory(), DefaultValueBuilder.getInstance(), FrameFeatureTypeBuilder.getInstance());
      AnchoredFeatureDefinitionLoader anchoredFeatureDefinitionLoader =
          new AnchoredFeatureDefinitionLoader(mlFeatureVersionUrnCreator, featureAnchorBuilder, featureVersionBuilder);
      DerivedFeatureDefinitionLoader derivedFeatureDefinitionLoader = new DerivedFeatureDefinitionLoader(
          mlFeatureVersionUrnCreator, featureAnchorBuilder, featureVersionBuilder);
      _instance = new FeatureDefinitionLoader(ConfigBuilder.get(), anchoredFeatureDefinitionLoader, derivedFeatureDefinitionLoader);
    }
    return _instance;
  }
}

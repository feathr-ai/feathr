package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.DerivedFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.utils.DerivedFeatureDependencyResolver;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor;
import com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import com.linkedin.feathr.featureDataModel.FeatureSourceArray;
import java.util.List;
import javax.annotation.Nonnull;


class CrossEnvironmentFeatureSourcesAnchorBuilder extends FeatureSourcesAnchorBuilder {
  private final DerivedFeatureTransformationFunctionBuilder<TransformationFunction>
      _derivedFeatureTransformationFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  public CrossEnvironmentFeatureSourcesAnchorBuilder(
      DerivationConfig derivationConfig, DerivedFeatureDependencyResolver derivedFeatureDependencyResolver,
      @Nonnull DerivedFeatureTransformationFunctionBuilder<TransformationFunction>
          derivedFeatureTransformationFunctionBuilder, @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    super(derivationConfig, derivedFeatureDependencyResolver);
    Preconditions.checkNotNull(derivedFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _derivedFeatureTransformationFunctionBuilder = derivedFeatureTransformationFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  @Override
  public Anchor buildWith(List<FeatureSource> featureSources) {
    FeatureSourceArray featureSourceArray = new FeatureSourceArray(featureSources);
    CrossEnvironmentFeatureSourcesAnchor crossEnvironmentFeatureAnchor = new CrossEnvironmentFeatureSourcesAnchor();
    crossEnvironmentFeatureAnchor.setSource(featureSourceArray);
    crossEnvironmentFeatureAnchor.setTransformationFunction(
        _derivedFeatureTransformationFunctionBuilder.build(getDerivationConfig()));
    crossEnvironmentFeatureAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    Anchor anchor = new Anchor();
    anchor.setCrossEnvironmentFeatureSourcesAnchor(crossEnvironmentFeatureAnchor);
    return anchor;
  }
}

package com.linkedin.frame.config.featureanchor.builder;


import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.DerivedFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.utils.DerivedFeatureDependencyResolver;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import com.linkedin.feathr.featureDataModel.FeatureSourceArray;
import java.util.List;
import javax.annotation.Nonnull;


class OnlineFeatureSourcesAnchorBuilder extends FeatureSourcesAnchorBuilder {
  private final DerivedFeatureTransformationFunctionBuilder<TransformationFunction>
      _derivedFeatureTransformationFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  public OnlineFeatureSourcesAnchorBuilder(DerivationConfig derivationConfig, DerivedFeatureDependencyResolver
      derivedFeatureDependencyResolver, @Nonnull DerivedFeatureTransformationFunctionBuilder<TransformationFunction>
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
    OnlineFeatureSourcesAnchor onlineFeatureAnchor = new OnlineFeatureSourcesAnchor();
    onlineFeatureAnchor.setSource(featureSourceArray);
    onlineFeatureAnchor.setTransformationFunction(_derivedFeatureTransformationFunctionBuilder.build(
        getDerivationConfig()));
    onlineFeatureAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    Anchor anchor = new Anchor();
    anchor.setOnlineFeatureSourcesAnchor(onlineFeatureAnchor);
    return anchor;
  }
}

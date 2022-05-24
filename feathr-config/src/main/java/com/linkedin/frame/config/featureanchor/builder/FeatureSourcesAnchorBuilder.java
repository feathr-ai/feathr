package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.frame.core.utils.DerivedFeatureDependencyResolver;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import java.util.List;


/**
 * Defines common behaviors for building derived features, where an anchor's source is other features.
 */
abstract class FeatureSourcesAnchorBuilder implements AnchorBuilder {
  private final DerivationConfig _derivationConfig;
  private final DerivedFeatureDependencyResolver _derivedFeatureDependencyResolver;

  public FeatureSourcesAnchorBuilder(
      DerivationConfig derivationConfig, DerivedFeatureDependencyResolver derivedFeatureDependencyResolver) {
    Preconditions.checkArgument(!(derivationConfig instanceof SequentialJoinConfig), "Invalid derivation config type, sequential join config was passed in.");
    _derivationConfig = derivationConfig;
    _derivedFeatureDependencyResolver = derivedFeatureDependencyResolver;
  }

  public abstract Anchor buildWith(List<FeatureSource> featureSources);

  @Override
  public Anchor build() {
    List<FeatureSource> featureSources = _derivedFeatureDependencyResolver.getDependentFeatures(_derivationConfig);
    return buildWith(featureSources);
  }

  DerivationConfig getDerivationConfig() {
    return _derivationConfig;
  }
}

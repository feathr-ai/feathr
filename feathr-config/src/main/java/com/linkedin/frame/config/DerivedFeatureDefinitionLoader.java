package com.linkedin.frame.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.config.featureanchor.FeatureAnchorBuilder;
import com.linkedin.frame.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.frame.config.featureversion.FeatureVersionBuilder;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationsConfig;
import com.linkedin.frame.core.utils.MlFeatureVersionUrnCreator;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.compute.FeatureVersion;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Delegate loader class of {@link FeatureDefinitionLoader} to load derived feature definitions.
 */
public class DerivedFeatureDefinitionLoader {
  private final MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator;
  private final FeatureAnchorBuilder _featureAnchorBuilder;
  private final FeatureVersionBuilder _featureVersionBuilder;

  public DerivedFeatureDefinitionLoader(@Nonnull MlFeatureVersionUrnCreator mlFeatureVersionUrnCreator,
      @Nonnull FeatureAnchorBuilder featureAnchorBuilder, @Nonnull FeatureVersionBuilder featureVersionBuilder)  {
    Preconditions.checkNotNull(mlFeatureVersionUrnCreator);
    Preconditions.checkNotNull(featureAnchorBuilder);
    Preconditions.checkNotNull(featureVersionBuilder);
    _mlFeatureVersionUrnCreator  =  mlFeatureVersionUrnCreator;
    _featureAnchorBuilder = featureAnchorBuilder;
    _featureVersionBuilder = featureVersionBuilder;
  }
  /**
   * build feature definition for derived features
   */
  Map<MlFeatureVersionUrn, FeatureDefinition> loadDerivationFeatureDefinition(
      @Nullable DerivationsConfig derivationsConfig, @Nonnull FeatureAnchorEnvironment featureAnchorEnvironment) {
    Preconditions.checkNotNull(featureAnchorEnvironment);
    Map<String, DerivationConfig> featureNameDerivationConfigMap =
        derivationsConfig == null ? Collections.emptyMap() : derivationsConfig.getDerivations();
    Map<MlFeatureVersionUrn, FeatureDefinition> featureDefinitions = new HashMap<>();
    for (Map.Entry<String, DerivationConfig> derivationConfigEntry : featureNameDerivationConfigMap.entrySet()) {
      String featureName = derivationConfigEntry.getKey();
      DerivationConfig derivationConfig = derivationConfigEntry.getValue();
      FeatureAnchor featureAnchor =
          _featureAnchorBuilder.buildDerivationFeatureAnchor(derivationConfig, featureAnchorEnvironment);
      FeatureVersion featureVersion = _featureVersionBuilder.build(derivationConfig);
      MlFeatureVersionUrn mlFeatureVersionUrn = _mlFeatureVersionUrnCreator.create(featureName);
      FeatureDefinition featureDefinition = new FeatureDefinition(featureVersion, Sets.newHashSet(featureAnchor));
      featureDefinitions.put(mlFeatureVersionUrn, featureDefinition);
    }
    return featureDefinitions;
  }
}

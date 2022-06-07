package com.linkedin.feathr.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.feathr.core.config.producer.FeatureDefConfig;
import com.linkedin.feathr.core.config.producer.anchors.AnchorsConfig;
import com.linkedin.feathr.core.config.producer.derivations.DerivationsConfig;
import com.linkedin.feathr.core.config.producer.sources.SourcesConfig;
import com.linkedin.feathr.core.configbuilder.ConfigBuilder;
import com.linkedin.feathr.core.configdataprovider.ConfigDataProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.collections.CollectionUtils;


/**
 * Loader class for {@link FeatureDefinition}, which encloses all characteristics of a feature, such as source and
 * transformation.
 */
public class FeatureDefinitionLoader {
  private final ConfigBuilder _configBuilder;
  private final AnchoredFeatureDefinitionLoader _anchoredFeatureDefinitionLoader;
  private final DerivedFeatureDefinitionLoader _derivedFeatureDefinitionLoader;


  /**
   * Constructor.
   * @param configBuilder Interface for building {@link FeatureDefConfig} from a
   *                      HOCON-based Frame config.
   * @param anchoredFeatureDefinitionLoader sub loader class for anchored features
   * @param derivedFeatureDefinitionLoader sub loader class for derived features
   */
  public FeatureDefinitionLoader(@Nonnull ConfigBuilder configBuilder, @Nonnull AnchoredFeatureDefinitionLoader
      anchoredFeatureDefinitionLoader, @Nonnull DerivedFeatureDefinitionLoader derivedFeatureDefinitionLoader) {
    Preconditions.checkNotNull(configBuilder);
    Preconditions.checkNotNull(anchoredFeatureDefinitionLoader);
    Preconditions.checkNotNull(derivedFeatureDefinitionLoader);
    _configBuilder = configBuilder;
    _anchoredFeatureDefinitionLoader = anchoredFeatureDefinitionLoader;
    _derivedFeatureDefinitionLoader = derivedFeatureDefinitionLoader;
  }

  /**
   * Load feature definitions from frame config data provider for a specific environment. Anchored features are built
   * from the anchors section of the frame config. Derived features are built from the derivation section. These two
   * parts will be built separately and then combined.
   *
   * @param configDataProvider data provider of frame configs.
   * @param featureAnchorEnvironment the environment of the frame configs. This will be used when building
   *                                 {@link FeatureDefinition} for derived feature. For example, if online
   *                                 environment is provided, we think that all feature sources of derived features
   *                                 are also from online.
   * @return feature definitions for all the features that are included in the frame configs.
   */
  public Map<MlFeatureVersionUrn, FeatureDefinition> loadAllFeatureDefinitions(@Nonnull ConfigDataProvider
      configDataProvider, @Nonnull FeatureAnchorEnvironment featureAnchorEnvironment) {
    Preconditions.checkNotNull(configDataProvider);
    Preconditions.checkNotNull(featureAnchorEnvironment);
    Map<MlFeatureVersionUrn, FeatureDefinition> allFeatureDefinitions = new HashMap<>();
    FeatureDefConfig featureDefConfig = _configBuilder.buildFeatureDefConfig(configDataProvider);
    Optional<AnchorsConfig> anchorsConfig = featureDefConfig.getAnchorsConfig();
    Optional<DerivationsConfig> derivationsConfig = featureDefConfig.getDerivationsConfig();
    Optional<SourcesConfig> sourcesConfig = featureDefConfig.getSourcesConfig();
    allFeatureDefinitions.putAll(_anchoredFeatureDefinitionLoader.loadAnchoredFeatureDefinition(
        anchorsConfig.orElse(null), sourcesConfig.orElse(null)
    ));
    allFeatureDefinitions.putAll(_derivedFeatureDefinitionLoader.loadDerivationFeatureDefinition(
        derivationsConfig.orElse(null), featureAnchorEnvironment
    ));
    return allFeatureDefinitions;
  }

  /**
   * Get feature definitions for specific featureUrns given frame config data provider. \
   *
   * @param configDataProvider data provider of frame configs.
   * @param featureUrns urn of features to be loaded.
   * @param featureAnchorEnvironment the environment of the frame configs. This will be used when building
   *                                 {@link FeatureDefinition} for derived feature. For example, if online
   *                                 environment is provided, we think that all feature sources of derived features
   *                                 are also from online.
   * @return feature definition of the provided feature urns.
   */
  public Map<MlFeatureVersionUrn, FeatureDefinition> loadFeatureDefinitions(
      @Nonnull ConfigDataProvider configDataProvider,
      @Nonnull List<MlFeatureVersionUrn> featureUrns, @Nonnull FeatureAnchorEnvironment featureAnchorEnvironment) {
    Preconditions.checkNotNull(configDataProvider);
    Preconditions.checkNotNull(featureUrns);
    Preconditions.checkNotNull(featureAnchorEnvironment);
    Preconditions.checkArgument(CollectionUtils.isNotEmpty(featureUrns), "Input feature urns cannot be"
        + "empty");
    Map<MlFeatureVersionUrn, FeatureDefinition> allFeatureDefinitions = loadAllFeatureDefinitions(configDataProvider,
        featureAnchorEnvironment);
    Map<MlFeatureVersionUrn, FeatureDefinition> requestedFeatureDefinitions = allFeatureDefinitions.entrySet().stream()
        .filter(entry -> featureUrns.contains(entry.getKey()))
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    return ImmutableMap.copyOf(requestedFeatureDefinitions);
  }
}

package com.linkedin.feathr.config;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.linkedin.feathr.config.featureversion.FeatureVersionBuilder;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.config.featureanchor.FeatureAnchorBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.AnchorsConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.SourcesConfig;
import com.linkedin.feathr.core.utils.MlFeatureVersionUrnCreator;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.compute.FeatureVersion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Delegate loader class of {@link FeatureDefinitionLoader} to load anchored feature definitions.
 */
public class AnchoredFeatureDefinitionLoader {
  private final MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator;
  private final FeatureAnchorBuilder _featureAnchorBuilder;
  private final FeatureVersionBuilder _featureVersionBuilder;

  /**
   * Constructor.
   * @param featureAnchorBuilder builder class for {@link FeatureAnchor}
   * @param mlFeatureVersionUrnCreator creator of {@link MlFeatureVersionUrn}
   * @param featureVersionBuilder builder class for {@link FeatureVersion}
   */
  public AnchoredFeatureDefinitionLoader(@Nonnull MlFeatureVersionUrnCreator mlFeatureVersionUrnCreator,
      @Nonnull FeatureAnchorBuilder featureAnchorBuilder, @Nonnull FeatureVersionBuilder featureVersionBuilder) {
    Preconditions.checkNotNull(mlFeatureVersionUrnCreator);
    Preconditions.checkNotNull(featureAnchorBuilder);
    Preconditions.checkNotNull(featureVersionBuilder);
    _mlFeatureVersionUrnCreator = mlFeatureVersionUrnCreator;
    _featureAnchorBuilder = featureAnchorBuilder;
    _featureVersionBuilder = featureVersionBuilder;
  }

  /**
   * build feature definition for anchored features.
   */
  Map<MlFeatureVersionUrn, FeatureDefinition> loadAnchoredFeatureDefinition(@Nullable AnchorsConfig anchorsConfig,
      @Nonnull SourcesConfig sourcesConfig) {
    Map<String, List<AnchorConfig>> featureNameAnchorConfigMap = extractAnchorConfigs(anchorsConfig);
    Map<MlFeatureVersionUrn, FeatureDefinition> featureDefinitions = new HashMap<>();
    for (String featureName : featureNameAnchorConfigMap.keySet()) {
      List<AnchorConfig> anchorConfigList = featureNameAnchorConfigMap.get(featureName);
      MlFeatureVersionUrn mlFeatureVersionUrn = _mlFeatureVersionUrnCreator.create(featureName);
      Set<FeatureAnchor> featureAnchors = new HashSet<>();
      List<FeatureVersion> featureVersions = new ArrayList<>();
      for (AnchorConfig anchorConfig : anchorConfigList) {
        FeatureConfig featureConfig = anchorConfig.getFeatures().get(featureName);
        FeatureAnchor featureAnchor = _featureAnchorBuilder.buildAnchoredFeatureAnchor(sourcesConfig, anchorConfig,
            featureConfig);
        featureAnchors.add(featureAnchor);
        featureVersions.add(_featureVersionBuilder.build(featureConfig));
      }
      if (featureVersions.stream().distinct().count() > 1) {
        List<FeatureConfig> debugFeatureConfigs = new ArrayList<>();
        for (AnchorConfig anchorConfig : anchorsConfig.getAnchors().values()) {
          for (String name : anchorConfig.getFeatures().keySet()) {
            if (name.equals(featureName)) {
              debugFeatureConfigs.add(anchorConfig.getFeatures().get(name));
            }
          }
        }
        throw new IllegalArgumentException(String.format("Feature %s is defined in multiple places and the "
            + "definition is not aligned. The feature configs are %s", featureName, debugFeatureConfigs));
      }
      FeatureDefinition featureDefinition = new FeatureDefinition(featureVersions.get(0), featureAnchors);
      featureDefinitions.put(mlFeatureVersionUrn, featureDefinition);
    }
    return featureDefinitions;
  }

  /**
   * Given anchors config section of the feature config, extract a map of feature name and list of anchor configs in
   * which the specific feature is built in.
   */
  @VisibleForTesting
  Map<String, List<AnchorConfig>> extractAnchorConfigs(@Nullable AnchorsConfig anchorsConfig) {
    Map<String, AnchorConfig> anchorNameAnchorConfigMap =
        anchorsConfig == null ? Collections.emptyMap() : anchorsConfig.getAnchors();
    Map<String, List<AnchorConfig>> featureNameAnchorConfigMap = new HashMap<>();
    for (AnchorConfig anchorConfig : anchorNameAnchorConfigMap.values()) {
      //Map of <FeatureName, FeatureConfig>
      Map<String, FeatureConfig> featureConfigs = anchorConfig.getFeatures();
      for (String featureName : featureConfigs.keySet()) {
        featureNameAnchorConfigMap.putIfAbsent(featureName, new ArrayList<>());
        List<AnchorConfig> anchorConfigList = featureNameAnchorConfigMap.get(featureName);
        anchorConfigList.add(anchorConfig);
      }
    }
    return featureNameAnchorConfigMap;
  }
}

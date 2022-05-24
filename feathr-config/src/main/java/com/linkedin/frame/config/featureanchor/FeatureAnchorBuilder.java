package com.linkedin.frame.config.featureanchor;

import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.builder.AnchorBuilderFactory;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.sources.SourceConfig;
import com.linkedin.frame.core.config.producer.sources.SourcesConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 * Builder class that builds {@link FeatureAnchor} pegasus object.
 */
public class FeatureAnchorBuilder {
  private final AnchorBuilderFactory _anchorBuilderFactory;

  public FeatureAnchorBuilder(AnchorBuilderFactory anchorBuilderFactory) {
    _anchorBuilderFactory = anchorBuilderFactory;
  }
  /**
   * Build {@link FeatureAnchor} object. If the source config section is present and the anchor source reference has a
   * match in the source config section, the source information will be extracted from the source config. If source
   * config section doesn't exist, it means the source is an inline offline path location, we directly pass the path to
   * construct an Hdfs data source.
   */
  public FeatureAnchor buildAnchoredFeatureAnchor(@Nullable SourcesConfig sourcesConfig,
      @Nonnull AnchorConfig anchorConfig, @Nonnull FeatureConfig featureConfig) {
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(featureConfig);
    FeatureAnchor featureAnchor = new FeatureAnchor();
    Optional<SourceConfig> sourceConfig = Optional.ofNullable(sourcesConfig)
        .map(SourcesConfig::getSources)
        .map(sources -> sources.get(anchorConfig.getSource()));
    if (sourceConfig.isPresent()) {
      featureAnchor.setAnchor(_anchorBuilderFactory.getDataSourceAnchorBuilder(sourceConfig.get(), featureConfig,
          anchorConfig).build());
    } else {
      featureAnchor.setAnchor(_anchorBuilderFactory.
          getHdfsAnchorBuilderByInlineSource(anchorConfig.getSource(), anchorConfig, featureConfig).build());
    }
    return featureAnchor;
  }

  /**
   * Builds {@link FeatureAnchor} for derived features. The source of it is a FeatureSource.
   */
  public FeatureAnchor buildDerivationFeatureAnchor(@Nonnull DerivationConfig derivationConfig,
      FeatureAnchorEnvironment anchorEnvironment) {
    FeatureAnchor featureAnchor = new FeatureAnchor();
    featureAnchor.setAnchor(_anchorBuilderFactory.
        getFeatureSourcesAnchorBuilder(derivationConfig, anchorEnvironment).build());
    return featureAnchor;
  }
}

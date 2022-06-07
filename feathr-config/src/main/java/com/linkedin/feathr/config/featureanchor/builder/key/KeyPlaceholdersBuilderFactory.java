package com.linkedin.feathr.config.featureanchor.builder.key;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.core.config.producer.sources.PassThroughConfig;
import com.linkedin.feathr.core.config.producer.sources.SourceConfig;
import javax.annotation.Nonnull;

/**
 * Factory class that returns a specific builder for key placeholders based on source of features.
 */
public class KeyPlaceholdersBuilderFactory {
  /**
   * Get key placeholders builder for anchored feature given the feature source.
   */
  public KeyPlaceholdersBuilder getAnchoredFeatureKeyPlaceholdersBuilder(@Nonnull SourceConfig sourceConfig,
      @Nonnull AnchorConfig anchorConfig) {
    Preconditions.checkNotNull(sourceConfig);
    Preconditions.checkNotNull(anchorConfig);
    switch (sourceConfig.getSourceType()) {
      case HDFS:
        return new OfflineAnchoredFeatureKeyPlaceholdersBuilder(anchorConfig);
      case COUCHBASE:
      case ESPRESSO:
      case CUSTOM:
      case RESTLI:
      case VENICE:
      case PINOT:
        return new OnlineAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig);
      case PASSTHROUGH:
        return ((PassThroughConfig) sourceConfig).getDataModel().isPresent()
            ? null : new OfflineAnchoredFeatureKeyPlaceholdersBuilder(anchorConfig);
      case KAFKA:
      case ROCKSDB:
      default:
        throw new IllegalArgumentException(String.format("source type %s is not supported",
            sourceConfig.getSourceType().toString()));
    }
  }

  /**
   * Get key placeholders builder for HDFS feature that have inline source.
   */
  public KeyPlaceholdersBuilder getKeyPlaceholdersForInlineHdfsSource(@Nonnull AnchorConfig anchorConfig) {
    Preconditions.checkNotNull(anchorConfig);
    return new OfflineAnchoredFeatureKeyPlaceholdersBuilder(anchorConfig);
  }

  /**
   * Get key placeholders builder for derived features.
   */
  public DerivedFeatureKeyPlaceholdersBuilder getDerivedFeatureKeyPlaceholdersBuilder(
      @Nonnull DerivationConfig derivationConfig) {
    Preconditions.checkNotNull(derivationConfig);
    return new DerivedFeatureKeyPlaceholdersBuilder(derivationConfig);
  }
}

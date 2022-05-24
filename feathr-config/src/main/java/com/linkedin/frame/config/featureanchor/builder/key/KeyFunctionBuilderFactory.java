package com.linkedin.frame.config.featureanchor.builder.key;

import com.google.common.base.Preconditions;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import com.linkedin.frame.core.config.producer.sources.SourceConfig;
import javax.annotation.Nonnull;

/**
 * Factory class that returns a specific builder for key function based on source of features.
 */
public class KeyFunctionBuilderFactory {
  /**
   * Get key function builder given the feature source.
   */
  public KeyFunctionBuilder getKeyFunctionBuilder(
      @Nonnull SourceConfig sourceConfig, @Nonnull AnchorConfig anchorConfig) {
    Preconditions.checkNotNull(sourceConfig);
    Preconditions.checkNotNull(anchorConfig);
    switch (sourceConfig.getSourceType()) {
      case HDFS:
        return new OfflineKeyFunctionBuilder(anchorConfig);
      case PASSTHROUGH:
        if (((PassThroughConfig) sourceConfig).getDataModel().isPresent()) {
          //in memory pass through anchor
          return null;
        } else {
          //observation pass through anchor
          return new OfflineKeyFunctionBuilder(anchorConfig);
        }
      case COUCHBASE:
      case ESPRESSO:
      case RESTLI:
      case CUSTOM:
      case VENICE:
        return new OnlineKeyFunctionBuilder(sourceConfig);
      case PINOT:
        // PINOT does not have keyFunction concept as it is an sql engine. Data gets fetched by filtering on
        // table columns, instead.
        return null;
      case KAFKA:
      case ROCKSDB:
      default:
        throw new IllegalArgumentException(String.format("source type %s is not supported",
            sourceConfig.getSourceType().toString()));
    }
  }

  /**
   * Get key function builder for HDFS features that have inline source.
   */
  public KeyFunctionBuilder getKeyFunctionBuilderForInlineHdfsSource(@Nonnull AnchorConfig anchorConfig) {
    Preconditions.checkNotNull(anchorConfig);
    return new OfflineKeyFunctionBuilder(anchorConfig);
  }
}

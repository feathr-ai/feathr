package com.linkedin.frame.config.featureanchor.builder.key;

import com.google.common.base.Preconditions;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithExtractor;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKey;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKeyExtractor;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithOnlyMvel;
import com.linkedin.feathr.featureDataModel.KeyPlaceholder;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;


class OfflineAnchoredFeatureKeyPlaceholdersBuilder implements KeyPlaceholdersBuilder {
  private final AnchorConfig _anchorConfig;

  OfflineAnchoredFeatureKeyPlaceholdersBuilder(@Nonnull AnchorConfig anchorConfig) {
    Preconditions.checkNotNull(anchorConfig);
    _anchorConfig = anchorConfig;
  }

  /**
   * Build key placeholders from the AnchorConfig. This is only set when keyAlias is present.
   */
  @Override
  public KeyPlaceholderArray build() {
    List<String> keyAlias;
    if (_anchorConfig instanceof AnchorConfigWithKey) {
      keyAlias = ((AnchorConfigWithKey) _anchorConfig).getKeyAlias().orElse(Collections.emptyList());
    } else if (_anchorConfig instanceof AnchorConfigWithExtractor) {
      keyAlias = ((AnchorConfigWithExtractor) _anchorConfig).getKeyAlias().orElse(Collections.emptyList());
    } else if (_anchorConfig instanceof AnchorConfigWithOnlyMvel || _anchorConfig
        instanceof AnchorConfigWithKeyExtractor) {
      keyAlias = Collections.emptyList();
    } else {
      throw new IllegalArgumentException(String.format("anchor type %s is not supported", _anchorConfig.getClass()));
    }
    List<KeyPlaceholder> keyPlaceholders  = keyAlias.stream()
        .map(k -> new KeyPlaceholder().setKeyPlaceholderRef(k))
        .collect(Collectors.toList());
    return new KeyPlaceholderArray(keyPlaceholders);
  }
}

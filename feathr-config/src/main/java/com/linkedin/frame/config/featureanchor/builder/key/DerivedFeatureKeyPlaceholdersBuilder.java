package com.linkedin.frame.config.featureanchor.builder.key;

import com.google.common.base.Preconditions;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExtractor;
import com.linkedin.frame.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.frame.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.feathr.featureDataModel.KeyPlaceholder;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;


class DerivedFeatureKeyPlaceholdersBuilder implements KeyPlaceholdersBuilder {
  private final DerivationConfig _derivationConfig;

  DerivedFeatureKeyPlaceholdersBuilder(@Nonnull DerivationConfig derivationConfig) {
    Preconditions.checkNotNull(derivationConfig);
    _derivationConfig = derivationConfig;
  }

  @Override
  public KeyPlaceholderArray build() {
    List<KeyPlaceholder> keyPlaceholderList = new ArrayList<>();
    Optional<List<String>> optionalKeys;
    if (_derivationConfig instanceof DerivationConfigWithExtractor) {
      optionalKeys = Optional.ofNullable(((DerivationConfigWithExtractor) _derivationConfig).getKeys());
    } else if (_derivationConfig instanceof DerivationConfigWithExpr) {
      optionalKeys = Optional.ofNullable(((DerivationConfigWithExpr) _derivationConfig).getKeys());
    } else if (_derivationConfig instanceof SequentialJoinConfig) {
      optionalKeys = Optional.ofNullable(((SequentialJoinConfig) _derivationConfig).getKeys());
    } else if (_derivationConfig instanceof SimpleDerivationConfig) {
      optionalKeys = Optional.empty();
    } else {
      throw new IllegalArgumentException(String.format("Unsupported derivation config type %s",
          _derivationConfig.getClass()));
    }
    if (optionalKeys.isPresent()) {
      keyPlaceholderList = optionalKeys.get().stream()
          .map(key -> new KeyPlaceholder().setKeyPlaceholderRef(key)).collect(Collectors.toList());
    }
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray(keyPlaceholderList);
    return keyPlaceholders;
  }
}

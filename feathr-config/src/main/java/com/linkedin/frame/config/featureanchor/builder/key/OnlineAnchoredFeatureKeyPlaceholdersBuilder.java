package com.linkedin.frame.config.featureanchor.builder.key;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.linkedin.frame.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.frame.core.config.producer.sources.CustomSourceConfig;
import com.linkedin.frame.core.config.producer.sources.EspressoConfig;
import com.linkedin.frame.core.config.producer.sources.PinotConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.frame.core.config.producer.sources.SourceConfig;
import com.linkedin.frame.core.config.producer.sources.VeniceConfig;
import com.linkedin.feathr.featureDataModel.KeyPlaceholder;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;


class OnlineAnchoredFeatureKeyPlaceholdersBuilder implements KeyPlaceholdersBuilder {
  private final SourceConfig _sourceConfig;

  OnlineAnchoredFeatureKeyPlaceholdersBuilder(@Nonnull SourceConfig sourceConfig) {
    Preconditions.checkNotNull(sourceConfig);
    _sourceConfig = sourceConfig;
  }
  /**
   * Build key placeholders from the the keyExpr field in the online sources section.
   */
  @Override
  public KeyPlaceholderArray build() {
    if (_sourceConfig instanceof RestliConfig) {
      return new KeyPlaceholderArray(buildKeyPlaceholdersForRestli((RestliConfig) _sourceConfig));
    } else if (_sourceConfig instanceof CouchbaseConfig) {
      String keyExpr = ((CouchbaseConfig) _sourceConfig).getKeyExpr();
      return new KeyPlaceholderArray(extractOnlineKeyPlaceHoldersFromMvel(keyExpr));
    } else if (_sourceConfig instanceof EspressoConfig) {
      String keyExpr = ((EspressoConfig) _sourceConfig).getKeyExpr();
      return new KeyPlaceholderArray(extractOnlineKeyPlaceHoldersFromMvel(keyExpr));
    } else if (_sourceConfig instanceof VeniceConfig) {
      String keyExpr = ((VeniceConfig) _sourceConfig).getKeyExpr();
      return new KeyPlaceholderArray(extractOnlineKeyPlaceHoldersFromMvel(keyExpr));
    } else if (_sourceConfig instanceof CustomSourceConfig) {
      String keyExpr = ((CustomSourceConfig) _sourceConfig).getKeyExpr();
      return new KeyPlaceholderArray(extractOnlineKeyPlaceHoldersFromMvel(keyExpr));
    } else if (_sourceConfig instanceof PinotConfig) {
      return new KeyPlaceholderArray(buildKeyPlaceholdersForPinot((PinotConfig) _sourceConfig));
    } else {
      throw new IllegalArgumentException(String.format("Input source type %s is not supported. Supported types are:"
          + "Restli, Couchbase, Espresso, Venice, and Pinot", _sourceConfig.getSourceType()));
    }
  }

  /**
   * For restli feature, key placeholders can be defined in keyExpr field, in reqParams filed, or in both fields.
   * We try to extract placeholders from both fields, then combine and deduplicate the result.
   */
  @VisibleForTesting
  List<KeyPlaceholder> buildKeyPlaceholdersForRestli(RestliConfig restliConfig) {
    Optional<String> optionalKeyExpr = restliConfig.getOptionalKeyExpr();
    Optional<Map<String, Object>> optionalReqParams = restliConfig.getReqParams();
    Set<KeyPlaceholder> uniquePlaceHolders = new HashSet<>();
    optionalKeyExpr.ifPresent(keyExpr -> uniquePlaceHolders.addAll(extractOnlineKeyPlaceHoldersFromMvel(keyExpr)));
    optionalReqParams.ifPresent(reqParams -> {
      for (Object value : reqParams.values()) {
        //When the value is a mvel expression, it will be stored in String format.
        if (value instanceof String) {
          uniquePlaceHolders.addAll(extractOnlineKeyPlaceHoldersFromMvel((String) value));
        }
      }
    });
    return new ArrayList<>(uniquePlaceHolders);
  }

  /**
   * For pinot feature, key placeholders is defined in queryArguments field. For example [\"key[0]\", \"key[1]\"]
   */
  @VisibleForTesting
  List<KeyPlaceholder> buildKeyPlaceholdersForPinot(PinotConfig pinotConfig) {
    Set<KeyPlaceholder> uniquePlaceHolders = new HashSet<>();
    for (String arg : pinotConfig.getQueryArguments()) {
      uniquePlaceHolders.addAll(extractOnlineKeyPlaceHoldersFromMvel(arg));
    }
    return new ArrayList<>(uniquePlaceHolders);
  }

  /**
   * keyExpr in the feature source rely on an ordered list of key parts (key[0], key[1], ...) to construct the actual
   * key object used to query the feature data source. More details can be found in
   * https://iwww.corp.linkedin.com/wiki/cf/pages/viewpage.action?pageId=257934466 So we use regex to parse out all key
   * placeholders that is in the format of key[number].
   */
  @VisibleForTesting
  List<KeyPlaceholder> extractOnlineKeyPlaceHoldersFromMvel(String mvel) {
    Set<KeyPlaceholder> uniqueKeyPlaceholders = new HashSet<>();
    Matcher matcher = Pattern.compile("key\\[\\d+]").matcher(mvel);
    while (matcher.find()) {
      String keyRef = mvel.substring(matcher.start(), matcher.end());
      uniqueKeyPlaceholders.add(new KeyPlaceholder().setKeyPlaceholderRef(keyRef));
    }
    return new ArrayList<>(uniqueKeyPlaceholders);
  }
}

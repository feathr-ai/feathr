package com.linkedin.feathr.config.featureanchor.builder.key;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.feathr.core.config.producer.sources.CustomSourceConfig;
import com.linkedin.feathr.core.config.producer.sources.EspressoConfig;
import com.linkedin.feathr.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.core.config.producer.sources.SourceConfig;
import com.linkedin.feathr.core.config.producer.sources.VeniceConfig;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import javax.annotation.Nonnull;


class OnlineKeyFunctionBuilder implements KeyFunctionBuilder<KeyFunction> {
  private final SourceConfig _sourceConfig;

  OnlineKeyFunctionBuilder(@Nonnull SourceConfig sourceConfig) {
    Preconditions.checkNotNull(sourceConfig);
    _sourceConfig = sourceConfig;
  }
  /**
   * Key function is defined in source config via keyExpr field.
   */
  @Override
  public KeyFunction build() {
    String keyExpr;
    if (_sourceConfig instanceof RestliConfig) {
      RestliConfig restliConfig = (RestliConfig) _sourceConfig;
      keyExpr = restliConfig.getOptionalKeyExpr().orElseThrow(() -> new IllegalArgumentException(
          String.format("RestliConfig %s doesn't have a keyExpr so that the key ", restliConfig)));
    } else if (_sourceConfig instanceof CouchbaseConfig) {
      keyExpr = ((CouchbaseConfig) _sourceConfig).getKeyExpr();
    } else if (_sourceConfig instanceof EspressoConfig) {
      EspressoConfig espressoConfig = (EspressoConfig) _sourceConfig;
      keyExpr = espressoConfig.getKeyExpr();
    } else if (_sourceConfig instanceof VeniceConfig) {
      keyExpr = ((VeniceConfig) _sourceConfig).getKeyExpr();
    } else if (_sourceConfig instanceof CustomSourceConfig) {
      keyExpr = ((CustomSourceConfig) _sourceConfig).getKeyExpr();
    } else {
      throw new IllegalArgumentException(String.format("Provided invalid source type: %s. Supported types are: "
          + "Restli, Couchbase, Espresso and Venice", _sourceConfig.getSourceType()));
    }
    KeyFunction keyFunction = new KeyFunction();
    MvelExpression mvelExpression = new MvelExpression();
    mvelExpression.setMvel(keyExpr);
    keyFunction.setMvelExpression(mvelExpression);
    return keyFunction;
  }
}

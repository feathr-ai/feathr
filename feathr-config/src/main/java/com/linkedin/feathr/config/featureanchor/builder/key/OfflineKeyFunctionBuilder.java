package com.linkedin.feathr.config.featureanchor.builder.key;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.core.config.producer.ExprType;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfigWithExtractor;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfigWithKey;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfigWithKeyExtractor;
import com.linkedin.feathr.core.config.producer.anchors.TypedKey;
import com.linkedin.feathr.featureDataModel.Clazz;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction;
import javax.annotation.Nonnull;


class OfflineKeyFunctionBuilder implements KeyFunctionBuilder<KeyFunction> {
  private final AnchorConfig _anchorConfig;

  OfflineKeyFunctionBuilder(@Nonnull AnchorConfig anchorConfig) {
    Preconditions.checkNotNull(anchorConfig);
    _anchorConfig = anchorConfig;
  }

  /**
   * Build key function based on key field, extractor and key extractor of the anchor config. Following is all of the
   * combinations that can be provided in the anchor config.
   *
   * 1. Anchor has key field only. We use the HOCON string of the keys to build Mvel or Spark function.
   * 2. Anchor has extractor field only. We build UDF function.
   * 3. Anchor has keyExtractor field only. We build UDF function.
   * 4. Key field and extractor field co-exist in anchor config, it will be parsed as AnchorConfigWithKeyExtractor. We
   * favor the key field to build Mvel/Spark function..
   * 5. Key extractor field and extractor field co-exist in anchor config, it will be parsed as AnchorConfigWithExtractor.
   * We favor key extractor field to build UDF function.
   *
   * Refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction
   * for more details on key extraction.
   */
  @Override
  public KeyFunction build() {
    if (_anchorConfig instanceof AnchorConfigWithKey) {
      return buildFromAnchorConfigWithKey((AnchorConfigWithKey) _anchorConfig);
    } else if (_anchorConfig instanceof AnchorConfigWithKeyExtractor) {
      return buildFromConfigWithKeyExtractor((AnchorConfigWithKeyExtractor) _anchorConfig);
    } else if (_anchorConfig instanceof AnchorConfigWithExtractor) {
      return buildFromConfigWithExtractor((AnchorConfigWithExtractor) _anchorConfig);
    } else {
      throw new IllegalArgumentException(String.format("Anchor config %s has unsupported type %s", _anchorConfig,
          _anchorConfig.getClass()));
    }
  }

  private KeyFunction buildFromAnchorConfigWithKey(AnchorConfigWithKey anchorConfigWithKey) {
    return buildFromTypedKey(anchorConfigWithKey.getTypedKey());
  }

  /**
   * If extractor is present, we still favor the presence of key. If keys not present, we use extractor to build
   * UDF function.
   */
  private KeyFunction buildFromConfigWithExtractor(AnchorConfigWithExtractor anchorConfigWithExtractor) {
    if (anchorConfigWithExtractor.getTypedKey().isPresent()) {
      return buildFromTypedKey(anchorConfigWithExtractor.getTypedKey().get());
    } else {
      String udfClass = anchorConfigWithExtractor.getKeyExtractor().orElse(anchorConfigWithExtractor.getExtractor());
      Clazz clazz = new Clazz().setFullyQualifiedName(udfClass);
      UserDefinedFunction userDefinedFunction = new UserDefinedFunction().setClazz(clazz);
      KeyFunction keyFunction = new KeyFunction();
      keyFunction.setUserDefinedFunction(userDefinedFunction);
      return keyFunction;
    }
  }

  private KeyFunction buildFromTypedKey(TypedKey typedKey) {
    String keyEpr = typedKey.getRawKeyExpr();
    if (typedKey.getKeyExprType() == ExprType.MVEL) {
      MvelExpression mvelExpression = new MvelExpression().setMvel(keyEpr);
      KeyFunction keyFunction = new KeyFunction();
      keyFunction.setMvelExpression(mvelExpression);
      return keyFunction;
    } else if (typedKey.getKeyExprType() == ExprType.SQL) {
      SparkSqlExpression sparkSqlExpression = new SparkSqlExpression().setSql(keyEpr);
      KeyFunction keyFunction = new KeyFunction();
      keyFunction.setSparkSqlExpression(sparkSqlExpression);
      return keyFunction;
    } else {
      throw new IllegalArgumentException(String.format("Typed key %s has unsupported expression type %s",
          typedKey, typedKey.getKeyExprType()));
    }
  }

  private KeyFunction buildFromConfigWithKeyExtractor(AnchorConfigWithKeyExtractor anchorConfigWithKeyExtractor) {
    String keyExtractor = anchorConfigWithKeyExtractor.getKeyExtractor();
    Clazz clazz = new Clazz().setFullyQualifiedName(keyExtractor);
    UserDefinedFunction userDefinedFunction = new UserDefinedFunction().setClazz(clazz);
    KeyFunction keyFunction = new KeyFunction();
    keyFunction.setUserDefinedFunction(userDefinedFunction);
    return keyFunction;
  }
}

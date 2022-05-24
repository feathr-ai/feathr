package com.linkedin.frame.core.configbuilder.typesafe.producer.anchors;

import com.linkedin.frame.core.config.producer.ExprType;
import com.linkedin.frame.core.config.producer.anchors.ComplexFeatureConfig;
import com.linkedin.frame.core.config.producer.common.FeatureTypeConfig;
import com.linkedin.frame.core.configbuilder.typesafe.producer.common.FeatureTypeConfigBuilder;
import com.typesafe.config.Config;
import org.apache.log4j.Logger;

import static com.linkedin.frame.core.config.producer.anchors.FeatureConfig.*;


/**
 * Builds an ExpressionBasedFeatureConfig object
 */
class ExpressionBasedFeatureConfigBuilder {
  private final static Logger logger = Logger.getLogger(ExpressionBasedFeatureConfigBuilder.class);

  private ExpressionBasedFeatureConfigBuilder() {
  }

  public static ComplexFeatureConfig build(String featureName, Config featureConfig) {
    String expr;
    ExprType exprType;
    if (featureConfig.hasPath(DEF_SQL_EXPR)) {
      expr = featureConfig.getString(DEF_SQL_EXPR);
      exprType = ExprType.SQL;
    } else if (featureConfig.hasPath(DEF)) {
      expr = featureConfig.getString(DEF);
      exprType = ExprType.MVEL;
    } else {
      throw new RuntimeException(
          "ExpressionBasedFeatureConfig should have " + DEF_SQL_EXPR + " field or " + DEF + " field but found none in : "
              + featureConfig);
    }

    FeatureTypeConfig featureTypeConfig = FeatureTypeConfigBuilder.build(featureConfig);

    String defaultValue = featureConfig.hasPath(DEFAULT) ? featureConfig.getValue(DEFAULT).render() : null;

    ComplexFeatureConfig configObj =
        new ComplexFeatureConfig(expr, exprType, defaultValue, featureTypeConfig);
    logger.trace("Built ExpressionBasedFeatureConfig for feature" + featureName);

    return configObj;
  }
}

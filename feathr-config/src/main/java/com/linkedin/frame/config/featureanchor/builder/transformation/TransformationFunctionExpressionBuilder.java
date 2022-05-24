package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.linkedin.data.template.StringMap;
import com.linkedin.frame.core.config.TimeWindowAggregationType;
import com.linkedin.frame.core.config.producer.ExprType;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithExtractor;
import com.linkedin.frame.core.config.producer.anchors.ComplexFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.SimpleFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.TimeWindowFeatureConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExtractor;
import com.linkedin.frame.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.feathr.featureDataModel.Clazz;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import javax.annotation.Nonnull;


/**
 * This class is used to build expression in Transform functions for features.
 */

public class TransformationFunctionExpressionBuilder {
  private final SlidingWindowAggregationBuilder _slidingWindowAggregationBuilder;
  private final SlidingWindowEmbeddingAggregationBuilder _slidingWindowEmbeddingAggregationBuilder;
  private final SlidingWindowLatestAvailableBuilder _slidingWindowLatestAvailableBuilder;

  public TransformationFunctionExpressionBuilder(@Nonnull SlidingWindowAggregationBuilder slidingWindowAggregationBuilder,
      @Nonnull SlidingWindowEmbeddingAggregationBuilder slidingWindowEmbeddingAggregationBuilder,
      @Nonnull SlidingWindowLatestAvailableBuilder slidingWindowLatestAvailableBuilder) {
    _slidingWindowAggregationBuilder = slidingWindowAggregationBuilder;
    _slidingWindowEmbeddingAggregationBuilder = slidingWindowEmbeddingAggregationBuilder;
    _slidingWindowLatestAvailableBuilder = slidingWindowLatestAvailableBuilder;
  }

  /**
   * Build transform function expression for anchored features.
   *
   * Transform function can be defined in anchor config via extractor field. In this case, we will build
   * UserDefined function.
   *
   * Or it can be defined in the feature config. Feature config can have following formats:
   *
   * 1. Simple feature. In this case, the expression will be treated as a Mvel transform function.
   *
   * 2. Complex feature with SparkSql transform function. In this case, will build SparksqlExpression
   *
   * 3. Complex feature with Mvel transform function. In this case, will build MvelExpression
   *
   * 4. Time Windowed feature. For now, we will build UnspecifieldFunction
   * TODO(PROML-9734) add SWA support.
   *
   */
  Object buildTransformationExpression(FeatureConfig featureConfig, AnchorConfig anchorConfig) {
    if (anchorConfig instanceof AnchorConfigWithExtractor) {
      AnchorConfigWithExtractor anchorConfigWithExtractor = (AnchorConfigWithExtractor) anchorConfig;
      UserDefinedFunction userDefinedFunction = new UserDefinedFunction();
      Clazz clazz = new Clazz();
      clazz.setFullyQualifiedName(anchorConfigWithExtractor.getExtractor());
      userDefinedFunction.setClazz(clazz);
      userDefinedFunction.setParameters(new StringMap(featureConfig.getParameters()));
      return userDefinedFunction;
    }
    if (featureConfig instanceof ComplexFeatureConfig) {
      ComplexFeatureConfig complexFeatureConfig = (ComplexFeatureConfig) featureConfig;
      if (complexFeatureConfig.getExprType() == ExprType.MVEL) {
        MvelExpression mvelExpression = new MvelExpression();
        mvelExpression.setMvel(complexFeatureConfig.getFeatureExpr());
        return mvelExpression;
      } else if (complexFeatureConfig.getExprType() == ExprType.SQL) {
        SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
        sparkSqlExpression.setSql(complexFeatureConfig.getFeatureExpr());
        return sparkSqlExpression;
      } else {
        throw new IllegalArgumentException(String.format("Expression type %s is unsupported in feature config %s",
            complexFeatureConfig.getExprType(), featureConfig));
      }
    } else if (featureConfig instanceof SimpleFeatureConfig) {
      SimpleFeatureConfig simpleFeatureConfig = (SimpleFeatureConfig) featureConfig;
      MvelExpression mvelExpression = new MvelExpression();
      mvelExpression.setMvel(simpleFeatureConfig.getFeatureName());
      return mvelExpression;
    } else if (featureConfig instanceof TimeWindowFeatureConfig) {
      TimeWindowFeatureConfig timeWindowFeatureConfig = (TimeWindowFeatureConfig) featureConfig;
      TimeWindowAggregationType timeWindowAggregationType = ((TimeWindowFeatureConfig) featureConfig).getAggregation();
      if (SlidingWindowAggregationBuilder.isSlidingWindowAggregationType(timeWindowAggregationType)) {
        return _slidingWindowAggregationBuilder.build(timeWindowFeatureConfig, anchorConfig);
      } else if (SlidingWindowEmbeddingAggregationBuilder.isSlidingWindowEmbeddingAggregationType(
          timeWindowAggregationType)) {
        return _slidingWindowEmbeddingAggregationBuilder.build(timeWindowFeatureConfig, anchorConfig);
      } else if (SlidingWindowLatestAvailableBuilder.isSlidingWindowLatestAvailableType(timeWindowAggregationType)) {
        return _slidingWindowLatestAvailableBuilder.build(timeWindowFeatureConfig, anchorConfig);
      } else {
        throw new IllegalArgumentException("Unsupported time window aggregation type " + timeWindowAggregationType);
      }
    } else {
      throw new IllegalArgumentException(String.format("Feature config type %s is not supported in feature "
              + "config %s", featureConfig.getClass(), featureConfig));
    }
  }


  /**
  * Build transform function expression for derived features.
  *
  * For derived features, the transform function will be defined in the derivation config. The config can have following
  * formats:
  *
  * 1. Simple derived feature. In this case, the expression will be treated as a Mvel transform function.
  *
  * 2. Complex derivation with SparkSql transform function. In this case, we build SpqrkSqlExpression
  *
  * 3. Complex derivation with Mvel transform function. In this case, we build mvelExpression
  *
  * 4. Derived feature expressed with a udf (extractor class). In this ase, we build UserDefinedFunction
  *
  */
 Object buildTransformationExpression(DerivationConfig derivationConfig) {
    if (derivationConfig instanceof SimpleDerivationConfig) {
      SimpleDerivationConfig simpleDerivationConfig = (SimpleDerivationConfig) derivationConfig;
      if (simpleDerivationConfig.getFeatureTypedExpr().getExprType() == ExprType.MVEL) {
        MvelExpression mvelExpression = new MvelExpression();
        mvelExpression.setMvel(simpleDerivationConfig.getFeatureTypedExpr().getExpr());
        return mvelExpression;
      } else if (simpleDerivationConfig.getFeatureTypedExpr().getExprType() == ExprType.SQL) {
        SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
        sparkSqlExpression.setSql(simpleDerivationConfig.getFeatureTypedExpr().getExpr());
        return sparkSqlExpression;
      } else {
        throw new IllegalArgumentException(String.format("Expression type %s is not supported for derived "
                + "feature %s", simpleDerivationConfig.getFeatureTypedExpr().getExpr(), derivationConfig));
      }
    } else if (derivationConfig instanceof DerivationConfigWithExpr) {
      DerivationConfigWithExpr derivationConfigWithExpr = (DerivationConfigWithExpr) derivationConfig;
      if (derivationConfigWithExpr.getTypedDefinition().getExprType() == ExprType.MVEL) {
        MvelExpression mvelExpression = new MvelExpression();
        mvelExpression.setMvel(derivationConfigWithExpr.getTypedDefinition().getExpr());
        return mvelExpression;
      } else if (((DerivationConfigWithExpr) derivationConfig).getTypedDefinition().getExprType() == ExprType.SQL) {
        SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
        sparkSqlExpression.setSql(derivationConfigWithExpr.getTypedDefinition().getExpr());
        return sparkSqlExpression;
      } else {
        throw new IllegalArgumentException(String.format("Expression type %s is not supported for derived "
                + "feature %s", derivationConfigWithExpr.getTypedDefinition().getExpr(), derivationConfig));
      }
    } else if (derivationConfig instanceof DerivationConfigWithExtractor) {
      DerivationConfigWithExtractor derivationConfigWithExtractor = (DerivationConfigWithExtractor) derivationConfig;
      UserDefinedFunction userDefinedFunction = new UserDefinedFunction();
      Clazz clazz = new Clazz();
      clazz.setFullyQualifiedName(derivationConfigWithExtractor.getClassName());
      userDefinedFunction.setClazz(clazz);
      return userDefinedFunction;
    } else {
      throw new IllegalArgumentException(String.format("Config type %s is not supported for derived config %s",
          derivationConfig.getClass(), derivationConfig));
    }
  }
}

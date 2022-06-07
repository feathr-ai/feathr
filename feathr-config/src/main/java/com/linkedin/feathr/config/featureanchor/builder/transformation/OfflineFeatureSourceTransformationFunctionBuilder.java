package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;

import javax.annotation.Nonnull;

/**
 * Builder class for {@link TransformationFunction}.
 */
class OfflineFeatureSourceTransformationFunctionBuilder
    implements DerivedFeatureTransformationFunctionBuilder<TransformationFunction> {
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;

  OfflineFeatureSourceTransformationFunctionBuilder(@Nonnull TransformationFunctionExpressionBuilder
      transformationFunctionExpressionBuilder) {
    Preconditions.checkNotNull(transformationFunctionExpressionBuilder);
    _transformationFunctionExpressionBuilder = transformationFunctionExpressionBuilder;
  }

  @Override
  public TransformationFunction build(DerivationConfig derivationConfig) {
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfig);
    TransformationFunction transformationFunction = new TransformationFunction();
    if (expression instanceof MvelExpression) {
      transformationFunction.setMvelExpression((MvelExpression) expression);
    } else if (expression instanceof SparkSqlExpression) {
      transformationFunction.setSparkSqlExpression((SparkSqlExpression) expression);
    } else if (expression instanceof UserDefinedFunction) {
      transformationFunction.setUserDefinedFunction((UserDefinedFunction) expression);
    } else {
      throw new IllegalStateException(String.format("Wrong type of function %s is built for feature %s. Valid types "
              + "are MvelExpression, SparkSqlExpression, UserDefinedFunction and UnspecifiedTransformationFunction",
          expression.getClass(), derivationConfig));
    }
    return transformationFunction;
  }
}

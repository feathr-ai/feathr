package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import javax.annotation.Nonnull;


/**
 * Builder class for {@link TransformationFunction}.
 */
class CrossEnvironmentFeatureSourceTransformationFunctionBuilder
    implements DerivedFeatureTransformationFunctionBuilder<TransformationFunction> {
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;

  CrossEnvironmentFeatureSourceTransformationFunctionBuilder(@Nonnull TransformationFunctionExpressionBuilder
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
    } else {
      throw new IllegalStateException(String.format("Wrong type of function %s is built for feature %s. Valid types"
              + "are MvelExpression and UnspecifiedTransformationFunction", expression.getClass(), derivationConfig));
    }
    return transformationFunction;
  }
}

package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import javax.annotation.Nonnull;

/**
 * Builder class for {@link TransformationFunction}.
 */
class OnlineDataSourceTransformationFunctionBuilder
    implements AnchoredFeatureTransformationFunctionBuilder<TransformationFunction> {
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;

  OnlineDataSourceTransformationFunctionBuilder(@Nonnull TransformationFunctionExpressionBuilder
      transformationFunctionExpressionBuilder) {
    Preconditions.checkNotNull(transformationFunctionExpressionBuilder);
    _transformationFunctionExpressionBuilder = transformationFunctionExpressionBuilder;
  }

  @Override
  public TransformationFunction build(FeatureConfig featureConfig, AnchorConfig anchorConfig) {
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig,
        anchorConfig);
    TransformationFunction transformationFunction = new TransformationFunction();
    if (expression instanceof MvelExpression) {
      transformationFunction.setMvelExpression((MvelExpression) expression);
    } else if (expression instanceof UserDefinedFunction) {
      transformationFunction.setUserDefinedFunction((UserDefinedFunction) expression);
    } else {
      throw new IllegalStateException(String.format("Wrong type of function %s is built for feature %s. Valid types"
              + "are MvelExpression and UserDefinedFunction", expression.getClass(), featureConfig));
    }
    return transformationFunction;
  }
}

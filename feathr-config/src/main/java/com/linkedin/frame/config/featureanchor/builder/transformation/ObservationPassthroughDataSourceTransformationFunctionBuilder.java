package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import javax.annotation.Nonnull;

/**
 * Builder class for {@link TransformationFunction}.
 */
class ObservationPassthroughDataSourceTransformationFunctionBuilder
    implements AnchoredFeatureTransformationFunctionBuilder<TransformationFunction> {
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;

  ObservationPassthroughDataSourceTransformationFunctionBuilder(@Nonnull TransformationFunctionExpressionBuilder
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
    } else if (expression instanceof SparkSqlExpression) {
      transformationFunction.setSparkSqlExpression((SparkSqlExpression) expression);
    } else if (expression instanceof UserDefinedFunction) {
      transformationFunction.setUserDefinedFunction((UserDefinedFunction) expression);
    } else {
      throw new IllegalStateException(String.format("Wrong type of function %s is built for feature %s. Valid type "
              + "is MvelExpression", expression.getClass(), featureConfig));
    }
    return transformationFunction;
  }
}

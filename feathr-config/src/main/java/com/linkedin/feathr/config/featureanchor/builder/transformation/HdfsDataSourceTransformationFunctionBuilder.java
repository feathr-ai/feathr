package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable;
import javax.annotation.Nonnull;

/**
 * Builder class for {@link TransformationFunction}.
 */
class HdfsDataSourceTransformationFunctionBuilder
    implements AnchoredFeatureTransformationFunctionBuilder<TransformationFunction> {
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;

  HdfsDataSourceTransformationFunctionBuilder(@Nonnull TransformationFunctionExpressionBuilder
    transformationFunctionExpressionBuilder) {
    Preconditions.checkNotNull(transformationFunctionExpressionBuilder);
    _transformationFunctionExpressionBuilder = transformationFunctionExpressionBuilder;
  }

  @Override
  public TransformationFunction build(FeatureConfig featureConfig, AnchorConfig anchorConfig) {
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig);
    TransformationFunction transformationFunction = new TransformationFunction();
    if (expression instanceof MvelExpression) {
      transformationFunction.setMvelExpression((MvelExpression) expression);
    } else if (expression instanceof SparkSqlExpression) {
      transformationFunction.setSparkSqlExpression((SparkSqlExpression) expression);
    } else if (expression instanceof UserDefinedFunction) {
      transformationFunction.setUserDefinedFunction((UserDefinedFunction) expression);
    } else if (expression instanceof SlidingWindowAggregation) {
      transformationFunction.setSlidingWindowAggregation((SlidingWindowAggregation) expression);
    } else if (expression instanceof SlidingWindowEmbeddingAggregation) {
      transformationFunction.setSlidingWindowEmbeddingAggregation((SlidingWindowEmbeddingAggregation) expression);
    } else if (expression instanceof SlidingWindowLatestAvailable) {
      transformationFunction.setSlidingWindowLatestAvailable((SlidingWindowLatestAvailable) expression);
    } else {
      throw new IllegalStateException(
          String.format("Wrong type of function %s is built for feature %s.", expression.getClass(), featureConfig));
    }
    return transformationFunction;
  }

}

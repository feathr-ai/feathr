package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.feathr.core.config.producer.sources.PassThroughConfig;
import com.linkedin.feathr.core.config.producer.sources.SourceConfig;
import javax.annotation.Nonnull;


/**
 * Factory class that returns a specific builder for transformation function based on source of features.
 */
public class TransformationFunctionBuilderFactory {
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;

  public TransformationFunctionBuilderFactory(@Nonnull TransformationFunctionExpressionBuilder
      transformationFunctionExpressionBuilder) {
    Preconditions.checkNotNull(transformationFunctionExpressionBuilder);
    _transformationFunctionExpressionBuilder = transformationFunctionExpressionBuilder;
  }

  /**
   * Get transformation function builder for anchored feature given the feature source.
   */
  public AnchoredFeatureTransformationFunctionBuilder getAnchoredFeatureTransformationFunctionBuilder(
      @Nonnull SourceConfig sourceConfig) {
    Preconditions.checkNotNull(sourceConfig);
    switch (sourceConfig.getSourceType()) {
      case HDFS:
        return new HdfsDataSourceTransformationFunctionBuilder(_transformationFunctionExpressionBuilder);
      case COUCHBASE:
      case CUSTOM:
      case ESPRESSO:
      case RESTLI:
      case VENICE:
      case PINOT:
        return new OnlineDataSourceTransformationFunctionBuilder(_transformationFunctionExpressionBuilder);
      case PASSTHROUGH:
        return ((PassThroughConfig) sourceConfig).getDataModel().isPresent()
            ? new OnlineDataSourceTransformationFunctionBuilder(_transformationFunctionExpressionBuilder)
            : new ObservationPassthroughDataSourceTransformationFunctionBuilder(_transformationFunctionExpressionBuilder);
      case KAFKA:
      case ROCKSDB:
      default:
        throw new IllegalArgumentException(String.format("source type %s is not supported",
            sourceConfig.getSourceType().toString()));
    }
  }

  /**
   * Get transformation function builder for HDFS features that have inline source.
   */
  public AnchoredFeatureTransformationFunctionBuilder getTransformationFunctionBuilderForInlineHdfsSource() {
    return new HdfsDataSourceTransformationFunctionBuilder(_transformationFunctionExpressionBuilder);
  }

  /**
   * Get transformation function builder for derived feature given the feature anchor environment.
   */
  public DerivedFeatureTransformationFunctionBuilder getDerivedFeatureTransformationFunctionBuilder(
      @Nonnull FeatureAnchorEnvironment anchorEnvironment) {
    Preconditions.checkNotNull(anchorEnvironment);
    switch (anchorEnvironment) {
      case ONLINE:
        return new OnlineFeatureSourceTransformationFunctionBuilder(_transformationFunctionExpressionBuilder);
      case OFFLINE:
        return new OfflineFeatureSourceTransformationFunctionBuilder(_transformationFunctionExpressionBuilder);
      case CROSS_ENVIRONMENT:
        return new CrossEnvironmentFeatureSourceTransformationFunctionBuilder(_transformationFunctionExpressionBuilder);
      default:
        throw new IllegalArgumentException("Invalid anchor environment: " + anchorEnvironment);
    }
  }
}

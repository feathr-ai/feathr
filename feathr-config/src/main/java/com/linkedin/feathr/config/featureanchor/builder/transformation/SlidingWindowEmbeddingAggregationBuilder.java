package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.linkedin.feathr.core.config.TimeWindowAggregationType;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.EmbeddingAggregationType;
import com.linkedin.feathr.featureDataModel.LateralViewArray;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.Filter;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.GroupBy;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.TargetColumn;
import com.linkedin.feathr.featureDataModel.Window;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.checkerframework.checker.nullness.qual.NonNull;


public class SlidingWindowEmbeddingAggregationBuilder
    extends SlidingWindowOperationBuilder<SlidingWindowEmbeddingAggregation> {
  private static final Map<TimeWindowAggregationType, EmbeddingAggregationType> EMBEDDING_AGGREGATION_TYPE_MAP =
      ImmutableMap.of(TimeWindowAggregationType.AVG_POOLING, EmbeddingAggregationType.AVG_POOLING,
          TimeWindowAggregationType.MAX_POOLING, EmbeddingAggregationType.MAX_POOLING,
          TimeWindowAggregationType.MIN_POOLING, EmbeddingAggregationType.MIN_POOLING);
  private static final SlidingWindowEmbeddingAggregationBuilder INSTANCE = new
      SlidingWindowEmbeddingAggregationBuilder();

  private SlidingWindowEmbeddingAggregationBuilder() {
  }

  public static SlidingWindowEmbeddingAggregationBuilder getInstance() {
    return INSTANCE;
  }

  public static boolean isSlidingWindowEmbeddingAggregationType(TimeWindowAggregationType timeWindowAggregationType) {
    return EMBEDDING_AGGREGATION_TYPE_MAP.containsKey(timeWindowAggregationType);
  }

  @Override
  SlidingWindowEmbeddingAggregation buildSlidingWindowOperationObject(@Nullable String filterStr,
      @Nullable String groupByStr, @Nullable Integer limit, @Nonnull Window window, @NonNull String targetColumnStr,
      @Nullable LateralViewArray lateralViews, @NonNull TimeWindowAggregationType timeWindowAggregationType)  {
    Preconditions.checkNotNull(window);
    Preconditions.checkNotNull(timeWindowAggregationType);
    Preconditions.checkNotNull(targetColumnStr);
    Preconditions.checkArgument(limit == null, String.format("Trying to set limit %s to "
            + "SlidingWindowEmbeddingAggregation", limit));
    SlidingWindowEmbeddingAggregation slidingWindowEmbeddingAggregation = new SlidingWindowEmbeddingAggregation();
    if (filterStr != null) {
      SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
      sparkSqlExpression.setSql(filterStr);
      Filter filter = new Filter();
      filter.setSparkSqlExpression(sparkSqlExpression);
      slidingWindowEmbeddingAggregation.setFilter(filter);
    }
    if (groupByStr != null) {
      GroupBy groupBy = new GroupBy();
      SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
      sparkSqlExpression.setSql(groupByStr);
      groupBy.setSparkSqlExpression(sparkSqlExpression);
      slidingWindowEmbeddingAggregation.setGroupBy(groupBy);
    }
    slidingWindowEmbeddingAggregation.setWindow(window);
    TargetColumn targetColumn = new TargetColumn();
    SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
    sparkSqlExpression.setSql(targetColumnStr);
    targetColumn.setSparkSqlExpression(sparkSqlExpression);
    slidingWindowEmbeddingAggregation.setTargetColumn(targetColumn);
    EmbeddingAggregationType aggregationType = EMBEDDING_AGGREGATION_TYPE_MAP.get(timeWindowAggregationType);
    if (aggregationType == null) {
      throw new IllegalArgumentException(String.format("Unsupported aggregation type %s for "
              + "SlidingWindowEmbeddingAggregation. Supported types are: %s", timeWindowAggregationType,
          EMBEDDING_AGGREGATION_TYPE_MAP.keySet()));
    }
    slidingWindowEmbeddingAggregation.setAggregationType(aggregationType);
    return slidingWindowEmbeddingAggregation;
  }
}

package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.linkedin.frame.core.config.TimeWindowAggregationType;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.AggregationType;
import com.linkedin.feathr.featureDataModel.LateralViewArray;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.Filter;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.GroupBy;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.TargetColumn;
import com.linkedin.feathr.featureDataModel.Window;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.checkerframework.checker.nullness.qual.NonNull;


public class SlidingWindowAggregationBuilder extends SlidingWindowOperationBuilder<SlidingWindowAggregation> {
  private static final SlidingWindowAggregationBuilder INSTANCE = new SlidingWindowAggregationBuilder();

  private static final Map<TimeWindowAggregationType, AggregationType> AGGREGATION_TYPE_MAP =
      ImmutableMap.of(TimeWindowAggregationType.AVG, AggregationType.AVG,
          TimeWindowAggregationType.MIN, AggregationType.MIN,
          TimeWindowAggregationType.MAX, AggregationType.MAX,
          TimeWindowAggregationType.SUM, AggregationType.SUM,
          TimeWindowAggregationType.COUNT, AggregationType.COUNT);

  private SlidingWindowAggregationBuilder() {
  }

  public static SlidingWindowAggregationBuilder getInstance() {
    return INSTANCE;
  }

  static boolean isSlidingWindowAggregationType(TimeWindowAggregationType timeWindowAggregationType) {
    return AGGREGATION_TYPE_MAP.containsKey(timeWindowAggregationType);
  }

  @Override
  SlidingWindowAggregation buildSlidingWindowOperationObject(@Nullable String filterStr, @Nullable String groupByStr,
      @Nullable Integer limit, @Nonnull Window window, @NonNull String targetColumnStr,
      @NonNull LateralViewArray lateralViews, @NonNull TimeWindowAggregationType timeWindowAggregationType) {
    Preconditions.checkNotNull(window);
    Preconditions.checkNotNull(timeWindowAggregationType);
    Preconditions.checkNotNull(targetColumnStr);
    Preconditions.checkNotNull(lateralViews);
    SlidingWindowAggregation slidingWindowAggregation = new SlidingWindowAggregation();
    if (filterStr != null) {
      SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
      sparkSqlExpression.setSql(filterStr);
      Filter filter = new Filter();
      filter.setSparkSqlExpression(sparkSqlExpression);
      slidingWindowAggregation.setFilter(filter);
    }
    if (groupByStr != null) {
      GroupBy groupBy = new GroupBy();
      SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
      sparkSqlExpression.setSql(groupByStr);
      groupBy.setSparkSqlExpression(sparkSqlExpression);
      slidingWindowAggregation.setGroupBy(groupBy);
    }
    if (limit != null) {
      slidingWindowAggregation.setLimit(limit);
    }
    slidingWindowAggregation.setWindow(window);
    AggregationType aggregationType = AGGREGATION_TYPE_MAP.get(timeWindowAggregationType);
    if (aggregationType == null) {
      throw new IllegalArgumentException(String.format("Unsupported aggregation type %s for SlidingWindowAggregation."
          + "Supported types are %s", timeWindowAggregationType, AGGREGATION_TYPE_MAP.keySet()));
    }
    slidingWindowAggregation.setAggregationType(aggregationType);
    TargetColumn targetColumn = new TargetColumn();
    SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
    sparkSqlExpression.setSql(targetColumnStr);
    targetColumn.setSparkSqlExpression(sparkSqlExpression);
    slidingWindowAggregation.setTargetColumn(targetColumn);
    slidingWindowAggregation.setLateralViews(lateralViews);
    return slidingWindowAggregation;
  }
}

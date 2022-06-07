package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.core.config.TimeWindowAggregationType;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.LateralViewArray;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.Filter;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.GroupBy;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.TargetColumn;
import com.linkedin.feathr.featureDataModel.Window;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.checkerframework.checker.nullness.qual.NonNull;


public class SlidingWindowLatestAvailableBuilder extends SlidingWindowOperationBuilder<SlidingWindowLatestAvailable> {
  private final static SlidingWindowLatestAvailableBuilder INSTANCE = new SlidingWindowLatestAvailableBuilder();

  private SlidingWindowLatestAvailableBuilder() {
  }

  public static SlidingWindowLatestAvailableBuilder getInstance() {
    return INSTANCE;
  }

  static boolean isSlidingWindowLatestAvailableType(TimeWindowAggregationType timeWindowAggregationType) {
    return timeWindowAggregationType == TimeWindowAggregationType.LATEST;
  }

  @Override
  SlidingWindowLatestAvailable buildSlidingWindowOperationObject(@Nullable String filterStr,
      @Nullable String groupByStr, @Nullable Integer limit, @Nonnull Window window, @NonNull String targetColumnStr,
      @NonNull LateralViewArray lateralViews, @NonNull TimeWindowAggregationType timeWindowAggregationType) {
    Preconditions.checkNotNull(window);
    Preconditions.checkNotNull(targetColumnStr);
    Preconditions.checkNotNull(lateralViews);
    Preconditions.checkArgument(timeWindowAggregationType == TimeWindowAggregationType.LATEST,
        String.format("Unsupported aggregation type %s for "
            + "SlidingWindowLatestAvailable. Supported type is LATEST only", timeWindowAggregationType));
    SlidingWindowLatestAvailable slidingWindowLatestAvailable = new SlidingWindowLatestAvailable();
    if (filterStr != null) {
      SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
      sparkSqlExpression.setSql(filterStr);
      Filter filter = new Filter();
      filter.setSparkSqlExpression(sparkSqlExpression);
      slidingWindowLatestAvailable.setFilter(filter);
    }
    if (groupByStr != null) {
      GroupBy groupBy = new GroupBy();
      SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
      sparkSqlExpression.setSql(groupByStr);
      groupBy.setSparkSqlExpression(sparkSqlExpression);
      slidingWindowLatestAvailable.setGroupBy(groupBy);
    }
    if (limit != null) {
      slidingWindowLatestAvailable.setLimit(limit);
    }
    slidingWindowLatestAvailable.setWindow(window);
    TargetColumn targetColumn = new TargetColumn();
    SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
    sparkSqlExpression.setSql(targetColumnStr);
    targetColumn.setSparkSqlExpression(sparkSqlExpression);
    slidingWindowLatestAvailable.setTargetColumn(targetColumn);
    slidingWindowLatestAvailable.setLateralViews(lateralViews);
    return slidingWindowLatestAvailable;
  }
}

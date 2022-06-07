package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.linkedin.feathr.core.config.TimeWindowAggregationType;
import com.linkedin.feathr.core.config.WindowType;
import com.linkedin.feathr.core.config.producer.ExprType;
import com.linkedin.feathr.core.config.producer.TypedExpr;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfigWithKey;
import com.linkedin.feathr.core.config.producer.anchors.TimeWindowFeatureConfig;
import com.linkedin.feathr.core.config.producer.anchors.WindowParametersConfig;
import com.linkedin.feathr.featureDataModel.EmbeddingAggregationType;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation;
import com.linkedin.feathr.featureDataModel.Unit;
import java.time.Duration;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class SlidingWindowEmbeddingAggregationBuilderTest {

  @Test
  public void testBuilder() {
    SlidingWindowEmbeddingAggregationBuilder slidingWindowEmbeddingAggregationBuilder = SlidingWindowEmbeddingAggregationBuilder.getInstance();
    WindowParametersConfig windowParameters = new WindowParametersConfig(WindowType.SLIDING, Duration.ofHours(12),
        null);
    TimeWindowFeatureConfig timeWindowFeatureConfig = new TimeWindowFeatureConfig(
        new TypedExpr("testColumnExpr", ExprType.SQL),
        TimeWindowAggregationType.AVG_POOLING, windowParameters,
        new TypedExpr("testFilter", ExprType.SQL),
        "testGroupBy", null, "testDecay", null, 200);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    when(anchorConfig.getLateralViewParams()).thenReturn(Optional.empty());
    SlidingWindowEmbeddingAggregation slidingWindowEmbeddingAggregation =
        slidingWindowEmbeddingAggregationBuilder.build(timeWindowFeatureConfig, anchorConfig);

    assertTrue(slidingWindowEmbeddingAggregation.getFilter().isSparkSqlExpression());
    assertEquals(slidingWindowEmbeddingAggregation.getFilter().getSparkSqlExpression().getSql(), "testFilter");
    assertEquals(slidingWindowEmbeddingAggregation.getAggregationType(), EmbeddingAggregationType.AVG_POOLING);
    assertEquals(slidingWindowEmbeddingAggregation.getGroupBy().getSparkSqlExpression().getSql(), "testGroupBy");
    assertEquals(slidingWindowEmbeddingAggregation.getTargetColumn().getSparkSqlExpression().getSql(), "testColumnExpr");
    assertEquals(slidingWindowEmbeddingAggregation.getWindow().getUnit(), Unit.HOUR);
    assertEquals(slidingWindowEmbeddingAggregation.getWindow().getSize(), new Integer(12));
  }

  @Test
  public void testSetInvalidAggregationType() {
    SlidingWindowEmbeddingAggregationBuilder slidingWindowEmbeddingAggregationBuilder = SlidingWindowEmbeddingAggregationBuilder.getInstance();
    WindowParametersConfig windowParameters = new WindowParametersConfig(WindowType.SLIDING, Duration.ofHours(12),
        null);
    TimeWindowFeatureConfig timeWindowFeatureConfig = new TimeWindowFeatureConfig(
        new TypedExpr("testColumnExpr", ExprType.SQL),
        TimeWindowAggregationType.MAX, windowParameters,
        new TypedExpr("testFilter", ExprType.SQL),
        "testGroupBy", null, "testDecay", null, 200);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    when(anchorConfig.getLateralViewParams()).thenReturn(Optional.empty());
    Exception exception = null;
    try {
      slidingWindowEmbeddingAggregationBuilder.build(timeWindowFeatureConfig, anchorConfig);
    } catch (Exception ex) {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Unsupported aggregation type"));
  }


  @Test
  public void testSetLimit() {
    SlidingWindowEmbeddingAggregationBuilder slidingWindowEmbeddingAggregationBuilder = SlidingWindowEmbeddingAggregationBuilder.getInstance();
    WindowParametersConfig windowParameters = new WindowParametersConfig(WindowType.SLIDING, Duration.ofHours(12),
        null);
    TimeWindowFeatureConfig timeWindowFeatureConfig = new TimeWindowFeatureConfig(
        new TypedExpr("testColumnExpr", ExprType.SQL),
        TimeWindowAggregationType.AVG_POOLING, windowParameters,
        new TypedExpr("testFilter", ExprType.SQL),
        "testGroupBy", 3, "testDecay", null, 200);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    when(anchorConfig.getLateralViewParams()).thenReturn(Optional.empty());
    Exception exception = null;
    try {
      slidingWindowEmbeddingAggregationBuilder.build(timeWindowFeatureConfig, anchorConfig);
    } catch (Exception ex) {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Trying to set limit"));
  }
}

package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.linkedin.frame.config.featureanchor.builder.transformation.SlidingWindowAggregationBuilder;
import com.linkedin.frame.core.config.TimeWindowAggregationType;
import com.linkedin.frame.core.config.WindowType;
import com.linkedin.frame.core.config.producer.ExprType;
import com.linkedin.frame.core.config.producer.TypedExpr;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKey;
import com.linkedin.frame.core.config.producer.anchors.LateralViewParams;
import com.linkedin.frame.core.config.producer.anchors.TimeWindowFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.WindowParametersConfig;
import com.linkedin.feathr.featureDataModel.AggregationType;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation;
import com.linkedin.feathr.featureDataModel.Unit;
import java.time.Duration;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class SlidingWindowAggregationBuilderTest {

  @Test
  public void testBuilder() {
    SlidingWindowAggregationBuilder slidingWindowAggregationBuilder = SlidingWindowAggregationBuilder.getInstance();
    WindowParametersConfig windowParameters = new WindowParametersConfig(WindowType.SLIDING, Duration.ofHours(12),
        null);
    TimeWindowFeatureConfig timeWindowFeatureConfig = new TimeWindowFeatureConfig(
        new TypedExpr("testColumnExpr", ExprType.SQL),
        TimeWindowAggregationType.MAX, windowParameters,
        new TypedExpr("testFilter", ExprType.SQL),
        "testGroupBy", 3, "testDecay", null, 200);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    LateralViewParams lateralViewParams = new LateralViewParams("testLateralDef", "testAlias", null);
    when(anchorConfig.getLateralViewParams()).thenReturn(Optional.of(lateralViewParams));
    SlidingWindowAggregation slidingWindowAggregation = slidingWindowAggregationBuilder.build(timeWindowFeatureConfig, anchorConfig);

    assertTrue(slidingWindowAggregation.getFilter().isSparkSqlExpression());
    assertEquals(slidingWindowAggregation.getFilter().getSparkSqlExpression().getSql(), "testFilter");
    assertEquals(slidingWindowAggregation.getLimit(), new Integer(3));
    assertEquals(slidingWindowAggregation.getAggregationType(), AggregationType.MAX);
    assertEquals(slidingWindowAggregation.getGroupBy().getSparkSqlExpression().getSql(), "testGroupBy");
    assertEquals(slidingWindowAggregation.getTargetColumn().getSparkSqlExpression().getSql(), "testColumnExpr");
    assertEquals(slidingWindowAggregation.getWindow().getUnit(), Unit.HOUR);
    assertEquals(slidingWindowAggregation.getWindow().getSize(), new Integer(12));
    assertEquals(slidingWindowAggregation.getLateralViews().size(), 1);
    assertEquals(slidingWindowAggregation.getLateralViews().get(0).getVirtualTableAlias(), "testAlias");
    assertEquals(slidingWindowAggregation.getLateralViews().get(0).getTableGeneratingFunction().getSparkSqlExpression().getSql(),
        "testLateralDef");
  }

  @Test
  public void testSetInvalidAggregationType() {
    SlidingWindowAggregationBuilder slidingWindowAggregationBuilder = SlidingWindowAggregationBuilder.getInstance();
    WindowParametersConfig windowParameters = new WindowParametersConfig(WindowType.SLIDING, Duration.ofHours(12),
        null);
    TimeWindowFeatureConfig timeWindowFeatureConfig = new TimeWindowFeatureConfig(
        new TypedExpr("testColumnExpr", ExprType.SQL),
        TimeWindowAggregationType.AVG_POOLING, windowParameters,
        new TypedExpr("testFilter", ExprType.SQL),
        "testGroupBy", 3, "testDecay", null, 200);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    LateralViewParams lateralViewParams = new LateralViewParams("testLateralDef", "testAlias", null);
    when(anchorConfig.getLateralViewParams()).thenReturn(Optional.of(lateralViewParams));
    Exception exception = null;
    try {
      slidingWindowAggregationBuilder.build(timeWindowFeatureConfig, anchorConfig);
    } catch (Exception ex) {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Unsupported aggregation type"));
  }
}

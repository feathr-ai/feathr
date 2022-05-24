package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.linkedin.frame.config.featureanchor.builder.transformation.SlidingWindowLatestAvailableBuilder;
import com.linkedin.frame.core.config.TimeWindowAggregationType;
import com.linkedin.frame.core.config.WindowType;
import com.linkedin.frame.core.config.producer.ExprType;
import com.linkedin.frame.core.config.producer.TypedExpr;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKey;
import com.linkedin.frame.core.config.producer.anchors.LateralViewParams;
import com.linkedin.frame.core.config.producer.anchors.TimeWindowFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.WindowParametersConfig;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable;
import com.linkedin.feathr.featureDataModel.Unit;
import java.time.Duration;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class SlidingWindowLatestAvailableBuilderTest {
  @Test
  public void testBuilder() {
    SlidingWindowLatestAvailableBuilder slidingWindowLatestAvailableBuilder = SlidingWindowLatestAvailableBuilder.getInstance();
    WindowParametersConfig windowParameters = new WindowParametersConfig(WindowType.SLIDING, Duration.ofHours(12),
        null);
    TimeWindowFeatureConfig timeWindowFeatureConfig = new TimeWindowFeatureConfig(
        new TypedExpr("testColumnExpr", ExprType.SQL),
        TimeWindowAggregationType.LATEST, windowParameters,
        new TypedExpr("testFilter", ExprType.SQL),
        "testGroupBy", 5, "testDecay", null, 200);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    LateralViewParams lateralViewParams = new LateralViewParams("testLateralDef", "testAlias", null);
    when(anchorConfig.getLateralViewParams()).thenReturn(Optional.of(lateralViewParams));
    SlidingWindowLatestAvailable slidingWindowLatestAvailable =
        slidingWindowLatestAvailableBuilder.build(timeWindowFeatureConfig, anchorConfig);

    assertTrue(slidingWindowLatestAvailable.getFilter().isSparkSqlExpression());
    assertEquals(slidingWindowLatestAvailable.getFilter().getSparkSqlExpression().getSql(), "testFilter");
    assertEquals(slidingWindowLatestAvailable.getGroupBy().getSparkSqlExpression().getSql(), "testGroupBy");
    assertEquals(slidingWindowLatestAvailable.getLimit(), new Integer(5));
    assertEquals(slidingWindowLatestAvailable.getTargetColumn().getSparkSqlExpression().getSql(), "testColumnExpr");
    assertEquals(slidingWindowLatestAvailable.getWindow().getUnit(), Unit.HOUR);
    assertEquals(slidingWindowLatestAvailable.getWindow().getSize(), new Integer(12));
    assertEquals(slidingWindowLatestAvailable.getLateralViews().size(), 1);
    assertEquals(slidingWindowLatestAvailable.getLateralViews().get(0).getVirtualTableAlias(), "testAlias");
    assertEquals(slidingWindowLatestAvailable.getLateralViews().get(0).getTableGeneratingFunction().getSparkSqlExpression().getSql(), "testLateralDef");
  }

  @Test
  public void testSetInvalidAggregationType() {
    SlidingWindowLatestAvailableBuilder slidingWindowLatestAvailableBuilder = SlidingWindowLatestAvailableBuilder.getInstance();
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
      slidingWindowLatestAvailableBuilder.build(timeWindowFeatureConfig, anchorConfig);
    } catch (Exception ex) {
      exception = ex;
    }
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Unsupported aggregation type"));
  }
}

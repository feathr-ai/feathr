package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.linkedin.frame.config.featureanchor.builder.transformation.SlidingWindowAggregationBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.SlidingWindowOperationBuilder;
import com.linkedin.frame.core.config.producer.anchors.LateralViewParams;
import com.linkedin.feathr.featureDataModel.LateralViewArray;
import com.linkedin.feathr.featureDataModel.Unit;
import com.linkedin.feathr.featureDataModel.Window;
import java.time.Duration;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class SlidingWindowOperationBuilderTest {
  @Test(dataProvider = "windowProvider")
  public void testBuildWindow(Duration duration, Window expected) {
    SlidingWindowOperationBuilder slidingWindowOperationBuilder = SlidingWindowAggregationBuilder.getInstance();
    Window actual = slidingWindowOperationBuilder.buildWindow(duration);
    assertEquals(actual, expected);
  }

  @DataProvider(name = "windowProvider")
  public Object[][] windowProvider() {
    return new Object[][] {
        {Duration.ofNanos(10), new Window().setUnit(Unit.SECOND).setSize(0)},
        {Duration.ofSeconds(50), new Window().setUnit(Unit.SECOND).setSize(50)},
        {Duration.ofSeconds(60), new Window().setUnit(Unit.MINUTE).setSize(1)},
        {Duration.ofSeconds(80), new Window().setUnit(Unit.SECOND).setSize(80)},
        {Duration.ofSeconds(120), new Window().setUnit(Unit.MINUTE).setSize(2)},
        {Duration.ofSeconds(3600), new Window().setUnit(Unit.HOUR).setSize(1)},
        {Duration.ofSeconds(3660), new Window().setUnit(Unit.MINUTE).setSize(61)},
        {Duration.ofSeconds(3661), new Window().setUnit(Unit.SECOND).setSize(3661)},
        {Duration.ofSeconds(86400), new Window().setUnit(Unit.DAY).setSize(1)},
        {Duration.ofSeconds(86460), new Window().setUnit(Unit.MINUTE).setSize(1441)},
        {Duration.ofSeconds(86461), new Window().setUnit(Unit.SECOND).setSize(86461)},
        {Duration.ofSeconds(90000), new Window().setUnit(Unit.HOUR).setSize(25)},
    };
  }

  @Test
  public void testBuildLateralViewArray() {
    SlidingWindowOperationBuilder slidingWindowOperationBuilder = SlidingWindowAggregationBuilder.getInstance();
    LateralViewParams lateralViewParams = new LateralViewParams("testLateralDef", "testAlias", "testFilter");
    LateralViewArray actual = slidingWindowOperationBuilder.buildLateralViews(lateralViewParams);
    assertEquals(actual.size(), 1);
    assertEquals(actual.get(0).getVirtualTableAlias(), "testAlias");
    assertEquals(actual.get(0).getTableGeneratingFunction().getSparkSqlExpression().getSql(), "testLateralDef");
  }
}

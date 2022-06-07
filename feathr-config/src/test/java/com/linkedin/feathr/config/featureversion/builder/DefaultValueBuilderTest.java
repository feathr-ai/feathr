package com.linkedin.feathr.config.featureversion.builder;

import com.linkedin.feathr.compute.FeatureValue;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;


public class DefaultValueBuilderTest {

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUnsupportedType() {
    DefaultValueBuilder defaultValueBuilder = DefaultValueBuilder.getInstance();
    defaultValueBuilder.build(0);
  }

  @Test
  public void testString() {
    DefaultValueBuilder defaultValueBuilder = DefaultValueBuilder.getInstance();
    FeatureValue actual = defaultValueBuilder.build("test");
    assertEquals(actual.getString(), "test");
  }
}

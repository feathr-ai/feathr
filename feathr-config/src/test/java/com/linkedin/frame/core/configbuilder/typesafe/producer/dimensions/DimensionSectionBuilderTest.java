package com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.frame.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions.DimensionsFixture.*;
import static org.testng.Assert.*;

public class DimensionSectionBuilderTest extends AbstractConfigBuilderTest {

  @Test(description = "Tests build of dimension section with multiple dimension types")
  public void testWithMultiDimTypes() {
    testConfigBuilder(dimSectionStr, DimensionSectionBuilder::build, expDimSection);
  }

  @Test(description = "Tests build of dimension section with special chars in namespace and dimension name")
  public void testWithSpecialChars() {
    testConfigBuilder(dimSectionWithSpecialCharsStr, DimensionSectionBuilder::build, expDimSectionWithSpecialChars);
  }
}

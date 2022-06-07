package com.linkedin.feathr.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.feathr.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import org.testng.annotations.Test;


public class DimensionSectionBuilderTest extends AbstractConfigBuilderTest {

  @Test(description = "Tests build of dimension section with multiple dimension types")
  public void testWithMultiDimTypes() {
    testConfigBuilder(DimensionsFixture.dimSectionStr, DimensionSectionBuilder::build, DimensionsFixture.expDimSection);
  }

  @Test(description = "Tests build of dimension section with special chars in namespace and dimension name")
  public void testWithSpecialChars() {
    testConfigBuilder(
        DimensionsFixture.dimSectionWithSpecialCharsStr, DimensionSectionBuilder::build, DimensionsFixture.expDimSectionWithSpecialChars);
  }
}

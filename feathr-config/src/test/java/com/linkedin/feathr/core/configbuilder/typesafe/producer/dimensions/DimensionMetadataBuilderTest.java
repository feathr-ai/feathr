package com.linkedin.feathr.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.feathr.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import org.testng.annotations.Test;

import static com.linkedin.feathr.core.configbuilder.typesafe.producer.dimensions.DimensionsFixture.*;

/**
 * Tests {@link DimensionMetadataBuilder}
 */
public class DimensionMetadataBuilderTest extends AbstractConfigBuilderTest {

  @Test(description = "Tests build of the dimension metadata of a discrete dimension type with a single version")
  public void testWithOneVersion() {
    testConfigBuilder(namespace, discreteDim1MdCfgStr, DimensionMetadataBuilder::build, expDiscreteDim1Md);
  }

  @Test(description = "Tests build of the dimension metadata of a categorical dimension type with multiple versions")
  public void testWithMultiVersions() {
    testConfigBuilder(namespace, catDim2MdCfgStr, DimensionMetadataBuilder::build, expCatDim2Md);
  }

  @Test(description = "Tests build of the dimension metadata of a dimension with special chars . and : in its name")
  public void testWithSpecialChars() {
    testConfigBuilder(namespaceWithSpecialChars, discreteDim3MdCfgStr, DimensionMetadataBuilder::build,
        expDiscreteDim3Md);
  }
}

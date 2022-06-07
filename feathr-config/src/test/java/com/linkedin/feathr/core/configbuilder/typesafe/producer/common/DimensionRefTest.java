package com.linkedin.feathr.core.configbuilder.typesafe.producer.common;

import com.linkedin.feathr.core.config.producer.common.DimensionRef;
import com.linkedin.feathr.core.config.producer.common.Namespace;
import org.testng.annotations.Test;

import static com.linkedin.feathr.core.configbuilder.typesafe.producer.features.FeaturesFixture.*;
import static org.testng.Assert.*;


/*
 * Tests build of DimensionRef objects
 */
public class DimensionRefTest {

  @Test(description = "Tests build of DimensionRef of a dimension with explicit namespace")
  public void testDimRefWithExplicitNamespace() {
    testDimensionRef(featureNamespace, dim1RefStr, dim1Ref);
  }

  @Test(description = "Tests build of DimensionRef with implicit namespace")
  public void testDimRefWithImplicitNamespace() {
    testDimensionRef(featureNamespace, dim2RefStr, dim2Ref);
  }

  @Test(description = "Tests build of DimensionRef of a dimension in a different namespace")
  public void testDimRefWithDifferentDimNamespace() {
    testDimensionRef(featureNamespace, dim3RefStr, dim3Ref);
  }

  private void testDimensionRef(Namespace namespace, String dimRefStr, DimensionRef expDimRef) {
    DimensionRef obsDimRef = new DimensionRef(namespace, dimRefStr);
    assertEquals(obsDimRef, expDimRef);
  }
}

package com.linkedin.feathr.config.featureversion.builder;

import com.linkedin.feathr.core.config.producer.definitions.FeatureType;
import com.linkedin.feathr.core.config.producer.common.FeatureTypeConfig;
import com.linkedin.feathr.compute.FrameFeatureType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;
import static org.testng.Assert.assertFalse;

/**
 * Test class for {@link FrameFeatureTypeBuilder}
 */
public class FrameFeatureTypeBuilderTest {

  @Test(dataProvider = "legacyFeatureTypeProvider")
  public void testBuildLegacyFeatureType(FeatureType featureType, FrameFeatureType expectedFeatureType) {
    FrameFeatureTypeBuilder builder = FrameFeatureTypeBuilder.getInstance();
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    Optional<FrameFeatureType> builtFeatureType = builder.build(featureTypeConfig);

    assertTrue(builtFeatureType.isPresent());
    assertEquals(builtFeatureType.get(), expectedFeatureType);
  }

  @Test(dataProvider = "tensorFeatureTypeProvider")
  public void testBuildTensorFeatureType(FeatureType featureType) {
    FrameFeatureTypeBuilder builder = FrameFeatureTypeBuilder.getInstance();
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    Optional<FrameFeatureType> builtFeatureType = builder.build(featureTypeConfig);

    assertTrue(builtFeatureType.isPresent());
    assertEquals(builtFeatureType.get(), FrameFeatureType.TENSOR);
  }

  @Test(description = "test UNSPECIFIED feature type", expectedExceptions = IllegalArgumentException.class)
  public void testInvalidFeatureType() {
    FrameFeatureTypeBuilder builder = FrameFeatureTypeBuilder.getInstance();
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.UNSPECIFIED);
    builder.build(featureTypeConfig);
  }

  @DataProvider(name = "legacyFeatureTypeProvider")
  public Object[][] legacyFeatureTypeProvider() {
    return new Object[][] {
        {FeatureType.NUMERIC, FrameFeatureType.NUMERIC},
        {FeatureType.BOOLEAN, FrameFeatureType.BOOLEAN},
        {FeatureType.CATEGORICAL, FrameFeatureType.CATEGORICAL},
        {FeatureType.CATEGORICAL_SET, FrameFeatureType.CATEGORICAL_SET},
        {FeatureType.TERM_VECTOR, FrameFeatureType.TERM_VECTOR},
        {FeatureType.DENSE_VECTOR, FrameFeatureType.DENSE_VECTOR},
        {FeatureType.TENSOR, FrameFeatureType.TENSOR}
    };
  }

  @DataProvider(name = "tensorFeatureTypeProvider")
  public Object[][] tensorFeatureTypeProvider() {
    return new Object[][] {
        {FeatureType.DENSE_TENSOR},
        {FeatureType.SPARSE_TENSOR},
        {FeatureType.RAGGED_TENSOR}
    };
  }
}

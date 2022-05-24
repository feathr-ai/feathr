package com.linkedin.frame.config.featureversion.builder;

import com.linkedin.frame.core.config.producer.common.FeatureTypeConfig;
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
  public void testBuildLegacyFeatureType(com.linkedin.frame.core.config.producer.definitions.FeatureType featureType, FrameFeatureType expectedFeatureType) {
    FrameFeatureTypeBuilder builder = FrameFeatureTypeBuilder.getInstance();
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    Optional<FrameFeatureType> builtFeatureType = builder.build(featureTypeConfig);

    assertTrue(builtFeatureType.isPresent());
    assertEquals(builtFeatureType.get(), expectedFeatureType);
  }

  @Test(dataProvider = "tensorFeatureTypeProvider")
  public void testBuildTensorFeatureType(com.linkedin.frame.core.config.producer.definitions.FeatureType featureType) {
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
    when(featureTypeConfig.getFeatureType()).thenReturn(com.linkedin.frame.core.config.producer.definitions.FeatureType.UNSPECIFIED);
    builder.build(featureTypeConfig);
  }

  @DataProvider(name = "legacyFeatureTypeProvider")
  public Object[][] legacyFeatureTypeProvider() {
    return new Object[][] {
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.NUMERIC, FrameFeatureType.NUMERIC},
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.BOOLEAN, FrameFeatureType.BOOLEAN},
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.CATEGORICAL, FrameFeatureType.CATEGORICAL},
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.CATEGORICAL_SET, FrameFeatureType.CATEGORICAL_SET},
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.TERM_VECTOR, FrameFeatureType.TERM_VECTOR},
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.DENSE_VECTOR, FrameFeatureType.DENSE_VECTOR},
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.TENSOR, FrameFeatureType.TENSOR}
    };
  }

  @DataProvider(name = "tensorFeatureTypeProvider")
  public Object[][] tensorFeatureTypeProvider() {
    return new Object[][] {
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.DENSE_TENSOR},
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.SPARSE_TENSOR},
        {com.linkedin.frame.core.config.producer.definitions.FeatureType.RAGGED_TENSOR}
    };
  }
}

package com.linkedin.frame.config.featureversion.builder;

import com.linkedin.frame.core.config.producer.anchors.ComplexFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.SimpleFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.TimeWindowFeatureConfig;
import com.linkedin.frame.core.config.producer.common.FeatureTypeConfig;
import com.linkedin.frame.core.config.producer.definitions.FeatureType;
import java.util.Optional;

import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class TensorFeatureFormatBuilderFactoryTest {
  @Test
  public void testEmptyFeatureTypeConfig() {
    TensorFeatureFormatBuilderFactory factory = new TensorFeatureFormatBuilderFactory();
    ComplexFeatureConfig featureConfig = mock(ComplexFeatureConfig.class);
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.empty());
    assertFalse(factory.getBuilder(featureConfig).isPresent());
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuildInstanceForExtractorBasedFeatureConfig(FeatureType featureType, Class clazz) {
    TensorFeatureFormatBuilderFactory factory = new TensorFeatureFormatBuilderFactory();
    SimpleFeatureConfig featureConfig = mock(SimpleFeatureConfig.class);
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    Optional<TensorFeatureFormatBuilder> tensorFeatureFormatBuilder = factory.getBuilder(featureConfig);
    if (clazz != null) {
      assertTrue(tensorFeatureFormatBuilder.isPresent());
      assertEquals(tensorFeatureFormatBuilder.get().getClass(), clazz);
    } else {
      assertFalse(tensorFeatureFormatBuilder.isPresent());
    }
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuildInstanceForExpressionBasedFeatureConfig(FeatureType featureType, Class clazz) {
    TensorFeatureFormatBuilderFactory factory = new TensorFeatureFormatBuilderFactory();
    ComplexFeatureConfig featureConfig = mock(ComplexFeatureConfig.class);
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    Optional<TensorFeatureFormatBuilder> tensorFeatureFormatBuilder = factory.getBuilder(featureConfig);
    if (clazz != null) {
      assertTrue(tensorFeatureFormatBuilder.isPresent());
      assertEquals(tensorFeatureFormatBuilder.get().getClass(), clazz);
    } else {
      assertFalse(tensorFeatureFormatBuilder.isPresent());
    }
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuildInstanceForTimeWindowFeatureConfig(FeatureType featureType, Class clazz) {
    TensorFeatureFormatBuilderFactory factory = new TensorFeatureFormatBuilderFactory();
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    when(featureConfig.getEmbeddingSize()).thenReturn(Optional.empty());
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    Optional<TensorFeatureFormatBuilder> tensorFeatureFormatBuilder = factory.getBuilder(featureConfig);
    if (clazz != null) {
      assertTrue(tensorFeatureFormatBuilder.isPresent());
      assertEquals(tensorFeatureFormatBuilder.get().getClass(), clazz);
    } else {
      assertFalse(tensorFeatureFormatBuilder.isPresent());
    }
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuildInstanceForDerivationConfig(FeatureType featureType, Class clazz) {
    TensorFeatureFormatBuilderFactory factory = new TensorFeatureFormatBuilderFactory();
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(derivationConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    Optional<TensorFeatureFormatBuilder> tensorFeatureFormatBuilder = factory.getBuilder(derivationConfig);
    if (clazz != null) {
      assertTrue(tensorFeatureFormatBuilder.isPresent());
      assertEquals(tensorFeatureFormatBuilder.get().getClass(), clazz);
    } else {
      assertFalse(tensorFeatureFormatBuilder.isPresent());
    }
  }

  @Test
  public void testWhenEmbeddingSizePresentAndFeatureTypeConfigNotPresent() {
    TensorFeatureFormatBuilderFactory factory = new TensorFeatureFormatBuilderFactory();
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.empty());
    when(featureConfig.getEmbeddingSize()).thenReturn(Optional.of(2));
    Optional<TensorFeatureFormatBuilder> tensorFeatureFormatBuilder = factory.getBuilder(featureConfig);
    assertTrue(tensorFeatureFormatBuilder.isPresent());
    assertEquals(tensorFeatureFormatBuilder.get().getClass(), FeatureTypeTensorFeatureFormatBuilder.class);
  }

  @DataProvider(name = "featureTypeProvider")
  public Object[][] featureTypeProvider() {
    return new Object[][] {
      {FeatureType.BOOLEAN, FeatureTypeTensorFeatureFormatBuilder.class},
      {FeatureType.NUMERIC, FeatureTypeTensorFeatureFormatBuilder.class},
      {FeatureType.CATEGORICAL, FeatureTypeTensorFeatureFormatBuilder.class},
      {FeatureType.CATEGORICAL_SET, FeatureTypeTensorFeatureFormatBuilder.class},
      {FeatureType.VECTOR, FeatureTypeTensorFeatureFormatBuilder.class},
      {FeatureType.DENSE_VECTOR, FeatureTypeTensorFeatureFormatBuilder.class},
      {FeatureType.TERM_VECTOR, FeatureTypeTensorFeatureFormatBuilder.class},
      {FeatureType.SPARSE_TENSOR, TensorTypeTensorFeatureFormatBuilder.class},
      {FeatureType.DENSE_TENSOR, TensorTypeTensorFeatureFormatBuilder.class},
      {FeatureType.TENSOR, null}
    };
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidFeatureType() {
    TensorFeatureFormatBuilderFactory factory = new TensorFeatureFormatBuilderFactory();
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.UNSPECIFIED);
    factory.getBuilder(featureConfig);
  }
}

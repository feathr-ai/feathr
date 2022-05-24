package com.linkedin.frame.config.featureversion;

import com.linkedin.data.DataMap;
import com.linkedin.frame.config.featureversion.builder.DefaultValueBuilder;
import com.linkedin.frame.config.featureversion.builder.FrameFeatureTypeBuilder;
import com.linkedin.frame.config.featureversion.builder.TensorFeatureFormatBuilder;
import com.linkedin.frame.config.featureversion.builder.TensorFeatureFormatBuilderFactory;
import com.linkedin.frame.core.config.producer.anchors.ComplexFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.SimpleFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.TimeWindowFeatureConfig;
import com.linkedin.frame.core.config.producer.common.FeatureTypeConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.compute.FrameFeatureType;
import com.linkedin.feathr.compute.FeatureValue;
import com.linkedin.feathr.compute.FeatureVersion;
import com.linkedin.feathr.compute.TensorFeatureFormat;
import java.util.Optional;
import org.mockito.Mock;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;
import static org.testng.Assert.*;


public class FeatureVersionBuilderTest {
  @Mock
  private TensorFeatureFormatBuilderFactory _tensorFeatureFormatBuilderFactory;
  @Mock
  private DefaultValueBuilder _defaultValueBuilder;
  @Mock
  private FrameFeatureTypeBuilder _featureTypeBuilder;

  private FeatureVersionBuilder _featureVersionBuilder;

  @BeforeMethod
  public void setup() {
    initMocks(this);
    _featureVersionBuilder = new FeatureVersionBuilder(_tensorFeatureFormatBuilderFactory, _defaultValueBuilder, _featureTypeBuilder);
  }

  @AfterMethod
  public void afterTest() {
    reset(_tensorFeatureFormatBuilderFactory, _defaultValueBuilder);
  }

  @Test
  public void testEmptyFeatureFormatAndNonEmptyDefaultValueBuilderSimpleConfig() {
    SimpleFeatureConfig featureConfig = mock(SimpleFeatureConfig.class);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.empty());
    String mockDefault = "";
    when(featureConfig.getDefaultValue()).thenReturn(Optional.of(mockDefault));
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.empty());
    FeatureValue featureValue = mock(FeatureValue.class);
    when(featureValue.data()).thenReturn(mock(DataMap.class));
    when(_defaultValueBuilder.build(mockDefault)).thenReturn(featureValue);
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getDefaultValue(), featureValue);
    // if the type is not built, then the FeatureVersion.hasType is false
    // if the user force to get the type, they get FrameFeatureType.UNSPECIFIED
    assertFalse(actual.hasType());
    assertEquals(actual.getType(), FrameFeatureType.UNSPECIFIED);
    assertFalse(actual.hasFormat());
  }

  @Test
  public void testNonEmptyTensorFeatureFormatAndEmptyDefaultValueBuilderSimpleConfig() {
    SimpleFeatureConfig featureConfig = mock(SimpleFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    when(featureConfig.getDefaultValue()).thenReturn(Optional.empty());
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(_featureTypeBuilder.build(featureTypeConfig)).thenReturn(Optional.empty());
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertFalse(actual.hasType());
    assertFalse(actual.hasDefaultValue());
    verify(featureConfig).getFeatureTypeConfig();
  }

  @Test
  public void testNonEmptyLegacyFeatureFormatAndEmptyDefaultValueBuilderSimpleConfig() {
    SimpleFeatureConfig featureConfig = mock(SimpleFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    when(featureConfig.getDefaultValue()).thenReturn(Optional.empty());
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(_featureTypeBuilder.build(featureTypeConfig)).thenReturn(Optional.of(FrameFeatureType.NUMERIC));
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertEquals(actual.getType(), FrameFeatureType.NUMERIC);
    assertFalse(actual.hasDefaultValue());
    verify(featureConfig).getFeatureTypeConfig();
    verify(featureConfig).getDefaultValue();
  }

  @Test
  public void testNonEmptyFeatureFormatAndNonEmptyDefaultValueBuilderSimpleConfig() {
    SimpleFeatureConfig featureConfig = mock(SimpleFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    String mockDefault = "";
    when(featureConfig.getDefaultValue()).thenReturn(Optional.of(mockDefault));
    FeatureValue featureValue = mock(FeatureValue.class);
    when(featureValue.data()).thenReturn(mock(DataMap.class));
    when(_defaultValueBuilder.build(mockDefault)).thenReturn(featureValue);
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(_featureTypeBuilder.build(featureTypeConfig)).thenReturn(Optional.of(FrameFeatureType.NUMERIC));
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertEquals(actual.getType(), FrameFeatureType.NUMERIC);
    assertEquals(actual.getDefaultValue(), featureValue);
    verify(featureConfig).getFeatureTypeConfig();
    verify(featureConfig).getDefaultValue();
  }

  @Test
  public void testEmptyFeatureFormatBuilderTimeWindowConfig() {
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.empty());
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertFalse(actual.hasDefaultValue());
    assertFalse(actual.hasType());
  }

  @Test
  public void testNonEmptyTensorFeatureFormatBuilderTimeWindowConfig() {
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(_featureTypeBuilder.build(featureTypeConfig)).thenReturn(Optional.empty());
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertFalse(actual.hasDefaultValue());
    assertFalse(actual.hasType());
    verify(featureConfig).getFeatureTypeConfig();
  }

  @Test
  public void testNonEmptyLegacyFeatureFormatBuilderTimeWindowConfig() {
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(_featureTypeBuilder.build(featureTypeConfig)).thenReturn(Optional.of(FrameFeatureType.NUMERIC));
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertEquals(actual.getType(), FrameFeatureType.NUMERIC);
    assertFalse(actual.hasDefaultValue());
    verify(featureConfig).getFeatureTypeConfig();
    verify(featureConfig).getDefaultValue();
  }

  @Test
  public void testEmptyFeatureFormatAndNonEmptyDefaultValueBuilderComplexConfig() {
    ComplexFeatureConfig featureConfig = mock(ComplexFeatureConfig.class);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.empty());
    String mockDefault = "";
    when(featureConfig.getDefaultValue()).thenReturn(Optional.of(mockDefault));
    FeatureValue featureValue = mock(FeatureValue.class);
    when(featureValue.data()).thenReturn(mock(DataMap.class));
    when(_defaultValueBuilder.build(mockDefault)).thenReturn(featureValue);
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.empty());
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertFalse(actual.hasFormat());
    assertEquals(actual.getDefaultValue(), featureValue);
    assertFalse(actual.hasType());
  }

  @Test
  public void testNonEmptyTensorFeatureFormatAndEmptyDefaultValueBuilderComplexConfig() {
    ComplexFeatureConfig featureConfig = mock(ComplexFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    when(featureConfig.getDefaultValue()).thenReturn(Optional.empty());
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(_featureTypeBuilder.build(featureTypeConfig)).thenReturn(Optional.empty());
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertFalse(actual.hasDefaultValue());
    assertFalse(actual.hasType());
    verify(featureConfig).getFeatureTypeConfig();
  }

  @Test
  public void testNonEmptyLegacyFeatureFormatAndEmptyDefaultValueBuilderComplexConfig() {
    ComplexFeatureConfig featureConfig = mock(ComplexFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    when(featureConfig.getDefaultValue()).thenReturn(Optional.empty());
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(_featureTypeBuilder.build(featureTypeConfig)).thenReturn(Optional.of(FrameFeatureType.NUMERIC));
    when(featureConfig.getFeatureTypeConfig()).thenReturn(Optional.of(featureTypeConfig));
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertEquals(actual.getType(), FrameFeatureType.NUMERIC);
    assertFalse(actual.hasDefaultValue());
    verify(featureConfig).getFeatureTypeConfig();
    verify(featureConfig).getDefaultValue();
  }

  @Test
  public void testNonEmptyFeatureFormatAndNonEmptyDefaultValueBuilderComplexConfig() {
    ComplexFeatureConfig featureConfig = mock(ComplexFeatureConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(featureConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    String mockDefault = "";
    when(featureConfig.getDefaultValue()).thenReturn(Optional.of(mockDefault));
    FeatureValue featureValue = mock(FeatureValue.class);
    when(featureValue.data()).thenReturn(mock(DataMap.class));
    when(_defaultValueBuilder.build(mockDefault)).thenReturn(featureValue);
    FeatureVersion actual = _featureVersionBuilder.build(featureConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    assertEquals(actual.getDefaultValue(), featureValue);
  }

  @Test(description = "test builder for derivation config with empty format")
  public void testEmptyFeatureFormatDerivationConfig() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(derivationConfig)).thenReturn(Optional.empty());
    when(derivationConfig.getFeatureTypeConfig()).thenReturn(Optional.empty());
    FeatureVersion actual = _featureVersionBuilder.build(derivationConfig);
    // if the type is not built, then the FeatureVersion.hasType is false
    // if the user force to get the type, they get FrameFeatureType.UNSPECIFIED
    assertFalse(actual.hasType());
    assertEquals(actual.getType(), FrameFeatureType.UNSPECIFIED);
    assertFalse(actual.hasFormat());
    verify(derivationConfig).getFeatureTypeConfig();
    verify(_tensorFeatureFormatBuilderFactory).getBuilder(derivationConfig);
  }

  @Test(description = "test builder for derivation config with format")
  public void testDerivationConfigWithFeatureFormat() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    TensorFeatureFormatBuilder tensorFeatureFormatBuilder = mock(TensorFeatureFormatBuilder.class);
    TensorFeatureFormat tensorFeatureFormat = mock(TensorFeatureFormat.class);
    when(tensorFeatureFormat.data()).thenReturn(mock(DataMap.class));
    when(tensorFeatureFormatBuilder.build()).thenReturn(tensorFeatureFormat);
    when(_tensorFeatureFormatBuilderFactory.getBuilder(derivationConfig)).thenReturn(Optional.of(tensorFeatureFormatBuilder));
    FeatureVersion actual = _featureVersionBuilder.build(derivationConfig);
    assertEquals(actual.getFormat(), tensorFeatureFormat);
    verify(tensorFeatureFormat, atLeastOnce()).data();
  }

  @Test
  public void testBuildDerivationFeatureVersion() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    FeatureVersion actual = _featureVersionBuilder.build(derivationConfig);
    assertFalse(actual.hasDefaultValue());
    assertFalse(actual.hasFormat());
  }
}

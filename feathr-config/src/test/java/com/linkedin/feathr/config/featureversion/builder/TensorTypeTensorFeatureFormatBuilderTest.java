package com.linkedin.feathr.config.featureversion.builder;

import com.linkedin.data.DataList;
import com.linkedin.feathr.core.config.producer.common.FeatureTypeConfig;
import com.linkedin.feathr.core.config.producer.definitions.FeatureType;
import com.linkedin.feathr.compute.Dimension;
import com.linkedin.feathr.compute.DimensionArray;
import com.linkedin.feathr.compute.DimensionType;
import com.linkedin.feathr.compute.TensorCategory;
import com.linkedin.feathr.compute.TensorFeatureFormat;
import com.linkedin.feathr.compute.ValueType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.mockito.Mockito;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class TensorTypeTensorFeatureFormatBuilderTest {
  @Test
  public void testValidateEmptyDimensionTypesAndNonEmptyShapes() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.empty());
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(mock(List.class)));
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    Exception exception = null;
    try {
      tensorTypeTensorFeatureFormatBuilder.validCheck();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Shapes are provided but Dimensions are not provided in config"));
  }

  @Test
  public void testValidateNonEmptyDimensionTypesAndEmptyShapes() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(mock(List.class)));
    when(featureTypeConfig.getShapes()).thenReturn(Optional.empty());
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.DENSE_TENSOR);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    tensorTypeTensorFeatureFormatBuilder.validCheck();
  }

  @Test
  public void testValidateEmptyDimensionTypesAndEmptyShapes() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(mock(List.class)));
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(mock(List.class)));
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.DENSE_TENSOR);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    tensorTypeTensorFeatureFormatBuilder.validCheck();
  }

  @Test
  public void testInvalidShape() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(Arrays.asList("LONG", "INT")));
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(Arrays.asList(3, 0)));
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    Exception exception = null;
    try {
      tensorTypeTensorFeatureFormatBuilder.validCheck();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Shapes should be larger than 0 or -1."));
  }

  @Test
  public void testUnequalShapesAndDimensionTypesSize() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    List<String> dimensionTypes = mock(List.class);
    List<Integer> shapes = mock(List.class);
    when(dimensionTypes.size()).thenReturn(2);
    when(shapes.size()).thenReturn(3);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(dimensionTypes));
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(shapes));
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    Exception exception = null;
    try {
      tensorTypeTensorFeatureFormatBuilder.validCheck();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("The size of dimension types"));
  }

  @Test
  public void testValidateEmptyShapeWhenEmbeddingSizePresent() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.empty());
    when(featureTypeConfig.getShapes()).thenReturn(Optional.empty());
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.DENSE_TENSOR);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig, 3);
    Exception exception = null;
    try {
      tensorTypeTensorFeatureFormatBuilder.validCheck();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Shapes are not present"));
  }

  @Test
  public void testValidateWrongShapeDimensionWhenEmbeddingSizePresent() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    List<String> dimensionTypes = mock(List.class);
    List<Integer> shapes = mock(List.class);
    when(dimensionTypes.size()).thenReturn(2);
    when(shapes.size()).thenReturn(2);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(dimensionTypes));
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(shapes));
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.DENSE_TENSOR);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig, 3);
    Exception exception = null;
    try {
      tensorTypeTensorFeatureFormatBuilder.validCheck();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("One dimensional shape is expected when embedding size is set"));
  }

  @Test
  public void testValidateWhenShapeAndEmbeddingSizeNotMatch() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    List<String> dimensionTypes = mock(List.class);
    List<Integer> shapes = Arrays.asList(5);
    when(dimensionTypes.size()).thenReturn(1);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(dimensionTypes));
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(shapes));
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.DENSE_TENSOR);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig, 4);
    Exception exception = null;
    try {
      tensorTypeTensorFeatureFormatBuilder.validCheck();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Embedding size 4 and shape size 5 don't match"));
  }

  @Test
  public void testInvalidFeatureTypeWhenEmbeddingSizeExist() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    List<String> dimensionTypes = mock(List.class);
    List<Integer> shapes = Arrays.asList(5);
    when(dimensionTypes.size()).thenReturn(1);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(dimensionTypes));
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(shapes));
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.SPARSE_TENSOR);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig, 5);
    Exception exception = null;
    try {
      tensorTypeTensorFeatureFormatBuilder.validCheck();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Dense tensor feature type is expected when embedding size is set"));
  }

  @Test
  public void testValidateNonEmptyDimensionTypesAndNonEmptyShapesWithEqualSize() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    List<String> dimensionTypes = mock(List.class);
    List<Integer> shapes = mock(List.class);
    when(dimensionTypes.size()).thenReturn(3);
    when(shapes.size()).thenReturn(3);
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(dimensionTypes));
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(shapes));
    when(featureTypeConfig.getFeatureType()).thenReturn(FeatureType.DENSE_TENSOR);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    tensorTypeTensorFeatureFormatBuilder.validCheck();
  }

  @Test
  public void testBuildValueType() {
    String valueTypeStr = "STRING";
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getValType()).thenReturn(Optional.of(valueTypeStr));
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder =
        new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    TensorTypeTensorFeatureFormatBuilder spy = Mockito.spy(tensorTypeTensorFeatureFormatBuilder);
    doNothing().when(spy).validCheck();
    ValueType valueType = spy.buildValueType();
    assertEquals(valueType, ValueType.STRING);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidValueType() {
    String valueTypeStr = "INVALID";
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getValType()).thenReturn(Optional.of(valueTypeStr));
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    TensorTypeTensorFeatureFormatBuilder spy = Mockito.spy(tensorTypeTensorFeatureFormatBuilder);
    doNothing().when(spy).validCheck();
    spy.buildValueType();
  }

  @DataProvider(name = "featureTypeProvider")
  public Object[][] featureTypeProvider() {
    return new Object[][] {
        {FeatureType.DENSE_TENSOR, TensorCategory.DENSE},
        {FeatureType.SPARSE_TENSOR, TensorCategory.SPARSE},
        {FeatureType.RAGGED_TENSOR, TensorCategory.RAGGED}
    };
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuildTensorCategory(FeatureType featureType, TensorCategory expectedTensorCategory) {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder =
        new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    TensorTypeTensorFeatureFormatBuilder spy = Mockito.spy(tensorTypeTensorFeatureFormatBuilder);
    doNothing().when(spy).validCheck();
    assertEquals(spy.buildTensorCategory(), expectedTensorCategory);
  }

  @DataProvider(name = "invalidFeatureTypeProvider")
  public Object[][] invalidFeatureTypeProvider() {
    return new Object[][] {
        {FeatureType.NUMERIC},
        {FeatureType.BOOLEAN},
        {FeatureType.CATEGORICAL},
        {FeatureType.CATEGORICAL_SET},
        {FeatureType.TERM_VECTOR},
        {FeatureType.DENSE_VECTOR},
        {FeatureType.TENSOR},
        {FeatureType.UNSPECIFIED}
    };
  }

  @Test(dataProvider = "invalidFeatureTypeProvider", expectedExceptions = IllegalArgumentException.class)
  public void testInvalidFeatureType(FeatureType featureType) {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    when(featureTypeConfig.getFeatureType()).thenReturn(featureType);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder = new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    tensorTypeTensorFeatureFormatBuilder.validCheck();
  }

  @Test
  public void testBuildDimensions() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    List<Integer> mockShapes = Arrays.asList(1, 2, 5);
    List<String> mockTypes = Arrays.asList("LONG", "INT", "STRING");
    when(featureTypeConfig.getShapes()).thenReturn(Optional.of(mockShapes));
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(mockTypes));
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder =
        new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    TensorTypeTensorFeatureFormatBuilder spy = Mockito.spy(tensorTypeTensorFeatureFormatBuilder);
    doNothing().when(spy).validCheck();
    List<Dimension> actual = spy.buildDimensions();
    assertEquals(actual.size(), 3);
    assertEquals(actual.get(0).getShape(), new Integer(1));
    assertEquals(actual.get(0).getType(), DimensionType.LONG);
    assertEquals(actual.get(1).getShape(), new Integer(2));
    assertEquals(actual.get(1).getType(), DimensionType.INT);
    assertEquals(actual.get(2).getShape(), new Integer(5));
    assertEquals(actual.get(2).getType(), DimensionType.STRING);
  }

  @Test
  public void testBuildDimensionsWhenShapesAreMissing() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    List<String> mockTypes = Arrays.asList("LONG", "INT", "STRING");
    when(featureTypeConfig.getShapes()).thenReturn(Optional.empty());
    when(featureTypeConfig.getDimensionTypes()).thenReturn(Optional.of(mockTypes));
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder =
        new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    TensorTypeTensorFeatureFormatBuilder spy = Mockito.spy(tensorTypeTensorFeatureFormatBuilder);
    doNothing().when(spy).validCheck();
    List<Dimension> actual = spy.buildDimensions();
    assertEquals(actual.size(), 3);
    assertEquals(actual.get(0).getShape(), new Integer(-1));
    assertEquals(actual.get(0).getType(), DimensionType.LONG);
    assertEquals(actual.get(1).getShape(), new Integer(-1));
    assertEquals(actual.get(1).getType(), DimensionType.INT);
    assertEquals(actual.get(2).getShape(), new Integer(-1));
    assertEquals(actual.get(2).getType(), DimensionType.STRING);
  }

  @Test
  public void testBuild() {
    FeatureTypeConfig featureTypeConfig = mock(FeatureTypeConfig.class);
    TensorTypeTensorFeatureFormatBuilder tensorTypeTensorFeatureFormatBuilder =
        new TensorTypeTensorFeatureFormatBuilder(featureTypeConfig);
    TensorTypeTensorFeatureFormatBuilder spy = Mockito.spy(tensorTypeTensorFeatureFormatBuilder);
    ValueType mockValueType = ValueType.BOOLEAN;
    TensorCategory mockTensorCategory = TensorCategory.DENSE;
    DimensionArray mockDimensions = mock(DimensionArray.class);
    when(mockDimensions.data()).thenReturn(mock(DataList.class));
    doReturn(mockValueType).when(spy).buildValueType();
    doReturn(mockTensorCategory).when(spy).buildTensorCategory();
    doReturn(mockDimensions).when(spy).buildDimensions();
    doNothing().when(spy).validCheck();
    TensorFeatureFormat tensorFeatureFormat = spy.build();
    assertEquals(tensorFeatureFormat.getValueType(), mockValueType);
    assertEquals(tensorFeatureFormat.getTensorCategory(), mockTensorCategory);
    assertEquals(tensorFeatureFormat.getDimensions(), mockDimensions);
  }
}

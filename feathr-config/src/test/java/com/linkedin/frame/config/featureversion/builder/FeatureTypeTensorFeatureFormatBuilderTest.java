package com.linkedin.frame.config.featureversion.builder;

import com.linkedin.frame.core.config.producer.definitions.FeatureType;
import com.linkedin.feathr.compute.Dimension;
import com.linkedin.feathr.compute.DimensionArray;
import com.linkedin.feathr.compute.DimensionType;
import com.linkedin.feathr.compute.TensorCategory;
import com.linkedin.feathr.compute.TensorFeatureFormat;
import com.linkedin.feathr.compute.ValueType;
import java.util.Arrays;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class FeatureTypeTensorFeatureFormatBuilderTest {
  @Test(dataProvider = "invalidTypeProvider", expectedExceptions = IllegalArgumentException.class)
  public void testInvalidType(FeatureType featureType) {
    FeatureTypeTensorFeatureFormatBuilder featureTypeTensorFeatureFormatBuilder =
        new FeatureTypeTensorFeatureFormatBuilder(featureType);
    featureTypeTensorFeatureFormatBuilder.validCheck();
  }

  @DataProvider(name = "invalidTypeProvider")
  public Object[][] invalidTypeProvider() {
    return new Object[][]{
      {FeatureType.DENSE_TENSOR},
      {FeatureType.SPARSE_TENSOR},
      {FeatureType.RAGGED_TENSOR},
      {FeatureType.TENSOR},
      {FeatureType.UNSPECIFIED}
    };
  }

  @Test
  public void testInvalidTypeWhenEmbeddingSizePresent() {
    FeatureTypeTensorFeatureFormatBuilder featureTypeTensorFeatureFormatBuilder =
        new FeatureTypeTensorFeatureFormatBuilder(FeatureType.TERM_VECTOR, 5);
    Exception exception = null;
    try {
      featureTypeTensorFeatureFormatBuilder.validCheck();
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception instanceof IllegalArgumentException);
    assertTrue(exception.getMessage().startsWith("Dense vector feature type is expected when embedding size is set"
        + ""));
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuildValueType(FeatureType featureType, ValueType expectedValueType, TensorCategory expectedTensorCategory,
      Dimension[] expectedDimensions) {
    FeatureTypeTensorFeatureFormatBuilder featureTypeTensorFeatureFormatBuilder =
        new FeatureTypeTensorFeatureFormatBuilder(featureType);
    ValueType actual = featureTypeTensorFeatureFormatBuilder.buildValueType();
    assertEquals(actual, expectedValueType);
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuildTensorCategory(FeatureType featureType, ValueType expectedValueType, TensorCategory expectedTensorCategory,
      Dimension[] expectedDimensions) {
    FeatureTypeTensorFeatureFormatBuilder featureTypeTensorFeatureFormatBuilder =
        new FeatureTypeTensorFeatureFormatBuilder(featureType);
    TensorCategory actual = featureTypeTensorFeatureFormatBuilder.buildTensorCategory();
    assertEquals(actual, expectedTensorCategory);
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuildDimensionsWhenEmbeddingSizeNotPresent(FeatureType featureType, ValueType expectedValueType, TensorCategory expectedTensorCategory,
      Dimension[] expectedDimensions) {
    FeatureTypeTensorFeatureFormatBuilder featureTypeTensorFeatureFormatBuilder =
        new FeatureTypeTensorFeatureFormatBuilder(featureType);
    DimensionArray actual = featureTypeTensorFeatureFormatBuilder.buildDimensions();
    assertEquals(actual, Arrays.asList(expectedDimensions));
  }

  @Test
  public void testBuildDimensionsWhenEmbeddingSizePresent() {
    FeatureTypeTensorFeatureFormatBuilder featureTypeTensorFeatureFormatBuilder =
        new FeatureTypeTensorFeatureFormatBuilder(FeatureType.DENSE_VECTOR, 2);
    DimensionArray actual = featureTypeTensorFeatureFormatBuilder.buildDimensions();
    assertEquals(actual.size(), 1);
    assertEquals(actual.get(0).getShape(), new Integer(2));
    assertEquals(actual.get(0).getType(), DimensionType.INT);
  }

  @Test(dataProvider = "featureTypeProvider")
  public void testBuild(FeatureType featureType, ValueType expectedValueType, TensorCategory expectedTensorCategory,
      Dimension[] expectedDimensions) {
    FeatureTypeTensorFeatureFormatBuilder featureTypeTensorFeatureFormatBuilder =
        new FeatureTypeTensorFeatureFormatBuilder(featureType);
    TensorFeatureFormat tensorFeatureFormat = featureTypeTensorFeatureFormatBuilder.build();
    assertEquals(tensorFeatureFormat.getTensorCategory(), featureTypeTensorFeatureFormatBuilder.buildTensorCategory());
    assertEquals(tensorFeatureFormat.getValueType(), featureTypeTensorFeatureFormatBuilder.buildValueType());
    assertEquals(tensorFeatureFormat.getDimensions(), featureTypeTensorFeatureFormatBuilder.buildDimensions());
  }

  @DataProvider(name = "featureTypeProvider")
  public Object[][] featureTypeProvider() {
    return new Object[][] {
        {FeatureType.BOOLEAN, ValueType.FLOAT, TensorCategory.DENSE, new Dimension[]{}},
        {FeatureType.NUMERIC, ValueType.FLOAT, TensorCategory.DENSE, new Dimension[]{}},
        {FeatureType.CATEGORICAL, ValueType.FLOAT, TensorCategory.SPARSE, new Dimension[]{new Dimension().setShape(-1).setType(DimensionType.STRING)}},
        {FeatureType.VECTOR, ValueType.FLOAT, TensorCategory.DENSE, new Dimension[]{new Dimension().setShape(-1).setType(DimensionType.INT)}},
        {FeatureType.DENSE_VECTOR, ValueType.FLOAT, TensorCategory.DENSE, new Dimension[]{new Dimension().setShape(-1).setType(DimensionType.INT)}},
        {FeatureType.CATEGORICAL_SET, ValueType.FLOAT, TensorCategory.SPARSE, new Dimension[]{new Dimension().setShape(-1).setType(DimensionType.STRING)}},
        {FeatureType.TERM_VECTOR, ValueType.FLOAT, TensorCategory.SPARSE, new Dimension[]{new Dimension().setShape(-1).setType(DimensionType.STRING)}}
    };
  }
}

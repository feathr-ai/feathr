package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.linkedin.data.DataMap;
import com.linkedin.frame.config.featureanchor.builder.transformation.HdfsDataSourceTransformationFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.TransformationFunctionExpressionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class HdfsDataSourceTransformationFunctionBuilderTest {
  @Mock
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;
  private HdfsDataSourceTransformationFunctionBuilder _hdfsDataSourceTransformationFunctionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _hdfsDataSourceTransformationFunctionBuilder = new HdfsDataSourceTransformationFunctionBuilder(
        _transformationFunctionExpressionBuilder);
  }

  @AfterMethod
  public void afterTest() {
    reset(_transformationFunctionExpressionBuilder);
  }


  @Test
  public void testBuildMvelTransformation() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    MvelExpression mvelExpression = new MvelExpression();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(
        mvelExpression);
    TransformationFunction transformationFunction = _hdfsDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
    assertTrue(transformationFunction.isMvelExpression());
    assertEquals(transformationFunction.getMvelExpression(), mvelExpression);
  }

  @Test
  public void testBuildSparkSqlTransformation() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(
        sparkSqlExpression);
    TransformationFunction transformationFunction = _hdfsDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
    assertTrue(transformationFunction.isSparkSqlExpression());
    assertEquals(transformationFunction.getSparkSqlExpression(), sparkSqlExpression);
  }

  @Test
  public void testBuildUdfTransformation() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    UserDefinedFunction userDefinedFunction = new UserDefinedFunction();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(
        userDefinedFunction);
    TransformationFunction transformationFunction = _hdfsDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
    assertTrue(transformationFunction.isUserDefinedFunction());
    assertEquals(transformationFunction.getUserDefinedFunction(), userDefinedFunction);
  }

  @Test
  public void testBuildSlidingWindowAggregationTransformation() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    SlidingWindowAggregation slidingWindowAggregation = new SlidingWindowAggregation();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(
        slidingWindowAggregation);
    TransformationFunction transformationFunction = _hdfsDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
    assertTrue(transformationFunction.isSlidingWindowAggregation());
    assertEquals(transformationFunction.getSlidingWindowAggregation(), slidingWindowAggregation);
  }

  @Test
  public void testBuildSlidingWindowEmbeddingAggregationTransformation() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    SlidingWindowEmbeddingAggregation slidingWindowEmbeddingAggregation = new SlidingWindowEmbeddingAggregation();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(
        slidingWindowEmbeddingAggregation);
    TransformationFunction transformationFunction = _hdfsDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
    assertTrue(transformationFunction.isSlidingWindowEmbeddingAggregation());
    assertEquals(transformationFunction.getSlidingWindowEmbeddingAggregation(), slidingWindowEmbeddingAggregation);
  }


  @Test
  public void testBuildSlidingWindowLatestAvailableTransformation() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    SlidingWindowLatestAvailable slidingWindowLatestAvailable = new SlidingWindowLatestAvailable();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(
        slidingWindowLatestAvailable);
    TransformationFunction transformationFunction = _hdfsDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
    assertTrue(transformationFunction.isSlidingWindowLatestAvailable());
    assertEquals(transformationFunction.getSlidingWindowLatestAvailable(), slidingWindowLatestAvailable);
  }


  @Test(expectedExceptions = IllegalStateException.class)
  public void testInvalidExpressionType() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    Object expression = mock(Object.class);
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(expression);
    _hdfsDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
  }
}

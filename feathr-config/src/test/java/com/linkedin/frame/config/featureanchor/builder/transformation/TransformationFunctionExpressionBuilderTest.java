package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.google.common.collect.ImmutableMap;
import com.linkedin.data.template.StringMap;
import com.linkedin.frame.core.config.TimeWindowAggregationType;
import com.linkedin.frame.core.config.producer.ExprType;
import com.linkedin.frame.core.config.producer.TypedExpr;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithExtractor;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKey;
import com.linkedin.frame.core.config.producer.anchors.ComplexFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.SimpleFeatureConfig;
import com.linkedin.frame.core.config.producer.anchors.TimeWindowFeatureConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExtractor;
import com.linkedin.frame.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import com.linkedin.feathr.featureDataModel.SlidingWindowAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation;
import com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable;
import java.util.Map;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class TransformationFunctionExpressionBuilderTest {
  @Mock
  private SlidingWindowAggregationBuilder _slidingWindowAggregationBuilder;

  @Mock
  private SlidingWindowEmbeddingAggregationBuilder _slidingWindowEmbeddingAggregationBuilder;

  @Mock
  private SlidingWindowLatestAvailableBuilder _slidingWindowLatestAvailableBuilder;

  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _transformationFunctionExpressionBuilder = new TransformationFunctionExpressionBuilder(
        _slidingWindowAggregationBuilder, _slidingWindowEmbeddingAggregationBuilder, _slidingWindowLatestAvailableBuilder);
  }

  @AfterMethod
  public void afterTest() {
    reset( _slidingWindowAggregationBuilder, _slidingWindowEmbeddingAggregationBuilder, _slidingWindowLatestAvailableBuilder);
  }

  @Test
  public void testBuildTransformationFunctionFromAnchorConfigWithExtractor() {
    AnchorConfigWithExtractor anchorConfig = mock(AnchorConfigWithExtractor.class);
    when(anchorConfig.getExtractor()).thenReturn("com.test.extractor");
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig);
    assertTrue(expression instanceof UserDefinedFunction);
    UserDefinedFunction userDefinedFunction = (UserDefinedFunction) expression;
    assertEquals(userDefinedFunction.getClazz().getFullyQualifiedName(), "com.test.extractor");
  }

  @Test
  public void testBuildTransformationFunctionFromAnchorConfigWithParameterizedExtractor() {
    AnchorConfigWithExtractor anchorConfig = mock(AnchorConfigWithExtractor.class);
    when(anchorConfig.getExtractor()).thenReturn("com.test.extractor");
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    Map<String, String> parameters = ImmutableMap.of("param1", "paramValue1");
    when(featureConfig.getParameters()).thenReturn(parameters);
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig);
    assertTrue(expression instanceof UserDefinedFunction);
    UserDefinedFunction userDefinedFunction = (UserDefinedFunction) expression;
    assertEquals(userDefinedFunction.getClazz().getFullyQualifiedName(), "com.test.extractor");
    assertEquals(userDefinedFunction.getParameters(), new StringMap(parameters));
  }

  @Test
  public void testBuildTransformationFunctionFromSimpleFeatureConfig() {
    SimpleFeatureConfig featureConfig = mock(SimpleFeatureConfig.class);
    when(featureConfig.getFeatureName()).thenReturn("testExpr");
    AnchorConfigWithKey anchorConfigWithKey = mock(AnchorConfigWithKey.class);
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfigWithKey);
    assertTrue(expression instanceof MvelExpression);
    assertEquals(((MvelExpression) expression).getMvel(), "testExpr");
  }

  @Test
  public void testBuildTransformationFunctionFromComplexFeatureConfig() {
    ComplexFeatureConfig featureConfig1 = mock(ComplexFeatureConfig.class);
    when(featureConfig1.getFeatureExpr()).thenReturn("testMvelExpr");
    when(featureConfig1.getExprType()).thenReturn(ExprType.MVEL);
    AnchorConfigWithKey anchorConfigWithKey = mock(AnchorConfigWithKey.class);
    Object expression1 = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig1, anchorConfigWithKey);
    assertTrue(expression1 instanceof MvelExpression);
    assertEquals(((MvelExpression) expression1).getMvel(), "testMvelExpr");


    ComplexFeatureConfig featureConfig2 = mock(ComplexFeatureConfig.class);
    when(featureConfig2.getFeatureExpr()).thenReturn("testSparkExpr");
    when(featureConfig2.getExprType()).thenReturn(ExprType.SQL);
    Object expression2 = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig2, anchorConfigWithKey);
    assertTrue(expression2 instanceof SparkSqlExpression);
    assertEquals(((SparkSqlExpression) expression2).getSql(), "testSparkExpr");
  }

  public void testBuildSlidingWindowAggregationFromTimeWindowFeatureConfig() {
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    when(featureConfig.getAggregation()).thenReturn(TimeWindowAggregationType.MAX);
    when(_slidingWindowAggregationBuilder.build(featureConfig, anchorConfig)).thenReturn(mock(SlidingWindowAggregation.class));
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig);
    assertTrue(expression instanceof SlidingWindowAggregation);
  }

  @Test
  public void testBuildSlidingWindowEmbeddingAggregationFromTimeWindowFeatureConfig() {
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    when(featureConfig.getAggregation()).thenReturn(TimeWindowAggregationType.MAX_POOLING);
    when(_slidingWindowEmbeddingAggregationBuilder.build(featureConfig, anchorConfig)).thenReturn(mock(
        SlidingWindowEmbeddingAggregation.class));
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig);
    assertTrue(expression instanceof SlidingWindowEmbeddingAggregation);
  }

  @Test
  public void testBuildSlidingWindowLatestAvailableFromTimeWindowFeatureConfig() {
    TimeWindowFeatureConfig featureConfig = mock(TimeWindowFeatureConfig.class);
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    when(featureConfig.getAggregation()).thenReturn(TimeWindowAggregationType.LATEST);
    when(_slidingWindowLatestAvailableBuilder.build(featureConfig, anchorConfig)).thenReturn(mock(
        SlidingWindowLatestAvailable.class));
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig);
    assertTrue(expression instanceof SlidingWindowLatestAvailable);
  }

  @Test
  public void testBuildTransformationFunctionFromSimpleDerivationConfig() {
    SimpleDerivationConfig simpleDerivationConfig1 = mock(SimpleDerivationConfig.class);
    when(simpleDerivationConfig1.getFeatureTypedExpr()).thenReturn(new TypedExpr("testMvelExpr", ExprType.MVEL));
    Object expression1 = _transformationFunctionExpressionBuilder.buildTransformationExpression(simpleDerivationConfig1);
    assertTrue(expression1 instanceof MvelExpression);
    assertEquals(((MvelExpression) expression1).getMvel(), "testMvelExpr");

    SimpleDerivationConfig simpleDerivationConfig2 = mock(SimpleDerivationConfig.class);
    when(simpleDerivationConfig2.getFeatureTypedExpr()).thenReturn(new TypedExpr("testSparkExpr", ExprType.SQL));
    Object expression2 = _transformationFunctionExpressionBuilder.buildTransformationExpression(simpleDerivationConfig2);
    assertTrue(expression2 instanceof SparkSqlExpression);
    assertEquals(((SparkSqlExpression) expression2).getSql(), "testSparkExpr");
  }

  @Test
  public void testBuildTransformationFunctionFromDerivationConfigWithExpr() {
    DerivationConfigWithExpr derivationConfigWithExpr1 = mock(DerivationConfigWithExpr.class);
    when(derivationConfigWithExpr1.getTypedDefinition()).thenReturn(new TypedExpr("testMvelExpr", ExprType.MVEL));
    Object expression1 = _transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfigWithExpr1);
    assertTrue(expression1 instanceof MvelExpression);
    assertEquals(((MvelExpression) expression1).getMvel(), "testMvelExpr");

    DerivationConfigWithExpr derivationConfigWithExpr2 = mock(DerivationConfigWithExpr.class);
    when(derivationConfigWithExpr2.getTypedDefinition()).thenReturn(new TypedExpr("testSparkExpr", ExprType.SQL));
    Object expression2 = _transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfigWithExpr2);
    assertTrue(expression2 instanceof SparkSqlExpression);
    assertEquals(((SparkSqlExpression) expression2).getSql(), "testSparkExpr");
  }

  @Test
  public void testBuildTransformationFunctionFromDerivationConfigWithExtractor() {
    DerivationConfigWithExtractor derivationConfigWithExtractor = mock(DerivationConfigWithExtractor.class);
    when(derivationConfigWithExtractor.getClassName()).thenReturn("com.test.extractor");
    Object expression = _transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfigWithExtractor);
    assertTrue(expression instanceof UserDefinedFunction);
    assertEquals(((UserDefinedFunction) expression).getClazz().getFullyQualifiedName(), "com.test.extractor");
  }
}

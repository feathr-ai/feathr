package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.linkedin.feathr.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.SparkSqlExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class OfflineFeatureSourceTransformationFunctionBuilderTest {
  @Mock
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;
  private OfflineFeatureSourceTransformationFunctionBuilder _offlineFeatureSourceTransformationFunctionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _offlineFeatureSourceTransformationFunctionBuilder = new OfflineFeatureSourceTransformationFunctionBuilder(
        _transformationFunctionExpressionBuilder);
  }

  @AfterMethod
  public void afterTest() {
    reset(_transformationFunctionExpressionBuilder);
  }

  @Test
  public void testBuildMvelTransformation() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    MvelExpression mvelExpression = new MvelExpression();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfig)).thenReturn(
        mvelExpression);
    TransformationFunction transformationFunction = _offlineFeatureSourceTransformationFunctionBuilder.build(derivationConfig);
    assertTrue(transformationFunction.isMvelExpression());
    assertEquals(transformationFunction.getMvelExpression(), mvelExpression);
  }

  @Test
  public void testBuildSparkSqlTransformation() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    SparkSqlExpression sparkSqlExpression = new SparkSqlExpression();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfig)).thenReturn(
        sparkSqlExpression);
    TransformationFunction transformationFunction = _offlineFeatureSourceTransformationFunctionBuilder.build(derivationConfig);
    assertTrue(transformationFunction.isSparkSqlExpression());
    assertEquals(transformationFunction.getSparkSqlExpression(), sparkSqlExpression);
  }

  @Test
  public void testBuildUdfTransformation() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    UserDefinedFunction userDefinedFunction = new UserDefinedFunction();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfig)).thenReturn(
        userDefinedFunction);
    TransformationFunction transformationFunction = _offlineFeatureSourceTransformationFunctionBuilder.build(derivationConfig);
    assertTrue(transformationFunction.isUserDefinedFunction());
    assertEquals(transformationFunction.getUserDefinedFunction(), userDefinedFunction);
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testInvalidExpressionType() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    Object expression = mock(Object.class);
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfig)).thenReturn(expression);
    _offlineFeatureSourceTransformationFunctionBuilder.build(derivationConfig);
  }

}

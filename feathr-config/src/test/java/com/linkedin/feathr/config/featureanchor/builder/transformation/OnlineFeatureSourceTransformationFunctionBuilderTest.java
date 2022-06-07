package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.linkedin.feathr.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class OnlineFeatureSourceTransformationFunctionBuilderTest {
  @Mock
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;
  private OnlineFeatureSourceTransformationFunctionBuilder _onlineFeatureSourceTransformationFunctionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _onlineFeatureSourceTransformationFunctionBuilder = new OnlineFeatureSourceTransformationFunctionBuilder(
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
    TransformationFunction transformationFunction = _onlineFeatureSourceTransformationFunctionBuilder.build(derivationConfig);
    assertTrue(transformationFunction.isMvelExpression());
    assertEquals(transformationFunction.getMvelExpression(), mvelExpression);
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testInvalidExpressionType() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    Object expression = mock(Object.class);
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfig)).thenReturn(expression);
    _onlineFeatureSourceTransformationFunctionBuilder.build(derivationConfig);
  }
}

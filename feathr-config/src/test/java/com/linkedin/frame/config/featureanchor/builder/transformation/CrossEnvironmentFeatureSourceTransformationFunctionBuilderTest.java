package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.linkedin.data.DataMap;
import com.linkedin.frame.config.featureanchor.builder.transformation.CrossEnvironmentFeatureSourceTransformationFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.TransformationFunctionExpressionBuilder;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class CrossEnvironmentFeatureSourceTransformationFunctionBuilderTest {
  @Mock
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;
  private CrossEnvironmentFeatureSourceTransformationFunctionBuilder
      _crossEnvironmentFeatureSourceTransformationFunctionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _crossEnvironmentFeatureSourceTransformationFunctionBuilder = new CrossEnvironmentFeatureSourceTransformationFunctionBuilder(
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
    TransformationFunction transformationFunction =  _crossEnvironmentFeatureSourceTransformationFunctionBuilder.build(derivationConfig);
    assertTrue(transformationFunction.isMvelExpression());
    assertEquals(transformationFunction.getMvelExpression(), mvelExpression);
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testInvalidExpressionType() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    Object expression = mock(Object.class);
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(derivationConfig)).thenReturn(expression);
    _crossEnvironmentFeatureSourceTransformationFunctionBuilder.build(derivationConfig);
  }
}

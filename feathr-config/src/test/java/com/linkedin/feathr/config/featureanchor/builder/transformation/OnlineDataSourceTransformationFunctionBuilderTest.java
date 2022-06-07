package com.linkedin.feathr.config.featureanchor.builder.transformation;

import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.UserDefinedFunction;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class OnlineDataSourceTransformationFunctionBuilderTest {
  @Mock
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;
  private OnlineDataSourceTransformationFunctionBuilder _onlineDataSourceTransformationFunctionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _onlineDataSourceTransformationFunctionBuilder = new OnlineDataSourceTransformationFunctionBuilder(
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
    TransformationFunction transformationFunction = _onlineDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
    assertTrue(transformationFunction.isMvelExpression());
    assertEquals(transformationFunction.getMvelExpression(), mvelExpression);
  }

  @Test
  public void testBuildUDFTransformation() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    UserDefinedFunction userDefinedFunction = new UserDefinedFunction();
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(
        userDefinedFunction);
    TransformationFunction transformationFunction = _onlineDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
    assertTrue(transformationFunction.isUserDefinedFunction());
    assertEquals(transformationFunction.getUserDefinedFunction(), userDefinedFunction);
  }

  @Test(expectedExceptions = IllegalStateException.class)
  public void testInvalidExpressionType() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    Object expression = mock(Object.class);
    when(_transformationFunctionExpressionBuilder.buildTransformationExpression(featureConfig, anchorConfig)).thenReturn(expression);
    _onlineDataSourceTransformationFunctionBuilder.build(featureConfig, anchorConfig);
  }
}

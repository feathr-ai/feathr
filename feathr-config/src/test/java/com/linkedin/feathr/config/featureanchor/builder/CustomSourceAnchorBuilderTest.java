package com.linkedin.feathr.config.featureanchor.builder;

import com.linkedin.feathr.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.CustomSourceConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey;
import com.linkedin.feathr.featureDataModel.CustomDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

/**
 * Unit tests for {@link CustomSourceAnchorBuilder}
 */
public class CustomSourceAnchorBuilderTest {

  @Mock
  private AnchoredFeatureTransformationFunctionBuilder _anchoredFeatureTransformationFunctionBuilder;

  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  @Mock
  private KeyFunctionBuilder _keyFunctionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_anchoredFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder, _keyFunctionBuilder);
  }

  @Test
  public void testBuild() {
    CustomSourceConfig sourceConfig = new CustomSourceConfig("dummySource", "key[0]", "com.linkedin.some.service.SomeEntity");
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunctionForOnlineDataSource.TransformationFunction
        transformationFunction = new TransformationFunctionForOnlineDataSource.TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction
    );
    OnlineDataSourceKey.KeyFunction keyFunction = new OnlineDataSourceKey.KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    CustomSourceAnchorBuilder espressoAnchorBuilder = new CustomSourceAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);
    FeatureAnchor.Anchor anchor = espressoAnchorBuilder.build();
    CustomDataSource customDataSource = anchor.getCustomDataSourceAnchor().getSource();
    assertEquals(customDataSource.getDataModel().getFullyQualifiedName(), "com.linkedin.some.service.SomeEntity");
    assertEquals(customDataSource.getDataSourceRef(), "dummySource");
    assertEquals(anchor.getCustomDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getCustomDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
    assertEquals(anchor.getCustomDataSourceAnchor().getSource().getKeyFunction(), keyFunction);
  }
}
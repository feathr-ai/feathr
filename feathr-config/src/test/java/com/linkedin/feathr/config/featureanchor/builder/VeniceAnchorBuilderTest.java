package com.linkedin.feathr.config.featureanchor.builder;

import com.linkedin.feathr.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.VeniceConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey;
import com.linkedin.feathr.featureDataModel.VeniceDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class VeniceAnchorBuilderTest {
  @Mock
  private AnchoredFeatureTransformationFunctionBuilder _anchoredFeatureTransformationFunctionBuilder;

  @Mock
  private KeyFunctionBuilder _keyFunctionBuilder;

  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

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
    String storeName = "vtstore";
    String keyExpr = "{\"memberId\" : (Integer)key[0], \"version\" : \"v2\"}";
    VeniceConfig sourceConfig = new VeniceConfig("dummySource", storeName, keyExpr);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    OnlineDataSourceKey.KeyFunction keyFunction = new OnlineDataSourceKey.KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    VeniceAnchorBuilder veniceAnchorBuilder = new VeniceAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);
    Anchor anchor = veniceAnchorBuilder.build();
    VeniceDataSource veniceDataSource = anchor.getVeniceDataSourceAnchor().getSource();
    assertEquals(veniceDataSource.getStoreName(), storeName);
    assertEquals(veniceDataSource.getDataSourceRef(), "dummySource");
    assertEquals(anchor.getVeniceDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getVeniceDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
    assertEquals(anchor.getVeniceDataSourceAnchor().getSource().getKeyFunction(), keyFunction);
  }
}

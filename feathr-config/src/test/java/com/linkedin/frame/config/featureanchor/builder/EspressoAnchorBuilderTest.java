package com.linkedin.frame.config.featureanchor.builder;

import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.EspressoConfig;

import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey;
import com.linkedin.feathr.featureDataModel.EspressoDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import java.net.URI;
import java.net.URISyntaxException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class EspressoAnchorBuilderTest {
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
  public void testBuild() throws URISyntaxException {
    EspressoConfig sourceConfig = new EspressoConfig("dummySource", "CareersPreferenceDB",
        "MemberPreference", "d2://EI_ESPRESSO_MT2", null);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction
    );
    OnlineDataSourceKey.KeyFunction keyFunction = new OnlineDataSourceKey.KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    EspressoAnchorBuilder espressoAnchorBuilder = new EspressoAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);
    Anchor anchor = espressoAnchorBuilder.build();
    EspressoDataSource espressoDataSource = anchor.getEspressoDataSourceAnchor().getSource();
    assertEquals(espressoDataSource.getDatabaseName(), "CareersPreferenceDB");
    assertEquals(espressoDataSource.getTableName(), "MemberPreference");
    assertEquals(espressoDataSource.getD2Uri(), new URI("d2://EI_ESPRESSO_MT2"));
    assertEquals(espressoDataSource.getDataSourceRef(),"dummySource");
    assertEquals(anchor.getEspressoDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getEspressoDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
    assertEquals(anchor.getEspressoDataSourceAnchor().getSource().getKeyFunction(), keyFunction);
  }
}

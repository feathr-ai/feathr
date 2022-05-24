package com.linkedin.frame.config;

import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.config.featureanchor.FeatureAnchorBuilder;
import com.linkedin.frame.config.featureversion.FeatureVersionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.AnchorsConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.SourcesConfig;
import com.linkedin.frame.core.utils.MlFeatureVersionUrnCreator;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.compute.FeatureVersion;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class AnchoredFeatureDefinitionLoaderTest {
  @Mock
  private MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator;
  @Mock
  private FeatureAnchorBuilder _featureAnchorBuilder;
  @Mock
  private FeatureVersionBuilder _featureVersionBuilder;
  private AnchoredFeatureDefinitionLoader _anchoredFeatureDefinitionLoader;

  @BeforeMethod
  public void setup() {
    MockitoAnnotations.initMocks(this);
    _anchoredFeatureDefinitionLoader = new AnchoredFeatureDefinitionLoader(_mlFeatureVersionUrnCreator,
        _featureAnchorBuilder, _featureVersionBuilder);
  }

  @AfterMethod
  public void afterTest() {
    reset(_mlFeatureVersionUrnCreator, _featureAnchorBuilder, _featureVersionBuilder);
  }

  @Test
  public void testExtractAnchorConfigs() {
    Map<String, AnchorConfig> anchorConfigs = new HashMap<>();
    AnchorConfig anchorConfig1 = mock(AnchorConfig.class);
    AnchorConfig anchorConfig2 = mock(AnchorConfig.class);
    AnchorConfig anchorConfig3 = mock(AnchorConfig.class);
    FeatureConfig featureConfig11 = mock(FeatureConfig.class);
    FeatureConfig featureConfig12 = mock(FeatureConfig.class);
    FeatureConfig featureConfig21 = mock(FeatureConfig.class);
    FeatureConfig featureConfig22 = mock(FeatureConfig.class);
    FeatureConfig featureConfig31 = mock(FeatureConfig.class);
    Map<String, FeatureConfig> featureConfigs1 = new HashMap<>();
    Map<String, FeatureConfig> featureConfigs2 = new HashMap<>();
    Map<String, FeatureConfig> featureConfigs3 = new HashMap<>();
    featureConfigs1.put("feature1", featureConfig11);
    featureConfigs1.put("feature2", featureConfig12);
    featureConfigs2.put("feature1", featureConfig21);
    featureConfigs2.put("feature2", featureConfig22);
    featureConfigs3.put("feature1", featureConfig31);
    anchorConfigs.put("anchor1", anchorConfig1);
    anchorConfigs.put("anchor2", anchorConfig2);
    anchorConfigs.put("anchor3", anchorConfig3);
    when(anchorConfig1.getFeatures()).thenReturn(featureConfigs1);
    when(anchorConfig2.getFeatures()).thenReturn(featureConfigs2);
    when(anchorConfig3.getFeatures()).thenReturn(featureConfigs3);
    AnchorsConfig anchorsConfig = new AnchorsConfig(anchorConfigs);
    Map<String, List<AnchorConfig>> actual = _anchoredFeatureDefinitionLoader.extractAnchorConfigs(anchorsConfig);
    assertEquals(actual.keySet().size(), 2);
    assertTrue(actual.containsKey("feature1"));
    assertTrue(actual.containsKey("feature2"));
    assertEquals(actual.get("feature1").size(), 3);
    assertEquals(actual.get("feature2").size(), 2);
    assertTrue(actual.get("feature1").containsAll(Arrays.asList(anchorConfig1, anchorConfig2, anchorConfig3)));
    assertTrue(actual.get("feature2").containsAll(Arrays.asList(anchorConfig1, anchorConfig2)));
  }

  @Test
  public void testBuildAnchoredFeatureDefinition() throws URISyntaxException {
    AnchorsConfig anchorsConfig = mock(AnchorsConfig.class);
    SourcesConfig sourcesConfig = mock(SourcesConfig.class);
    AnchoredFeatureDefinitionLoader spy = spy(_anchoredFeatureDefinitionLoader);
    Map<String, List<AnchorConfig>> anchorConfigMap = new HashMap<>();
    AnchorConfig anchorConfig1 = mock(AnchorConfig.class);
    AnchorConfig anchorConfig2 = mock(AnchorConfig.class);
    AnchorConfig anchorConfig3 = mock(AnchorConfig.class);
    FeatureConfig featureConfig1 = mock(FeatureConfig.class);
    FeatureConfig featureConfig2 = mock(FeatureConfig.class);
    FeatureConfig featureConfig3 = mock(FeatureConfig.class);
    FeatureConfig featureConfig4 = mock(FeatureConfig.class);
    Map<String, FeatureConfig> featureConfigMap1 = new HashMap<>();
    featureConfigMap1.put("feature1", featureConfig1);
    Map<String, FeatureConfig> featureConfigMap2 = new HashMap<>();
    featureConfigMap2.put("feature1", featureConfig2);
    featureConfigMap2.put("feature2", featureConfig3);
    Map<String, FeatureConfig> featureConfigMap3 = new HashMap<>();
    featureConfigMap3.put("feature1", featureConfig4);
    anchorConfigMap.put("feature1", Arrays.asList(anchorConfig1, anchorConfig2, anchorConfig3));
    anchorConfigMap.put("feature2", Arrays.asList(anchorConfig2));
    doReturn(anchorConfigMap).when(spy).extractAnchorConfigs(anchorsConfig);
    MlFeatureVersionUrn featureVersionUrn1 = MlFeatureVersionUrn.deserialize(
        "urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature1),1,2,3)");
    MlFeatureVersionUrn featureVersionUrn2 = MlFeatureVersionUrn.deserialize(
        "urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature2),3,2,1)");
    when(_mlFeatureVersionUrnCreator.create("feature1")).thenReturn(featureVersionUrn1);
    when(_mlFeatureVersionUrnCreator.create("feature2")).thenReturn(featureVersionUrn2);
    FeatureAnchor featureAnchor1 = mock(FeatureAnchor.class);
    FeatureAnchor featureAnchor2 = mock(FeatureAnchor.class);
    FeatureAnchor featureAnchor3 = mock(FeatureAnchor.class);
    FeatureAnchor featureAnchor4 = mock(FeatureAnchor.class);
    when(_featureAnchorBuilder.buildAnchoredFeatureAnchor(sourcesConfig, anchorConfig1, featureConfig1)).thenReturn(
        featureAnchor1);
    when(_featureAnchorBuilder.buildAnchoredFeatureAnchor(sourcesConfig, anchorConfig2, featureConfig2)).thenReturn(
        featureAnchor2);
    when(_featureAnchorBuilder.buildAnchoredFeatureAnchor(sourcesConfig, anchorConfig2, featureConfig3)).thenReturn(
        featureAnchor3);
    when(_featureAnchorBuilder.buildAnchoredFeatureAnchor(sourcesConfig, anchorConfig3, featureConfig4)).thenReturn(
        featureAnchor4);
    when(anchorConfig1.getFeatures()).thenReturn(featureConfigMap1);
    when(anchorConfig2.getFeatures()).thenReturn(featureConfigMap2);
    when(anchorConfig3.getFeatures()).thenReturn(featureConfigMap3);
    FeatureVersion featureVersion1 = mock(FeatureVersion.class);
    FeatureVersion featureVersion2 = mock(FeatureVersion.class);
    when(_featureVersionBuilder.build(featureConfig1)).thenReturn(featureVersion1);
    when(_featureVersionBuilder.build(featureConfig2)).thenReturn(featureVersion1);
    when(_featureVersionBuilder.build(featureConfig3)).thenReturn(featureVersion2);
    when(_featureVersionBuilder.build(featureConfig4)).thenReturn(featureVersion1);
    Map<MlFeatureVersionUrn, FeatureDefinition> actual = spy.loadAnchoredFeatureDefinition(anchorsConfig, sourcesConfig);
    assertEquals(actual.size(), 2);
    assertTrue(actual.containsKey(featureVersionUrn1));
    assertTrue(actual.containsKey(featureVersionUrn2));
    assertEquals(actual.get(featureVersionUrn1).getFeatureVersion(), featureVersion1);
    assertEquals(actual.get(featureVersionUrn2).getFeatureVersion(), featureVersion2);
    assertEquals(actual.get(featureVersionUrn1).getFeatureAnchors().size(), 3);
    assertEquals(actual.get(featureVersionUrn2).getFeatureAnchors().size(), 1);
    assertTrue(actual.get(featureVersionUrn1).getFeatureAnchors().containsAll(Arrays.asList(featureAnchor1,
        featureAnchor2, featureAnchor4)));
    assertTrue(actual.get(featureVersionUrn2).getFeatureAnchors().containsAll(Arrays.asList(featureAnchor3)));
  }
}

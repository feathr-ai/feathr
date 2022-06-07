package com.linkedin.feathr.config;

import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.feathr.core.config.producer.FeatureDefConfig;
import com.linkedin.feathr.core.config.producer.anchors.AnchorsConfig;
import com.linkedin.feathr.core.config.producer.derivations.DerivationsConfig;
import com.linkedin.feathr.core.config.producer.sources.SourcesConfig;
import com.linkedin.feathr.core.configbuilder.ConfigBuilder;
import com.linkedin.feathr.core.configdataprovider.ConfigDataProvider;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class FeatureDefinitionLoaderTest {
  @Mock
  private ConfigBuilder _configBuilder;
  @Mock
  private AnchoredFeatureDefinitionLoader _anchoredFeatureDefinitionLoader;
  @Mock
  private DerivedFeatureDefinitionLoader _derivedFeatureDefinitionLoader;

  private FeatureDefinitionLoader _featureDefinitionLoader;

  @BeforeMethod
  public void setup() {
    MockitoAnnotations.initMocks(this);
    _featureDefinitionLoader = new FeatureDefinitionLoader(_configBuilder,
        _anchoredFeatureDefinitionLoader, _derivedFeatureDefinitionLoader);
  }

  @AfterMethod
  public void afterTest() {
    reset(_configBuilder, _anchoredFeatureDefinitionLoader, _derivedFeatureDefinitionLoader);
  }

  @Test
  public void testGetDefinitions() throws URISyntaxException {
    MlFeatureVersionUrn featureVersionUrn1 = MlFeatureVersionUrn.deserialize(
        "urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature1),1,2,3)");
    MlFeatureVersionUrn featureVersionUrn2 = MlFeatureVersionUrn.deserialize(
        "urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature2),3,2,1)");
    MlFeatureVersionUrn featureVersionUrn3 = MlFeatureVersionUrn.deserialize(
        "urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature2),2,3,1)");
    ConfigDataProvider configDataProvider = mock(ConfigDataProvider.class);
    FeatureDefConfig featureDefConfig = mock(FeatureDefConfig.class);
    SourcesConfig sourcesConfig = mock(SourcesConfig.class);
    AnchorsConfig anchorsConfig = mock(AnchorsConfig.class);
    DerivationsConfig derivationsConfig = mock(DerivationsConfig.class);
    when(featureDefConfig.getSourcesConfig()).thenReturn(Optional.of(sourcesConfig));
    when(featureDefConfig.getAnchorsConfig()).thenReturn(Optional.of(anchorsConfig));
    when(featureDefConfig.getDerivationsConfig()).thenReturn(Optional.of(derivationsConfig));
    when(_configBuilder.buildFeatureDefConfig(configDataProvider)).thenReturn(featureDefConfig);
    Map<MlFeatureVersionUrn, FeatureDefinition> anchoredFeatureDefinitions = new HashMap<>();
    Map<MlFeatureVersionUrn, FeatureDefinition> derivedFeatureDefinitions = new HashMap<>();
    FeatureDefinition featureDefinition1 = mock(FeatureDefinition.class);
    FeatureDefinition featureDefinition2 = mock(FeatureDefinition.class);
    FeatureDefinition featureDefinition3 = mock(FeatureDefinition.class);
    anchoredFeatureDefinitions.put(featureVersionUrn1, featureDefinition1);
    anchoredFeatureDefinitions.put(featureVersionUrn2, featureDefinition2);
    derivedFeatureDefinitions.put(featureVersionUrn3, featureDefinition3);
    when(_anchoredFeatureDefinitionLoader.loadAnchoredFeatureDefinition(anchorsConfig, sourcesConfig))
        .thenReturn(anchoredFeatureDefinitions);
    when(_derivedFeatureDefinitionLoader.loadDerivationFeatureDefinition(derivationsConfig, FeatureAnchorEnvironment.ONLINE))
        .thenReturn(derivedFeatureDefinitions);
    Map<MlFeatureVersionUrn, FeatureDefinition> actual = _featureDefinitionLoader.loadAllFeatureDefinitions(configDataProvider,
        FeatureAnchorEnvironment.ONLINE);
    assertEquals(actual.size(), 3);
    assertTrue(actual.containsKey(featureVersionUrn1));
    assertTrue(actual.containsKey(featureVersionUrn2));
    assertTrue(actual.containsKey(featureVersionUrn3));
    assertEquals(actual.get(featureVersionUrn1), featureDefinition1);
    assertEquals(actual.get(featureVersionUrn2), featureDefinition2);
    assertEquals(actual.get(featureVersionUrn3), featureDefinition3);
  }



  @Test
  public void testGetFeatureDefinitions() throws URISyntaxException {
    MlFeatureVersionUrn featureVersionUrn1 = MlFeatureVersionUrn.deserialize(
        "urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature1),1,2,3)");
    MlFeatureVersionUrn featureVersionUrn2 = MlFeatureVersionUrn.deserialize(
        "urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature2),3,2,1)");
    MlFeatureVersionUrn featureVersionUrn3 = MlFeatureVersionUrn.deserialize(
        "urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature2),2,3,1)");
    ConfigDataProvider configDataProvider = mock(ConfigDataProvider.class);
    FeatureDefConfig featureDefConfig = mock(FeatureDefConfig.class);
    SourcesConfig sourcesConfig = mock(SourcesConfig.class);
    AnchorsConfig anchorsConfig = mock(AnchorsConfig.class);
    DerivationsConfig derivationsConfig = mock(DerivationsConfig.class);
    when(featureDefConfig.getSourcesConfig()).thenReturn(Optional.of(sourcesConfig));
    when(featureDefConfig.getAnchorsConfig()).thenReturn(Optional.of(anchorsConfig));
    when(featureDefConfig.getDerivationsConfig()).thenReturn(Optional.of(derivationsConfig));
    when(_configBuilder.buildFeatureDefConfig(configDataProvider)).thenReturn(featureDefConfig);
    Map<MlFeatureVersionUrn, FeatureDefinition> anchoredFeatureDefinitions = new HashMap<>();
    Map<MlFeatureVersionUrn, FeatureDefinition> derivedFeatureDefinitions = new HashMap<>();
    FeatureDefinition featureDefinition1 = mock(FeatureDefinition.class);
    FeatureDefinition featureDefinition2 = mock(FeatureDefinition.class);
    FeatureDefinition featureDefinition3 = mock(FeatureDefinition.class);
    anchoredFeatureDefinitions.put(featureVersionUrn1, featureDefinition1);
    anchoredFeatureDefinitions.put(featureVersionUrn2, featureDefinition2);
    derivedFeatureDefinitions.put(featureVersionUrn3, featureDefinition3);
    when(_anchoredFeatureDefinitionLoader.loadAnchoredFeatureDefinition(anchorsConfig, sourcesConfig))
        .thenReturn(anchoredFeatureDefinitions);
    when(_derivedFeatureDefinitionLoader.loadDerivationFeatureDefinition(derivationsConfig, FeatureAnchorEnvironment.ONLINE))
        .thenReturn(derivedFeatureDefinitions);
    Map<MlFeatureVersionUrn, FeatureDefinition> actual = _featureDefinitionLoader
        .loadFeatureDefinitions(configDataProvider, Arrays.asList(featureVersionUrn1, featureVersionUrn3),
            FeatureAnchorEnvironment.ONLINE);
    assertEquals(actual.size(), 2);
    assertTrue(actual.containsKey(featureVersionUrn1));
    assertFalse(actual.containsKey(featureVersionUrn2));
    assertTrue(actual.containsKey(featureVersionUrn3));
    assertEquals(actual.get(featureVersionUrn1), featureDefinition1);
    assertEquals(actual.get(featureVersionUrn3), featureDefinition3);
  }
}

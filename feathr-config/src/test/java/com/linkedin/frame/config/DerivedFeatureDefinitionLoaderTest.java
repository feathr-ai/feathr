package com.linkedin.frame.config;

import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.config.featureanchor.FeatureAnchorBuilder;
import com.linkedin.frame.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.frame.config.featureversion.FeatureVersionBuilder;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.frame.core.config.producer.derivations.DerivationsConfig;
import com.linkedin.frame.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.frame.core.utils.MlFeatureVersionUrnCreator;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.compute.FeatureVersion;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class DerivedFeatureDefinitionLoaderTest {
  @Mock
  private MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator;
  @Mock
  private FeatureAnchorBuilder _featureAnchorBuilder;
  @Mock
  private FeatureVersionBuilder _featureVersionBuilder;
  private DerivedFeatureDefinitionLoader _derivedFeatureDefinitionLoader;

  @BeforeMethod
  public void setup() {
    MockitoAnnotations.initMocks(this);
    _derivedFeatureDefinitionLoader = new DerivedFeatureDefinitionLoader(_mlFeatureVersionUrnCreator,
        _featureAnchorBuilder, _featureVersionBuilder);
  }

  @AfterMethod
  public void afterTest() {
    reset(_mlFeatureVersionUrnCreator, _featureAnchorBuilder, _featureVersionBuilder);
  }

  @Test
  public void testBuildDerivationFeatureDefinition() throws URISyntaxException {
    DerivationsConfig derivationsConfig = mock(DerivationsConfig.class);
    Map<String, DerivationConfig> derivationConfigMap = new HashMap<>();
    DerivationConfig derivationConfig1 = mock(SimpleDerivationConfig.class);
    DerivationConfig derivationConfig2 = mock(DerivationConfigWithExpr.class);
    derivationConfigMap.put("feature1", derivationConfig1);
    derivationConfigMap.put("feature2", derivationConfig2);
    when(derivationsConfig.getDerivations()).thenReturn(derivationConfigMap);
    FeatureAnchor featureAnchor1 = mock(FeatureAnchor.class);
    FeatureAnchor featureAnchor2 = mock(FeatureAnchor.class);
    when(_featureAnchorBuilder.buildDerivationFeatureAnchor(derivationConfig1, FeatureAnchorEnvironment.ONLINE)).thenReturn(featureAnchor1);
    when(_featureAnchorBuilder.buildDerivationFeatureAnchor(derivationConfig2, FeatureAnchorEnvironment.ONLINE)).thenReturn(featureAnchor2);
    MlFeatureVersionUrn featureVersionUrn1 = MlFeatureVersionUrn.deserialize("urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature1),1,2,3)");
    MlFeatureVersionUrn featureVersionUrn2 = MlFeatureVersionUrn.deserialize("urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature2),3,2,1)");
    when(_mlFeatureVersionUrnCreator.create("feature1")).thenReturn(featureVersionUrn1);
    when(_mlFeatureVersionUrnCreator.create("feature2")).thenReturn(featureVersionUrn2);
    FeatureVersion featureVersion1 = mock(FeatureVersion.class);
    FeatureVersion featureVersion2 = mock(FeatureVersion.class);
    when(_featureVersionBuilder.build(derivationConfig1)).thenReturn(featureVersion1);
    when(_featureVersionBuilder.build(derivationConfig2)).thenReturn(featureVersion2);
    Map<MlFeatureVersionUrn, FeatureDefinition> actual =
        _derivedFeatureDefinitionLoader.loadDerivationFeatureDefinition(derivationsConfig, FeatureAnchorEnvironment.ONLINE);
    assertEquals(actual.size(), 2);
    assertTrue(actual.containsKey(featureVersionUrn1));
    assertTrue(actual.containsKey(featureVersionUrn2));
    assertEquals(actual.get(featureVersionUrn1).getFeatureVersion(), featureVersion1);
    assertEquals(actual.get(featureVersionUrn2).getFeatureVersion(), featureVersion2);
    assertEquals(actual.get(featureVersionUrn1).getFeatureAnchors().size(), 1);
    assertEquals(actual.get(featureVersionUrn2).getFeatureAnchors().size(), 1);
    assertTrue(actual.get(featureVersionUrn1).getFeatureAnchors().equals(Collections.singleton(featureAnchor1)));
    assertTrue(actual.get(featureVersionUrn2).getFeatureAnchors().equals(Collections.singleton(featureAnchor2)));
  }
}

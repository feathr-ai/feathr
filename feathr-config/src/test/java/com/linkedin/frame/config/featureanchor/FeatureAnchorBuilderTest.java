package com.linkedin.frame.config.featureanchor;

import com.linkedin.data.DataMap;
import com.linkedin.frame.config.featureanchor.builder.AnchorBuilder;
import com.linkedin.frame.config.featureanchor.builder.AnchorBuilderFactory;
import com.linkedin.frame.core.config.producer.ExprType;
import com.linkedin.frame.core.config.producer.TypedExpr;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.frame.core.config.producer.sources.SourceConfig;
import com.linkedin.frame.core.config.producer.sources.SourcesConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import java.util.HashMap;
import java.util.Map;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;


public class FeatureAnchorBuilderTest {
  @Mock
  private AnchorBuilderFactory _anchorBuilderFactory;
  @Mock
  private AnchorBuilder _anchorBuilder;

  private FeatureAnchorBuilder _featureAnchorBuilder;

  @BeforeMethod
  public void setup() {
    MockitoAnnotations.initMocks(this);
    _featureAnchorBuilder = new FeatureAnchorBuilder(_anchorBuilderFactory);
  }

  @AfterMethod
  public void afterTest() {
    reset(_anchorBuilderFactory, _anchorBuilder);
  }

  @Test
  public void testBuildAnchoredFeatureAnchorWhenSourcesSectionExists() {
    SourcesConfig sourcesConfig = mock(SourcesConfig.class);
    Map<String, SourceConfig> sourceConfigMap = new HashMap<>();
    SourceConfig sourceConfig1 = mock(SourceConfig.class);
    SourceConfig sourceConfig2 = mock(SourceConfig.class);
    sourceConfigMap.put("source1", sourceConfig1);
    sourceConfigMap.put("source2", sourceConfig2);
    when(sourcesConfig.getSources()).thenReturn(sourceConfigMap);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    when(anchorConfig.getSource()).thenReturn("source2");
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    when(_anchorBuilderFactory.getDataSourceAnchorBuilder(sourceConfig2, featureConfig, anchorConfig))
        .thenReturn(_anchorBuilder);
    Anchor anchor = mock(Anchor.class);
    when(anchor.data()).thenReturn(mock(DataMap.class));
    when(_anchorBuilder.build()).thenReturn(anchor);
    FeatureAnchor featureAnchor = _featureAnchorBuilder.buildAnchoredFeatureAnchor(sourcesConfig, anchorConfig, featureConfig);
    assertEquals(featureAnchor.getAnchor(), anchor);
  }

  @Test
  public void testBuildAnchoredFeatureAnchorWhenSourcesSectionNotExists() {
    SourcesConfig sourcesConfig = mock(SourcesConfig.class);
    Map<String, SourceConfig> sourceConfigMap = new HashMap<>();
    SourceConfig sourceConfig1 = mock(SourceConfig.class);
    sourceConfigMap.put("source1", sourceConfig1);
    when(sourcesConfig.getSources()).thenReturn(sourceConfigMap);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    when(anchorConfig.getSource()).thenReturn("testHdfsPath");
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    when(_anchorBuilderFactory.getHdfsAnchorBuilderByInlineSource("testHdfsPath",  anchorConfig, featureConfig))
        .thenReturn(_anchorBuilder);
    Anchor anchor = mock(Anchor.class);
    when(anchor.data()).thenReturn(mock(DataMap.class));
    when(_anchorBuilder.build()).thenReturn(anchor);
    FeatureAnchor featureAnchor = _featureAnchorBuilder.buildAnchoredFeatureAnchor(sourcesConfig, anchorConfig, featureConfig);
    assertEquals(featureAnchor.getAnchor(), anchor);
  }

  @Test
  public void testBuildDerivationFeatureAnchor() {
    DerivationConfig derivationConfig = new SimpleDerivationConfig(new TypedExpr("a + b", ExprType.MVEL));
    when(_anchorBuilderFactory.getFeatureSourcesAnchorBuilder(derivationConfig, FeatureAnchorEnvironment.ONLINE))
        .thenReturn(_anchorBuilder);
    Anchor anchor = mock(Anchor.class);
    when(anchor.data()).thenReturn(mock(DataMap.class));
    //Need to mock .data() to pass FeatureAnchor::setAnchor data safety check
    when(anchor.data()).thenReturn(mock(DataMap.class));
    when(_anchorBuilder.build()).thenReturn(anchor);
    FeatureAnchor featureAnchor = _featureAnchorBuilder.buildDerivationFeatureAnchor(derivationConfig, FeatureAnchorEnvironment.ONLINE);
    verify(_anchorBuilderFactory).getFeatureSourcesAnchorBuilder(derivationConfig, FeatureAnchorEnvironment.ONLINE);
    assertEquals(featureAnchor.getAnchor(), anchor);
  }
}

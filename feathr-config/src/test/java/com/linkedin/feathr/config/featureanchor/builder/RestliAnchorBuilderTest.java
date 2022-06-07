package com.linkedin.feathr.config.featureanchor.builder;

import com.linkedin.data.DataMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey;
import com.linkedin.feathr.featureDataModel.RestliDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
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


public class RestliAnchorBuilderTest {
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
    reset(_anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);
  }

  @Test
  public void testBuild() {
    RestliConfig sourceConfig = buildRestliConfig("dummySource");
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    OnlineDataSourceKey.KeyFunction keyFunction = new OnlineDataSourceKey.KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    RestliAnchorBuilder restliAnchorBuilder = new RestliAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);
    Anchor anchor = restliAnchorBuilder.build();

    RestliDataSource restliDataSource = anchor.getRestliDataSourceAnchor().getSource();
    assertEquals(restliDataSource.getResourceName(), "setOperations");
    assertEquals(restliDataSource.getDataSourceRef(), "dummySource");
    assertTrue(restliDataSource.getProjections().containsAll(Arrays.asList("positions", "members")));
    assertEquals(anchor.getRestliDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getRestliDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
    assertEquals(anchor.getRestliDataSourceAnchor().getSource().getKeyFunction(), keyFunction);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testSetFinder() {
    RestliConfig sourceConfig = mock(RestliConfig.class);
    when(sourceConfig.getFinder()).thenReturn(Optional.of("testFinder"));
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    RestliAnchorBuilder restliAnchorBuilder = new RestliAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);
    restliAnchorBuilder.build();
  }

  static RestliConfig buildRestliConfig(String sourceName) {
    String resourceName = "setOperations";

    String keyExpr = "toUrn(\\\"member\\\", key[0])";

    Map<String, String> map = new HashMap<>();
    map.put("firstEdgeType", "MemberToMember");
    map.put("secondEdgeType", "MemberToMember");
    DataMap dataMap = new DataMap(map);

    String mvelExpr = "key[1]";

    Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.put("operator", "INTERSECT");
    paramsMap.put("edgeSetSpecifications", dataMap);
    paramsMap.put("second", mvelExpr);

    PathSpec pathSpec = new PathSpec("positions", "members");

    return new RestliConfig(sourceName, resourceName, keyExpr, paramsMap, pathSpec);
  }
}

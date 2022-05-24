package com.linkedin.frame.config.featureanchor.builder;

import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.RestliFinderDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource;
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


public class RestliFinderAnchorBuilderTest {
  private static final String DUMMY_SOURCE_NAME = "dummyRestLiSource";

  @Mock
  private AnchoredFeatureTransformationFunctionBuilder _anchoredFeatureTransformationFunctionBuilder;
  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_anchoredFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder);
  }

  @Test
  public void testBuild() {
    RestliConfig sourceConfig = buildRestliConfigWithFinder();
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunctionForOnlineDataSource.TransformationFunction
        transformationFunction = new TransformationFunctionForOnlineDataSource.TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);

    RestliFinderAnchorBuilder restliAnchorBuilder = new RestliFinderAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder);
    Anchor anchor = restliAnchorBuilder.build();

    RestliFinderDataSource restliFinderDataSource = anchor.getRestliFinderDataSourceAnchor().getSource();
    assertEquals(restliFinderDataSource.getResourceName(), "setOperations");
    assertEquals(restliFinderDataSource.getFinderMethod(), "testFinder");
    assertEquals(restliFinderDataSource.getDataSourceRef(), DUMMY_SOURCE_NAME);
    assertTrue(restliFinderDataSource.getProjections().containsAll(Arrays.asList("positions", "members")));
    assertEquals(anchor.getRestliFinderDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getRestliFinderDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
  }

  @Test(expectedExceptions = NullPointerException.class)
  public void testNotSetFinder() {
    RestliConfig sourceConfig = mock(RestliConfig.class);
    when(sourceConfig.getFinder()).thenReturn(Optional.empty());
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    RestliFinderAnchorBuilder restliFinderAnchorBuilder = new RestliFinderAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder);
    restliFinderAnchorBuilder.build();
  }

  static RestliConfig buildRestliConfigWithFinder() {
    return buildRestliConfigWithFinder(DUMMY_SOURCE_NAME);
  }

  static RestliConfig buildRestliConfigWithFinder(String sourceName) {
    String resourceName = "setOperations";

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

    return new RestliConfig(sourceName, resourceName, paramsMap, pathSpec, "testFinder");
  }
}

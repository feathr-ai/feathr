package com.linkedin.feathr.config.featureanchor.builder;

import com.linkedin.data.DataMap;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.featureDataModel.RequestParameterValue;
import com.linkedin.feathr.featureDataModel.RequestParameterValueMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class BaseRestliAnchorBuilderTest {
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
    reset( _keyPlaceholdersBuilder, _keyFunctionBuilder);
  }

  @Test
  public void testBuildStaticRequestParameterValueMap() {
    RestliConfig sourceConfig = mock(RestliConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    RestliAnchorBuilder restliAnchorBuilder = new RestliAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);

    Map<String, Object> params = new HashMap<>();
    params.put("param1", "testParam1");
    params.put("param2", Arrays.asList("testListElement1", "testListElement2"));
    DataMap dataMap = new DataMap();
    dataMap.put("testMapKey1", "testMapValue1");
    dataMap.put("testMapKey2", "testMapValue2");
    params.put("param3", dataMap);
    RequestParameterValueMap requestParameterValueMap = restliAnchorBuilder.buildRequestParameterValueMap(params);
    RequestParameterValue value1 = requestParameterValueMap.get("param1");
    assertTrue(value1.isJsonString());
    assertEquals(value1.getJsonString(), "testParam1");
    RequestParameterValue value2 = requestParameterValueMap.get("param2");
    assertTrue(value2.isJsonString());
    assertEquals(value2.getJsonString(), "[\"testListElement1\",\"testListElement2\"]");
    RequestParameterValue value3 = requestParameterValueMap.get("param3");
    assertTrue(value3.isJsonString());
    assertEquals(value3.getJsonString(), "{\"testMapKey1\":\"testMapValue1\",\"testMapKey2\":\"testMapValue2\"}");

  }

  @Test
  public void testBuildMvelRequestParameterValueMap() {
    RestliConfig sourceConfig = mock(RestliConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    RestliAnchorBuilder restliAnchorBuilder = new RestliAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);

    Map<String, Object> params = new HashMap<>();
    String mvelExpr = "key[0]";
    DataMap param1DataMap = new DataMap();
    param1DataMap.put(RestliConfig.MVEL_KEY, mvelExpr);

    params.put("param1", param1DataMap);
    params.put("param2", Arrays.asList("testListElement1", "testListElement2"));

    DataMap dataMap = new DataMap();
    dataMap.put("testMapKey1", "testMapValue1");
    dataMap.put("testMapKey2", "testMapValue2");
    params.put("param3", dataMap);

    RequestParameterValueMap requestParameterValueMap = restliAnchorBuilder.buildRequestParameterValueMap(params);
    RequestParameterValue value1 = requestParameterValueMap.get("param1");
    assertTrue(value1.isMvelExpression());
    assertEquals(value1.getMvelExpression().getMvel(), mvelExpr);
    RequestParameterValue value2 = requestParameterValueMap.get("param2");
    assertTrue(value2.isJsonString());
    assertEquals(value2.getJsonString(), "[\"testListElement1\",\"testListElement2\"]");
    RequestParameterValue value3 = requestParameterValueMap.get("param3");
    assertTrue(value3.isJsonString());
    assertEquals(value3.getJsonString(), "{\"testMapKey1\":\"testMapValue1\",\"testMapKey2\":\"testMapValue2\"}");

  }
}

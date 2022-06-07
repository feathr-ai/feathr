package com.linkedin.feathr.config.featureanchor.builder.key;

import com.linkedin.feathr.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.feathr.core.config.producer.sources.EspressoConfig;
import com.linkedin.feathr.core.config.producer.sources.PinotConfig;
import com.linkedin.feathr.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.core.config.producer.sources.SourceConfig;
import com.linkedin.feathr.core.config.producer.sources.VeniceConfig;
import com.linkedin.feathr.featureDataModel.KeyPlaceholder;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class OnlineKeyPlaceholdersBuilderTest {
  @Test
  public void testBuild() {
    KeyPlaceholderArray expected = new KeyPlaceholderArray(Arrays.asList(
        new KeyPlaceholder().setKeyPlaceholderRef("key[0]"), new KeyPlaceholder().setKeyPlaceholderRef("key[1]")));
    RestliConfig restliConfig = mock(RestliConfig.class);
    when(restliConfig.getOptionalKeyExpr()).thenReturn(Optional.of("{\"srcId\" : (long)key[0], \"version\" : (int)key[1]}"));
    OnlineAnchoredFeatureKeyPlaceholdersBuilder onlineAnchoredFeatureKeyPlaceholdersBuilder =
        new OnlineAnchoredFeatureKeyPlaceholdersBuilder(restliConfig);
    KeyPlaceholderArray actual = onlineAnchoredFeatureKeyPlaceholdersBuilder.build();
    assertEqualsNoOrder(actual.toArray(), expected.toArray());

    CouchbaseConfig couchbaseConfig = mock(CouchbaseConfig.class);
    when(couchbaseConfig.getKeyExpr()).thenReturn("{\"srcId\" : (long)key[0], \"version\" : (int)key[1]}");
    onlineAnchoredFeatureKeyPlaceholdersBuilder = new OnlineAnchoredFeatureKeyPlaceholdersBuilder(couchbaseConfig);
    actual = onlineAnchoredFeatureKeyPlaceholdersBuilder.build();
    assertEqualsNoOrder(actual.toArray(), expected.toArray());

    EspressoConfig espressoConfig = mock(EspressoConfig.class);
    when(espressoConfig.getKeyExpr()).thenReturn("{\"srcId\" : (long)key[0], \"version\" : (int)key[1]}");
    onlineAnchoredFeatureKeyPlaceholdersBuilder = new OnlineAnchoredFeatureKeyPlaceholdersBuilder(espressoConfig);
    actual = onlineAnchoredFeatureKeyPlaceholdersBuilder.build();
    assertEqualsNoOrder(actual.toArray(), expected.toArray());


    VeniceConfig veniceConfig = mock(VeniceConfig.class);
    when(veniceConfig.getKeyExpr()).thenReturn("{\"srcId\" : (long)key[0], \"version\" : (int)key[1]}");
    onlineAnchoredFeatureKeyPlaceholdersBuilder = new OnlineAnchoredFeatureKeyPlaceholdersBuilder(veniceConfig);
    actual = onlineAnchoredFeatureKeyPlaceholdersBuilder.build();
    assertEqualsNoOrder(actual.toArray(), expected.toArray());

    PinotConfig pinotConfig = mock(PinotConfig.class);
    when(pinotConfig.getQueryArguments()).thenReturn(new String[]{"key[0]", "key[1]"});
    onlineAnchoredFeatureKeyPlaceholdersBuilder = new OnlineAnchoredFeatureKeyPlaceholdersBuilder(pinotConfig);
    actual = onlineAnchoredFeatureKeyPlaceholdersBuilder.build();
    assertEqualsNoOrder(actual.toArray(), expected.toArray());
  }

  @Test
  public void testExtractOnlineKeyPlaceHoldersFromMve() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    OnlineAnchoredFeatureKeyPlaceholdersBuilder onlineAnchoredFeatureKeyPlaceholdersBuilder =
        new OnlineAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig);
    String mvel = "{\"srcId\" : (long)key[0], \"version\" : (int)key[1]}";
    List<KeyPlaceholder> actual = onlineAnchoredFeatureKeyPlaceholdersBuilder.extractOnlineKeyPlaceHoldersFromMvel(mvel);
    List<KeyPlaceholder> expected = Arrays.asList(new KeyPlaceholder().setKeyPlaceholderRef("key[0]"), new KeyPlaceholder().setKeyPlaceholderRef("key[1]"));
    assertEquals(actual.size(), 2);
    assertEqualsNoOrder(actual.toArray(), expected.toArray());

    mvel ="{\"urn\": new com.linkedin.common.urn.Urn(key[0], key[1]).toString(), \"dimension\": Integer.parseInt(key[2]), \"version\": key[3]}";
    actual = onlineAnchoredFeatureKeyPlaceholdersBuilder.extractOnlineKeyPlaceHoldersFromMvel(mvel);
    expected = Arrays.asList(new KeyPlaceholder().setKeyPlaceholderRef("key[0]"), new KeyPlaceholder().setKeyPlaceholderRef("key[1]"),
        new KeyPlaceholder().setKeyPlaceholderRef("key[2]"), new KeyPlaceholder().setKeyPlaceholderRef("key[3]"));
    assertEquals(actual.size(), 4);
    assertEqualsNoOrder(actual.toArray(), expected.toArray());
  }



  @Test
  public void testBuildKeyPlaceholdersForRestliWithExprOnly() {
    RestliConfig restliConfig = mock(RestliConfig.class);
    when(restliConfig.getOptionalKeyExpr()).thenReturn(Optional.of("{\"srcId\" : (long)key[0], \"version\" : (int)key[1]}"));
    when(restliConfig.getReqParams()).thenReturn(Optional.empty());
    OnlineAnchoredFeatureKeyPlaceholdersBuilder onlineAnchoredFeatureKeyPlaceholdersBuilder =
        new OnlineAnchoredFeatureKeyPlaceholdersBuilder(restliConfig);
    List<KeyPlaceholder> keyPlaceholders = onlineAnchoredFeatureKeyPlaceholdersBuilder.buildKeyPlaceholdersForRestli(restliConfig);
    List<KeyPlaceholder> expected = Arrays.asList(new KeyPlaceholder().setKeyPlaceholderRef("key[0]"), new KeyPlaceholder().setKeyPlaceholderRef("key[1]"));
    assertEqualsNoOrder(keyPlaceholders.toArray(), expected.toArray());
  }

  @Test
  public void testBuildKeyPlaceholdersForRestliWithReqParamsOnly() {
    RestliConfig restliConfig = mock(RestliConfig.class);
    when(restliConfig.getOptionalKeyExpr()).thenReturn(Optional.empty());
    Map<String, Object> reqParams = new HashMap<>();
    reqParams.put("param1", "{\"srcId\" : (long)key[0], \"version\" : (int)key[1]}");
    reqParams.put("param2", "{\"urn\": new com.linkedin.common.urn.Urn(key[1], key[2]).toString(), \"dimension\": Integer.parseInt(key[3]), \"version\": key[4]}");
    reqParams.put("param3", 123);
    when(restliConfig.getReqParams()).thenReturn(Optional.of(reqParams));
    OnlineAnchoredFeatureKeyPlaceholdersBuilder onlineAnchoredFeatureKeyPlaceholdersBuilder =
        new OnlineAnchoredFeatureKeyPlaceholdersBuilder(restliConfig);
    List<KeyPlaceholder> keyPlaceholders = onlineAnchoredFeatureKeyPlaceholdersBuilder.buildKeyPlaceholdersForRestli(restliConfig);
    List<KeyPlaceholder> expected = Arrays.asList(new KeyPlaceholder().setKeyPlaceholderRef("key[0]"), new KeyPlaceholder().setKeyPlaceholderRef("key[1]"),
        new KeyPlaceholder().setKeyPlaceholderRef("key[2]"), new KeyPlaceholder().setKeyPlaceholderRef("key[3]"), new KeyPlaceholder().setKeyPlaceholderRef("key[4]"));
    assertEqualsNoOrder(keyPlaceholders.toArray(), expected.toArray());
  }


  @Test
  public void testBuildKeyPlaceholdersForRestliWithBothExprAndReqParams() {
    RestliConfig restliConfig = mock(RestliConfig.class);
    when(restliConfig.getOptionalKeyExpr()).thenReturn(Optional.of("{\"srcId\" : (long)key[0], \"version\" : (int)key[1]}"));
    Map<String, Object> reqParams = new HashMap<>();
    reqParams.put("param1", "{\"srcId\" : (long)key[1], \"version\" : (int)key[2]}");
    reqParams.put("param2", "{\"urn\": new com.linkedin.common.urn.Urn(key[1], key[2]).toString(), \"dimension\": Integer.parseInt(key[3]), \"version\": key[4]}");
    reqParams.put("param3", 123);
    when(restliConfig.getReqParams()).thenReturn(Optional.of(reqParams));
    OnlineAnchoredFeatureKeyPlaceholdersBuilder onlineAnchoredFeatureKeyPlaceholdersBuilder =
        new OnlineAnchoredFeatureKeyPlaceholdersBuilder(restliConfig);
    List<KeyPlaceholder> keyPlaceholders = onlineAnchoredFeatureKeyPlaceholdersBuilder.buildKeyPlaceholdersForRestli(restliConfig);
    List<KeyPlaceholder> expected = Arrays.asList(new KeyPlaceholder().setKeyPlaceholderRef("key[0]"), new KeyPlaceholder().setKeyPlaceholderRef("key[1]"),
        new KeyPlaceholder().setKeyPlaceholderRef("key[2]"), new KeyPlaceholder().setKeyPlaceholderRef("key[3]"), new KeyPlaceholder().setKeyPlaceholderRef("key[4]"));
    assertEqualsNoOrder(keyPlaceholders.toArray(), expected.toArray());
  }

  @Test
  public void testBuildKeyPlaceholdersForPinot() {
    PinotConfig pinotConfig = mock(PinotConfig.class);
    when(pinotConfig.getQueryArguments()).thenReturn(new String[]{"key[0]", "key[1]"});
    OnlineAnchoredFeatureKeyPlaceholdersBuilder onlineAnchoredFeatureKeyPlaceholdersBuilder =
        new OnlineAnchoredFeatureKeyPlaceholdersBuilder(pinotConfig);
    List<KeyPlaceholder> keyPlaceholders = onlineAnchoredFeatureKeyPlaceholdersBuilder.buildKeyPlaceholdersForPinot(pinotConfig);
    List<KeyPlaceholder> expected = Arrays.asList(new KeyPlaceholder().setKeyPlaceholderRef("key[0]"), new KeyPlaceholder().setKeyPlaceholderRef("key[1]"));
    assertEqualsNoOrder(keyPlaceholders.toArray(), expected.toArray());
  }
}

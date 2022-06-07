package com.linkedin.feathr.config.featureanchor.builder;

import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.core.config.producer.derivations.BaseFeatureConfig;
import com.linkedin.feathr.core.config.producer.derivations.KeyedFeature;
import com.linkedin.feathr.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.feathr.core.utils.MlFeatureVersionUrnCreator;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.featureDataModel.ExpansionKeyFunction;
import com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor;
import com.linkedin.feathr.featureDataModel.StandardAggregation;
import com.linkedin.feathr.featureDataModel.KeyPlaceholder;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class SequentialJoinFeatureSourcesAnchorBuilderTest {
  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_keyPlaceholdersBuilder);
  }

  @Test(description = "check isSequentialJoinDefaultOutputKey static function")
  public void testIsSequentialJoinDefaultOutputKey() {
    String key1 = "__SequentialJoinDefaultOutputKey__1";
    assertTrue(SequentialJoinFeatureSourcesAnchorBuilder.isSequentialJoinDefaultOutputKey(key1));
    String key2 = "randomKey";
    assertFalse(SequentialJoinFeatureSourcesAnchorBuilder.isSequentialJoinDefaultOutputKey(key2));
  }

  @Test(description = "test inner helper function buildKeyFunctionInExpansionKeyFunction")
  public void testBuildKeyFunctionInExpansionKeyFunction() {
    SequentialJoinFeatureSourcesAnchorBuilder sequentialJoinFeatureSourcesAnchorBuilder =
        new SequentialJoinFeatureSourcesAnchorBuilder(mock(SequentialJoinConfig.class), mock(MlFeatureVersionUrnCreator.class),
            mock(KeyPlaceholdersBuilder.class));
    BaseFeatureConfig baseFeatureConfig1 = mock(BaseFeatureConfig.class);
    when(baseFeatureConfig1.getTransformation()).thenReturn(Optional.of("testFunction()"));
    when(baseFeatureConfig1.getOutputKeys()).thenReturn(Optional.of(Arrays.asList("key0")));
    KeyedFeature expansion1 = mock(KeyedFeature.class);
    ExpansionKeyFunction.KeyFunction keyFunction1 = sequentialJoinFeatureSourcesAnchorBuilder.buildKeyFunctionInExpansionKeyFunction(baseFeatureConfig1, expansion1);
    assertTrue(keyFunction1.isMvelExpression());
    assertEquals(keyFunction1.getMvelExpression().getMvel(), "testFunction()");

    BaseFeatureConfig baseFeatureConfig2 = mock(BaseFeatureConfig.class);
    when(baseFeatureConfig2.getTransformation()).thenReturn(Optional.of("testFunction()"));
    when(baseFeatureConfig2.getOutputKeys()).thenReturn(Optional.empty());
    KeyedFeature expansion2 = mock(KeyedFeature.class);
    Exception exception = null;
    try {
      sequentialJoinFeatureSourcesAnchorBuilder.buildKeyFunctionInExpansionKeyFunction(baseFeatureConfig2, expansion2);
    } catch (Exception ex) {
      exception = ex;
    }
    assertNotNull(exception);
    assertTrue(exception.getMessage().startsWith("transformation/transformationClass is set in base feature config"));


    BaseFeatureConfig baseFeatureConfig3 = mock(BaseFeatureConfig.class);
    when(baseFeatureConfig3.getTransformation()).thenReturn(Optional.empty());
    when(baseFeatureConfig3.getOutputKeys()).thenReturn(Optional.of(Arrays.asList("key0")));
    KeyedFeature expansion3 = mock(KeyedFeature.class);
    ExpansionKeyFunction.KeyFunction keyFunction3 = sequentialJoinFeatureSourcesAnchorBuilder.buildKeyFunctionInExpansionKeyFunction(baseFeatureConfig3, expansion3);
    assertTrue(keyFunction3.isIdentityFunction());


    BaseFeatureConfig baseFeatureConfig4 = mock(BaseFeatureConfig.class);
    when(baseFeatureConfig4.getTransformation()).thenReturn(Optional.empty());
    when(baseFeatureConfig4.getOutputKeys()).thenReturn(Optional.empty());
    KeyedFeature expansion4 = mock(KeyedFeature.class);
    when(expansion4.getKey()).thenReturn(Arrays.asList("key1"));
    ExpansionKeyFunction.KeyFunction keyFunction4 = sequentialJoinFeatureSourcesAnchorBuilder.buildKeyFunctionInExpansionKeyFunction(baseFeatureConfig4, expansion4);
    assertTrue(keyFunction4.isIdentityFunction());

    BaseFeatureConfig baseFeatureConfig5 = mock(BaseFeatureConfig.class);
    when(baseFeatureConfig5.getTransformation()).thenReturn(Optional.empty());
    when(baseFeatureConfig5.getTransformationClass()).thenReturn(Optional.of("com.linkedin.frame.MyFeatureTransformer"));
    when(baseFeatureConfig5.getOutputKeys()).thenReturn(Optional.of(Arrays.asList("key0")));
    KeyedFeature expansion5 = mock(KeyedFeature.class);
    when(expansion5.getKey()).thenReturn(Arrays.asList("key0"));
    ExpansionKeyFunction.KeyFunction keyFunction5 = sequentialJoinFeatureSourcesAnchorBuilder.buildKeyFunctionInExpansionKeyFunction(baseFeatureConfig5, expansion5);
    assertTrue(keyFunction5.isUserDefinedFunction());
    assertEquals(keyFunction5.getUserDefinedFunction().getClazz().getFullyQualifiedName(), "com.linkedin.frame.MyFeatureTransformer");

  }

  @Test(description = "If both base outputKey and transformation are missing, the expansion can only have one key", expectedExceptions = IllegalArgumentException.class)
  public void testBuildOfflineSeqJoinFail() {
    SequentialJoinFeatureSourcesAnchorBuilder sequentialJoinFeatureSourcesAnchorBuilder =
        new SequentialJoinFeatureSourcesAnchorBuilder(mock(SequentialJoinConfig.class), mock(MlFeatureVersionUrnCreator.class),
            mock(KeyPlaceholdersBuilder.class));

    BaseFeatureConfig baseFeatureConfig4 = mock(BaseFeatureConfig.class);
    when(baseFeatureConfig4.getTransformation()).thenReturn(Optional.empty());
    when(baseFeatureConfig4.getOutputKeys()).thenReturn(Optional.empty());
    KeyedFeature expansion4 = mock(KeyedFeature.class);
    when(expansion4.getKey()).thenReturn(Arrays.asList("key1", "key2"));
    ExpansionKeyFunction.KeyFunction keyFunction4 = sequentialJoinFeatureSourcesAnchorBuilder.buildKeyFunctionInExpansionKeyFunction(baseFeatureConfig4, expansion4);
    assertTrue(keyFunction4.isIdentityFunction());
  }

  @Test(description = "Since there are syntax discrepancies between frame-online and frame-offline for sequential join features per go/frame/sequentialjoin, thus we test them separately. This test covers handling frame-online syntax for sequential join.")
  public void testBuildOnlineSequentialJoinAnchor() throws URISyntaxException {
    List<String> keys = Arrays.asList("memberId", "jobId");
    String baseKeyExpr = "memberId";
    List<String> baseOutputKeys = Collections.singletonList("memberUrn");
    BaseFeatureConfig base = new BaseFeatureConfig(baseKeyExpr, "featureA", baseOutputKeys,
        "import com.linkedin.frame.MyFeatureUtils; MyFeatureUtils.dotProduct(featureA);", null);
    KeyedFeature expansion = new KeyedFeature("[memberUrn, jobId]", "featureB");
    SequentialJoinConfig sequentialJoinConfig = new SequentialJoinConfig(keys, base, expansion, "ELEMENTWISE_MAX", null);

    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    SequentialJoinFeatureSourcesAnchorBuilder sequentialJoinFeatureSourcesAnchorBuilder = new SequentialJoinFeatureSourcesAnchorBuilder(
        sequentialJoinConfig, MlFeatureVersionUrnCreator.getInstance(), _keyPlaceholdersBuilder);
    FeatureAnchor.Anchor anchor = sequentialJoinFeatureSourcesAnchorBuilder.build();
    SequentialJoinFeatureSourcesAnchor sequentialJoinFeatureSourcesAnchor = anchor.getSequentialJoinFeatureSourcesAnchor();

    assertEquals(sequentialJoinFeatureSourcesAnchor.getKeyPlaceholders(), keyPlaceholders);
    assertBaseFeatureSource(sequentialJoinFeatureSourcesAnchor, MlFeatureVersionUrn.deserialize("urn:li:mlFeatureVersion:(urn:li:mlFeature:(global,featureA),0,0,0)"), Arrays.asList("memberId"));
    assertExpansionKeyFunction(sequentialJoinFeatureSourcesAnchor, "import com.linkedin.frame.MyFeatureUtils; MyFeatureUtils.dotProduct(featureA);", Arrays.asList("memberUrn"));
    assertExpansionFeatureSource(sequentialJoinFeatureSourcesAnchor, MlFeatureVersionUrn.deserialize("urn:li:mlFeatureVersion:(urn:li:mlFeature:(global,featureB),0,0,0)"), Arrays.asList("memberUrn", "jobId"));
    assertReduceFunction(sequentialJoinFeatureSourcesAnchor, "ELEMENTWISE_MAX");
  }

  @Test(description = "Since there are syntax discrepancies between frame-online and frame-offline for sequential join features per go/frame/sequentialjoin, thus we test them separately. This test covers handling frame-offline syntax for sequential join.")
  public void testBuildOfflineSequentialJoinAnchor() throws URISyntaxException {
    List<String> keys = Collections.singletonList("memberId");
    String baseKeyExpr = "memberId";
    BaseFeatureConfig base = new BaseFeatureConfig(baseKeyExpr, "featureA", null, null, null);
    KeyedFeature expansion = new KeyedFeature("key.entityUrn", "featureB");
    SequentialJoinConfig sequentialJoinConfig = new SequentialJoinConfig(keys, base, expansion, "UNION");

    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    SequentialJoinFeatureSourcesAnchorBuilder sequentialJoinFeatureSourcesAnchorBuilder = new SequentialJoinFeatureSourcesAnchorBuilder(
        sequentialJoinConfig, MlFeatureVersionUrnCreator.getInstance(), _keyPlaceholdersBuilder);
    FeatureAnchor.Anchor anchor = sequentialJoinFeatureSourcesAnchorBuilder.build();
    SequentialJoinFeatureSourcesAnchor sequentialJoinFeatureSourcesAnchor = anchor.getSequentialJoinFeatureSourcesAnchor();

    assertEquals(sequentialJoinFeatureSourcesAnchor.getKeyPlaceholders(), keyPlaceholders);
    assertBaseFeatureSource(sequentialJoinFeatureSourcesAnchor, MlFeatureVersionUrn.deserialize("urn:li:mlFeatureVersion:(urn:li:mlFeature:(global,featureA),0,0,0)"), Arrays.asList("memberId"));
    assertReduceFunction(sequentialJoinFeatureSourcesAnchor, "UNION");
    assertExpansionFeatureSource(sequentialJoinFeatureSourcesAnchor, MlFeatureVersionUrn.deserialize("urn:li:mlFeatureVersion:(urn:li:mlFeature:(global,featureB),0,0,0)"), Arrays.asList("__SequentialJoinDefaultOutputKey__0"));
    assertTrue(sequentialJoinFeatureSourcesAnchor.getExpansionKeyFunction().getKeyFunction().isIdentityFunction());
    assertEquals(sequentialJoinFeatureSourcesAnchor.getExpansionKeyFunction().getKeyPlaceholders().size(), 1);
    assertEquals(sequentialJoinFeatureSourcesAnchor.getExpansionKeyFunction().getKeyPlaceholders().iterator().next().getKeyPlaceholderRef(), "__SequentialJoinDefaultOutputKey__0");
  }


  private void assertBaseFeatureSource(SequentialJoinFeatureSourcesAnchor sequentialJoinFeatureSourcesAnchor, MlFeatureVersionUrn featureVersionUrn, List<String> keyPlaceholderRefs) {
    FeatureSource base = sequentialJoinFeatureSourcesAnchor.getBase();
    assertTrue(base.getKeyPlaceholderRefs().containsAll(keyPlaceholderRefs));
    assertEquals(base.getUrn(), featureVersionUrn);
  }

  private void assertExpansionKeyFunction(SequentialJoinFeatureSourcesAnchor sequentialJoinFeatureSourcesAnchor, String transformation, List<String> keyPlaceholderRefs) {
    ExpansionKeyFunction expansionKeyFunction = sequentialJoinFeatureSourcesAnchor.getExpansionKeyFunction();
    assertEquals(expansionKeyFunction.getKeyFunction().getMvelExpression().getMvel(), transformation);
    List<KeyPlaceholder> expected = keyPlaceholderRefs.stream()
        .map(keyPlaceholderRef -> new KeyPlaceholder().setKeyPlaceholderRef(keyPlaceholderRef))
        .collect(Collectors.toList());
    assertTrue(expansionKeyFunction.getKeyPlaceholders().containsAll(expected));
  }

  private void assertIdentityFunction(SequentialJoinFeatureSourcesAnchor sequentialJoinFeatureSourcesAnchor) {
    ExpansionKeyFunction expansionKeyFunction = sequentialJoinFeatureSourcesAnchor.getExpansionKeyFunction();
    assertTrue(expansionKeyFunction.getKeyFunction().isIdentityFunction());
  }

  private void assertExpansionFeatureSource(SequentialJoinFeatureSourcesAnchor sequentialJoinFeatureSourcesAnchor, MlFeatureVersionUrn featureVersionUrn, List<String> keyPlaceholderRefs) {
    FeatureSource expansion = sequentialJoinFeatureSourcesAnchor.getExpansion();
    assertTrue(expansion.getKeyPlaceholderRefs().containsAll(keyPlaceholderRefs));
    assertEquals(expansion.getUrn(), featureVersionUrn);
  }

  private void assertReduceFunction(SequentialJoinFeatureSourcesAnchor sequentialJoinFeatureSourcesAnchor, String aggregationType) {
    StandardAggregation standardAggregation =
        sequentialJoinFeatureSourcesAnchor.getReductionFunction().getStandardAggregation();
    assertEquals(standardAggregation, StandardAggregation.valueOf(aggregationType.toUpperCase()));
  }
}

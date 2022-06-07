package com.linkedin.feathr.core.utils;

import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.data.template.StringArray;
import com.linkedin.feathr.core.config.producer.ExprType;
import com.linkedin.feathr.core.config.producer.TypedExpr;
import com.linkedin.feathr.core.config.producer.derivations.BaseFeatureConfig;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfigWithExtractor;
import com.linkedin.feathr.core.config.producer.derivations.KeyedFeature;
import com.linkedin.feathr.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.feathr.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.feathr.exception.FrameException;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class DerivedFeatureDependencyResolverTest {
  MlFeatureVersionUrnCreator mlFeatureVersionUrnCreator = MlFeatureVersionUrnCreator.getInstance();
  DerivedFeatureDependencyResolver _derivedFeatureDependencyResolver =
      new DerivedFeatureDependencyResolver(mlFeatureVersionUrnCreator, MvelInputsResolver.getInstance());

  @Test(description = "Tests SimpleDerivationConfig feature with SQL expression should throw exception.",
      expectedExceptions = FrameException.class)
  public void testSqlExpressionThrowException() {
    SimpleDerivationConfig simpleDerivationConfig =
        new SimpleDerivationConfig(new TypedExpr("careers_preference_location", ExprType.SQL));
    _derivedFeatureDependencyResolver.getDependentFeatures(simpleDerivationConfig);
  }

  @Test(description = "Tests SimpleDerivationConfig feature with single input features.")
  public void testSingleInputFeatureForSimpleDerivationConfig() {
    SimpleDerivationConfig simpleDerivationConfig =
        new SimpleDerivationConfig(new TypedExpr("careers_preference_location", ExprType.MVEL));
    List<FeatureSource> inputFeatures =
        _derivedFeatureDependencyResolver.getDependentFeatures(simpleDerivationConfig);
    List<FeatureSource> expected =
        Collections.singletonList(buildFeatureSource(mlFeatureVersionUrnCreator.create("careers_preference_location"), new StringArray(), null));
    assertEquals(inputFeatures, expected);
  }

  @Test(description = "Tests SimpleDerivationConfig feature with multiple input features with multiple imports.")
  public void testMultipleInputFeatureForSimpleDerivationConfigWithMultipleImports() {
    SimpleDerivationConfig simpleDerivationConfig = new SimpleDerivationConfig(
        "import com.linkedin.frame.stz.ExtractorA; import com.linkedin.frame.stz.ExtractorB; ExtractorA.test(featureA) + ExtractorB.apply(featureB, featureC)");

    List<FeatureSource> inputFeatures =
        _derivedFeatureDependencyResolver.getDependentFeatures(simpleDerivationConfig);
    List<FeatureSource> expected =
        Arrays.asList(
            buildFeatureSource(mlFeatureVersionUrnCreator.create("featureA"), new StringArray(), null),
            buildFeatureSource(mlFeatureVersionUrnCreator.create("featureB"), new StringArray(), null),
            buildFeatureSource(mlFeatureVersionUrnCreator.create("featureC"), new StringArray(), null));
    assertEquals(inputFeatures, expected);
  }

  @Test(description = "Tests SequentialJoinConfig feature.")
  public void testSequentialJoinConfig() {
    List<String> keys = Collections.singletonList("memberId");
    String baseKeyExpr = "memberId";
    List<String> baseOutputKeys = Collections.singletonList("memberUrn");
    BaseFeatureConfig base = new BaseFeatureConfig(baseKeyExpr, "MemberIndustryId", baseOutputKeys,
        "import com.linkedin.frame.MyFeatureUtils; MyFeatureUtils.dotProduct(MemberIndustryId);", null);
    KeyedFeature expansion = new KeyedFeature("skillId", "MemberIndustryName");
    SequentialJoinConfig expSequentialJoin2ConfigObj =
        new SequentialJoinConfig(keys, base, expansion, "ELEMENTWISE_MAX");

    List<FeatureSource> inputFeatures =
        _derivedFeatureDependencyResolver.getDependentFeatures(expSequentialJoin2ConfigObj);
    List<FeatureSource> expected = Arrays.asList(
        buildFeatureSource(mlFeatureVersionUrnCreator.create("MemberIndustryId"), new StringArray(Collections.singletonList("memberId")), null),
        buildFeatureSource(mlFeatureVersionUrnCreator.create("MemberIndustryName"), new StringArray(Collections.singletonList("skillId")), null));
    assertEquals(inputFeatures, expected);
  }

  @Test(description = "Tests DerivationConfigWithExpr feature with two same feature but different alias.")
  public void testDerivationConfigWithExprWithTwoSameFeatureOfDiffereentAlias() {
    List<String> keys = Arrays.asList("m", "j");
    Map<String, KeyedFeature> inputs = new HashMap<>();
    inputs.put("foo", new KeyedFeature("m", "featureA"));
    inputs.put("bar", new KeyedFeature("j", "featureA"));

    String definition = "cosineSimilarity(foo, bar)";
    DerivationConfigWithExpr expDerivation2ConfigObj =
        new DerivationConfigWithExpr(keys, inputs, new TypedExpr(definition, ExprType.MVEL));

    List<FeatureSource> inputFeatures =
        _derivedFeatureDependencyResolver.getDependentFeatures(expDerivation2ConfigObj);
    List<FeatureSource> expected = Arrays.asList(
        buildFeatureSource(mlFeatureVersionUrnCreator.create("featureA"), new StringArray(Collections.singletonList("m")), "foo"),
        buildFeatureSource(mlFeatureVersionUrnCreator.create("featureA"), new StringArray(Collections.singletonList("j")), "bar"));
    assertEqualsNoOrder(inputFeatures.toArray(), expected.toArray());
  }

  @Test(description = "Tests DerivationConfigWithExtractor feature.")
  public void testDerivationConfigWithExtractor() {
    List<String> keys = Collections.singletonList("member");
    List<KeyedFeature> inputs = Collections.singletonList(new KeyedFeature("member", "foo"));
    String className = "com.linkedin.jymbii.nice.derived.MemberPlaceSimTopK";
    DerivationConfigWithExtractor expDerivation3ConfigObj = new DerivationConfigWithExtractor(keys, inputs, className);

    List<FeatureSource> inputFeatures =
        _derivedFeatureDependencyResolver.getDependentFeatures(expDerivation3ConfigObj);
    List<FeatureSource> expected = Collections.singletonList(
        buildFeatureSource(mlFeatureVersionUrnCreator.create("foo"), new StringArray(Collections.singletonList("member")), null));
    assertEquals(inputFeatures, expected);
  }

  private FeatureSource buildFeatureSource(MlFeatureVersionUrn mlFeatureVersionUrn, StringArray keyPlaceholders,
      @Nullable String alias) {
    FeatureSource featureSource = new FeatureSource();
    featureSource.setUrn(mlFeatureVersionUrn);
    if (alias != null) {
      featureSource.setAlias(alias);
    }
    featureSource.setKeyPlaceholderRefs(keyPlaceholders);
    return featureSource;
  }
}

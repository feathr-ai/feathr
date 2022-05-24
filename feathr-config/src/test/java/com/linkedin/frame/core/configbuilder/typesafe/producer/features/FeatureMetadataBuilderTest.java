package com.linkedin.frame.core.configbuilder.typesafe.producer.features;

import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.linkedin.frame.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.producer.features.FeaturesFixture.*;

/**
 * Unit tests for {@link FeatureMetadataBuilder}
 */
public class FeatureMetadataBuilderTest extends AbstractConfigBuilderTest {
  @Test(description = "Tests build of a feature metadata with one version")
  public void testFeatureMetadataBuilderWithOneVersion() {
    testConfigBuilder(featureNamespace, feature2MetadataStr, FeatureMetadataBuilder::build, expFeature2Md);
  }

  @Test(description = "Tests build of a feature metadata with enough owners")
  public void testFeatureMetadataBuilderWithEnoughOwners() {
    testConfigBuilder(featureNamespace, featureMetadataStrWith2Owners, FeatureMetadataBuilder::build, expFeatureMdWith2Owners);
  }

  @Test(description = "Tests build of a feature metadata with single owner",
      expectedExceptions = ConfigBuilderException.class)
  public void testFeatureMetadataBuilderWithSingleOwners() {
    buildConfig(featureNamespace, featureMetadataStrWith1Owners, FeatureMetadataBuilder::build);
  }

  @Test(description = "Tests build of a feature metadata with multiple versions")
  public void testFeatureMetadataBuilderWithMultiVersions() {
    testConfigBuilder(featureNamespace, feature1MetadataStr, FeatureMetadataBuilder::build, expFeature1Md);
  }

  @Test(description = "Tests build of feature metadata of a scalar feature with special chars")
  public void testFeatureMetadataWithNoDimsWithSpecialChars() {
    testConfigBuilder(featureNamespaceWithSpecialChars, featureMdStrWithSpecialChars, FeatureMetadataBuilder::build,
        expFeatureMdWithSpecialChars);
  }
}

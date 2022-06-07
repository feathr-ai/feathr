package com.linkedin.feathr.core.configbuilder.typesafe.producer.features;

import com.linkedin.feathr.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import com.linkedin.feathr.core.config.producer.common.Namespace;
import com.linkedin.feathr.core.config.producer.features.FeatureMetadata;
import com.linkedin.feathr.core.config.producer.features.FeatureSection;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.Test;

import static com.linkedin.feathr.core.config.producer.FeatureDefConfig.*;
import static com.linkedin.feathr.core.configbuilder.typesafe.producer.features.FeaturesFixture.*;
import static org.testng.Assert.*;


/**
 * Unit tests for {@link FeatureSectionBuilder}
 */
public class FeatureSectionBuilderTest extends AbstractConfigBuilderTest {

  @Test(description = "Tests build of features section")
  public void testFeatureSectionBuild() {
    testConfigBuilder(featureSectionStr, FeatureSectionBuilder::build, expFeatureSection);
  }

  @Test(description = "Tests iteration on a features section")
  public void testFeatureSectionIteration() {
    Config fullConfig = ConfigFactory.parseString(featureSectionStr);
    Config featureSectionConfig = fullConfig.getConfig(FEATURES);
    FeatureSection obsFeatureSection = FeatureSectionBuilder.build(featureSectionConfig);

    assertNotNull(obsFeatureSection);

    for (Pair<Namespace, Set<FeatureMetadata>> pair : obsFeatureSection) {
      Namespace ns = pair.getLeft();
      Set<FeatureMetadata> featureMdSet = pair.getRight();
      assertNotNull(ns);
      assertNotNull(featureMdSet);
      assertTrue(featureMdSet.size() > 0);
    }
  }
}

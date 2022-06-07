package com.linkedin.feathr.core.configbuilder.typesafe.producer.features;

import com.linkedin.feathr.core.config.ConfigObj;
import com.linkedin.feathr.core.config.producer.common.Version;
import com.linkedin.feathr.core.config.producer.features.ValueType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

import static com.linkedin.feathr.core.configbuilder.typesafe.producer.features.FeaturesFixture.*;
import static com.linkedin.feathr.core.utils.Utils.*;
import static org.testng.Assert.*;


/**
 * Unit tests for {@link FeatureDefinitionBuilder}
 */
public class FeatureDefinitionBuilderTest {

  @Test(description = "Tests build of FeatureDefinition of multi-dimensional feature")
  public void testFeatureDefWithDims() {
    testFeatureDefConfigBuilder(feature1Name, feature1ValType, feature1Ver1DefStr, expFeature1Ver1Def);
  }

  @Test(description = "Tests build of FeatureDefinition of a scalar feature")
  public void testFeatureDefWithNoDims() {
    testFeatureDefConfigBuilder(feature2Name, feature2ValType, feature2DefStr, expFeature2Def);
  }

  private void testFeatureDefConfigBuilder(String featureName, ValueType valType, String configStr,
      ConfigObj expFeatureDefConfigObj) {
    Config fullConfig = ConfigFactory.parseString(configStr);
    String versionStr = fullConfig.root().keySet().iterator().next();
    Version version = new Version(versionStr);
    Config featureDefConfig = fullConfig.getConfig(quote(versionStr));
    ConfigObj obsFeatureDefConfigObj = FeatureDefinitionBuilder.build(featureNamespace, featureName, version, valType,
        featureDefConfig);

    assertEquals(obsFeatureDefConfigObj, expFeatureDefConfigObj);
  }
}
